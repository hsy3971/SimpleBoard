package boardExample.simpleBoard.domain;

import boardExample.simpleBoard.repository.AttachmentRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class FileStore {
    //  파일경로
    @Value("${com.example.upload.path}") // application.properties의 변수
    private String fileDirPath;

    //  전체 파일 저장
    public List<Attachment> storeFiles(List<MultipartFile> multipartFiles, AttachmentType attachmentType) throws IOException {
        List<Attachment> attachments = new ArrayList<>();
        for (MultipartFile multipartFile : multipartFiles) {
            if (!multipartFile.isEmpty()) {
                Attachment attachment = storeFile(multipartFile, attachmentType);
                attachments.add(attachment);
            }
        }

        return attachments;
    }
    //  파일 저장 로직
    public Attachment storeFile(MultipartFile multipartFile, AttachmentType attachmentType) throws IOException {
        if (multipartFile.isEmpty()) {
            return null;
        }

        String originalFilename = multipartFile.getOriginalFilename();
        String storeFilename = createStoreFilename(originalFilename);
//      createPath한 경로에 파일 저장
        multipartFile.transferTo(new File(createPath(storeFilename, attachmentType)));
//      Attachment DB에 저장하자.

        return Attachment.builder()
                .originfilename(originalFilename)
                .storePath(storeFilename)
                .attachmenttype(attachmentType)
                .build();

    }
    // 파일 경로 구성
    public String createPath(String storeFilename, AttachmentType attachmentType) {
//        경로를 구분하기 위해 AttachmentType을 파라미터로 입력받아 파일의 경로를 설정
        String viaPath = (attachmentType == AttachmentType.IMAGE) ? "images/" : "generals/";
        return fileDirPath+viaPath+storeFilename;
    }
    //  저장할 파일 이름 구성
    private String createStoreFilename(String originalFilename) {
//      파일의 이름이 겹치지 않게 UUID를 통해 설정
        String uuid = UUID.randomUUID().toString();
        String ext = extractExt(originalFilename);
//        확장자를 추출하여 UUID 뒤에 붙여 출력
        String storeFilename = uuid + ext;

        return storeFilename;
    }
    // 확장자 추출
    private String extractExt(String originalFilename) {
        int idx = originalFilename.lastIndexOf(".");
        String ext = originalFilename.substring(idx);
        return ext;
    }
}
