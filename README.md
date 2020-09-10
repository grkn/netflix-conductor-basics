# Netflix-conductor-basics
Netflix conductor basics. Workflow orchestration on microservices (https://netflix.github.io/conductor/)

# Docker

Please download docker before using this application. After installing docker, then docker-compose up will start conductor-server.

conductor-ui added to docker-compose.yaml. you can access by typing http://localhost:5000 for visualization

# Spring Boot App

Please run Application class and start the application.

# Rest Endpoint

Send @Post to http://localhost:8099/context/api/v1/helloworld

example json body is below : 

```
{
    "name" : "grkn"
}
```

After rest endpoint is triggered, the if you check the console you can see the asynchrous call from conductor-server.

I will explain the details in Medium as soon as possible.
