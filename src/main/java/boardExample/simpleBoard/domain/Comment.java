package boardExample.simpleBoard.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String comment; // 댓글 내용
    @Column(nullable = false)
    private String created_date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm"));
    @Column(nullable = false)
    private String modified_date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm"));

    // 외래키
    @ManyToOne
    @JoinColumn(name = "board_id")
    private Board board;
//    Spring은 Front로 데이터를 보낼 때 Json으로 보내야하는 상황이면 Jackson을 통해 Json 형태로 변환하는데 순환구조일 경우 에러가 떠버린다.
//    위의 코드처럼 해당 연관관계 매핑 부분에 @JsonIgnore을 붙여주면 순환 참조 관계를 막을 수 있다
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    public Comment(Long id, String comment, Board board, Member member) {
        this.id = id;
        this.comment = comment;
        this.board = board;
        this.member = member;
    }
    public void update(String comment, String modified_date) {
        this.comment = comment;
        this.modified_date = modified_date;
    }
}
