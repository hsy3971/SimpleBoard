package boardExample.simpleBoard.controller.api;

import boardExample.simpleBoard.domain.Comment;
import boardExample.simpleBoard.domain.Member;
import boardExample.simpleBoard.dto.CommentDto;
import boardExample.simpleBoard.dto.MemberDto;
import boardExample.simpleBoard.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CommentApiController {
    private final CommentService commentService;
    private final HttpSession httpSession;
    //read
    @GetMapping("/boards/{bid}/comments")
    public List<Comment> read(@PathVariable Long bid) {
        return commentService.findAll(bid);
    }
    //create
    @PostMapping("/boards/{bid}/comments")
    public ResponseEntity commentSave(@PathVariable("bid") Long id, @RequestBody CommentDto.Response response, Authentication authentication) {
        MemberDto.UserSessionDto user = (MemberDto.UserSessionDto) httpSession.getAttribute("user");
        String uid = null;
//        세션일때와 아닐때로 구글로그인연동인지 일반 로그인인지를 구분
        if (user != null) {
            uid = user.getUid();
        }
        else {
            Member member = (Member) authentication.getPrincipal();  //userDetail 객체를 가져옴
            uid = member.getUid();
        }
        return ResponseEntity.ok(commentService.commentSave(uid, id, response));
    }
    //update
    @PutMapping({"/boards/{bid}/comments/{cid}"})
    public ResponseEntity update(@PathVariable Long bid, @PathVariable Long cid, @RequestBody CommentDto commentDto) {
        commentService.update(cid, commentDto);
        return ResponseEntity.ok(cid);
    }

    //delete
    @DeleteMapping("/boards/{bid}/comments/{cid}")
    public ResponseEntity delete(@PathVariable Long bid, @PathVariable Long cid) {
        commentService.delete(cid);
        return ResponseEntity.ok(cid);
    }
}