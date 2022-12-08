package boardExample.simpleBoard.service;

import boardExample.simpleBoard.domain.Board;
import boardExample.simpleBoard.domain.Comment;
import boardExample.simpleBoard.domain.Member;
import boardExample.simpleBoard.dto.BoardDto;
import boardExample.simpleBoard.dto.CommentDto;
import boardExample.simpleBoard.repository.BoardRepository;
import boardExample.simpleBoard.repository.CommentRepository;
import boardExample.simpleBoard.repository.MemberRepository;
import ch.qos.logback.core.pattern.color.BlackCompositeConverter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class CommentServiceTest {

    @Autowired CommentRepository commentRepository;
    @Autowired CommentService commentService;
    @Autowired
    BoardRepository boardRepository;
    @Autowired
    MemberRepository memberRepository;

    @Test
    public void 댓글작성() throws Exception {
        Member member = Member.builder().uname("HongSung").uid("foodb00").u_pw("0000").email("foodb00@naver.com").u_auth("USER").build();
        Member realmember = memberRepository.save(member);
        BoardDto boards = BoardDto.builder()
                .uid(123L)
                .subject("제목1")
                .content("내용1")
                .name(realmember.getUname())
                .viewcnt(0)
                .member(realmember)
                .build();
        Board board = boards.toEntity();
        Board realboard = boardRepository.save(board);
        CommentDto dto = CommentDto.builder()
                .id(null)
                .comment("안녕")
                .board(realboard)
                .member(realmember)
                .build();
        Comment cmt = dto.toEntity();
        Comment result = commentRepository.save(cmt);
        System.out.println("cmt.getId() = " + cmt.getId());
        System.out.println("result.getId() = " + result.getId());
        assertEquals(cmt.getId(), result.getId());
    }
    @Test
    public void 댓글수정() throws Exception {
        Member member = Member.builder().uname("HongSung").uid("foodb00").u_pw("0000").email("foodb00@naver.com").u_auth("USER").build();
        Member realmember = memberRepository.save(member);
        BoardDto boards = BoardDto.builder()
                .uid(123L)
                .subject("제목1")
                .content("내용1")
                .name(realmember.getUname())
                .viewcnt(0)
                .member(realmember)
                .build();
        Board board = boards.toEntity();
        Board realboard = boardRepository.save(board);
        CommentDto dto = CommentDto.builder()
                .id(null)
                .comment("안녕")
                .board(realboard)
                .member(realmember)
                .build();
        Comment cmt = dto.toEntity();
        Comment result = commentRepository.save(cmt);
        CommentDto dto2 = CommentDto.builder()
                .comment("바이루")
                .build();
        commentService.update(result.getId(), dto2);
        Optional<Comment> find = commentRepository.findById(result.getId());
        assertEquals(result.getId(), find.get().getId());
    }
    @Test
    public void 댓글삭제() throws Exception {
        Member member = Member.builder().uname("HongSung").uid("foodb00").u_pw("0000").email("foodb00@naver.com").u_auth("USER").build();
        Member realmember = memberRepository.save(member);
        BoardDto boards = BoardDto.builder()
                .uid(123L)
                .subject("제목1")
                .content("내용1")
                .name(realmember.getUname())
                .viewcnt(0)
                .member(realmember)
                .build();
        Board board = boards.toEntity();
        Board realboard = boardRepository.save(board);
        CommentDto dto = CommentDto.builder()
                .id(null)
                .comment("안녕")
                .board(realboard)
                .member(realmember)
                .build();
        Comment cmt = dto.toEntity();
        Comment result = commentRepository.save(cmt);
        Optional<Comment> cmtId = commentRepository.findById(result.getId());
        System.out.println("cmtId = " + cmtId);
        commentService.delete(result.getId());
        Optional<Comment> cmtId2 = commentRepository.findById(result.getId());
        System.out.println("cmtId2 = " + cmtId2);

    }
    @Test
    public void 댓글조회() throws Exception {
        Member member = Member.builder().uname("HongSung").uid("foodb00").u_pw("0000").email("foodb00@naver.com").u_auth("USER").build();
        Member realmember = memberRepository.save(member);
        BoardDto boards = BoardDto.builder()
                .uid(123L)
                .subject("제목1")
                .content("내용1")
                .name(realmember.getUname())
                .viewcnt(0)
                .member(realmember)
                .build();
        Board board = boards.toEntity();
        Board realboard = boardRepository.save(board);
        CommentDto dto = CommentDto.builder()
                .id(null)
                .comment("안녕")
                .board(realboard)
                .member(realmember)
                .build();
        Comment cmt = dto.toEntity();
        Comment result = commentRepository.save(cmt);
        Optional<Comment> find = commentRepository.findById(result.getId());
        System.out.println("find.get().getComment() = " + find.get().getComment());
    }
}