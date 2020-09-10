package com.conductor.example.worker.decisiontask;

import com.netflix.conductor.client.worker.Worker;
import com.netflix.conductor.common.metadata.tasks.Task;
import com.netflix.conductor.common.metadata.tasks.TaskResult;
import org.springframework.stereotype.Component;

@Component
public class SetupEpisodesWorker implements Worker {

    private static final String SETUP_EPISODES = "setup_episodes";

    @Override
    public String getTaskDefName() {
        return SETUP_EPISODES;
    }

    @Override
    public TaskResult execute(Task task) {
        String movieId = (String) task.getInputData().get("movieId");
        System.out.println("Setup Episodes. Movie Id : " + movieId);
        return TaskResult.complete();
    }
}
