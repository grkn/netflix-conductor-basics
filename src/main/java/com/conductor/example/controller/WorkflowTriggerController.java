package com.conductor.example.controller;

import com.conductor.example.resource.HelloWorldResource;
import com.conductor.example.worker.service.WorkflowClientService;
import com.google.common.collect.ImmutableMap;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/context/api/v1")
public class WorkflowTriggerController {

    private final WorkflowClientService workflowClientService;

    public WorkflowTriggerController(WorkflowClientService workflowClientService) {
        this.workflowClientService = workflowClientService;
    }

    @PostMapping("/helloworld")
    public ResponseEntity<Void> helloWorldTrigger(@RequestBody @Valid HelloWorldResource helloWorldResource) {
        workflowClientService.triggerWorkflowByNameAndInput("hello_world_workflow",
                ImmutableMap.of("name", helloWorldResource.getName()));
        return ResponseEntity.noContent().build();
    }
}
