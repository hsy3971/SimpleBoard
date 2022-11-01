package boardExample.simpleBoard.service;

import boardExample.simpleBoard.domain.Board;
import boardExample.simpleBoard.domain.Member;
import boardExample.simpleBoard.dto.BoardDto;
import boardExample.simpleBoard.repository.BoardRepository;
import boardExample.simpleBoard.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;

    // BoardList와 BoardOne도 BoardDto로 변경을 해야하는데 생각보다 쉽지않다. 그래서 일다 넘어가자.
    public List<Board> BoardList() {
        return boardRepository.findAll();
    }
    // 페이징
    @Transactional
    public Page<Board> pageList(Pageable pageable) {
        int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1);
//        페이지 번호(0부터 시작하기때문에 ex) 1이라면 0으로 저장된다?), 페이지당 데이터의 수, 정렬방향
        pageable = PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "uid"));
        return boardRepository.findAll(pageable);
    }

    public Optional<Board> BoardOne(Long uid) {
        //jdbcTemplate을 통해 selectOne이라는 함수를 썼지만 나는 JpaRepository를 사용하였다.
        //차이점은 findById는 uid의 값형태가 Long형에 해당하지만 jdbctemplate은 String에 해당한다.
        //이후에 문제가 발생하였을때 참고하기
        return boardRepository.findById(uid);
    }
    // Transactional : 오류가 났을때 롤백 하기 위함
    @Transactional
    public Long BoardAdd(BoardDto board) {
        Member num = memberRepository.findByUno(board.getMember().getUno());
        Board data = board.toEntity();
        return boardRepository.save(data).getUid();
    }

    public void BoardDelete(Long uid) {
        boardRepository.deleteById(uid);
    }

    //변경감지를 통한 update구현?
    //일단 update변경감지를 통해서 일어나게 된다면 따로 무슨 함수를 해줄 필요가 없는건가?
    //변경감지가 뭐였는지 다시 한번 remind하기
    //그리고 블로그에 editForm.html에 해당하는게 없다.(사실 addForm과 동일한 형태이긴 하다.)
    @Transactional
    public Long BoardUpdate(Long uid, BoardDto boardDto) {
        SimpleDateFormat format = new SimpleDateFormat ( "yyyy-MM-dd HH:mm");
        Date time = new Date();
        String localTime = format.format(time);
        // updateBoard를 통해서 제목과 내용만 바꿀 수 있게 설정
        Board board = boardRepository.findById(uid).orElseThrow(()->new IllegalArgumentException("해당 게시글이 없습니다. id = " + uid));
        board.updateBoard(boardDto.getSubject(), boardDto.getContent(), localTime);
        return uid;
    }

//    1. 검색을 했을 때 10개이상이어서 페이지가 2페이지 이상일 경우, 2번을 클릭하면 2번페이지의 키워드로 검색한 리스트가 나와야 하는데 나오질 않는다.
//    -> html에서 그대로 ,searchKeyword로 받아서 같이 url로 출력하면 되고, 문제는 boardservice에 있는 findByContaining함수가 문제였다.
//    페이징에서 했던 int page를 초기화를 해줬어야 하는데 그러지않아서 되질 않았다. 하고나니까 잘 돌아감.
//    (나중에 안 문제는 1번페이지에 해당하는게 page=0에 있고 2번페이지에 해당하는게 page=1에 있고 번호가 하나씩 밀려있다.) -> 이 문제를 알고나니 어떤 부분이 문제인지를 금방 찾았다.
    @Transactional
    public Page<Board> findBySubjectContaining(String searchKeyword, Pageable pageable) {
        int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1);
//        페이지 번호(0부터 시작하기때문에 ex) 1이라면 0으로 저장된다?), 페이지당 데이터의 수, 정렬방향
        pageable = PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, "uid"));
        return boardRepository.findBySubjectContaining(searchKeyword, pageable);
    }
    @Transactional
    public int viewCount(Long uid) {
        return boardRepository.viewCount(uid);
    }

    @Transactional
    public Member findUno(Long uid) {
        return memberRepository.findByUno(uid);
    }

    /* READ 게시글 리스트 조회 readOnly 속성으로 조회속도 개선 */
    // 이것도 결국에는 findById를 통해서 id값을 통해 게시글을 가져오는데 기존에 있던 BoardOne메서드는 entity를 return한다. 하지만 이건 dto를 return한다.(차이점) -> api에서 이 메서드를 써야하는 이유
    @Transactional(readOnly = true)
    public BoardDto.Response findBoard(Long id) {
        Board board = boardRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("해당 게시글이 존재하지 않습니다. id: " + id));
        // entity -> dto로 변환후 return
        return new BoardDto.Response(board);
    }
}
