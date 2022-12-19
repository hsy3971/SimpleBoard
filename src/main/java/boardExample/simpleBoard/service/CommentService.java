package boardExample.simpleBoard.service;

import boardExample.simpleBoard.domain.Board;
import boardExample.simpleBoard.domain.Comment;
import boardExample.simpleBoard.domain.DeleteStatus;
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

        Comment parent = null;
        dto.setMember(member);
        dto.setBoard(board);
//      자식 댓글인 경우
        if (dto.getParent() != null) {
            parent = commentRepository.findCommentByIdWithParent(dto.getId()).get();
            dto.updateParent(parent);
        }
        Comment comment = dto.toEntity();
        commentRepository.save(comment);
        return dto.getId();
    }

    public Long parentSave(String uid, Long id, Long cid, CommentDto dto) {
        Member member = memberRepository.findByUid(uid).get();
        Board board = boardRepository.findById(id).get();
        dto.setMember(member);
        dto.setBoard(board);
//      부모인 댓글을 찾아서 updateParent에 넣어준다
        Comment comment = commentRepository.findById(cid).get();
        dto.updateParent(comment);
        Comment cmt = dto.toEntity();
        commentRepository.save(cmt);
        return cmt.getId();
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
        pageable = PageRequest.of(page, 3, Sort.by(Sort.Direction.DESC, "id"));
        return commentRepository.findAllByBoard(board, pageable);
    }
    private List<CommentDto> convertNestedStructure(List<Comment> comments) {
        List<CommentDto> result = new ArrayList<>();
        Map<Long, CommentDto> map = new HashMap<>();
        comments.stream().forEach(c -> {
            CommentDto dto = CommentDto.convertCommentToDto(c);
            map.put(dto.getId(), dto);
            if(c.getParent() != null) map.get(c.getParent().getId()).getChildren().add(dto);
            else result.add(dto);
        });
        System.out.println("result = " + result);
        System.out.println("map = " + map);
        return result;
    }

    @Transactional
    public void deleteComment(Long commentId) {
        Comment comment = commentRepository.findCommentByIdWithParent(commentId).orElseThrow(() -> new IllegalArgumentException("해당 댓글이 존재하지 않습니다. id=" + commentId));
//      여기 한줄 공백
        if(comment.getChildren().size() != 0) { // 자식이 있으면 상태만 변경
            comment.changeDeletedStatus(DeleteStatus.YES);
        } else { // 삭제 가능한 조상 댓글을 구해서 삭제
            commentRepository.delete(getDeletableAncestorComment(comment));
        }
    }

    private Comment getDeletableAncestorComment(Comment comment) { // 삭제 가능한 조상 댓글을 구함
        Comment parent = comment.getParent(); // 현재 댓글의 부모를 구함
        if(parent != null && parent.getChildren().size() == 1 && parent.getIsdeleted() == DeleteStatus.YES)
            // 부모가 있고, 부모의 자식이 1개(지금 삭제하는 댓글)이고, 부모의 삭제 상태가 TRUE인 댓글이라면 재귀
            return getDeletableAncestorComment(parent);
        return comment; // 삭제해야하는 댓글 반환
    }
    @Transactional
    public List<CommentDto> findCommentsByTicketId(Long boardId) {
        boardRepository.findById(boardId).orElseThrow(() -> new IllegalArgumentException("해당 게시물이 존재하지 않습니다. boardId: " + boardId));
        return convertNestedStructure(commentRepository.findCommentByTicketId(boardId));
    }
}
