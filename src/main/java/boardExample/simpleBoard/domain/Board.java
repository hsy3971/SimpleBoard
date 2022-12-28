package boardExample.simpleBoard.domain;

import lombok.Builder;
import lombok.Getter;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
//@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Board {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uid;
    @Column(nullable = false, length = 20)
    private String subject;
    @Column(nullable = false)
    private String content;
    private String name;
    @Column(columnDefinition = "integer default 0", length = 11)
    private Integer viewcnt;

    @Column(columnDefinition = "integer default 0")
    private Integer likecnt;

    private String regdate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm"));

    private String updatedate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm"));
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_no")
    private Member member;

    @OneToMany(mappedBy = "board", cascade = CascadeType.REMOVE)
    private List<Comment> comments;

    @OneToMany(mappedBy = "likeBoard", cascade = CascadeType.ALL)
    Set<Like> likes = new HashSet<>();

    public Board(){

    }
    @Builder
    public Board(Long uid, String subject, String content, Member member, String name, Integer viewcnt) {
        this.uid = uid;
        this.subject = subject;
        this.content = content;
        this.member = member;
        this.name = name;
        this.viewcnt = viewcnt;
    }
    public void setName(String username) {
        this.name = username;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public void updateBoard(String subject, String content, String updatedate) {
        this.subject = subject;
        this.content = content;
        this.updatedate = updatedate;
    }
}
