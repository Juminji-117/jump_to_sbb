package com.ll.exam.sbb.question;

import com.ll.exam.sbb.DataNotFoundException;
import com.ll.exam.sbb.answer.AnswerForm;
import com.ll.exam.sbb.user.SiteUser;
import com.ll.exam.sbb.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.security.Principal;

@RequestMapping("/question")
@Controller
@RequiredArgsConstructor // 생성자 주입
public class QuestionController {
    // @Autowired // 필드 주입
    private final QuestionService questionService;
    private final UserService userService;

    @GetMapping("/list")
    // 이 자리에 @ResponseBody가 없으면 resources/question_list/question_list.html 파일을 뷰로 삼는다.
    public String list(Model model, @RequestParam(defaultValue = "0") int page) {
        Page<Question> paging = questionService.getList(page);

        // 미래에 실행된 question_list.html 에서
        // questionList 라는 이름으로 questionList 변수를 사용할 수 있다.
        model.addAttribute("paging", paging);

        return "question_list";
    }

    @GetMapping("/detail/{id}")
    public String detail(Model model, @PathVariable long id, AnswerForm answerForm) {
        // question fetchType.Eager로 설정하면 이 때 answerList 가져옴
        Question question = questionService.getQuestion(id);

        model.addAttribute("question", question);

        return "question_detail";
    }

    // 데이터 받아 post 요청이 아니므로 questionForm(subject와 content를 묶은 DTO 객체) @Valid(유효성 체크) 할 필요 없음
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{id}")
    public String questionModify(QuestionForm questionForm, @PathVariable("id") Integer id, Principal principal) {
        // id에 해당하는 question 찾아오기
        Question question = this.questionService.getQuestion(id);

        // question id 존재하지 않을 경우 예외 발생시키기
        if ( question == null ) {
            throw new DataNotFoundException("%d번 질문은 존재하지 않습니다.");
        }

        // question.작성자 == 현재 회원 정보 동일성 체크
        // 동일성 체크해서 modify 페이지(질문폼) 리턴해주기만 하면 되므로 SiteUser 객체 생성할 필요 X
        if(!question.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }

        // question에서 subject와 content 가져와 questionform에 default value값으로 넣어줌 (≒html의 placeholder 같은 역할)
        questionForm.setSubject(question.getSubject());
        questionForm.setContent(question.getContent());

        // 질문폼 리턴
        return "question_form";
    }

    @PostMapping("/modify/{id}")
    public String questionModify(@Valid QuestionForm questionForm, BindingResult bindingResult,
                                 Principal principal, @PathVariable("id") Integer id) {
        // @Valid의 바인딩 오류를 담아내는 BindingResult 인터페이스
        if (bindingResult.hasErrors()) {
            return "question_form";
        }

        // id에 해당하는 question 찾아오기
        Question question = this.questionService.getQuestion(id);

        // question.작성자 == 현재 회원 정보 동일성 체크
        // SiteUser에 관한 정보는 수정하지 않으므로 객체 생성할 필요 X
        if (!question.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }

        this.questionService.modify(question, questionForm.getSubject(), questionForm.getContent());

        //redirect할 때도 메서드 리턴 타입 String
        return String.format("redirect:/question/detail/%s", id);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/create")
    public String questionCreate(QuestionForm questionForm) {
        return "question_form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create")
    public String questionCreate(Principal principal, Model model, @Valid QuestionForm questionForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) { // import org.springframework.validation.BindingResult 해줘야 에러 안뜸
            return "question_form";
        }

        // siteUser를 author로 저장해야 되기 때문에 SiteUser 객체(id, userName, password, email 속성 지닌) 생성
        // getUser() -> findByUserName() -> Optional<SiteUser> 리턴
        SiteUser siteUser = userService.getUser(principal.getName());


        questionService.create(questionForm.getSubject(), questionForm.getContent(),siteUser);

        // 질문 저장후 질문목록으로 이동
        return "redirect:/question/list";
    }
}
