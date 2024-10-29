package boardExample.simpleBoard.repository;

import boardExample.simpleBoard.domain.Board;
import boardExample.simpleBoard.domain.Like;
import boardExample.simpleBoard.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface LikeRepository extends JpaRepository<Like, Long> {
    /** 유저가 특정 게시물을 좋아요 했는지 확인 **/
    boolean existsByLikeBoardAndLikeMember(Board likeBoard, Member likeMember);
    @Transactional
    void deleteByLikeBoardAndLikeMember(Board likeBoard, Member likeMember);
}
