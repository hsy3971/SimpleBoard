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

    //Get방식
    @GetMapping("/signup")
    public String signupForm(Model model) {
        model.addAttribute("signupForm", MemberDto.builder().build().toEntity());
        return "members/signup";
    }

    @PostMapping("/signup")
    public String signUp(@Valid MemberDto memberDto, Errors errors, Model model) {

        if (errors.hasErrors()) {
            /* 회원가입 실패 시 입력 데이터 값 유지 */
            model.addAttribute("signupForm", memberDto);

            /* 유효성 검사를 통과하지 못 한 필드와 메시지 핸들링 */
            Map<String, String> validatorResult = memberService.validateHandling(errors);
            for(String key : validatorResult.keySet()) {
                model.addAttribute(key, validatorResult.get(key));
            }
            return "members/signup";
        }
        // 구조를 바꿔준 이유에 대해서 정확히 알기(결국 memberDto로 받아도 MemberService에서 다시 Member타입으로 선언해서 dto로 받은것을 Member타입에 넣어주면 된다.)
        memberService.joinMember(memberDto);
        //회원가입이 정상적으로 완료됐다면 홈화면으로 돌린다
        //단순 return "viewName"같은 경우는 viewName에 해당하는 view를 보여주는 것이고, return "redirect:/"같은 경우는 redirect오른쪽 주소로 URL 요청을 다시 하는 것이다.
        return "redirect:/login";
    }
    /**
     * localhost:8080시 login 으로 redirect
     * @return
     */
    @GetMapping
    public String root() {
        return "redirect:/login";
    }

    /**
     * 로그인 폼
     * @return
     */
//    실제 로그인을 진행하는 @PostMapping 방식의 메서드는 스프링 시큐리티가 대신 처리하므로 직접 구현할 필요가 없다.
    @GetMapping("/login")
    public String login(){
        return "members/login";
    }

}
