package boardExample.simpleBoard.dto;

import boardExample.simpleBoard.domain.Board;
import boardExample.simpleBoard.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BoardDto{

    private Long uid;
    private String subject;
    private String content;
    private String name;
    //viewcnt가 DB에서는 int형으로 null값을 허용했는데 자바에서는 Primitive Type(boolean, byte, short, int, long, float, double, char)으로 되어있기 때문에 null값을 허용하면 안된다.
    //그래서 int->Integer로 변경하니 오류가 해결되었다.
    private Integer viewcnt;

    private String regdate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm"));
    private String updatedate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm"));
    private Member member;

    //필요한 Entity는 toEntity()로 추가하면 됩니다.
    //dto에서 필요한 부분을 빌더패턴을 통해 entity로 만듭니다.
    //dto가 저장될 때 entity로 변환되어 저장되어야 하기 때문에 toEntity 메소드도 만들어줬다.
//    dto -> entity
    public Board toEntity() {
        Board build = Board.builder()
                .uid(uid)
                .subject(subject)
                .content(content)
                .name(name)
                .member(member)
                .viewcnt(0)
                .build();
        return build;
    }
    @Builder
    public BoardDto(Long uid, String subject, String content, Member member, String name, Integer viewcnt) {
        this.uid = uid;
        this.subject = subject;
        this.content = content;
        this.member = member;
        this.name = name;
        this.viewcnt = viewcnt;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    // 추가했지만 보류
    /**
     * 게시글 정보를 리턴할 응답(Response) 클래스
     * Entity 클래스를 생성자 파라미터로 받아 데이터를 Dto로 변환하여 응답
     * 별도의 전달 객체를 활용해 연관관계를 맺은 엔티티간의 무한참조를 방지
     */
    @Getter
    public static class Response {
        private final Long uid;
        private final String subject;
        private final String content;
        private final String name;
        private final String regdate;
        private final String updatedate;
        private final int viewcnt;
        private final Long usernum;
//        댓글
        private final List<CommentDto.Response> comments;

        /* Entity -> Dto*/
        public Response(Board board) {
            this.uid = board.getUid();
            this.subject = board.getSubject();
            this.name = board.getName();
            this.content = board.getContent();
            this.regdate = board.getRegdate();
            this.updatedate = board.getUpdatedate();
            this.viewcnt = board.getViewcnt();
            this.usernum = board.getMember().getUno();
            this.comments = board.getComments().stream().map(CommentDto.Response::new).collect(Collectors.toList());
        }
    }
}
