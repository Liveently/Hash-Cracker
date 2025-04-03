package ru.kosolap.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ru.kosolap.json.HashAndLength;
import ru.kosolap.json.RequestId;
import ru.kosolap.json.TaskStatus;
import ru.kosolap.service.ManagerService;

@RestController
@RequestMapping("/api")
public class Controller {

    private final ManagerService service;

    @Autowired
    public Controller(ManagerService service) {
        this.service = service;
    }

    @PostMapping("/hash/crack") 
    public RequestId postMethod(@RequestBody HashAndLength body) {
        return service.getRequestId(body);
    }

    @GetMapping("/hash/status") 
    public TaskStatus getMessage(RequestId id) {
        return service.getTaskStatus(id);
    }
}
