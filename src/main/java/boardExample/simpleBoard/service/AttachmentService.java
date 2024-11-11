package boardExample.simpleBoard.service;

import boardExample.simpleBoard.domain.*;
import boardExample.simpleBoard.repository.AttachmentRepository;
import boardExample.simpleBoard.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Service
@RequiredArgsConstructor
public class AttachmentService {

    private final AttachmentRepository attachmentRepository;
    private final BoardRepository boardRepository;
    private final FileStore fileStore;

    public List<Attachment> saveAttachments(Map<AttachmentType, List<MultipartFile>> multipartFileListMap) throws IOException {
        List<Attachment> imageFiles = fileStore.storeFiles(multipartFileListMap.get(AttachmentType.IMAGE), AttachmentType.IMAGE);
        List<Attachment> generalFiles = fileStore.storeFiles(multipartFileListMap.get(AttachmentType.GENERAL), AttachmentType.GENERAL);
        List<Attachment> result = Stream.of(imageFiles, generalFiles)
                .flatMap(f -> f.stream())
                .collect(Collectors.toList());
        return result;
    }
    //  board별 전체 첨부파일(이미지, 일반)
    public void findBoardAttachments(Long id, HashMap<String, AttachmentType> updateFile) {
        Board board = boardRepository.findById(id).get();
//      board 전체 파일(이미지,일반)
        List<Attachment> attachedFiles = board.getAttachedFiles();
//      기존에 해당 게시글에 있던 첨부파일들을 받을 id리스트
        List<Long> ids = new ArrayList<>();
//      해당 Board의 전체 첨부파일과 updateFile비교
        for (Attachment a : attachedFiles) {
            String storeFilename = a.getStorefilename();
            AttachmentType attachmentType = a.getAttachmenttype();
//          해당 게시글(Board)의 전체 첨부파일과 updateFile과 비교시에 포함하지 않는것들만 삭제해주면 된다.
            if(!updateFile.containsKey(storeFilename)) {
//                로컬에서 삭제
//                File file = new File(fileStore.createPath(storeFilename, attachmentType));
//                boolean delete = file.delete();
                // S3에서 파일 삭제
                fileStore.deleteFileFromS3(storeFilename, attachmentType);
                Attachment filename = attachmentRepository.findByStorefilename(storeFilename).get();
//              지워줄 파일들을 ids에 넣어준다.
                ids.add(filename.getId());
            }
        }
//      해당 ids를 포함하는 파일들을 모두삭제(DB)
        attachmentRepository.deleteAllByIds(ids);
    }
}
