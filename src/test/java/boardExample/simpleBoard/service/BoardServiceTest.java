package boardExample.simpleBoard.service;

import boardExample.simpleBoard.domain.Board;
import boardExample.simpleBoard.domain.Member;
import boardExample.simpleBoard.dto.BoardDto;
import boardExample.simpleBoard.repository.BoardRepository;
import boardExample.simpleBoard.repository.MemberRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class BoardServiceTest {
    @Autowired BoardRepository boardRepository;
    @Autowired BoardService boardService;
    @Autowired MemberRepository memberRepository;

    @Test
    public void 게시글생성() throws Exception {
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
        boardRepository.save(board);
        Optional<Board> board1 = boardRepository.findById(boards.getUid());
        Optional<Board> board2 = boardRepository.findById(board.getUid());
        //then
        assertEquals(board1, board2);
    }
    @Test
    public void 게시글삭제() throws Exception {
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
        Board save = boardRepository.save(board);
        Optional<Board> id1 = boardRepository.findById(save.getUid());
        System.out.println("id1 = " + id1);
        boardService.BoardDelete(save.getUid());
        Optional<Board> id2 = boardRepository.findById(save.getUid());
        System.out.println("id2 = " + id2);
    }
    @Test
    public void 게시글수정() throws Exception {
        Member member = Member.builder().uname("HongSung").uid("foodb00").u_pw("0000").email("foodb00@naver.com").u_auth("USER").build();
        Member realmember = memberRepository.save(member);
        BoardDto boards1 = BoardDto.builder()
                .uid(123L)
                .subject("제목1")
                .content("내용1")
                .name(realmember.getUname())
                .viewcnt(0)
                .member(realmember)
                .build();
        Board board = boards1.toEntity();
        Board save = boardRepository.save(board);
        System.out.println("board getSub = " + board.getSubject());
        System.out.println("board getCont = " + board.getContent());
        BoardDto boards2 = BoardDto.builder()
                .subject("제목2")
                .content("내용2")
                .name(member.getUname())
                .viewcnt(0)
                .member(member)
                .build();
        boardService.BoardUpdate(save.getUid(), boards2);
        System.out.println("save getSub = " + save.getSubject());
        System.out.println("savee getCont = " + save.getContent());
    }

    @Test
    public void 게시글조회() throws Exception {
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
        Board save = boardRepository.save(board);
        Optional<Board> id1 = boardRepository.findById(save.getUid());
        System.out.println("id1.get().getSubject() = " + id1.get().getUid());
    }
}