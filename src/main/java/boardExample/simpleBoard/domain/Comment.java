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
    @ManyToOne
    @JoinColumn(name = "board_id")
    private Board board;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Enumerated(value = EnumType.STRING)
    private DeleteStatus isdeleted;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Comment parent;

    @OneToMany(mappedBy = "parent", orphanRemoval = true)
    private List<Comment> children = new ArrayList<>();


    @Builder
    public Comment(Long id, String comment, Board board, Member member, Comment parent) {
        this.id = id;
        this.comment = comment;
        this.board = board;
        this.member = member;
        this.parent = parent;
    }
    public void update(String comment, String modified_date) {
        this.comment = comment;
        this.modified_date = modified_date;
    }

    public void changeDeletedStatus(DeleteStatus deleteStatus) {
        this.isdeleted = deleteStatus;
    }

}
