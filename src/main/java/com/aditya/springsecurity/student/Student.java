package com.aditya.springsecurity.student;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Data
public class Student {
    private final Integer studnetId;
    private final String studentName;
}
