package ru.kosolap.json;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

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

    public TaskStatus() {
        this.status = TaskStatusEnum.IN_PROGRESS; // Устанавливаем значение из Enum
        this.answer = new ArrayList<>();
        this.startTime = Instant.now();
    }
}
