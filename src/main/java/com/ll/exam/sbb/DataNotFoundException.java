package com.ll.exam.sbb;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// HttpStatus.NOT_FOUND == code 404
// 필수는 아니지만 조금 더 표준 준수
@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "데이터가 존재하지 않습니다.")
public class DataNotFoundException extends RuntimeException {
    public DataNotFoundException(String question_not_found) {
    }
}