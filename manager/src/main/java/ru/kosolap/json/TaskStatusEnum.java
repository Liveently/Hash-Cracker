package ru.kosolap.json;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum TaskStatusEnum {
    IN_QUEUE("IN_QUEUE"),      // В очереди
    IN_PROGRESS("IN_PROGRESS"), // В работе
    ERROR("ERROR"),            // Ошибка
    READY("READY"),            // Готово
    TIMEOUT("TIMEOUT");        // Истекло время

    private final String value;

    TaskStatusEnum(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static TaskStatusEnum fromString(String value) {
        for (TaskStatusEnum status : TaskStatusEnum.values()) {
            if (status.value.equalsIgnoreCase(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown status: " + value);
    }
}
