package boardExample.simpleBoard.dto;

import boardExample.simpleBoard.domain.Board;
import boardExample.simpleBoard.domain.Comment;
import boardExample.simpleBoard.domain.Member;
import lombok.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto{
    private Long id;
    private String comment;
    private String created_date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm"));
    private String modified_date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm"));
    private Board board;
    private Member member;
    private Comment parent;

    private List<CommentDto> children = new ArrayList<>();
    private Long ref;
    private Long reforder;
    private Long step;
    private Long answernum;

    public Comment toEntity() {
        Comment build = Comment.builder()
                .id(id)
                .comment(comment)
                .board(board)
                .member(member)
                .parent(parent)
                .ref(ref)
                .reforder(reforder)
                .step(step)
                .answernum(answernum)
                .build();
        return build;
    }

    @Builder
    public CommentDto(Long id, String comment, Board board, Member member) {
        this.id = id;
        this.comment = comment;
        this.board = board;
        this.member = member;
    }
    public void setBoard(Board board) {
        this.board = board;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    // 부모 댓글 수정
    public void updateParent(Comment parent){
        this.parent = parent;
    }

    public void setRef(Long ref, Long reforder, Long step, Long answernum) {
        this.ref = ref;
        this.reforder = reforder;
        this.step = step;
        this.answernum = answernum;
    }
    @RequiredArgsConstructor
    @Getter
    public static class Request {
        private Long id;
        private String comment;
        private Long userId;
        private Long postsId;
        private Long parentId;
        public Request(Comment comment) {
            this.id = comment.getId();
            this.comment = comment.getComment();
            this.userId = comment.getMember().getUno();
            this.postsId = comment.getBoard().getUid();
            this.parentId = comment.getParent().getId();
        }
    }
}