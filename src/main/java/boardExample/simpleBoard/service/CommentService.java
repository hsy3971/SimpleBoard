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
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;

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
//      ref의 제일 큰값을 찾고 있다면 MAX값을, 없다면 0을 return
        Long commentRef = commentRepository.findByRef(board.getUid());

        dto.setMember(member);
        dto.setBoard(board);
        dto.setRef(commentRef+1L);
        dto.setStep(0L);
        dto.setReforder(0L);
        dto.setAnswernum(0L);

        Comment comment = dto.toEntity();
        commentRepository.save(comment);
        return dto.getId();
    }
    //  cid가 해당 부모댓글이다. 최상의 루트노드를 삭제하면 다같이 삭제된다.
    public Long parentSave(String uid, Long id, Long cid, CommentDto.Response response) {
        Member member = memberRepository.findByUid(uid).get();
        Board board = boardRepository.findById(id).get();
        CommentDto dto = CommentDto.builder()
                .comment(response.getComment())
                .board(board)
                .member(member)
                .build();
        dto.setMember(member);
        dto.setBoard(board);

//      부모인 댓글을 찾아서 updateParent에 넣어준다
        Comment comment = commentRepository.findById(cid).get();
        Long refOrderResult  = refOrderAndUpdate(comment);
        if (refOrderResult == null) {
            return null;
        }
        dto.updateParent(comment);
        dto.setRef(comment.getRef());
        dto.setStep(comment.getStep()+1L);
        dto.setReforder(refOrderResult);
        dto.setAnswernum(0L);
        //부모댓글의 자식컬럼수 + 1 업데이트
        commentRepository.updateAnswerNum(comment.getId(), comment.getAnswernum());
        Comment cmt = dto.toEntity();
        commentRepository.save(cmt);
        return cmt.getId();
    }

    private Long refOrderAndUpdate(Comment comment) {

        Long saveStep = comment.getStep() + 1l;
        Long refOrder = comment.getReforder();
        Long answerNum = comment.getAnswernum();
        Long ref = comment.getRef();

        //부모 댓글그룹의 answerNum(자식수)
        Long answerNumSum = commentRepository.findBySumAnswerNum(ref);
        //SELECT SUM(answerNum) FROM BOARD_COMMENTS WHERE ref = ?1
        //부모 댓글그룹의 최댓값 step
        Long maxStep = commentRepository.findByNvlMaxStep(ref);
        //SELECT MAX(step) FROM BOARD_COMMENTS WHERE ref = ?1

        //저장할 대댓글 step과 그룹내의최댓값 step의 조건 비교
        /*
        step + 1 < 그룹리스트에서 max step값  AnswerNum sum + 1 * NO UPDATE
        step + 1 = 그룹리스트에서 max step값  refOrder + AnswerNum + 1 * UPDATE
        step + 1 > 그룹리스트에서 max step값  refOrder + 1 * UPDATE
        */
        if (saveStep < maxStep) {
            return answerNumSum + 1l;
        } else if (saveStep == maxStep) {
            commentRepository.updateRefOrderPlus(ref, refOrder + answerNum);
            //UPDATE BOARD_COMMENTS SET refOrder = refOrder + 1 WHERE ref = ?1 AND refOrder > ?2
            return refOrder + answerNum + 1l;
        } else if (saveStep > maxStep) {
            commentRepository.updateRefOrderPlus(ref, refOrder);
            //UPDATE BOARD_COMMENTS SET refOrder = refOrder + 1 WHERE ref = ?1 AND refOrder > ?2
            return refOrder + 1l;
        }

        return null;
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
        if (comment.getParent() != null) {
            Long parentId = comment.getParent().getId();
            commentRepository.updateAnswerNumMinus(parentId);
        }
//        commentRepository.updateRefOrderMinus(comment.getRef(), comment.getReforder());
        commentRepository.delete(comment);
    }

    @Transactional
    public Page<Comment> findBoardByComments(Long boardId, Pageable pageable) {
        boardRepository.findById(boardId).orElseThrow(() -> new IllegalArgumentException("해당 게시물이 존재하지 않습니다. boardId: " + boardId));
        return commentRepository.findAllByBoardByComments(boardId, pageable);
    }

    @Transactional
    public List<Comment> findAll(Long id) {
        Board board = boardRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("해당 게시글이 존재하지 않습니다. id: " + id));
        return board.getComments();
    }
}