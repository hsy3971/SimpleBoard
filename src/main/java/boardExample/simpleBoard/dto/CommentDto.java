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
    //  둘다 commentDto로 선언하는게 맞나?
    private Comment parent;
    //    스읍...좀그렇다
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
    // 금단의 setter...
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
    public void setRef(Long ref) {
        this.ref = ref;
    }
    public void setReforder(Long reforder) {
        this.reforder = reforder;
    }
    public void setStep(Long step) {
        this.step = step;
    }
    public void setAnswernum(Long answernum) {
        this.answernum = answernum;
    }

    // 추가했지만 보류
    /**
     * 댓글 정보를 리턴할 응답(Response) 클래스
     * Entity 클래스를 생성자 파라미터로 받아 데이터를 Dto로 변환하여 응답
     * 별도의 전달 객체를 활용해 연관관계를 맺은 엔티티간의 무한참조를 방지
     */
    @RequiredArgsConstructor
    @Getter
    public static class Response {
        private Long id;
        private String comment;
//        private String createdDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm"));
//        private String modifiedDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm"));
//        private String username;
        private Long userId;
        private Long postsId;
        private Long parentId;
        /* Entity -> Dto*/
        public Response(Comment comment) {
            this.id = comment.getId();
            this.comment = comment.getComment();
//            this.createdDate = comment.getCreated_date();
//            this.modifiedDate = comment.getModified_date();
//            this.username = comment.getMember().getUserName();
            this.userId = comment.getMember().getUno();
            this.postsId = comment.getBoard().getUid();
            this.parentId = comment.getParent().getId();
        }
    }
}