package boardExample.simpleBoard.controller;

import boardExample.simpleBoard.domain.*;
import boardExample.simpleBoard.dto.BoardDto;
import boardExample.simpleBoard.dto.MemberDto;
import boardExample.simpleBoard.service.*;
import lombok.RequiredArgsConstructor;
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
    public String boards(Model model, @PageableDefault Pageable pageable, String searchKeyword) {
        Page<Board> list = null;
        MemberDto.UserSessionDto user = (MemberDto.UserSessionDto) httpSession.getAttribute("user");
        if (user != null) {
            model.addAttribute("info", user.getUname());
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
    public String board(@PathVariable("uid") Long uid, Model model, @PageableDefault Pageable pageable, HttpServletRequest request, HttpServletResponse response) {
        MemberDto.UserSessionDto user = (MemberDto.UserSessionDto) httpSession.getAttribute("user");
        Board board = boardService.BoardOne(uid).get();
        boolean like = false;
        Long id = null;
        if (user != null) {
            id = user.getUno();
            Member likeMember = memberService.findByUno(id);
            model.addAttribute("userid", user);
            // 현재 로그인한 유저가 이 게시물을 좋아요 했는지 안 했는지 여부 확인
            like = boardService.findLike(board, likeMember);
        }
        // 조회수 증가
        boardService.updateView(board.getUid(), request, response);
        // 해당 게시물의 1페이지의 갯수만큼 list에 저장된다(만약 1페이지당 10개로 지정했다면 10개에 해당되는 댓글들이 저장된다.)
        Page<Comment> list = commentService.findBoardByComments(board.getUid(), pageable);
        List<Comment> cnt = board.getComments();
        // 게시글을 등록한 사람인지 확인
        if (id != null) {
            if (id.equals(board.getMember().getUno())) {
                model.addAttribute("writer", true);
            }
        }
        // 해당 게시물의 댓글의 갯수
        int totalComments = cnt.size();
        // 한 페이지의 댓글 갯수
        int pageSize = list.getSize();
        // 필요한 페이지 수
        int totalPages = 0;
        // 페이징 계산
        if (pageSize != 0) {
            totalPages = ((totalComments % pageSize) == 0) ? totalComments / pageSize : (totalComments / pageSize) + 1;
        }
        if (totalComments != 0) {
            model.addAttribute("comments", list);
        }
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("board", board);
        model.addAttribute("like", like);
        return "boards/Board";
    }

    // 게시글 등록
    @GetMapping("/add")
    public String addForm(Model model) {
        MemberDto.UserSessionDto user = (MemberDto.UserSessionDto) httpSession.getAttribute("user");
        String name = user.getUname();
        Board board = BoardDto.builder().build().toEntity();
        board.setName(name);
        model.addAttribute("board", board);
        return "boards/addForm";
    }

    // 게시글 수정
    @GetMapping("/{uid}/edit")
    public String editForm(@PathVariable("uid") Long uid, Model model) {
        Board board = boardService.BoardOne(uid).get();
        model.addAttribute("board", board);
        return "boards/editForm";
    }
}