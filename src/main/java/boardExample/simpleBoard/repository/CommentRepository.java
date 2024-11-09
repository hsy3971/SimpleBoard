package boardExample.simpleBoard.repository;

import boardExample.simpleBoard.domain.Board;
import boardExample.simpleBoard.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface CommentRepository extends JpaRepository<Comment, Long>, CustomCommentRepository {
//  ref중에서 가장 큰 값을 검색하고 없다면 0을 반환하자.(있다면 Long형으로 반환)
//    mysql에서는 NVL함수가 존재하지 않기 때문에 IFNULL을 써야한다.
//    @Query("select CAST(NVL(MAX(ref),0) as java.lang.Long) from Comment b where b.board.uid = :bId")
    @Query("select CAST(IFNULL(MAX(ref),0) as java.lang.Long) from Comment b where b.board.uid = :bId")
    Long findByRef(@Param("bId") Long bId);
//    해당 게시물의 ref그룹에 해당되는 모든 자식수들의 합을 구해준다.
    @Query("select SUM(answernum) from Comment c where c.board = :board and c.ref = :ref")
    Long findBySumAnswerNum(@Param("ref") Long ref, @Param("board") Board board);
//    해당 게시물의 ref그룹에 해당되는 것 중에서 가장 큰 step을 찾아준다.
    @Query("select MAX(step) from Comment c where c.board = :board and c.ref = :ref")
    Long findByNvlMaxStep(@Param("ref") Long ref, @Param("board") Board board);

//    해당 게시물의 ref그룹내에 순서가 num보다 큰 것들을 모두 +1씩 업데이트해준다.
    @Transactional
    @Modifying
    @Query("update Comment c set c.reforder = c.reforder + 1 where c.board = :board and c.ref = :ref AND c.reforder > :num")
    public void updateRefOrderPlus(@Param("ref") Long ref, @Param("num") Long num, @Param("board") Board board);
    // updateAnswerNum : 부모댓글의 자식수를 +1 업데이트 해준다.
    @Transactional
    @Modifying
    @Query("update Comment c set c.answernum = c.answernum + 1 where c.id = :parentId AND c.answernum = :answerNumber")
    public void updateAnswerNum(@Param("parentId") Long parentId, @Param("answerNumber") Long answerNumber);
    // updateAnswerNumMinus : 부모댓글의 자식수를 -1 업데이트 해준다.
    @Transactional
    @Modifying
    @Query("update Comment c set c.answernum = c.answernum - 1 where c.id = :parentId")
    public void updateAnswerNumMinus(@Param("parentId") Long parentId);
}