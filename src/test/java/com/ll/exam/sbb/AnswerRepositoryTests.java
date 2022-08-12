package com.ll.exam.sbb;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class AnswerRepositoryTests {
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private AnswerRepository answerRepository;
    private int lastSampleDataId;
    @BeforeEach
    void beforeEach() {
        clearData();
        createSampleData();
    }


    private void clearData() {
        QuestionRepositoryTests.clearData(questionRepository);
        answerRepository.deleteAll(); // DELETE FROM question;
        answerRepository.truncateTable();
    }
    private void createSampleData() {
        QuestionRepositoryTests.createSampleData(questionRepository);
        Question q = questionRepository.findById(1).get();
        Answer a1 = new Answer();
        a1.setContent("sbb는 질문답변 게시판 입니다.");
        a1.setQuestion(q);
        a1.setCreateDate(LocalDateTime.now());
        answerRepository.save(a1);
        Answer a2 = new Answer();
        a2.setContent("sbb에서는 주로 스프링부트관련 내용을 다룹니다.");
        a2.setQuestion(q);
        a2.setCreateDate(LocalDateTime.now());
        answerRepository.save(a2);
    }

    @Test
    void 저장() {
        Question q = questionRepository.findById(2).get();
        Answer a = new Answer();
        a.setContent("네 자동으로 생성됩니다.");
        a.setQuestion(q);
        a.setCreateDate(LocalDateTime.now());
        answerRepository.save(a);
    }

    @Test
    void 조회() {
        Answer a = this.answerRepository.findById(1).get();
        assertThat(a.getContent()).isEqualTo("sbb는 질문답변 게시판 입니다.");
    }
    @Test
    void 관련된_question_조회() {
        Answer a = this.answerRepository.findById(1).get(); // DB통신
        Question q = a.getQuestion(); // DB통신하지 않고 a로부터 question(엄밀히 말하면 Question.id) 바로 가져올 수 있음

        assertThat(q.getId()).isEqualTo(1);
    }

    @Transactional
    @Rollback(false)
    @Test
    void question으로부터_관련된_질문들_조회() {
        // SELECT * FROM question WHERE id = 1
        Question q = questionRepository.findById(1).get();
        // DB 연결이 끊김

        //DB 연결이 끊겼기 때문에 관련된_question_조회() TEST처럼 바로 다시 가져올 수 없음(변경 전 : fetchType.LAZY였으므로)
        // 즉 다시 DB 통신해야 됨 -> Question 엔티티에서 answerList fetchType.Eager로 바꿔서 해결완료
        // 이번엔 Eager을 다시 지우고 하나의 트랜잭션으로 처리해서 문제 해결하기 -> [아직 미해결]
        // SELECT * FROM answer WHERE question_id = 1
        List<Answer> answerList = q.getAnswerList();

        assertThat(answerList.size()).isEqualTo(2);
        assertThat(answerList.get(0).getContent()).isEqualTo("sbb는 질문답변 게시판 입니다.");
    }
}