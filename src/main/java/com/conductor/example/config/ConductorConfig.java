package com.conductor.example.config;

import com.netflix.conductor.client.automator.TaskRunnerConfigurer;
import com.netflix.conductor.client.http.TaskClient;
import com.netflix.conductor.client.http.WorkflowClient;
import com.netflix.conductor.client.worker.Worker;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ConfigurationProperties(value = "conductor.config")
public class ConductorConfig {

    private String url;
    private Integer treadCount;

    @Bean
    public WorkflowClient workflowClient() {
        WorkflowClient client = new WorkflowClient();
        client.setRootURI(url);
        return client;
    }

    @Bean
    public TaskClient taskClient(List<Worker> workerList) {
        TaskClient taskClient = new TaskClient();
        taskClient.setRootURI(url);

        TaskRunnerConfigurer taskRunnerConfigurer = new TaskRunnerConfigurer.Builder(taskClient, workerList)
                .withThreadCount(treadCount)
                .withWorkerNamePrefix("BasicSamples")
                .build();


        taskRunnerConfigurer.init();
        return taskClient;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setTreadCount(Integer treadCount) {
        this.treadCount = treadCount;
    }

    public Integer getTreadCount() {
        return treadCount;
    }
}
