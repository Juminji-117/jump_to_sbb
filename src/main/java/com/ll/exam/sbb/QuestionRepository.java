package com.ll.exam.sbb;

import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Question, Integer> { // Integer는 Primary Key
    Question findBySubject(String subject);
}
