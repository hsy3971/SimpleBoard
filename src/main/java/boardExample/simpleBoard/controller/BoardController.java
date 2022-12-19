package boardExample.simpleBoard.controller;

import boardExample.simpleBoard.domain.Board;
import boardExample.simpleBoard.domain.Comment;
import boardExample.simpleBoard.domain.Member;
import boardExample.simpleBoard.dto.BoardDto;
import boardExample.simpleBoard.dto.MemberDto;
import boardExample.simpleBoard.exception.BadRequestException;
import boardExample.simpleBoard.repository.MemberRepository;
import boardExample.simpleBoard.service.BoardService;
import boardExample.simpleBoard.service.CommentService;
import boardExample.simpleBoard.service.MemberDetailsServiceImpl;
import boardExample.simpleBoard.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/boards")
public class BoardController {
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
    public String board(@PathVariable("uid") Long uid, Model model, Authentication authentication, @PageableDefault Pageable pageable) {
        MemberDto.UserSessionDto user = (MemberDto.UserSessionDto) httpSession.getAttribute("user");
        Long id = null;

        if (user != null) {
            id = user.getUno();
            model.addAttribute("userid", user);
        }
        // 일반 로그인일때
        else {
            if (authentication != null) {
                Member member = (Member) authentication.getPrincipal();
                id = member.getUno();
                model.addAttribute("userid", member);
            }
        }
        Page<Comment> list = null;
        boardService.viewCount(uid);
        Optional<Board> result = boardService.BoardOne(uid);
        Board board = result.get();
        list = commentService.commentpageList(pageable, board);

        if (list != null && !list.isEmpty()) {
            model.addAttribute("comments", list);
        }

        if (id != null) {
            if (id.equals(board.getMember().getUno())) {
                model.addAttribute("writer", true);
            }
        }
        model.addAttribute("board", board);

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
}