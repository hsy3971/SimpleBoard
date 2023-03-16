package boardExample.simpleBoard.repository;

import boardExample.simpleBoard.domain.Attachment;
import boardExample.simpleBoard.domain.Board;
import boardExample.simpleBoard.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface AttachmentRepository extends JpaRepository<Attachment, Long> {
    //  filename으로 찾기
    Optional<Attachment> findByStorefilename(String storefilename);
    @Transactional
    @Modifying
//  ids리스트안에 attachment의 id가 포함되어 있다면 제거하라.
    @Query("delete from Attachment a where a.id in :ids")
    void deleteAllByIds(@Param("ids") List<Long> ids);
}
