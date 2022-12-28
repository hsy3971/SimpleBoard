package boardExample.simpleBoard.controller.api;

import boardExample.simpleBoard.domain.Board;
import boardExample.simpleBoard.domain.Member;
import boardExample.simpleBoard.dto.BoardDto;
import boardExample.simpleBoard.dto.MemberDto;
import boardExample.simpleBoard.service.BoardService;
import boardExample.simpleBoard.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class BoardApiController {
    private final MemberService memberService;
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
        Member num = memberService.findByUno(uno);
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
    // 글 좋아요
    @PostMapping("/boards/{uid}/like")
    public boolean like(@PathVariable Long uid, Authentication authentication){
        MemberDto.UserSessionDto user = (MemberDto.UserSessionDto) httpSession.getAttribute("user");
        Long uno = null;
        if (user != null) {
            uno = user.getUno();
        }
        else {
            Member member = (Member) authentication.getPrincipal();
            uno = member.getUno();
        }
        Board board = boardService.BoardOne(uid).get();
        Member member = memberService.findByUno(uno);
        // 저장 true, 삭제 false
        boolean result = boardService.saveLike(board, member);
        return result;
    }
}
