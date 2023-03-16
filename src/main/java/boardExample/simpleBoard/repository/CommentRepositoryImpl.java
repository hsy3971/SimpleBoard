package boardExample.simpleBoard.repository;

import boardExample.simpleBoard.domain.Comment;
import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static boardExample.simpleBoard.domain.QBoard.board;
import static boardExample.simpleBoard.domain.QComment.comment1;
import static boardExample.simpleBoard.domain.QMember.member;

@RequiredArgsConstructor
public class CommentRepositoryImpl implements CustomCommentRepository{
    private final JPAQueryFactory queryFactory;

    @Override
    public List<Comment> findCommentByTicketId(Long boardId) {
        return queryFactory.selectFrom(comment1)
                .leftJoin(comment1.parent)
                .fetchJoin()
                .where(comment1.board.uid.eq(boardId))
                .orderBy(
                        comment1.parent.id.asc().nullsFirst(),
                        comment1.modified_date.asc()
                ).fetch();
    }

    //  querydsl에서 원하는 값들을 찾고 페이징하자(PageImpl을 쓰는게 최선일지 생각해보기) -> offset~limit까지
//  기존에는 Pageable의 PageRequest.of를 통해 uid별로 정렬후 페이징을 해줬다.
    public Page<Comment> findAllByBoardByComments(Long boardId, Pageable pageable) {
        List<Comment> comments = queryFactory.selectFrom(comment1)
                .innerJoin(comment1.board, board)
                .innerJoin(comment1.member, member)
                .where(comment1.board.uid.eq(boardId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(comment1.ref.desc(),
                        comment1.reforder.asc())
                .fetch();
        return new PageImpl<>(comments,pageable,comments.size());
    }
}
