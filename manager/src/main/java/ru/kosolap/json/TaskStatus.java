package ru.kosolap.json;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
@JsonIgnoreProperties("startTime")
public class TaskStatus {
    private TaskStatusEnum status;
    private List<String> answer;
    private Instant startTime;
    private ConcurrentHashMap<Integer, Double> progressMap; // partNumber → progress (0-100)

    public TaskStatus() {
        this.status = TaskStatusEnum.IN_PROGRESS;
        this.answer = new ArrayList<>();
        this.startTime = Instant.now();
        this.progressMap = new ConcurrentHashMap<>();
    }

    public void updateProgress(int partNumber, double progress) {
        progressMap.put(partNumber, progress);
    }


    @JsonIgnore
    public boolean isCompleted() {
        System.out.println("Точно готово!");
        return progressMap.values().stream().allMatch(p -> p == 100.0);
    }
}
