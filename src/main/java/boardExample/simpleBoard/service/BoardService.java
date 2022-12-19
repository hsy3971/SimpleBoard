package boardExample.simpleBoard.service;

import boardExample.simpleBoard.domain.Board;
import boardExample.simpleBoard.domain.Member;
import boardExample.simpleBoard.dto.BoardDto;
import boardExample.simpleBoard.repository.BoardRepository;
import boardExample.simpleBoard.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;

    public List<Board> BoardList() {
        return boardRepository.findAll();
    }
    // 페이징
    @Transactional
    public Page<Board> pageList(Pageable pageable) {
        int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1);
        pageable = PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "uid"));
        return boardRepository.findAll(pageable);
    }

    public Optional<Board> BoardOne(Long uid) {
        return boardRepository.findById(uid);
    }
    @Transactional
    public Long BoardAdd(BoardDto board) {
        Member num = memberRepository.findByUno(board.getMember().getUno());
        Board data = board.toEntity();
        return boardRepository.save(data).getUid();
    }

    public void BoardDelete(Long uid) {
        boardRepository.deleteById(uid);
    }

    @Transactional
    public Long BoardUpdate(Long uid, BoardDto boardDto) {
        SimpleDateFormat format = new SimpleDateFormat ( "yyyy-MM-dd HH:mm");
        Date time = new Date();
        String localTime = format.format(time);
        // updateBoard를 통해서 제목과 내용만 바꿀 수 있게 설정
        Board board = boardRepository.findById(uid).orElseThrow(()->new IllegalArgumentException("해당 게시글이 없습니다. id = " + uid));
        board.updateBoard(boardDto.getSubject(), boardDto.getContent(), localTime);
        return uid;
    }

    @Transactional
    public Page<Board> findBySubjectContaining(String searchKeyword, Pageable pageable) {
        int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1);
        pageable = PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "uid"));
        return boardRepository.findBySubjectContaining(searchKeyword, pageable);
    }
    @Transactional
    public int viewCount(Long uid) {
        return boardRepository.viewCount(uid);
    }

    @Transactional
    public Member findUno(Long uid) {
        return memberRepository.findByUno(uid);
    }

}
