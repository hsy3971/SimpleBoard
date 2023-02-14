package boardExample.simpleBoard.controller;

import boardExample.simpleBoard.domain.Member;
import boardExample.simpleBoard.dto.CommentDto;
import boardExample.simpleBoard.dto.MemberDto;
import boardExample.simpleBoard.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
    private final HttpSession httpSession;

    @PostMapping(value = "/boards/{boardId}/comments/reply")
    @ResponseBody
    public ResponseEntity replySave(@PathVariable("boardId") Long boardId, @RequestBody CommentDto.Response response, Authentication authentication, @PageableDefault Pageable pageable) {
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
        return ResponseEntity.ok(commentService.parentSave(uid, boardId, response.getParentId(), response));
    }
}
