package com.ll.exam.sbb.answer;

import com.ll.exam.sbb.question.Question;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AnswerService {
    private final AnswerRepository answerRepository;

    public void create(Question question, String content) {
        Answer answer = new Answer();
        answer.setContent(content);
        answer.setCreateDate(LocalDateTime.now());
        question.addAnswer(answer); // OneToMany의 단방향 문제 해결하기 위해 answer.setQuestion 대신 바로 question 객체에 저장하는 것이 좋음
        answerRepository.save(answer);
    }
}
