package backend.com.backend.question.controller;

import backend.com.backend.answer.controller.AnswerController;
import backend.com.backend.question.dto.QuestionDto;
import backend.com.backend.question.entity.Question;
import backend.com.backend.question.mapper.QuestionMapper;
import backend.com.backend.question.service.QuestionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.net.URI;
import java.util.List;

import static java.util.Comparator.comparing;

@RestController
@RequestMapping("/questions")
@Validated
public class QuestionController {
    private final static String QUESTION_DEFAULT_URL = "/questions";
    private final QuestionService questionService;
    private final QuestionMapper questionMapper;
    private final AnswerController answerController;

    public QuestionController(QuestionService questionService, QuestionMapper questionMapper, AnswerController answerController) {
        this.questionService = questionService;
        this.questionMapper = questionMapper;
        this.answerController = answerController;
    }

    @PostMapping
    public ResponseEntity postQuestion(@Valid @RequestBody QuestionDto.Post postDto) {
        Question question = questionService.createQuestion(questionMapper.questionPostDtoToQuestion(postDto));

        URI location =
                UriComponentsBuilder
                        .newInstance()
                        .path(QUESTION_DEFAULT_URL + "/{question-id}")
                        .buildAndExpand(question.getId())
                        .toUri(); // "questions/{question-id}"
        return ResponseEntity.created(location).build();
    }

    @PatchMapping("/{question-id}")
    public ResponseEntity patchQuestion(@PathVariable("question-id") @Positive long questionId,
                                        @Valid @RequestBody QuestionDto.Patch patchDto) {
        patchDto.setQuestionId(questionId);

        Question question = questionService.updateQuestion(questionMapper.questionPatchDtoToQuestion(patchDto));

        return new ResponseEntity<>(questionMapper.questionToQuestionResponseDto(question), HttpStatus.OK);
    }

    @GetMapping("/{question-id}")
    public ResponseEntity getQuestion(@PathVariable("question-id") @Positive long questionId,
                                      @Positive @RequestParam(defaultValue = "1") int page,
                                      @Positive @RequestParam(defaultValue = "3") int size,
                                      @RequestParam(defaultValue = "newest") String sortOption) {
        Question question = questionService.findQuestion(questionId);
        return answerController.bySortOption(questionId, page, size, sortOption, question);
        //질문컨트롤러에 getQuestion메소드 내부에서 위 로직으로 실행해야 할 듯해.
        //일단 질문이 품고 있는 답변리스트의 size()를 재서 0이면 그냥 response 리턴하면 될테고
        // 0 초과이면 multi리스폰스로 내보내서 질문 한 개에 답변 리스트들을 stream으로 구성해보면 좋겠지
        // postman

    }


    @GetMapping
    public ResponseEntity getQuestions() {
        List<Question> questions = questionService.findQuestions();

        List<QuestionDto.Response> response = questionMapper.questionsToQuestionResponseDtos(questions);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{question-id}")
    public ResponseEntity deleteQuestion(@PathVariable("question-id") @Positive long questionId) {
        questionService.deleteQuestion(questionId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
