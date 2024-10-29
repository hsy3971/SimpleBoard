package boardExample.simpleBoard.controller.api;

import boardExample.simpleBoard.domain.Comment;
import boardExample.simpleBoard.domain.Member;
import boardExample.simpleBoard.dto.CommentDto;
import boardExample.simpleBoard.dto.MemberDto;
import boardExample.simpleBoard.repository.MemberRepository;
import boardExample.simpleBoard.service.CommentService;
import boardExample.simpleBoard.service.MemberService;
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
    private final MemberService memberService;
    private final CommentService commentService;
    private final HttpSession httpSession;
    //read
    @GetMapping("/boards/{bid}/comments")
    public List<Comment> read(@PathVariable Long bid) {
        return commentService.findAll(bid);
    }
    //create
    @PostMapping("/boards/{bid}/comments")
    public ResponseEntity commentSave(@PathVariable("bid") Long id, @RequestBody CommentDto.Request req) {
        String uid = (String) httpSession.getAttribute("uid");
        Member user = memberService.findByUid(uid).get();
        String userid = user.getUid();
        return ResponseEntity.ok(commentService.commentSave(userid, id, req));
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

    //답글 작성
    @PostMapping(value = "/boards/{boardId}/comments/reply")
    @ResponseBody
    public ResponseEntity replySave(@PathVariable("boardId") Long boardId, @RequestBody CommentDto.Request req) {
        String uid = (String) httpSession.getAttribute("uid");
        Member user = memberService.findByUid(uid).get();
        String userid = user.getUid();
        return ResponseEntity.ok(commentService.parentSave(userid, boardId, req.getParentId(), req));
    }
}