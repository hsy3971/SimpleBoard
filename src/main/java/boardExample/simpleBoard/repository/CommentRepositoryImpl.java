package boardExample.simpleBoard.repository;

import boardExample.simpleBoard.domain.Comment;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static boardExample.simpleBoard.domain.QComment.comment1;

@RequiredArgsConstructor
public class CommentRepositoryImpl implements CustomCommentRepository{
    private final JPAQueryFactory queryFactory;

    @Override
    public List<Comment> findCommentByTicketId(Long cId) {
        return queryFactory.selectFrom(comment1)
                .leftJoin(comment1.parent)
                .fetchJoin()
                .where(comment1.id.eq(cId))
                .orderBy(
                        comment1.parent.id.asc().nullsFirst(),
                        comment1.modified_date.asc()
                ).fetch();
    }
}
