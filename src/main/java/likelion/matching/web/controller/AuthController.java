package likelion.matching.web.controller;

import jakarta.servlet.http.HttpSession;
import likelion.matching.domain.entity.MemberStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import likelion.matching.domain.entity.Gender;
import likelion.matching.domain.entity.Member;
import likelion.matching.domain.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@Slf4j
public class AuthController {

    private final MemberRepository memberRepository;

    @PostMapping("/check")
    public ResponseEntity<?> checkId(@RequestBody Map<String, String> request, HttpSession session) {
        try {
            log.info("🔍 인스타 ID 확인 요청 - 입력값: {}", request);
            
            String instaId = request.get("instarId");

            if (instaId == null || instaId.isBlank()) {
                return ResponseEntity.badRequest().body(Map.of(
                        "status", "fail",
                        "message", "인스타 ID가 누락되었습니다."
                ));
            }

            Optional<Member> memberOpt = memberRepository.findByInstarId(instaId);

            if (memberOpt.isPresent()) {
                Member member = memberOpt.get();
                session.setAttribute("memberId", member.getId());

                if (member.getStatus() == MemberStatus.WAITING || member.getAnswers().size() >= 5) {
                    return ResponseEntity.ok(Map.of(
                            "status", "done",
                            "isNewUser", false,
                            "message", "이미 답변을 제출하셨습니다. 감사합니다!"
                    ));
                }

                return ResponseEntity.ok(Map.of(
                        "status", "success",
                        "isNewUser", false,
                        "message", "기존 유저입니다. 바로 질문 단계로 이동합니다."
                ));
            } else {
                session.setAttribute("pendingInstaId", instaId);
                return ResponseEntity.ok(Map.of(
                        "status", "success",
                        "isNewUser", true,
                        "message", "신규 유저입니다. 성별 정보를 입력해주세요."
                ));
            }
        } catch (Exception e) { 
            // 오류 상세 로깅
            log.error("❌ 인스타 ID 확인 중 오류 발생", e);
            log.error("❌ 상세 오류 메시지: {}", e.getMessage());
            
            // 스택 트레이스 전체 로깅
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            log.error("❌ 스택 트레이스: {}", sw.toString());
            
            // 데이터베이스 관련 오류인지 확인
            if (e.getMessage() != null && e.getMessage().contains("Table") && e.getMessage().contains("doesn't exist")) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(Map.of(
                                "status", "error",
                                "message", "데이터베이스 테이블이 존재하지 않습니다. 관리자에게 문의하세요.",
                                "error", e.getMessage(),
                                "errorType", "DATABASE_TABLE_MISSING"
                        ));
            }
            
            // 기타 오류 응답
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "status", "error",
                            "message", "서버 오류가 발생했습니다. 잠시 후 다시 시도해주세요.",
                            "error", e.getMessage(),
                            "errorType", "INTERNAL_SERVER_ERROR"
                    ));
        }
    }


    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody Map<String, String> request, HttpSession session) {
        String instaId = (String) session.getAttribute("pendingInstaId");
        String genderStr = request.get("gender");

        if (instaId == null || genderStr == null || genderStr.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of(
                    "status", "fail",
                    "message", "입력 정보가 부족합니다."
            ));
        }

        try {
            Gender gender = Gender.valueOf(genderStr.toUpperCase());

            Member member = new Member();
            member.setInstarId(instaId);
            member.setGender(gender);
            member = memberRepository.save(member);

            session.setAttribute("memberId", member.getId());
            session.removeAttribute("pendingInstaId");

            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "회원가입 및 로그인 완료"
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "status", "fail",
                    "message", "성별 값이 잘못되었습니다. (male/female)"
            ));
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(409).body(Map.of(
                    "status", "fail",
                    "message", "이미 존재하는 인스타 ID입니다."
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                    "status", "error",
                    "message", "알 수 없는 오류가 발생했습니다."
            ));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpSession session) {
        if (session.getAttribute("memberId") == null) {
            return ResponseEntity.badRequest().body(Map.of(
                    "status", "fail",
                    "message", "로그인 상태가 아닙니다."
            ));
        }


        session.removeAttribute("memberId");

        return ResponseEntity.ok(Map.of(
                "status", "success",
                "message", "로그아웃 완료"
        ));
    }

    @GetMapping("/status")
    public ResponseEntity<?> checkLoginStatus(HttpSession session) {
        Long memberId = (Long) session.getAttribute("memberId");

        boolean isLoggedIn = memberId != null;

        return ResponseEntity.ok(Map.of(
                "status", "success",
                "isLoggedIn", isLoggedIn,
                "message", isLoggedIn ? "로그인 상태입니다." : "로그인되어 있지 않습니다."
        ));
    }



}