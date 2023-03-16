package boardExample.simpleBoard.controller.api;

import boardExample.simpleBoard.domain.Attachment;
import boardExample.simpleBoard.domain.AttachmentType;
import boardExample.simpleBoard.domain.Board;
import boardExample.simpleBoard.domain.Member;
import boardExample.simpleBoard.dto.BoardAddForm;
import boardExample.simpleBoard.dto.BoardDto;
import boardExample.simpleBoard.dto.MemberDto;
import boardExample.simpleBoard.repository.AttachmentRepository;
import boardExample.simpleBoard.repository.BoardRepository;
import boardExample.simpleBoard.service.AttachmentService;
import boardExample.simpleBoard.service.BoardService;
import boardExample.simpleBoard.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.hibernate.mapping.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class BoardApiController {
    private final MemberService memberService;
    private final BoardService boardService;
    private final HttpSession httpSession;
    private final AttachmentService attachmentService;

    /* 비동기 게시글 조회 */
    @GetMapping("/boards/{uid}")
    public ResponseEntity read(@PathVariable Long uid) {
        return ResponseEntity.ok(boardService.BoardOne(uid).get());
    }
    // 비동기 게시글 저장
    @PostMapping("/boards")
    public ResponseEntity create(@ModelAttribute BoardAddForm boardAddForm, Authentication authentication) throws IOException {
        MemberDto.UserSessionDto user = (MemberDto.UserSessionDto) httpSession.getAttribute("user");
        Long uno = null;
        if (user != null) {
            uno = user.getUno();
        }
        else {
            Member member = (Member) authentication.getPrincipal();
            uno = member.getUno();
        }
        BoardDto dto = boardAddForm.createBoardDto(memberService.findByUno(uno));
        Board post = boardService.BoardAdd(dto);
        return ResponseEntity.ok(post);
    }
    // 비동기 게시글 수정
    @PutMapping("/boards/{uid}")
    public ResponseEntity update(@PathVariable("uid") Long uid, @ModelAttribute BoardAddForm form) throws IOException {
        List<String> imageList = form.getImage_list();
        List<String> generalList = form.getGeneral_list();
//      수정에서 remove후 갱신된 파일들
        HashMap<String, AttachmentType> updateFile = new HashMap<>();
        for (String i : imageList) {
            updateFile.put(i, AttachmentType.IMAGE);
        }
        for (String g : generalList) {
            updateFile.put(g, AttachmentType.GENERAL);
        }
//      갱신된 파일들 저장하기
        attachmentService.findBoardAttachments(uid, updateFile);
        List<MultipartFile> imageFiles = form.getImageFiles();
        List<MultipartFile> generalFiles = form.getGeneralFiles();
//      새로 파일첨부한것들을 update
        BoardDto dto = form.updateBoardDto(imageFiles, generalFiles, form.getSubject(), form.getContent());
        boardService.BoardUpdate(uid, dto);
        return ResponseEntity.ok(uid);
    }
    /* 비동기 게시글 삭제 */
    @DeleteMapping("/boards/{uid}")
    public ResponseEntity delete(@PathVariable Long uid) {
        boardService.BoardDelete(uid);
        return ResponseEntity.ok(uid);
    }
}
