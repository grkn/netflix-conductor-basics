package com.conductor.example.worker.decisiontask;

import com.netflix.conductor.client.worker.Worker;
import com.netflix.conductor.common.metadata.tasks.Task;
import com.netflix.conductor.common.metadata.tasks.TaskResult;
import org.springframework.stereotype.Component;

@Component
public class GenerateMovieArtwork implements Worker {

    private static final String TYPE_DEF = "generate_movie_artwork";

    @Override
    public String getTaskDefName() {
        return TYPE_DEF;
    }

    @Override
    public TaskResult execute(Task task) {
        String movieId = (String) task.getInputData().get("movieId");
        System.out.println("Generate Movie Artwork. Movie Id : " + movieId);
        return TaskResult.complete();
    }
}
