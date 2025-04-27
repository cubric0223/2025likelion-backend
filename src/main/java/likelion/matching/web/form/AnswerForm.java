package likelion.matching.web.form;

import jakarta.validation.constraints.NotNull;
import likelion.matching.domain.entity.Choice;
import lombok.Data;

@Data
public class AnswerForm {


    private int questionNumber;

    @NotNull
    private Choice choice;
}
