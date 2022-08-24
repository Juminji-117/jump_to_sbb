package com.ll.exam.sbb.question;

import com.ll.exam.sbb.base.RepositoryUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long>, RepositoryUtil { // Long은 Primary Key
    Question findBySubject(String subject);

    Question findBySubjectAndContent(String subject, String content);

    List<Question> findBySubjectLike(String s); // 여러개 반환될 수도 있으므로 리턴타입을 List로

    Page<Question> findBySubjectContains(String kw, Pageable pageable);

    Page<Question>findBySubjectContainsOrContentContains(String kw, String kw_, Pageable pageable);

    Page<Question>findBySubjectContainsOrContentContainsOrAuthor_usernameContains(String kw, String kw_, String kw__,Pageable pageable);

    //Question 입장에서 Answer 엔티티는 AnswerList -> AnswerList_content == Answer객체의 content
    Page<Question> findBySubjectContainsOrContentContainsOrAuthor_usernameContainsOrAnswerList_contentContains(String kw, String kw_, String kw__, String kw___, Pageable pageable);

    @Transactional
    @Modifying
    @Query(value = "ALTER TABLE question AUTO_INCREMENT = 1", nativeQuery = true)
    void truncate(); // 이건 지우면 X. truncateTable 하면 자동으로 이게 실행되므로.
}

