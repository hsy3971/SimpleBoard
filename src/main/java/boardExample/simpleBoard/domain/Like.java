package boardExample.simpleBoard.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "Likes")
public class Like {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uid")
    private Member likeMember;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bid")
    private Board likeBoard;

    @Builder
    public Like(Member likeMember, Board likeBoard) {
        this.likeMember = likeMember;
        this.likeBoard = likeBoard;
    }
}
