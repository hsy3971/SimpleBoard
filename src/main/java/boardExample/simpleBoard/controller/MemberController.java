package boardExample.simpleBoard.controller;

import boardExample.simpleBoard.dto.MemberDto;
import boardExample.simpleBoard.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/signup")
    public String signupForm(Model model) {
        model.addAttribute("signupForm", MemberDto.builder().build().toEntity());
        return "members/signup";
    }

    @PostMapping("/signup")
    public String signUp(@Valid MemberDto memberDto, Errors errors, Model model) {

        if (errors.hasErrors()) {
            // 회원가입 실패 시 입력 데이터 값 유지
            model.addAttribute("signupForm", memberDto);

            // 유효성 검사를 통과하지 못 한 필드와 메시지 핸들링
            Map<String, String> validatorResult = memberService.validateHandling(errors);
            for(String key : validatorResult.keySet()) {
                model.addAttribute(key, validatorResult.get(key));
            }
            return "members/signup";
        }
        memberService.joinMember(memberDto);
        return "redirect:/login";
    }
    /**
     * localhost:8080시 login 으로 redirect
     * @return
     */
    @GetMapping
    public String root() {
        return "redirect:/boards";
    }

    /**
     * 로그인 폼
     * @return
     */
    @GetMapping("/login")
    public String login(){
        return "members/login";
    }

}
