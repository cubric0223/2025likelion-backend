package likelion.matching.web.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import likelion.matching.domain.member.entity.Member;
import likelion.matching.domain.member.repository.MemberRepository;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final MemberRepository memberRepository;

    private boolean isAdmin(HttpSession session) {
        Long memberId = (Long) session.getAttribute("memberId");
        if (memberId == null) return false;

        return memberRepository.findById(memberId)
                .map(m -> "admin".equalsIgnoreCase(m.getInstarId()))
                .orElse(false);
    }

    @GetMapping("/memberlist")
    public ResponseEntity<?> getAllMembers(HttpSession session) {
        if (!isAdmin(session)) {
            return ResponseEntity.status(403).body(Map.of(
                    "status", "fail",
                    "message", "관리자만 접근 가능합니다."
            ));
        }

        List<Member> members = memberRepository.findAll();
        return ResponseEntity.ok(Map.of(
                "status", "success",
                "count", members.size(),
                "members", members
        ));
    }

    @PostMapping("/reset-db")
    @Transactional
    public ResponseEntity<?> resetDatabase(HttpSession session) {
        if (!isAdmin(session)) {
            return ResponseEntity.status(403).body(Map.of(
                    "status", "fail",
                    "message", "관리자만 접근 가능합니다."
            ));
        }

        memberRepository.deleteAll();
        return ResponseEntity.ok(Map.of(
                "status", "success",
                "message", "모든 사용자 및 답변이 초기화되었습니다."
        ));
    }
}
