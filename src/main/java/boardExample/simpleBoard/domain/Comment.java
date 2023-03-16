package boardExample.simpleBoard.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

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
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "board_id")
    private Board board;
    //    JsonIgnore를 통해 무한참조 방지
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Comment parent;

    @JsonIgnore
    @OneToMany(mappedBy = "parent", orphanRemoval = true)
    private List<Comment> children = new ArrayList<>();
    //  그륩
    private Long ref;
    //  같은 그룹내의 순서
    private Long reforder;
    //  단계
    private Long step;
    //  자식수
    private Long answernum;

    @Builder
    public Comment(Long id, String comment, Board board, Member member, Comment parent, Long ref, Long reforder, Long step, Long answernum) {
        this.id = id;
        this.comment = comment;
        this.board = board;
        this.member = member;
        this.parent = parent;
        this.ref = ref;
        this.reforder = reforder;
        this.step = step;
        this.answernum = answernum;
    }
    public void update(String comment, String modified_date) {
        this.comment = comment;
        this.modified_date = modified_date;
    }

}
