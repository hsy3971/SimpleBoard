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
//final
@RequiredArgsConstructor
//이렇게 RequestMapping할시에 이것이 default url이 된다.
//그리고 index.html이 없을시 404에러발생(설계를 할때 기본적으로 index.html은 있어야함)
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
    //웹실행 안될시 밑에 url다시 점검
    //게시글 조회(게시글 상세)
    //Long형의 uid가 의미하는 것은 게시판의 uid값에 해당(해당 uid의 게시판을 찾기 위해서 받아주는 파라미터)
    @GetMapping("/{uid}")
    public String board(@PathVariable("uid") Long uid, Model model, Authentication authentication, @PageableDefault Pageable pageable) {
        MemberDto.UserSessionDto user = (MemberDto.UserSessionDto) httpSession.getAttribute("user");
        Long id = null;
        // 구글 로그인일때(session의 유무로 로그인 상태를 판단하고 각 상태에 따라 코딩을 해주었다.) -> 내가 모를 원인이 있을수도 있음.
        if (user != null) {
            id = user.getUno();
            //        member라는 로그인한 유저정보를 던져줘서 뷰에서 .uid를 통해 뷰에 comment.member.uid와 비교를 한다.
            model.addAttribute("userid", user);
        }
        // 일반 로그인일때
        else {
            if (authentication != null) {
                Member member = (Member) authentication.getPrincipal();
                id = member.getUno();
                //        member라는 로그인한 유저정보를 던져줘서 뷰에서 .uid를 통해 뷰에 comment.member.uid와 비교를 한다.
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
//        board의 외래키인 u_no와 Long형에 해당하는 로그인한 아이디의 u_no와 비교(name으로 비교시 동명이인이 있을 수 있기떄문에 불가)
        if (id != null) {
            if (id.equals(board.getMember().getUno())) {
                model.addAttribute("writer", true);
            }
        }
//        밑에 방식으로 comment마다 아이디를 비교를 해주고 맞다면 true 아니라면 false를 던져준다.
//        댓글리스트가 여러개이기 떄문에 true,true,false이런식으로 넘어올수있다. false값만 있다면 버튼이 비활성화된다.
//        근데 위처럼 true,true,false 이런식으로 되면은 마지막이 false가 떴기때문에 모두 버튼이 비활성화돼버린다.
//        비슷한 이유로 만약 t,t값으로 모두 수정과 삭제버튼이 잘 뜨고 있는데 다른아이디로 로그인을 하고 댓글을 작성시 모두 비활성화되버림.
//        이유는 모르겠지만 isWriter로 넘겼을때 값이 모두 통일되지 않아도 결국엔 원인모를 이유로 값이 통일되버린다.
//        그래서 밑에 member라는 객체를 model을 통해 넘겨서 뷰에서 비교를 시행하였다.
//        for (int i=0; i<comments.size(); i++) {
//            // comments에서 getMember에 getUid를 했을때 댓글을 작성한 id를 가져올까?
//            boolean isWriter = comments.get(i).getMember().getUid().equals(member.getUid());
//            log.info("isWriter? : " + isWriter);
//            model.addAttribute("isWriter", isWriter);
//        }
        model.addAttribute("board", board);

        return "boards/Board";
    }

    // 일단 setter의 대안인 builder를 사용했는데 혹시라도 돌려봤을 시 안될경우에는 그냥 protected없애고 생성자로 추가하자.(최대한 되게 해보고나서)
    // 게시글 등록
    // https://coding-nyan.tistory.com/127(spring security로 로그인한 사용자 정보를 가져오는법)
    // modelattribute를 여러개 넘기는 것보다 리스트로 한꺼번에 받아서 넘기는 것이 바람직하다.
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
        // 멤버의 uno에 해당하는 회원을 찾는다
        // 금단의 setter를 써버림(이걸 쓴 이유가 작성자에 name을 출력하기 위해서 썼다... 사실 없어도 괜찮을꺼같은데?? 일단 보류)
        board.setName(name);
        //이것도 toEntity()를 통해 값을 넣어주었는데 혹시라도 빠뜨린게 있을수있기 때문에 보류
        model.addAttribute("board", board);
        return "boards/addForm";
    }
    // 첫번째로 get방식에서 setMember와 setName을 통해 board를 업데이트하고 model로 넘겨줬는데 착각했던 것이 저렇게 get을 통해 뷰로 넘겨주면 당연히 post방식에서도
    // 변경했던 값이 그대로 적용되는 줄 알았다. 실제로 setName을 통해 이름이 잘 전달이 됐었으니까 하지만 착각이었다. get에서 model로 뷰로 넘겨주었을때 form의 형태로 action,method를 입력함으로써
    // 다시 post방식으로 넘겨주는데 여기서 input type=hidden을 통해 th:field로 name이라는 값을 넘겨주고 있었기때문에 setName을 출력하면 잘 나오는 것이었다. 그래서 마찬가지로
    // 내가 get에서 setMember를 해주고 똑같이 hidden으로 member라는 값을 post방식에 넘겨주니 데이터가 잘 전달이 되었다.
//    핵심은 input type=hidden을 언제 쓰고 어떻게 쓰는지이다.(사용법을 잘 익혀두자) -> addForm에 어떨때 쓰는지 메모해둠.
//    @PostMapping("/add")
//    public String addBoard(@Valid @ModelAttribute BoardDto board, BindingResult result) {
//        if (result.hasErrors()) {
//            return "boards/addForm";
//        }
//        boardService.BoardAdd(board);
//        return "redirect:/boards";
//    }

    // RequestMapping : GET 방식과 POST 방식을 모두 처리
    // GET방식의 경우 데이터를 화면에 뿌릴 때 많이 사용되고 POST방식은 전송한 데이터를 insert할 때 많이 사용된다.
    // 번외로 RequestMapping말고 get,post를 쓰는이유
    // get과 post사용시 url을 중복 사용할 수 있다(가독성 증가, url을 경제적으로 사용할 수 있다.
    // @RequestMapping("/insertBoard")으로 GET 방식의 요청을 받았다면 POST 방식의 요청은 다른 url을 써야한다.

    // 게시글 수정
    // 여기도 BoardDto로 바꿔줘야됨.
    @GetMapping("/{uid}/edit")
    public String editForm(@PathVariable("uid") Long uid, Model model) {
        Optional<Board> result = boardService.BoardOne(uid);
        Board board = result.get();
        model.addAttribute("board", board);
        return "boards/editForm";
    }
}