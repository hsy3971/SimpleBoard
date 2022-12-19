package boardExample.simpleBoard.repository;

import boardExample.simpleBoard.domain.Board;
import boardExample.simpleBoard.domain.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long>, CustomCommentRepository {
//  댓글 페이징(게시판별 페이징을 위해 findAllBy를 씀)
    Page<Comment> findAllByBoard(Board board, Pageable pageable);
    @Query("select c from Comment c left join fetch c.parent where c.id = :id")
    Optional<Comment> findCommentByIdWithParent(@Param("id") Long id);
}
