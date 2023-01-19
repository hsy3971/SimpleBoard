package boardExample.simpleBoard.repository;

import boardExample.simpleBoard.domain.Attachment;
import boardExample.simpleBoard.domain.Board;
import boardExample.simpleBoard.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AttachmentRepository extends JpaRepository<Attachment, Long> {
//  filename으로 찾기
    Optional<Attachment> findByStorefilename(String storefilename);
//    Optional<Attachment> findByAttachmentBoard_Uid(Long id);
}
