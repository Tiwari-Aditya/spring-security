package com.aditya.springsecurityamigocode.student;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Data
public class Student {
    private final Integer studnetId;
    private final String studentName;
}
