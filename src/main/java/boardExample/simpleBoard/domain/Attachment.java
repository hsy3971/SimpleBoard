package boardExample.simpleBoard.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)

public class Attachment {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
//  원본이름
    private String originfilename;
//  파일저장한이름
    private String storefilename;
    @Enumerated(EnumType.STRING)
    private AttachmentType attachmenttype;

//    JsonIgnore: 무한참조 방지
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonManagedReference
    @JsonIgnore
    @JoinColumn(name = "boardid")
    private Board attachmentBoard;

    @Builder
    public Attachment(Long id, String originfilename, String storePath, AttachmentType attachmenttype) {
        this.id = id;
        this.originfilename = originfilename;
        this.storefilename = storePath;
        this.attachmenttype = attachmenttype;
    }
    public void setAttachmentBoard(Board attachmentBoard) {
        this.attachmentBoard = attachmentBoard;
    }
}
