package ru.kosolap.json;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum TaskStatusEnum {
    IN_PROGRESS("IN_PROGRESS"), // В работе
    PARTIAL_SUCCESS_TIMEOUT("PARTIAL_SUCCESS_TIMEOUT"),     // Частично успешно
    PARTIAL_SUCCESS_WORKING("PARTIAL_SUCCESS_WORKING"),     // Частично успешно, но ещё работает
    SUCCESS("SUCCESS"),            // Готово
    TIMEOUT("TIMEOUT"),       // Истекло время
    NO_RESULTS("NO_RESULTS");     //нет результата


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
