package boardExample.simpleBoard.repository;

import boardExample.simpleBoard.domain.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CustomCommentRepository {
    public List<Comment> findCommentByTicketId(Long boardId);
    public Page<Comment> findAllByBoardByComments(Long boardId, Pageable pageable);
}
