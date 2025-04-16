package likelion.matching.web.controller;

import jakarta.validation.Valid;
import likelion.matching.domain.member.entity.Answer;
import likelion.matching.domain.member.entity.Member;
import likelion.matching.domain.member.repository.MemberRepository;
import likelion.matching.web.form.AnswerForm;
import likelion.matching.web.form.QuestionForm;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class MatchingController {

    private final MemberRepository memberRepository;

    @PostMapping("/create")
    public ResponseEntity<?> createMember(@Valid @RequestBody QuestionForm form){
        Member member = new Member();
        member.setName(form.getName());
        member.setGender(form.getGender());
        member.setInstarId(form.getInstarId());

        for (AnswerForm answerForm : form.getAnswers()) {
            Answer answer = new Answer();
            answer.setQuestionNumber(answerForm.getQuestionNumber());
            answer.setChoice(answerForm.getChoice());
            answer.setMember(member);
            member.getAnswers().add(answer);
        }
        memberRepository.save(member);
        return ResponseEntity.ok(Map.of(
                "status", "success",
                "message", "매칭 저장 완료!"
        ));
    }
    @GetMapping("/members")
    public ResponseEntity<?> getAllMembers(){
        return ResponseEntity.ok(memberRepository.findAll());
    }
    @DeleteMapping("/members/{id}")
    public ResponseEntity<?> deleteMember(@PathVariable Long id){
        memberRepository.deleteById(id);
        return ResponseEntity.ok(Map.of(
                "status", "success",
                "message", "멤버 삭제 완료!"
        ));
    }
}