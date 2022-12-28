package boardExample.simpleBoard.service;

import boardExample.simpleBoard.domain.Board;
import boardExample.simpleBoard.domain.Like;
import boardExample.simpleBoard.domain.Member;
import boardExample.simpleBoard.dto.BoardDto;
import boardExample.simpleBoard.repository.BoardRepository;
import boardExample.simpleBoard.repository.LikeRepository;
import boardExample.simpleBoard.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;
    private final LikeRepository likeRepository;
    private final static String VIEWCOOKIENAME = "alreadyViewCookie";

    public List<Board> BoardList() {
        return boardRepository.findAll();
    }
    // 페이징
    @Transactional
    public Page<Board> pageList(Pageable pageable) {
        int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1);
        pageable = PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "uid"));
        return boardRepository.findAll(pageable);
    }

    public Optional<Board> BoardOne(Long uid) {
        return boardRepository.findById(uid);
    }
    @Transactional
    public Long BoardAdd(BoardDto board) {
        Member num = memberRepository.findByUno(board.getMember().getUno());
        Board data = board.toEntity();
        return boardRepository.save(data).getUid();
    }

    public void BoardDelete(Long uid) {
        boardRepository.deleteById(uid);
    }

    @Transactional
    public Long BoardUpdate(Long uid, BoardDto boardDto) {
        SimpleDateFormat format = new SimpleDateFormat ( "yyyy-MM-dd HH:mm");
        Date time = new Date();
        String localTime = format.format(time);
        // updateBoard를 통해서 제목과 내용만 바꿀 수 있게 설정
        Board board = boardRepository.findById(uid).orElseThrow(()->new IllegalArgumentException("해당 게시글이 없습니다. id = " + uid));
        board.updateBoard(boardDto.getSubject(), boardDto.getContent(), localTime);
        return uid;
    }

    @Transactional
    public Page<Board> findBySubjectContaining(String searchKeyword, Pageable pageable) {
        int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1);
        pageable = PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "uid"));
        return boardRepository.findBySubjectContaining(searchKeyword, pageable);
    }
//    @Transactional
//    public int viewCount(Long uid) {
//        return boardRepository.viewCount(uid);
//    }
//    조회수 중복 카운트 방지
//    1. 글을 조회 요청이 오면 HttpServletRequest에 글의 고유 ID를 키(Key)로 하는 쿠키가 존재하는지 확인합니다.
//    2. 글의 고유ID 쿠키가 존재하지 않는다면 글의 고유 ID를 Key로 쿠키를 추가하고 글의 조회수를 증가시키고 글의 정보를 응답해줍니다.
//    3. 글의 고유ID 쿠키가 존재한다면 글의 조회수를 증가시키지 않고 글의 정보만 응답합니다.
    @Transactional
    public int updateView(Long bId, HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        boolean checkCookie = false;
        int result = 0;
        if(cookies != null){
            for (Cookie cookie : cookies)
            {
                // 이미 조회를 한 경우 체크
                if (cookie.getName().equals(VIEWCOOKIENAME+bId)) checkCookie = true;
            }
            if(!checkCookie){
                Cookie newCookie = createCookieForForNotOverlap(bId);
                response.addCookie(newCookie);
                result = boardRepository.updateView(bId);
            }
        } else {
            Cookie newCookie = createCookieForForNotOverlap(bId);
            response.addCookie(newCookie);
            result = boardRepository.updateView(bId);
        }
        return result;
    }
    /*
     * 조회수 중복 방지를 위한 쿠키 생성 메소드
     * @param cookie
     * @return
     * */
    private Cookie createCookieForForNotOverlap(Long bId) {
        Cookie cookie = new Cookie(VIEWCOOKIENAME+bId, String.valueOf(bId));
        cookie.setComment("조회수 중복 증가 방지 쿠키");	// 쿠키 용도 설명 기재
        cookie.setMaxAge(getRemainSecondForTommorow()); 	// 하루를 준다.
        cookie.setHttpOnly(true);				// 서버에서만 조작 가능
        return cookie;
    }

    // 다음 날 정각까지 남은 시간(초)
    private int getRemainSecondForTommorow() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime tommorow = LocalDateTime.now().plusDays(1L).truncatedTo(ChronoUnit.DAYS);
        return (int) now.until(tommorow, ChronoUnit.SECONDS);
    }

    /** 글 좋아요 확인 **/
    public boolean findLike(Board likeBoard, Member likeMember) {

        return likeRepository.existsByLikeBoardAndLikeMember(likeBoard, likeMember);
    }

    /** 글 좋아요 **/
    public boolean saveLike(Board likeBoard, Member likeMember) {

        /** 로그인한 유저가 해당 게시물을 좋아요 했는지 안 했는지 확인 **/
        if(!findLike(likeBoard, likeMember)){

            /* 좋아요 하지 않은 게시물이면 좋아요 추가, true 반환 */
//            Member member = memberRepository.findByUno(likeMember.getUno()).orElseThrow(() -> new IllegalArgumentException("해당 회원이 존재하지 않습니다."));
//            Board board = boardRepository.findById(likeBoard.getUid()).orElseThrow(()->new IllegalArgumentException("해당 게시물이 없습니다."));
            /* 좋아요 엔티티 생성 */
            Like memberLikeBoard = new Like(likeMember, likeBoard);
            likeRepository.save(memberLikeBoard);
            boardRepository.plusLike(likeBoard.getUid());
            return true;
        } else {

            /* 좋아요 한 게시물이면 좋아요 삭제, false 반환 */
            likeRepository.deleteByLikeBoardAndLikeMember(likeBoard, likeMember);
            boardRepository.minusLike(likeBoard.getUid());
            return false;
        }
    }
}
