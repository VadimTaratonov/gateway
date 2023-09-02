package ru.taratonov.gateway.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;
import ru.taratonov.gateway.exception.IllegalArgumentOfEnumException;

import java.util.Arrays;
import java.util.stream.Collectors;

@Getter
public enum EmploymentStatus {
    UNEMPLOYED("unemployed"),
    SELF_EMPLOYED("self-employed"),
    BUSINESS_OWNER("business-owner"),
    EMPLOYED("employed");

    @JsonCreator
    static EmploymentStatus findValue(String findValue) {
        return Arrays.stream(EmploymentStatus.values())
                .filter(value -> value.name().equalsIgnoreCase(findValue))
                .findFirst()
                .orElseThrow(() -> IllegalArgumentOfEnumException.createWith(
                        Arrays.stream(EmploymentStatus.values())
                                .map(EmploymentStatus::getTitle)
                                .collect(Collectors.toList())));
    }

    private final String title;

    EmploymentStatus(String title) {
        this.title = title;
    }

}
