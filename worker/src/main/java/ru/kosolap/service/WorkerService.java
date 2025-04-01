package ru.kosolap.service;

import jakarta.xml.bind.DatatypeConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.support.NullValue;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.ws.wsdl.wsdl11.provider.SoapProvider;
import org.paukov.combinatorics.Generator;

import java.security.MessageDigest;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;

import org.springframework.web.client.RestTemplate;
import org.paukov.combinatorics.CombinatoricsFactory;
import org.paukov.combinatorics.ICombinatoricsVector;
import org.paukov.combinatorics.permutations.PermutationGenerator;
import org.paukov.combinatorics.util.ComplexCombinationGenerator;

import ru.kosolap.json.CrackHashManagerRequest;
import ru.kosolap.json.CrackHashWorkerResponse;

@Service
public class WorkerService {

    private final RestTemplate restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory());

    private final Duration taskTimeout = Duration.parse(System.getenv().getOrDefault("TASK_TIMEOUT", "PT5M") );

    @Async
    public void crackHashTask(CrackHashManagerRequest body) { //получает задачу
        String ALPHABET = String.join("", body.getAlphabet().getSymbols());
        int ALPHABET_SIZE = ALPHABET.length();
        int POSITIONS_NUM = body.getMaxLength(); 
        int ALL_PARTS_NUM = body.getPartCount(); 
        int MY_PART_IDX = body.getPartNumber(); 
        byte[] HASH = DatatypeConverter.parseHexBinary(body.getHash());
        

        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch(Exception e) {
            e.printStackTrace();
        }

        ICombinatoricsVector<String> vector = CombinatoricsFactory.createVector(ALPHABET.split("")); //создаётся вектрр, все символы пароля

        Generator<String> gen = CombinatoricsFactory.createPermutationWithRepetitionGenerator(vector, POSITIONS_NUM); //генерация всех комбинаций

        int idx = 0;
        System.out.println("Start count permutations...");
        Instant startTime = Instant.now();
        for (ICombinatoricsVector<String> perm : gen) {
            if (idx % ALL_PARTS_NUM == MY_PART_IDX) { 

                String str = String.join("", perm.getVector());
                byte[] combHash = md5.digest(str.toString().getBytes()); //проверка хеша
                if (Arrays.equals(combHash, HASH)) {
                    System.out.println("I found hash: " + str.toString());
                    
                    sendAnswer(body.getRequestId(), str);
                }
            }

            Duration dur = Duration.between(startTime, Instant.now()); //прерывание при превышении времени
                if (dur.toMillis() > taskTimeout.toMillis()) {
                    System.out.println("Exceeded time limit: exiting");
                    return;
                }

            idx++;

        }
        System.out.println("End count permutations...");

    }

    private void sendAnswer(String id, String answer) { //отправка ответа менеджеру

        String managerPort = System.getenv("MANAGER_PORT");
        String managerUrl = "http://manager:" + managerPort;

        CrackHashWorkerResponse response = new CrackHashWorkerResponse();
        response.setRequestId(id);
        response.setAnswers(new CrackHashWorkerResponse.Answers());
        response.getAnswers().getWords().add(answer);

        restTemplate.patchForObject(managerUrl + "/internal/api/manager/hash/crack/request", response, Void.class);
    }

}
