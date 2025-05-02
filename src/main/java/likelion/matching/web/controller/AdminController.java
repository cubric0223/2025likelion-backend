package likelion.matching.web.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import likelion.matching.domain.service.MatchingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import likelion.matching.domain.entity.Member;
import likelion.matching.domain.repository.MemberRepository;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final MemberRepository memberRepository;
    private final MatchingService matchingService;

    private static final String ADMIN_PASSWORD = "123456";

//    private boolean checkAdminPassword(HttpServletRequest request) {
//        String password = request.getParameter("password");
//        return ADMIN_PASSWORD.equals(password);
//    }

    @GetMapping("/member-list")
    public ResponseEntity<?> getAllMembers(@RequestParam String password) {
        if (!ADMIN_PASSWORD.equals(password)) {
            return ResponseEntity.status(403).body(Map.of(
                    "status", "fail",
                    "message", "비밀번호가 틀렸습니다."
            ));
        }

        List<Member> members = memberRepository.findAll();
        return ResponseEntity.ok(Map.of(
                "status", "success",
                "count", members.size(),
                "members", members
        ));
    }

//    @GetMapping("/member-list")
//    public ResponseEntity<?> getAllMembers(@RequestParam String password) {
//        if (!"123456".equals(password)) {
//            return ResponseEntity.status(403).body(Map.of(
//                    "status", "fail",
//                    "message", "비밀번호가 틀렸습니다."
//            ));
//        }
//
//        List<Member> members = memberRepository.findAll();
//        return ResponseEntity.ok(Map.of(
//                "status", "success",
//                "count", members.size(),
//                "members", members
//        ));
//    }


    @PostMapping("/reset-db")
    @Transactional
    public ResponseEntity<?> resetDatabase(@RequestParam String password) {
        if (!ADMIN_PASSWORD.equals(password)) {
            return ResponseEntity.status(403).body(Map.of(
                    "status", "fail",
                    "message", "비밀번호가 틀렸습니다."
            ));
        }

        memberRepository.deleteAll();
        return ResponseEntity.ok(Map.of(
                "status", "success",
                "message", "모든 사용자 및 답변이 초기화되었습니다."
        ));
    }

    @PostMapping("/matching")
    public ResponseEntity<?> runMatching(@RequestParam String password){
        if (!ADMIN_PASSWORD.equals(password)) {
            return ResponseEntity.status(403).body(Map.of(
                    "status", "fail",
                    "message", "비밀번호가 틀렸습니다."
            ));
        }
        matchingService.runMatching();
        return ResponseEntity.ok(Map.of(
                "status", "success",
                "message", "매칭이 완료 되었습니다!"
        ));
    }
}