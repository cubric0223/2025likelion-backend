package likelion.matching.web.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@Slf4j
@Controller
@RequiredArgsConstructor
public class MatchingViewController {



//    @GetMapping("/member-list")
//    public String viewMembersPage() {
//        return "members";
//    }

    @GetMapping("/admin/members")
    public String viewMembersPage() {
        return "members";
    }

    @GetMapping("/admin/matching")
    public String matching(){
        return "matching";
    }

    @GetMapping("/login")
    public String loginPage(){
        return "loginForm";
    }

    @GetMapping("/matching-page")
    public String matchingResultPage(){
        return "matching-result";
    }
    @GetMapping("/admin/reset")
    public String resetDBPage(){
        return "reset-db";
    }

    @GetMapping(value = {"/", "/{path:^(?!api|assets|static).*$}/**"})
    public String forward() {
        return "index";
    }
}