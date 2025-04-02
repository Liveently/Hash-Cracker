package ru.kosolap.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import ru.kosolap.json.CrackHashWorkerResponse;
import ru.kosolap.service.ManagerService;

@RestController
@RequestMapping("/internal")
public class InternalController {
    private final ManagerService service;

    @Autowired
    public InternalController(ManagerService service) {
        this.service = service;
    }

    @PatchMapping("/api/manager/hash/crack/request")
    public void recieveAnswer(@RequestBody CrackHashWorkerResponse response) { //получает ответы от узлов
        System.out.println("Получен ответ от воркера: " + response);
        service.recieveAnswer(response);
    }

    @PatchMapping("/api/manager/hash/crack/progress")
    public void updateProgress(@RequestBody CrackHashWorkerResponse response) { // обновляет прогресс от узлов
        System.out.println("Обновление прогресса от воркера: " + response);
        service.updateProgress(response);
    }

}
