package ru.kosolap.service;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import jakarta.security.auth.message.callback.PrivateKeyCallback.Request;
import ru.kosolap.json.CrackHashManagerRequest;
import ru.kosolap.json.CrackHashWorkerResponse;
import ru.kosolap.json.HashAndLength;
import ru.kosolap.json.RequestId;
import ru.kosolap.json.TaskStatus;
import ru.kosolap.json.TaskStatusEnum;


@Service
public class ManagerService {

    private final RestTemplate restTemplate = new RestTemplate();
    private ConcurrentHashMap<RequestId, TaskStatus> idAndStatus; 
    private final String LETTERS_AND_DIGITS = System.getenv().getOrDefault("LETTERS_AND_DIGITS", "abcdefghijklmnopqrstuvwxyz0123456789");

    private final Duration taskTimeout = Duration.parse(System.getenv().getOrDefault("TASK_TIMEOUT", "PT5M") );


    public ManagerService() {
        this.idAndStatus = new ConcurrentHashMap<RequestId, TaskStatus>();
    }

    private CrackHashManagerRequest.Alphabet initAlphabet() { 
        CrackHashManagerRequest.Alphabet alphabet = new CrackHashManagerRequest.Alphabet();

        for (String charString : LETTERS_AND_DIGITS.split("")) {
            alphabet.getSymbols().add(charString);
        }

        return alphabet;
    }

    public RequestId getRequestId(HashAndLength body) { //отправляет задачу воркерам

        List<String> addresses = getWorkers();

        RequestId requestId = new RequestId(UUID.randomUUID().toString());

        idAndStatus.put(requestId, new TaskStatus()); // Статус автоматически будет IN_PROGRESS

        CrackHashManagerRequest request = new CrackHashManagerRequest();

        request.setRequestId(requestId.getRequestId());
        request.setHash(body.getHash());
        request.setMaxLength(body.getMaxLength());
        request.setAlphabet(initAlphabet());
 

        int partCount = Integer.parseInt( System.getenv().getOrDefault("HASH_PARTS", "4") );
        request.setPartCount(partCount);


        for (int part = 0; part < partCount; part++) { // какая по счету часть достается воркеру
            int addrIdx = part % addresses.size();
            String addr = addresses.get(addrIdx);
            String workerPort = System.getenv("WORKER_PORT");
            String workerUrl = "http://" + addr + ":" + workerPort;
            request.setPartNumber(part); 
            restTemplate.postForObject(workerUrl + "/internal/api/worker/hash/crack/task", request, Void.class); //http запрос каждому воркеру
        }

        return requestId;
    }

    public TaskStatus getTaskStatus(RequestId id) { 
        TaskStatus status = idAndStatus.get(id);
        System.out.println(status.toString());

        Duration dur = Duration.between(status.getStartTime(), Instant.now()); 
        
        if (dur.toMillis() > taskTimeout.toMillis() && status.getAnswer().isEmpty()) {
            status.setStatus(TaskStatusEnum.TIMEOUT);
        }

        return status;
    }

    public void recieveAnswer(CrackHashWorkerResponse response) { //получает ответ от воркера
        if (response.getAnswers().getWords().isEmpty()) {
            return;
        }

        TaskStatus status = idAndStatus.get(new RequestId(response.getRequestId())); //есть ли слова в ответе - если есть добавление в список ответов и статус READY
        if (status == null) {
            return;
        }
        System.out.println(status);


        status.getAnswer().addAll(response.getAnswers().getWords());
        status.setStatus(TaskStatusEnum.READY);

    }

    public List<String> getWorkers() { 
        InetAddress[] machines = null;   //Запрашивает все машины с именем "worker". Извлекает их IP-адреса и возвращает список.
        try {
            machines = InetAddress.getAllByName("worker");
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        List<String> addresses = new ArrayList<>();
        for(InetAddress address : machines){
            addresses.add(address.getHostAddress());
            System.out.println(address.getHostAddress());
        }
        return addresses;
    }
}