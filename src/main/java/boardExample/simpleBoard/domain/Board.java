package boardExample.simpleBoard.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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
//  응답데이터에서 제외(LAZY에서 EAGER로 바꾸면 해결되지만 LAZY를 유지하기위해 @JsonIgnore사용
    @JsonIgnore
    private Member member;

    @JsonIgnore
    @OneToMany(mappedBy = "board", cascade = CascadeType.REMOVE)
    private List<Comment> comments;

    @OneToMany(mappedBy = "likeBoard", cascade = CascadeType.ALL)
    Set<Like> likes = new HashSet<>();
    @OneToMany(mappedBy = "attachmentBoard", cascade = CascadeType.ALL)
    @JsonBackReference //순환참조 방지
    private List<Attachment> attachedFiles = new ArrayList<>();
    @Builder
    public Board(Long uid, String subject, String content, Member member, String name, Integer viewcnt, Integer likecnt, List<Attachment> attachedFiles, String updatedate) {
        this.uid = uid;
        this.subject = subject;
        this.content = content;
        this.member = member;
        this.name = name;
        this.viewcnt = viewcnt;
        this.likecnt = likecnt;
        this.attachedFiles = attachedFiles;
        this.updatedate = updatedate;
    }
    public void setName(String username) {
        this.name = username;
    }

    public void setMember(Member member) {
        this.member = member;
    }

//  보류
    public void setAttachment(Attachment attachment) {
        this.attachedFiles.add(attachment);
        attachment.setAttachmentBoard(this);
    }

    public void updateBoard(String subject, String content, String updatedate) {
        this.subject = subject;
        this.content = content;
        this.updatedate = updatedate;
    }
}
