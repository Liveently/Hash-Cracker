package ru.kosolap.service;

import jakarta.xml.bind.DatatypeConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.paukov.combinatorics.Generator;
import org.springframework.web.client.RestTemplate;
import org.paukov.combinatorics.CombinatoricsFactory;
import org.paukov.combinatorics.ICombinatoricsVector;

import java.security.MessageDigest;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.lang.Math;


import ru.kosolap.json.CrackHashManagerRequest;
import ru.kosolap.json.CrackHashWorkerResponse;

@Service
public class WorkerService {

    private final RestTemplate restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory());

    private final Duration taskTimeout = Duration.parse(System.getenv().getOrDefault("TASK_TIMEOUT", "PT5M"));

    @Async
    public void crackHashTask(CrackHashManagerRequest body) { // получает задачу
        String ALPHABET = String.join("", body.getAlphabet().getSymbols());
        int POSITIONS_NUM = body.getMaxLength();
        int ALL_PARTS_NUM = body.getPartCount();
        int MY_PART_IDX = body.getPartNumber();
        byte[] HASH = DatatypeConverter.parseHexBinary(body.getHash());

        MessageDigest md5;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        ICombinatoricsVector<String> vector = CombinatoricsFactory.createVector(ALPHABET.split(""));
        Generator<String> gen = CombinatoricsFactory.createPermutationWithRepetitionGenerator(vector, POSITIONS_NUM);

        long idx = 0;
        long totalPermutations = (long) gen.getNumberOfGeneratedObjects(); // Общее количество комбинаций
        Instant startTime = Instant.now();

        System.out.println("Start count permutations...");

        for (ICombinatoricsVector<String> perm : gen) {
            if (idx % ALL_PARTS_NUM == MY_PART_IDX) {
                String str = String.join("", perm.getVector());
                byte[] combHash = md5.digest(str.getBytes());

                if (Arrays.equals(combHash, HASH)) {
                    System.out.println("I found hash: " + str);

                    double progress = (idx / (double) totalPermutations) * 100;
                    progress = Math.round(progress * 100.0) / 100.0;

                    sendAnswer(body.getRequestId(), str, progress, body.getPartNumber()); 
                }
            }

            // Вычисляем прогресс выполнения
            double progress = (idx / (double) totalPermutations) * 100;
            progress = Math.round(progress * 100.0) / 100.0;


            int progressUpdateInterval = Integer.parseInt(System.getenv().getOrDefault("PROGRESS_UPDATE_INTERVAL", "10000"));

            // Каждые progressUpdateInterval итераций отправляем прогресс
            if (idx % progressUpdateInterval == 0) {
                sendProgress(body.getRequestId(), progress, body.getPartNumber());
            }

            // Проверка на таймаут
            Duration dur = Duration.between(startTime, Instant.now());
            if (dur.toMillis() > taskTimeout.toMillis()) {
                System.out.println("Exceeded time limit: exiting");
                sendProgress(body.getRequestId(), progress, body.getPartNumber());
                return;
            }

            idx++;
        }

        System.out.println("End count permutations...");
        sendProgress(body.getRequestId(), 100.0, body.getPartNumber()); // Если всё перебрали, ставим 100%
    }

    private void sendAnswer(String id, String answer, double progress, int partNumber) { // Отправка ответа менеджеру
        String managerPort = System.getenv("MANAGER_PORT");
        String managerUrl = "http://manager:" + managerPort;

        CrackHashWorkerResponse response = new CrackHashWorkerResponse();
        response.setRequestId(id);
        response.setAnswers(new CrackHashWorkerResponse.Answers());
        response.getAnswers().getWords().add(answer);
        response.setProgress(progress); // Передаём прогресс 100%
        response.setPartNumber(partNumber);

        System.out.println("Отправляем в менеджер: " + response);


        restTemplate.patchForObject(managerUrl + "/internal/api/manager/hash/crack/request", response, Void.class);
    }



    private void sendProgress(String id, double progress, int partNumber) {
    String managerPort = System.getenv("MANAGER_PORT");
    String managerUrl = "http://manager:" + managerPort;

    CrackHashWorkerResponse response = new CrackHashWorkerResponse();
    response.setRequestId(id);
    response.setProgress(progress);
    response.setPartNumber(partNumber);

    response.setAnswers(new CrackHashWorkerResponse.Answers());

    restTemplate.patchForObject(managerUrl + "/internal/api/manager/hash/crack/progress", response, Void.class);
    }


}

