package boardExample.simpleBoard.dto;

import boardExample.simpleBoard.domain.AttachmentType;
import boardExample.simpleBoard.domain.Board;
import boardExample.simpleBoard.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private Map<AttachmentType, List<MultipartFile>> attachmentFiles = new HashMap<>();

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
}
