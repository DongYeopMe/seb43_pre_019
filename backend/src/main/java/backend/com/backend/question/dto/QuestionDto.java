package backend.com.backend.question.dto;

import backend.com.backend.answer.dto.AnswerDto;
import backend.com.backend.answer.entity.Answer;
import backend.com.backend.response.PageInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

public class QuestionDto {
    @Getter
    @AllArgsConstructor
    public static class Post {
        @NotBlank(message = "Title is missing")
        @Size(min = 15, max = 150,  message = "Title must be at least 15 characters and cannot be longer than 150 characters")
        private String title;

        @NotBlank(message = "Body is missing.")
        private String body;

        @NotBlank(message = "Detail is missing.")
        private String details;


    }

    @Getter
    @AllArgsConstructor
    public static class Patch {
        private Long id;

        @NotBlank(message = "Title is missing")
        @Size(min = 15, max = 150,  message = "Title must be at least 15 characters and cannot be longer than 150 characters")
        private String title;

        @NotBlank(message = "Body is missing.")
        private String body;

        @NotBlank(message = "Detail is missing.")
        private String details;


        public void setQuestionId(long id) {
            this.id = id;
        }
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class Response {
        private Long id;
        private String title;
        private String body;
        private String details;
        private List<AnswerDto.Response> answers;
    }

    @Getter
    @Setter
    public static class ResponseWithPage {
        private Long id;
        private String title;
        private String body;
        private String details;
        private List<AnswerDto.Response> answers;
        private PageInfo pageInfos;
    }
}
