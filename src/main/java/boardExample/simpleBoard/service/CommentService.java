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
    public Long commentSave(String uid, Long id, CommentDto.Request req) {
        Optional<Member> userid = memberRepository.findByUid(uid);
        Member member = userid.get();
        Board board = boardRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("댓글 쓰기 실패: 해당 게시글이 존재하지 않습니다." + id));
//      해당 게시글의 ref의 가장 큰값이 있다면 MAX값을, 없다면 0을 반환
        Long commentRef = commentRepository.findByRef(board.getUid());
        CommentDto dto = CommentDto.builder()
                .comment(req.getComment())
                .board(board)
                .member(member)
                .build();
//      그룹,깊이,같은그룹내 순서,자식수
        Long ref = commentRef+1L;
        Long step = 0L;
        Long refOrder = 0L;
        Long answerNum = 0L;
//        commentsave에선 dto의 set함수를 통해 초기화를 시켜주고 toEntity를 통해서 comment를 초기화시키고 save로 넘겨줫다
//        parentsave에선 dto의 builder함수를 통해 comment를 초기화하고 member,board를 초기화 해줬다.
        dto.setRef(ref,step,refOrder,answerNum);
        Comment comment = dto.toEntity();
        commentRepository.save(comment);
        return dto.getId();
    }
    //  cid : 부모댓글, 최상의 루트노드를 삭제하면 다같이 삭제된다.
    public Long parentSave(String uid, Long id, Long cid, CommentDto.Request req) {
        Member member = memberRepository.findByUid(uid).get();
        Board board = boardRepository.findById(id).get();
        CommentDto dto = CommentDto.builder()
                .comment(req.getComment())
                .board(board)
                .member(member)
                .build();

//      부모인 댓글을 찾아서 updateParent에 넣어준다
        Comment comment = commentRepository.findById(cid).get();
        Long refOrderResult  = refOrderAndUpdate(comment, board);

        dto.updateParent(comment);
        Long ref = comment.getRef();
        Long step = comment.getStep()+1L;
        Long refOrder = refOrderResult;
        Long answerNum = 0L;
        dto.setRef(ref,refOrder,step,answerNum);
        //부모댓글의 자식컬럼수 + 1 업데이트
        commentRepository.updateAnswerNum(comment.getId(), comment.getAnswernum());
        Comment cmt = dto.toEntity();
        commentRepository.save(cmt);
        return cmt.getId();
    }

    private Long refOrderAndUpdate(Comment comment, Board board) {

        Long saveStep = comment.getStep() + 1l;
        Long refOrder = comment.getReforder();
        Long answerNum = comment.getAnswernum();
        Long ref = comment.getRef();
        //부모 댓글그룹의 answerNum(자식수)
        Long answerNumSum = commentRepository.findBySumAnswerNum(ref, board);
        //부모 댓글그룹의 최댓값 step
        Long maxStep = commentRepository.findByNvlMaxStep(ref, board);
        //저장할 대댓글 step과 그룹내의최댓값 step의 조건 비교
        if (saveStep < maxStep) {
            return answerNumSum + 1l;
        } else if (saveStep == maxStep) {
            commentRepository.updateRefOrderPlus(ref, refOrder + answerNum, board);
            return refOrder + answerNum + 1l;
        } else {
            commentRepository.updateRefOrderPlus(ref, refOrder, board);
            return refOrder + 1l;
        }
    }

    /* UPDATE */
    @Transactional
    public void update(Long id, CommentDto commentDto) {
        String localTime = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date());
        Comment comment = commentRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 댓글이 존재하지 않습니다." + id));
        comment.update(commentDto.getComment(), localTime);
    }
    /* Delete */
    @Transactional
    public void delete(Long id) {
        Comment comment = commentRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 댓글이 존재하지 않습니다. id=" + id));
//      삭제하는 댓글의 부모가 있다면 부모댓글의 자식수를 -1 해준다.
        if (comment.getParent() != null) {
            Long parentId = comment.getParent().getId();
            commentRepository.updateAnswerNumMinus(parentId);
        }
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