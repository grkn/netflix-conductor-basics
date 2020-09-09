package com.conductor.example.worker;

import com.netflix.conductor.client.worker.Worker;
import com.netflix.conductor.common.metadata.tasks.Task;
import com.netflix.conductor.common.metadata.tasks.TaskResult;
import org.springframework.stereotype.Service;

@Service
public class HelloWorldWorker implements Worker {

    private static final String TYPE_DEF = "hello_world";

    @Override
    public String getTaskDefName() {
        return TYPE_DEF;
    }

    @Override
    public TaskResult execute(Task task) {
        System.out.println("HELLO WORLD " + task.getInputData().get("name"));
        return TaskResult.complete();
    }
}
