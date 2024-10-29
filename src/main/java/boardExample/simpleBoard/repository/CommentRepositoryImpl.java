package boardExample.simpleBoard.repository;

import boardExample.simpleBoard.domain.Comment;
import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static boardExample.simpleBoard.domain.QBoard.board;
import static boardExample.simpleBoard.domain.QComment.comment1;
import static boardExample.simpleBoard.domain.QMember.member;

@RequiredArgsConstructor
public class CommentRepositoryImpl implements CustomCommentRepository{
    private final JPAQueryFactory queryFactory;

//  게시물에 해당되는 댓글을 가져오고 시작 지점과 페이지 사이즈를 지정하고 정렬하여 return한다.
    public Page<Comment> findAllByBoardByComments(Long boardId, Pageable pageable) {
        List<Comment> comments = queryFactory.selectFrom(comment1)
                .where(comment1.board.uid.eq(boardId))
                .offset(pageable.getOffset())   // 시작 지점(default)
                .limit(pageable.getPageSize())  // 페이지 사이즈(default)
                .orderBy(comment1.ref.desc(),
                        comment1.reforder.asc())
                .fetch();
        return new PageImpl<>(comments,pageable,comments.size());
    }
}
