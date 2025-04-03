package ru.kosolap.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kosolap.service.WorkerService;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class WorkerController {

    private final WorkerService workerService;

    public WorkerController(WorkerService workerService) {
        this.workerService = workerService;
    }

    @GetMapping("/workers")
    public List<String> getWorkers() {
        return workerService.getWorkers(); // Возвращает список IP-адресов воркеров
    }

    @GetMapping("/workers/health")
    public Map<String, String> checkWorkersHealth() {
        List<String> workerIps = workerService.getWorkers();
        // Для каждого воркера вызываем checkHealth и создаем карту с IP и статусом здоровья
        return workerIps.stream()
                        .collect(Collectors.toMap(ip -> ip, workerService::checkHealth));
    }
}
