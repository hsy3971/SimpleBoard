package boardExample.simpleBoard.controller.api;

import boardExample.simpleBoard.domain.Member;
import boardExample.simpleBoard.dto.BoardDto;
import boardExample.simpleBoard.dto.MemberDto;
import boardExample.simpleBoard.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class BoardApiController {
    private final BoardService boardService;
    private final HttpSession httpSession;

    // 비동기 게시글 저장
    @PostMapping("/boards")
    public ResponseEntity create(@RequestBody BoardDto boardDto, Authentication authentication) {
        MemberDto.UserSessionDto user = (MemberDto.UserSessionDto) httpSession.getAttribute("user");
        Long uno = null;
        if (user != null) {
            uno = user.getUno();
        }
        else {
            Member member = (Member) authentication.getPrincipal();
            uno = member.getUno();
        }
        Member num = boardService.findUno(uno);
        boardDto.setMember(num);
        return ResponseEntity.ok(boardService.BoardAdd(boardDto));
    }
    // 비동기 게시글 수정
    @PutMapping("/boards/{uid}")
    public ResponseEntity update(@PathVariable("uid") Long uid, @RequestBody BoardDto dto) {
        boardService.BoardUpdate(uid, dto);
        return ResponseEntity.ok(uid);
    }
    /* 비동기 게시글 삭제 */
    @DeleteMapping("/boards/{uid}")
    public ResponseEntity delete(@PathVariable Long uid) {
        boardService.BoardDelete(uid);
        return ResponseEntity.ok(uid);
    }
}
