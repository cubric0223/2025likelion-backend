package likelion.matching.web.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@Slf4j
@Controller
@RequiredArgsConstructor
public class MatchingViewController {

    @GetMapping("/match-form")
    public String matchFormPage() {
        return "match-api-form";
    }

    @GetMapping("/member-list")
    public String viewMembersPage() {
        return "members";
    }

    @GetMapping("/success")
    public String successPage() {
        return "success";
    }
}
