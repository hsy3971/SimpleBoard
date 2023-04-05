package boardExample.simpleBoard.repository;

import boardExample.simpleBoard.domain.Board;
import boardExample.simpleBoard.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface CommentRepository extends JpaRepository<Comment, Long>, CustomCommentRepository {
    @Query("select CAST(NVL(MAX(ref),0) as java.lang.Long) from Comment b where b.board.uid = :bId")
    Long findByRef(@Param("bId") Long bId);
    @Query("select SUM(answernum) from Comment c where c.board = :board and c.ref = :ref")
    Long findBySumAnswerNum(@Param("ref") Long ref, @Param("board") Board board);
    @Query("select MAX(step) from Comment c where c.board = :board and c.ref = :ref")
    Long findByNvlMaxStep(@Param("ref") Long ref, @Param("board") Board board);

    @Transactional
    @Modifying
    @Query("update Comment c set c.reforder = c.reforder + 1 where c.board = :board and c.ref = :ref AND c.reforder > :num")
    public void updateRefOrderPlus(@Param("ref") Long ref, @Param("num") Long num, @Param("board") Board board);
    //  새롭게 넣어준 쿼리문\
    @Transactional
    @Modifying
    @Query("update Comment c set c.answernum = c.answernum + 1 where c.id = :parentId AND c.answernum = :answerNumber")
    public void updateAnswerNum(@Param("parentId") Long parentId, @Param("answerNumber") Long answerNumber);
    //  삭제시 부모 자식수 -1
    @Transactional
    @Modifying
    @Query("update Comment c set c.answernum = c.answernum - 1 where c.id = :parentId")
    public void updateAnswerNumMinus(@Param("parentId") Long parentId);
}