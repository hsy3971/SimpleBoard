package boardExample.simpleBoard.dto;

import boardExample.simpleBoard.domain.AttachmentType;
import boardExample.simpleBoard.domain.Board;
import boardExample.simpleBoard.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BoardDto{

    private String subject;
    private String content;
    private String name;

    private String regdate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm"));
    private String updatedate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm"));
    private Member member;
    private Map<AttachmentType, List<MultipartFile>> attachmentFiles = new ConcurrentHashMap<>();

//    dto -> entity
    public Board toEntity() {
        Board build = Board.builder()
                .subject(subject)
                .content(content)
                .name(name)
                .member(member)
                .viewcnt(0)
                .likecnt(0)
                .attachedFiles(new ArrayList<>())
                .build();
        return build;
    }

    @Builder
    public BoardDto(String subject, String content, Member member, String name, Integer viewcnt, Map<AttachmentType, List<MultipartFile>> attachmentFiles) {
        this.subject = subject;
        this.content = content;
        this.member = member;
        this.name = name;
        this.attachmentFiles = attachmentFiles;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    // 추가했지만 보류
    /**
     * 게시글 정보를 리턴할 응답(Response) 클래스
     * Entity 클래스를 생성자 파라미터로 받아 데이터를 Dto로 변환하여 응답
     * 별도의 전달 객체를 활용해 연관관계를 맺은 엔티티간의 무한참조를 방지
     */
//    @Getter
//    public static class Response {
//        private final Long uid;
//        private final String subject;
//        private final String content;
//        private final String name;
//        private final String regdate;
//        private final String updatedate;
//        private final int viewcnt;
//        private final Long usernum;
////        댓글
//        private final List<CommentDto.Response> comments;
//
//        /* Entity -> Dto*/
//        public Response(Board board) {
//            this.uid = board.getUid();
//            this.subject = board.getSubject();
//            this.name = board.getName();
//            this.content = board.getContent();
//            this.regdate = board.getRegdate();
//            this.updatedate = board.getUpdatedate();
//            this.viewcnt = board.getViewcnt();
//            this.usernum = board.getMember().getUno();
//            this.comments = board.getComments().stream().map(CommentDto.Response::new).collect(Collectors.toList());
//        }
//    }
}
