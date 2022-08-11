package com.ll.exam.sbb;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class SbbApplicationTests { // QustionRepositoryTests ver.me
	@Autowired
	private QuestionRepository questionRepository;
	Question q1 = new Question();
	Question q2 = new Question();
	Question q3 = new Question();


	@BeforeEach
	public void beforeEach() {
		truncateQuestionTable();
		makeQuestionTestData();
	}

	private void makeQuestionTestData() {
		q1.setSubject("sbb가 무엇인가요?");
		q1.setContent("sbb에 대해서 알고 싶습니다.");
		q1.setCreateDate(LocalDateTime.now());
		questionRepository.save(q1);

		q2.setSubject("스프링부트 모델 질문입니다.");
		q2.setContent("id는 자동으로 생성되나요?");
		q2.setCreateDate(LocalDateTime.now());
		questionRepository.save(q2);

		q2.setSubject("질문3");
		q2.setContent("내용3");
		q2.setCreateDate(LocalDateTime.now());
		questionRepository.save(q3);
	}

	private void truncateQuestionTable() {
		questionRepository.disableForeignKeyChecks();
		questionRepository.truncate();
		questionRepository.enableForeignKeyChecks();
	}


	@Test
	void 저장() {
	assertThat(q1.getId()).isGreaterThan(0);
	assertThat(q2.getId()).isGreaterThan(q1.getId());
}

	@Test
	void 수정() {
		Optional<Question> oq = questionRepository.findById(1);
		assertTrue(oq.isPresent());
		Question q = oq.get();
		q.setSubject("수정된 제목");
		questionRepository.save(q); // UPDATE
	}

	@Test
	void 삭제() {
		assertEquals(3, questionRepository.count());
		Optional<Question> oq = questionRepository.findById(1);
		assertTrue(oq.isPresent());
		Question q = oq.get();

		questionRepository.delete(q);

		assertEquals(2, questionRepository.count());
	}
	@Test
	void findAll() {
		List<Question> all = questionRepository.findAll();
		assertThat(all.size()).isEqualTo(3);

		Question q = all.get(0);
		assertThat(q.getSubject()).isEqualTo("sbb가 무엇인가요?");
	}

	@Test
	void findBySubject() {
		Question q = questionRepository.findBySubject("sbb가 무엇인가요?");
		assertThat(q.getId()).isEqualTo(1);
	}

	@Test
	void findBySubjectAndContent() {
		Question q = questionRepository.findBySubjectAndContent(
				"sbb가 무엇인가요?", "sbb에 대해서 알고 싶습니다.");
		assertThat(q.getId()).isEqualTo(1);
	}

	@Test
	void findBySubjectLike() {
		List<Question> qList = questionRepository.findBySubjectLike("sbb%");
		Question q = qList.get(0);

		assertThat(q.getSubject()).isEqualTo("sbb가 무엇인가요?");
	}
}