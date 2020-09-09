package com.conductor.example.worker.service;

import com.netflix.conductor.client.http.WorkflowClient;
import com.netflix.conductor.common.metadata.workflow.StartWorkflowRequest;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class WorkflowClientService {

    private final WorkflowClient workflowClient;

    public WorkflowClientService(WorkflowClient workflowClient) {
        this.workflowClient = workflowClient;
    }

    public void triggerWorkflowByNameAndInput(String workflowName, Map<String, Object> input) {
        StartWorkflowRequest startWorkflowRequest = new StartWorkflowRequest();
        startWorkflowRequest.setInput(input);
        startWorkflowRequest.withName(workflowName);
        workflowClient.startWorkflow(startWorkflowRequest);
    }
}
