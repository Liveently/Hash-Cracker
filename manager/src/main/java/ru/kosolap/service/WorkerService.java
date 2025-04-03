package ru.kosolap.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

@Service
public class WorkerService {

    private final RestTemplate restTemplate = new RestTemplate();

    // Получение списка IP-адресов всех контейнеров "worker"
    public List<String> getWorkers() {
        List<String> addresses = new ArrayList<>();
        try {
            InetAddress[] machines = InetAddress.getAllByName("worker");
            for (InetAddress address : machines) {
                addresses.add(address.getHostAddress());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return addresses;
    }

    public String checkHealth(String ip) {
        String workerPort = System.getenv("WORKER_PORT");  
        String workerUrl = "http://" + ip + ":" + workerPort + "/health";  

        try {
            String response = restTemplate.getForObject(workerUrl, String.class);
            if ("Worker is healthy".equals(response)) {
                return "Healthy";
            } else {
                return "Unhealthy";
            }
        } catch (Exception e) {
            return "Not Responding";
        }
    }
}

