package boardExample.simpleBoard.repository;

import boardExample.simpleBoard.domain.Comment;

import java.util.List;

public interface CustomCommentRepository {
    public List<Comment> findCommentByTicketId(Long cId);
}
