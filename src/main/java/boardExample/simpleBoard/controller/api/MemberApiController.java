package boardExample.simpleBoard.controller.api;

import boardExample.simpleBoard.domain.Member;
import boardExample.simpleBoard.dto.BoardDto;
import boardExample.simpleBoard.dto.MemberDto;
import boardExample.simpleBoard.exception.BadRequestException;
import boardExample.simpleBoard.repository.MemberRepository;
import boardExample.simpleBoard.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MemberApiController {
    private final MemberService memberService;

    @GetMapping("/users-ids/{uid}/exists")
    public ResponseEntity<String> checkIdDuplication(@RequestParam(value = "uid") String uid) throws BadRequestException {
        if (memberService.existsByMemberId(uid) == true) {
            throw new BadRequestException("이미 사용중인 아이디 입니다.");
        } else {
            return ResponseEntity.ok("사용 가능한 아이디 입니다.");
        }
    }
    // 조회
    @GetMapping("/users-ids/{uid}")
    public ResponseEntity read(@PathVariable("uid") Long uid) {
        return ResponseEntity.ok(memberService.findByUno(uid));
    }
}
