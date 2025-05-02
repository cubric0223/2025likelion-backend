package likelion.matching.web.controller;

import jakarta.servlet.http.HttpSession;
import likelion.matching.domain.entity.MemberStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import likelion.matching.domain.entity.Answer;
import likelion.matching.domain.entity.Member;
import likelion.matching.domain.repository.MemberRepository;
import likelion.matching.domain.member.form.AnswerForm;

import java.util.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final MemberRepository memberRepository;

    @PostMapping("/submit")
    public ResponseEntity<?> submitAnswers(@RequestBody Map<String, List<AnswerForm>> request, HttpSession session) {
        Long memberId = (Long) session.getAttribute("memberId");

        if (memberId == null) {
            return ResponseEntity.status(401).body(Map.of(
                    "status", "fail",
                    "message", "로그인 상태가 아닙니다."
            ));
        }

        List<AnswerForm> answerForms = request.get("answers");
        if (answerForms == null || answerForms.size() < 5) {
            return ResponseEntity.badRequest().body(Map.of(
                    "status", "fail",
                    "message", "답변이 부족하거나 잘못된 형식입니다."
            ));
        }

        Optional<Member> optionalMember = memberRepository.findById(memberId);
        if (optionalMember.isEmpty()) {
            return ResponseEntity.status(404).body(Map.of(
                    "status", "fail",
                    "message", "사용자 정보를 찾을 수 없습니다."
            ));
        }

        Member member = optionalMember.get();
        member.getAnswers().clear();

        for (AnswerForm form : answerForms) {
            Answer answer = new Answer();
            answer.setQuestionNumber(form.getQuestionNumber());
            answer.setChoice(form.getChoice());
            answer.setMember(member);
            member.getAnswers().add(answer);
        }

        member.setStatus(MemberStatus.WAITING); // 상태 업데이트
        memberRepository.save(member);

        return ResponseEntity.ok(Map.of(
                "status", "success",
                "message", "답변 제출 완료! 매칭 대기 중입니다."
        ));
    }
}