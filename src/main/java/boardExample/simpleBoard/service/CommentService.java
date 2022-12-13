package boardExample.simpleBoard.service;

import boardExample.simpleBoard.domain.Board;
import boardExample.simpleBoard.domain.Comment;
import boardExample.simpleBoard.domain.Member;
import boardExample.simpleBoard.dto.CommentDto;
import boardExample.simpleBoard.repository.BoardRepository;
import boardExample.simpleBoard.repository.CommentRepository;
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
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;

//    CREATE
    @Transactional
    public Long commentSave(String uid, Long id, CommentDto dto) {
        Optional<Member> userid = memberRepository.findByUid(uid);
        Member member = userid.get();
        Board board = boardRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("댓글 쓰기 실패: 해당 게시글이 존재하지 않습니다." + id));

        dto.setMember(member);
        dto.setBoard(board);

        Comment comment = dto.toEntity();
        commentRepository.save(comment);
        return dto.getId();
    }

    /* UPDATE */
    @Transactional
    public void update(Long id, CommentDto commentDto) {
        SimpleDateFormat format = new SimpleDateFormat ( "yyyy-MM-dd HH:mm");
        Date time = new Date();
        String localTime = format.format(time);
        Comment comment = commentRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 댓글이 존재하지 않습니다." + id));
        comment.update(commentDto.getComment(), localTime);
    }

    @Transactional
    public void delete(Long id) {
        Comment comment = commentRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 댓글이 존재하지 않습니다. id=" + id));

        commentRepository.delete(comment);
    }
    @Transactional
    public Page<Comment> commentpageList(Pageable pageable, Board board) {
        int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1);
//        페이지 번호(0부터 시작하기때문에 ex) 1이라면 0으로 저장된다?), 페이지당 데이터의 수, 정렬방향, uid별로 정렬하라
        pageable = PageRequest.of(page, 3, Sort.by(Sort.Direction.DESC, "id"));
        System.out.println("pageable = " + pageable);
        return commentRepository.findAllByBoard(board, pageable);
    }

    /* READ */
//    해당 게시글에 대한 댓글리스트를 가져온다.
//    @Transactional(readOnly = true)
//    public List<CommentDto.Response> findAll(Long id) {
//        Board board = boardRepository.findById(id).orElseThrow(() ->
//                new IllegalArgumentException("해당 게시글이 존재하지 않습니다. id: " + id));
//        List<Comment> comments = board.getComments();
//        System.out.println("comments = " + comments);
//        return comments.stream().map(CommentDto.Response::new).collect(Collectors.toList());
//    }
}
