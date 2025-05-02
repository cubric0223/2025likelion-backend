package likelion.matching.domain.member.form;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import likelion.matching.domain.entity.Gender;
import likelion.matching.domain.member.form.AnswerForm;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;


@Data
public class QuestionForm{
    @NotEmpty
    private String name;
    @NotNull
    private Gender gender;
    @NotEmpty
    private String instarId;

    @Valid
    @NotNull
    @Size(min = 5)
    private List<@Valid AnswerForm> answers = new ArrayList<>();
}
