package boardExample.simpleBoard.controller;

import boardExample.simpleBoard.domain.Attachment;
import boardExample.simpleBoard.domain.AttachmentType;
import boardExample.simpleBoard.domain.FileStore;
import boardExample.simpleBoard.repository.AttachmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriUtils;

import java.io.File;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;

@RestController
@RequiredArgsConstructor
public class AttachmentController {

    private final FileStore fileStore;

    @GetMapping("/images/{filename}")
    public ResponseEntity<Resource> processImg(@PathVariable String filename) throws MalformedURLException {
        // S3 URL 생성
        String fileUrl = fileStore.getFileUrl(filename, AttachmentType.IMAGE);
        Resource resource = new UrlResource(fileUrl);

        return ResponseEntity.ok().body(resource);
//        로컬 환경
//        return new UrlResource("file:" + fileStore.createPath(filename, AttachmentType.IMAGE));
    }

    @GetMapping("/attaches/{filename}")
    public ResponseEntity<Resource> processAttaches(@PathVariable String filename, @RequestParam String originName) throws MalformedURLException {
//        로컬 환경
//        UrlResource urlResource = new UrlResource("file:" + fileStore.createPath(filename, AttachmentType.GENERAL));
        // S3 URL 생성
        String fileUrl = fileStore.getFileUrl(filename, AttachmentType.GENERAL);
        Resource resource = new UrlResource(fileUrl);
        String encodedUploadFileName = UriUtils.encode(originName, StandardCharsets.UTF_8);
        String contentDisposition = "attachment; filename=\"" + encodedUploadFileName + "\"";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                .body(resource);
    }
}
