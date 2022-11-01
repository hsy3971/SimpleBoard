package boardExample.simpleBoard.domain;

import lombok.Builder;
import lombok.Getter;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Entity
//@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Board {

    // identity : 기본키 생성을 데이터베이스에 위임해주고 id가 null 이면 알아서 auto_increment 해줌
    // h2데이터베이스 버전문제라 해가지고 sequence로 고치면 가능하다길래 일단 identity->sequence로 고침.(이후에 변경할지도)
    // 컬럼name은 대소문자 구분X
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uid;
    @Column(nullable = false, length = 20)
    private String subject;
    @Column(nullable = false)
    private String content;
    private String name;
//    viewcnt가 DB에서는 int형으로 null값을 허용했는데 자바에서는 Primitive Type(boolean, byte, short, int, long, float, double, char)으로 되어있기 때문에 null값을 허용하면 안된다.
//    그래서 int->Integer로 변경하니 오류가 해결되었다.(Long형으로 바꿔도 됨)
    @Column(columnDefinition = "integer default 0", length = 11)
    //조회수가 출력이 안되길래 0으로 해주었더니 일단은 잘된다...(사실 DB에도 그렇고 default값을 0으로 해주었는데 왜 null값이 들어가는지 모르겠다. 그래서 출력이 안되는거같음)
    private Integer viewcnt;

    private String regdate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm"));

    private String updatedate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm"));
    //다대일 단방향으로 지정했다.(Member에서는 Board의 값을 받지않는다.)
    @ManyToOne(fetch = FetchType.LAZY)
//    name은 단지 매핑할 외래 키의 이름, 즉 단순히 컬럼명을 만들어주는 것
    @JoinColumn(name = "user_no")
    private Member member;

    //게시글 삭제시 댓글도 전부삭제하기 위해 cascade = REMOVE
    @OneToMany(mappedBy = "board", cascade = CascadeType.REMOVE)
    private List<Comment> comments;

    public Board(){

    }
    //기존에 공통된값인 name,regdate,updatedate는 생성자에서 빼주었다.(안그러면 빈값을 받아오기 때문에 null값이 넘어가게 된다.)
    //생성자에 Member를 추가해줬음
    //name을 builder에 추가(로그인한 사용자의 정보로 받기위해서)
    @Builder
    public Board(Long uid, String subject, String content, Member member, String name, Integer viewcnt) {
        this.uid = uid;
        this.subject = subject;
        this.content = content;
        this.member = member;
        this.name = name;
        this.viewcnt = viewcnt;
    }
    // setter를 써버렸다... 원래는 사용하면 안되는 것인데 진짜 방법을 모르겠어서 사용함.
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
