//package likelion.matching.web.controller;
//
//import jakarta.validation.Valid;
//import likelion.matching.domain.entity.Answer;
//import likelion.matching.domain.entity.Member;
//import likelion.matching.domain.repository.MemberRepository;
//import likelion.matching.domain.service.MatchingService;
//import likelion.matching.web.form.AnswerForm;
//import likelion.matching.web.form.QuestionForm;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.Map;
//
//@RestController
//@RequiredArgsConstructor
//public class MatchingController {
//
//    private final MemberRepository memberRepository;
//    private final MatchingService matchingService;
//
//    @PostMapping("/create")
//    public ResponseEntity<?> createMember(@Valid @RequestBody QuestionForm form){
//        if (memberRepository.existsByInstarId(form.getInstarId())) {
//            throw new IllegalArgumentException("이미 존재하는 인스타 ID입니다.");
//        }
//
//        Member member = new Member();
//        member.setName(form.getName());
//        member.setGender(form.getGender());
//        member.setInstarId(form.getInstarId());
//
//        for (AnswerForm answerForm : form.getAnswers()) {
//            Answer answer = new Answer();
//            answer.setQuestionNumber(answerForm.getQuestionNumber());
//            answer.setChoice(answerForm.getChoice());
//            answer.setMember(member);
//            member.getAnswers().add(answer);
//        }
//        memberRepository.save(member);
//        return ResponseEntity.ok(Map.of(
//                "status", "success",
//                "message", "매칭 저장 완료!"
//        ));
//    }
//
//    @PostMapping("/matching")
//    public ResponseEntity<?> runMatching(){
//        matchingService.runMatching();
//        return ResponseEntity.ok("매칭 완료");
//    }
//
//
//    @GetMapping("/members")
//    public ResponseEntity<?> getAllMembers(){
//        return ResponseEntity.ok(memberRepository.findAll());
//    }
//
//
//
//    @DeleteMapping("/members/{id}")
//    public ResponseEntity<?> deleteMember(@PathVariable Long id) {
//        Member member = memberRepository.findById(id)
//                .orElseThrow(() -> new IllegalArgumentException("해당 멤버를 찾을 수 없습니다."));
//
//
//        Member matched = member.getMatchedMember();
//        if (matched != null) {
//            matched.setMatchedMember(null); // 상대방도 끊고
//            member.setMatchedMember(null);  // 본인도 끊고
//            memberRepository.save(matched); // 상대방 저장
//        }
//
//        // 본인 삭제
//        memberRepository.delete(member);
//
//        return ResponseEntity.ok(Map.of(
//                "status", "success",
//                "message", "멤버 삭제 완료!"
//        ));
//    }
//}