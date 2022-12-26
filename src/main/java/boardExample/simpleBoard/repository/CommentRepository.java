package boardExample.simpleBoard.repository;

import boardExample.simpleBoard.domain.Board;
import boardExample.simpleBoard.domain.Comment;
import boardExample.simpleBoard.domain.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long>, CustomCommentRepository {
//  댓글 페이징(게시판별 페이징을 위해 findAllBy를 씀)
//    Page<Comment> findAllByBoard(Board board, Pageable pageable);
    @Query("select CAST(NVL(MAX(ref),0) as java.lang.Long) from Comment b where b.board.uid = :bId")
    Long findByRef(@Param("bId") Long bId);

    @Query("select SUM(answernum) from Comment c where c.ref = :ref")
    Long findBySumAnswerNum(@Param("ref") Long ref);

    @Query("select MAX(step) from Comment c where c.ref = :ref")
    Long findByNvlMaxStep(@Param("ref") Long ref);

    @Transactional
    @Modifying
    @Query("update Comment c set c.reforder = c.reforder + 1 where c.ref = :ref AND c.reforder > :num")
    public void updateRefOrderPlus(@Param("ref") Long ref, @Param("num") Long num);
//  새롭게 넣어준 쿼리문\
    @Transactional
    @Modifying
    @Query("update Comment c set c.answernum = c.answernum + 1 where c.id = :parentId AND c.answernum = :answerNumber")
    public void updateAnswerNum(@Param("parentId") Long parentId, @Param("answerNumber") Long answerNumber);
    @Transactional
    @Modifying
    @Query("update Comment c set c.answernum = c.answernum - 1 where c.id = :parentId")
    public void updateAnswerNumMinus(@Param("parentId") Long parentId);
    //  그룹순서를 다시 빼줄라면 refOrderAndUpdate에서 했던 3가지 경우를 모두 해줘야한다.(하면 순차적으로 숫자가 깔끔해지고 하지 않더라도 기능에는 문제가 없지만 숫자가 정갈하지 않다.
//    @Transactional
//    @Modifying
//    @Query("update Comment c set c.reforder = c.reforder - 1 where c.ref = :ref AND c.reforder > :num")
//    public void updateRefOrderMinus(@Param("ref") Long ref, @Param("num") Long num);
}