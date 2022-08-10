package com.ll.exam.sbb;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Integer> { // Integer는 Primary Key
    Question findBySubject(String subject);

    Question findBySubjectAndContent(String subject, String content);
    List<Question> findBySubjectLike(String s); // 여러개 반환될 수도 있으므로 리턴타입을 List로
}
