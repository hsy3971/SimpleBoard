package boardExample.simpleBoard.repository;

import boardExample.simpleBoard.domain.Board;
import boardExample.simpleBoard.domain.Like;
import boardExample.simpleBoard.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface LikeRepository extends JpaRepository<Like, Long> {
    /** 유저가 특정 게시물을 좋아요 했는지 확인 **/
//  existsBy특정컬럼네임이므로 밑에 like_board와 like_member가 맞는지 확인해보기
//    문제:Long형과 객체형으로 타입이 달라서 오류가 뜬다.
    boolean existsByLikeBoardAndLikeMember(Board likeBoard, Member likeMember);
    @Transactional
    void deleteByLikeBoardAndLikeMember(Board likeBoard, Member likeMember);
}
