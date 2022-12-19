package boardExample.simpleBoard.repository;

import boardExample.simpleBoard.domain.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BoardRepository extends JpaRepository<Board, Long> {
    @Modifying
    @Query("update Board p set p.viewcnt = p.viewcnt + 1 where p.uid = :uid")
    // 자바 8버전이상에서는 @Param을 붙여줘야 한다.
    int viewCount(@Param("uid") Long uid);
    Page<Board> findBySubjectContaining(String searchKeyword, Pageable pageable);
}
