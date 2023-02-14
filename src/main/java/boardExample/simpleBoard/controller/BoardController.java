package boardExample.simpleBoard.controller;

import boardExample.simpleBoard.domain.*;
import boardExample.simpleBoard.dto.BoardDto;
import boardExample.simpleBoard.dto.MemberDto;
import boardExample.simpleBoard.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/boards")
public class BoardController {
    private final MemberService memberService;
    private final BoardService boardService;
    private final CommentService commentService;
    private final HttpSession httpSession;

    //목록 조회
    @GetMapping
    public String boards(Model model, Authentication authentication, @PageableDefault Pageable pageable, String searchKeyword) {
        Page<Board> list = null;
        MemberDto.UserSessionDto user = (MemberDto.UserSessionDto) httpSession.getAttribute("user");
        if (user != null) {
            model.addAttribute("info", user.getUname());
        }
        else {
            if (authentication != null) {
                Member member = (Member) authentication.getPrincipal();  //userDetail 객체를 가져옴
                model.addAttribute("info", member.getUname());
            }
        }

        if (searchKeyword == null) {
            list = boardService.pageList(pageable);
        }
        else {
            list = boardService.findBySubjectContaining(searchKeyword, pageable);
        }
        model.addAttribute("boards", list);

        return "boards/Boards";
    }
    //게시글 조회(게시글 상세)
    @GetMapping("/{uid}")
    public String board(@PathVariable("uid") Long uid, Model model, Authentication authentication, @PageableDefault Pageable pageable, HttpServletRequest request, HttpServletResponse response) {
        MemberDto.UserSessionDto user = (MemberDto.UserSessionDto) httpSession.getAttribute("user");
        Board board = boardService.BoardOne(uid).get();
        Long id = null;
        boolean like = false;

        if (user != null) {
            id = user.getUno();
            Member likeMember = memberService.findByUno(id);
            model.addAttribute("userid", user);
//            현재 로그인한 유저가 이 게시물을 좋아요 했는지 안 했는지 여부 확인
            like = boardService.findLike(board, likeMember);
        }
        // 일반 로그인일때
        else {
            if (authentication != null) {
                Member member = (Member) authentication.getPrincipal();
                id = member.getUno();
                Member likeMember = memberService.findByUno(id);
                model.addAttribute("userid", member);
//                현재 로그인한 유저가 이 게시물을 좋아요 했는지 안 했는지 여부 확인
                like = boardService.findLike(board, likeMember);
            }
        }
        Page<Comment> list = null;
//        uid:게시판의 id
        Long bId = board.getUid();
        boardService.updateView(bId, request, response);
        list = commentService.findBoardByComments(bId, pageable);
        List<Comment> cnt = board.getComments();
        int cnt_size = cnt.size();
        int list_size = list.getSize();
        int mok = cnt_size / list_size;
        int res = cnt_size % list_size;

        if (res != 0) {
            mok += 1;
        }
        if (cnt_size != 0) {
            model.addAttribute("comments", list);
        }
        model.addAttribute("totalPages", mok);

        if (id != null) {
            if (id.equals(board.getMember().getUno())) {
                model.addAttribute("writer", true);
            }
        }
        model.addAttribute("board", board);
        model.addAttribute("like", like);
        return "boards/Board";
    }

    // 게시글 등록
    @GetMapping("/add")
    public String addForm(Model model, Authentication authentication) {
        MemberDto.UserSessionDto user = (MemberDto.UserSessionDto) httpSession.getAttribute("user");
        String name = null;
        if (user != null) {
            name = user.getUname();
        }
        else {
            Member member = (Member) authentication.getPrincipal();
            name = member.getUname();
        }
        Board board = BoardDto.builder().build().toEntity();
        board.setName(name);
        model.addAttribute("board", board);
        return "boards/addForm";
    }

    // 게시글 수정
    @GetMapping("/{uid}/edit")
    public String editForm(@PathVariable("uid") Long uid, Model model) {
        Optional<Board> result = boardService.BoardOne(uid);
        Board board = result.get();
        model.addAttribute("board", board);
        return "boards/editForm";
    }

    // 게시글 좋아요
    @PostMapping("/{uid}/like")
//    api호출을 위해 선언
    @ResponseBody
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