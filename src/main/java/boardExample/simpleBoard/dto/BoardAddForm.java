package boardExample.simpleBoard.dto;

import boardExample.simpleBoard.domain.AttachmentType;
import boardExample.simpleBoard.domain.Member;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Data
@NoArgsConstructor
public class BoardAddForm {
//  이 친구는 여기서 유효성검사를 실시(나는 .js파일에서 따로 실시하였다)
    @NotBlank
    private String subject;
    private String name;
    @NotBlank
    private String content;
    private List<MultipartFile> imageFiles;
    private List<MultipartFile> generalFiles;

    @Builder
    public BoardAddForm(String subject, String name, String content, List<MultipartFile> imageFiles, List<MultipartFile> generalFiles) {
        this.subject = subject;
        this.name = name;
        this.content = content;
        this.imageFiles = (imageFiles != null) ? imageFiles : new ArrayList<>();
        this.generalFiles = (generalFiles != null) ? generalFiles : new ArrayList<>();
    }


    public BoardDto createBoardDto(Member member) {
        Map<AttachmentType, List<MultipartFile>> attachments = getAttachmentTypeListMap();
        return BoardDto.builder()
                .name(name)
                .subject(subject)
                .content(content)
                .member(member)
                .attachmentFiles(attachments)
                .build();
    }
//    update
    public BoardDto updateBoardDto(List<MultipartFile> imageFiles, List<MultipartFile> generalFiles, String subject, String content) {
        Map<AttachmentType, List<MultipartFile>> attachments = getAttachmentTypeListMap();
        attachments.put(AttachmentType.IMAGE, imageFiles);
        attachments.put(AttachmentType.GENERAL, generalFiles);
        return BoardDto.builder()
                .subject(subject)
                .content(content)
                .attachmentFiles(attachments)
                .build();
    }

    private Map<AttachmentType, List<MultipartFile>> getAttachmentTypeListMap() {
        Map<AttachmentType, List<MultipartFile>> attachments = new ConcurrentHashMap<>();
//      Map에 넣기(키, 값)
        attachments.put(AttachmentType.IMAGE, imageFiles);
        attachments.put(AttachmentType.GENERAL, generalFiles);
        return attachments;
    }
}
