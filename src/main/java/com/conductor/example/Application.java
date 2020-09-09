package com.conductor.example;

import com.conductor.example.worker.create.WorkflowDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class Application {


    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
        try {
            WorkflowDefinition.loadWorkflowsAndTasks();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
