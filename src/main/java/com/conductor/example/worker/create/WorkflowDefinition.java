package com.conductor.example.worker.create;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class WorkflowDefinition {
    private static final String TASK_DEF_PATH = "/metadata/taskdefs";
    private static final String WORKFLOW_DEF_PATH = "/metadata/workflow";
    private static final String CONDUCTOR_URL = "http://localhost:2090/api";
    private static ObjectMapper objectMapper = new ObjectMapper();
    private static List<JsonNode> workflows = new ArrayList<>();
    private static RestTemplate restTemplate = new RestTemplate();
    private static Boolean IS_DEFINABLE = true;
    private static final String DECISION_TYPE = "DECISION";

    public static class Task {
        private String name;
        private String type;
        private JsonNode jsonNode;

        public Task(String name, String type, JsonNode jsonNode) {
            this.name = name;
            this.type = type;
            this.jsonNode = jsonNode;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        @Override
        public String toString() {
            return "Task{" +
                    "name='" + name + '\'' +
                    ", type='" + type + '\'' +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Task task = (Task) o;
            return Objects.equals(name, task.name) &&
                    Objects.equals(type, task.type);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name, type);
        }

        public JsonNode getJsonNode() {
            return jsonNode;
        }

        public void setJsonNode(JsonNode jsonNode) {
            this.jsonNode = jsonNode;
        }
    }

    public static void loadWorkflowsAndTasks() throws IOException {
        List<Task> taskList = readTasks();
        List<Task> workflowTaskNames = readTaskNameFromWorkflow();
        checkTasksInWorkflow(taskList, workflowTaskNames);
        if (IS_DEFINABLE) {
            defineTasksInConductorServer(taskList);
            defineWorkflowsInConductorServer(workflows);
        }
    }

    private static void defineWorkflowsInConductorServer(List<JsonNode> workflows) {
        ArrayNode arrayNode = new ArrayNode(JsonNodeFactory.instance);
        workflows.forEach(arrayNode::add);
        restTemplate.put(CONDUCTOR_URL + WORKFLOW_DEF_PATH, new HttpEntity<>(arrayNode));
    }

    private static void defineTasksInConductorServer(List<Task> taskList) throws JsonProcessingException {
        ResponseEntity<String> tasks = restTemplate.getForEntity(CONDUCTOR_URL + TASK_DEF_PATH, String.class);

        if (tasks.getStatusCode().is2xxSuccessful() && tasks.getBody() != null) {

            ArrayNode conductorTasks = (ArrayNode) objectMapper.readTree(tasks.getBody());
            conductorTasks.forEach(jsonNode -> {

                Iterator<Task> taskIterator = taskList.iterator();
                while (taskIterator.hasNext()) {
                    Task task = taskIterator.next();
                    if (jsonNode.get("name").textValue().equals(task.getName())) {
                        restTemplate.put(CONDUCTOR_URL + TASK_DEF_PATH, new HttpEntity<>(task.getJsonNode()));
                        taskIterator.remove();
                    }
                }
            });
            ArrayNode arrayNode = new ArrayNode(JsonNodeFactory.instance);
            taskList.forEach(task -> arrayNode.add(task.getJsonNode()));
            if (!arrayNode.isEmpty()) {
                HttpEntity<JsonNode> httpEntity = new HttpEntity<>(arrayNode);
                restTemplate.postForObject(CONDUCTOR_URL + TASK_DEF_PATH, httpEntity, Void.class);
            }
        }
    }

    private static void checkTasksInWorkflow(List<Task> taskList, List<Task> workflowsTaskNames) {
        for (Task workflowTask : workflowsTaskNames) {
            if (workflowTask.getType().equals("SIMPLE") && !taskList.contains(workflowTask)) {
                System.out.println("Workflow Task scan failed. Be careful about :" + workflowTask);
                throw new IllegalArgumentException("Workflow Task scan failed. Be careful about :" + workflowTask);
            }
        }
    }

    private static List<Task> readTasks() throws IOException {
        List<Task> taskNames = new ArrayList<>();
        ArrayNode jsonNode = (ArrayNode) objectMapper.readTree(Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource("task.json")));
        jsonNode.forEach(node -> {
            taskNames.add(new Task(node.get("name").textValue(), "SIMPLE", node));
        });
        return taskNames;
    }

    private static List<Task> readTaskNameFromWorkflow() throws IOException {
        ArrayNode arrayNode = (ArrayNode) objectMapper.readTree(Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource("workflow.json")));
        final List<Task> taskNames = new ArrayList<>();
        arrayNode.elements().forEachRemaining(jsonNode -> {
            workflows.add(jsonNode);
            ArrayNode lArrayNode = (ArrayNode) jsonNode.get("tasks");
            lArrayNode.forEach(node -> taskNames.add(new Task(node.get("name").textValue(), node.get("type").textValue(), node)));
        });
        return taskNames;
    }
}