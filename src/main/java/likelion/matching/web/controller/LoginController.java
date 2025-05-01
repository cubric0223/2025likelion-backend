package likelion.matching.web.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import likelion.matching.domain.entity.Member;
import likelion.matching.domain.repository.MemberRepository;
import likelion.matching.web.form.LoginForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
public class LoginController {
    private final MemberRepository memberRepository;

    @PostMapping("/login")
public ResponseEntity<?> login(@Valid @RequestBody LoginForm form, HttpServletRequest request) {
    try {
        log.info(" Login 요청 들어옴 - instarId: {}", form.getInstarId());

        String instarId = form.getInstarId();
        Member member = memberRepository.findByInstarId(instarId).orElse(null);

        if (member == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "해당 인스타 ID의 사용자가 없습니다."));
        }

        HttpSession session = request.getSession();
        session.setAttribute("LOGIN_MEMBER", member);

        return ResponseEntity.ok(Map.of("message", "로그인 성공"));

    } catch (Exception e) {
        log.error("❌ 로그인 처리 중 에러 발생", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of(
                        "message", "서버 내부 오류 발생",
                        "error", e.getMessage()
                ));
    }
}

    @GetMapping("/matching-result")
    public ResponseEntity<?> matchingResult(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("LOGIN_MEMBER") == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "로그인된 사용자가 없습니다."));
        }

        Member member = (Member) session.getAttribute("LOGIN_MEMBER");
        String matchedInstarId = (member.getMatchedMember() != null)
                ? member.getMatchedMember().getInstarId()
                : null;

        Map<String, Object> response = new HashMap<>();
        response.put("yourInstarId", member.getInstarId());
        response.put("matchedInstarId", matchedInstarId);

        return ResponseEntity.ok(response);
    }
}