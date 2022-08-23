package com.ll.exam.sbb.answer;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

// AnswerController.modify()에서 answerForm.setContent 필요
@Setter
@Getter
public class AnswerForm {
    @NotEmpty(message = "내용은 필수항목입니다.")
    private String content;
}
