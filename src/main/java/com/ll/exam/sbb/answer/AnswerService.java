package com.ll.exam.sbb.answer;

import com.ll.exam.sbb.DataNotFoundException;
import com.ll.exam.sbb.question.Question;
import com.ll.exam.sbb.user.SiteUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AnswerService {
    private final AnswerRepository answerRepository;

    public void create(Question question, String content, SiteUser author) {
        Answer answer = new Answer();
        answer.setContent(content);
        answer.setCreateDate(LocalDateTime.now());
        answer.setAuthor(author);
        question.addAnswer(answer); // OneToMany의 단방향 문제 해결하기 위해 answer.setQuestion 대신 바로 question 객체에 저장하는 것이 좋음

        answerRepository.save(answer);
    }

    public void vote(Answer answer, SiteUser siteUser) {
        answer.getVoter().add(siteUser);

        answerRepository.save(answer);
    }

    public Answer getAnswer(Long id) {
        return answerRepository.findById(id).orElseThrow(() -> new DataNotFoundException("answer not found"));
    }

    public void modify(Answer answer, String content) {
        answer.setContent(content);
        answer.setModifyDate(LocalDateTime.now());
        answerRepository.save(answer);
    }
}
