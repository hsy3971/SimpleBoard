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
    private final AttachmentRepository attachmentRepository;

    @GetMapping("/images/{filename}")
    public Resource processImg(@PathVariable String filename) throws MalformedURLException {
        return new UrlResource("file:" + fileStore.createPath(filename, AttachmentType.IMAGE));
    }

    @GetMapping("/attaches/{filename}")
    public ResponseEntity<Resource> processAttaches(@PathVariable String filename, @RequestParam String originName) throws MalformedURLException {
        UrlResource urlResource = new UrlResource("file:" + fileStore.createPath(filename, AttachmentType.GENERAL));

        String encodedUploadFileName = UriUtils.encode(originName, StandardCharsets.UTF_8);
        String contentDisposition = "attachment; filename=\"" + encodedUploadFileName + "\"";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                .body(urlResource);
    }

    @PostMapping("/removeFile")
    public ResponseEntity<Boolean> removeFile(String fileName, AttachmentType fileType){
        boolean result;
//      파일삭제
        File file = new File(fileStore.createPath(fileName, fileType));
        result = file.delete();
//      DB삭제(이미지)
        Attachment attachment = attachmentRepository.findByStorefilename(fileName).get();
        attachmentRepository.delete(attachment);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
