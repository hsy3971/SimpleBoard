package boardExample.simpleBoard.service;

import boardExample.simpleBoard.domain.*;
import boardExample.simpleBoard.dto.BoardDto;
import boardExample.simpleBoard.repository.AttachmentRepository;
import boardExample.simpleBoard.repository.BoardRepository;
import boardExample.simpleBoard.repository.LikeRepository;
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
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final LikeRepository likeRepository;
    private final AttachmentService attachmentService;

    private final AttachmentRepository attachmentRepository;

    private final FileStore fileStore;
    private final static String VIEWCOOKIENAME = "alreadyViewCookie";

    // 페이징
    @Transactional
    public Page<Board> pageList(Pageable pageable) {
        int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber());
        pageable = PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "uid"));
        return boardRepository.findAll(pageable);
    }

    public Optional<Board> BoardOne(Long uid) {
        return boardRepository.findById(uid);
    }
    @Transactional
    public Board BoardAdd(BoardDto dto) throws IOException {
        List<Attachment> attachments = attachmentService.saveAttachments(dto.getAttachmentFiles());
        Board board = dto.toEntity();
        attachments.stream()
                .forEach(attachment -> board.setAttachment(attachment));
        Board saveBoard = boardRepository.save(board);
        attachments.forEach(attachmentRepository::save);
        return saveBoard;
    }
    public void BoardDelete(Long uid) {
        Board board = boardRepository.findById(uid).orElseThrow(() -> new IllegalArgumentException("Invalid board ID"));
        board.getAttachedFiles().forEach(attachedFile -> {
            String path = fileStore.createPath(attachedFile.getStorefilename(), attachedFile.getAttachmenttype());
            File file = new File(path);
            if (file.exists()) {
                file.delete();
            }
        });
        boardRepository.deleteById(uid);
    }

    @Transactional
    public Long BoardUpdate(Long uid, BoardDto dto) throws IOException {
        String localTime = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date());
        // updateBoard를 통해서 제목,내용,이미지,일반파일 업데이트
        Board board = boardRepository.findById(uid).orElseThrow(()->new IllegalArgumentException("해당 게시글이 없습니다. id = " + uid));
//      텍스트 업데이트
        board.updateBoard(dto.getSubject(), dto.getContent(), localTime);
        //      실제 파일로 저장
        List<Attachment> attachments = attachmentService.saveAttachments(dto.getAttachmentFiles());
//      attachment DB 저장
        attachments.forEach(attachment -> {
            attachment.setAttachmentBoard(board);
            attachmentRepository.save(attachment);
        });
        return uid;
    }

    @Transactional
    public Page<Board> findBySubjectContaining(String searchKeyword, Pageable pageable) {
        int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber());
        pageable = PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "uid"));
        return boardRepository.findBySubjectContaining(searchKeyword, pageable);
    }
    //    조회수 중복 카운트 방지
//    1. 글을 조회 요청이 오면 HttpServletRequest에 글의 고유 ID를 키(Key)로 하는 쿠키가 존재하는지 확인합니다.
//    2. 글의 고유ID 쿠키가 존재하지 않는다면 글의 고유 ID를 Key로 쿠키를 추가하고 글의 조회수를 증가시키고 글의 정보를 응답해줍니다.
//    3. 글의 고유ID 쿠키가 존재한다면 글의 조회수를 증가시키지 않고 글의 정보만 응답합니다.
    @Transactional
    public int updateView(Long bId, HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        boolean checkCookie = false;
        int result = 0;
//      cookies가 null이거나, cookies 배열 내에 VIEWCOOKIENAME + bId라는 이름의 쿠키가 없을 때만 조회수 증가 및 쿠키 추가 작업을 수행
        if (cookies == null || Arrays.stream(cookies).noneMatch(cookie -> cookie.getName().equals(VIEWCOOKIENAME + bId))) {
            // 조회 쿠키가 없을 경우, 새로운 쿠키 생성 및 조회수 증가
            Cookie newCookie = createCookieForForNotOverlap(bId);
            response.addCookie(newCookie);
//          query가 잘 동작했다면 1반환, 아니라면 0반환
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
//        쿠키 이름을 alreadyViewCookie + 게시글번호로 구분한다.
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
    public void saveLike(Board likeBoard, Member likeMember) {
        /** 로그인한 유저가 해당 게시물을 좋아요 했는지 안 했는지 확인 **/
        if(!findLike(likeBoard, likeMember)){
            /* 좋아요 엔티티 생성 */
            Like memberLikeBoard = Like.builder().likeBoard(likeBoard).likeMember(likeMember).build();
            likeRepository.save(memberLikeBoard);
            boardRepository.plusLike(likeBoard.getUid());
        } else {
            /* 좋아요 한 게시물이면 좋아요 삭제, false 반환 */
            likeRepository.deleteByLikeBoardAndLikeMember(likeBoard, likeMember);
            boardRepository.minusLike(likeBoard.getUid());
        }
    }
}
