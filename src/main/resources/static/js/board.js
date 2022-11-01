function boardCreate() {
//    view에서 가져온 id인 subject와 변수명을 맞춰주지 않으면 controller로 안넘어간다.(다른 것들도 다 마찬가지 ex)name, content) -> 이것땜에 어제 날밤샜는데ㅅㅂ
    const data = {
        subject: $('#subject').val(),
        name: $('#name').val(),
        content: $('#content').val()
    }
//    var param = {"subject":data.title, "content":data.content, "name":data.name}
    // 공백 및 빈 문자열 체크
    if (!data.subject || data.subject.trim() === "" || !data.content || data.content.trim() === "") {
        alert("공백 또는 입력하지 않은 부분이 있습니다.");
        return false;
    } else {
        $.ajax({
            type: 'POST',
            url: '/api/boards',
            contentType: 'application/json; charset=utf-8',
            data: JSON.stringify(data)
        }).done(function () {
            alert('게시글이 등록되었습니다.');
            window.location.href = "/boards";
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });
    }
};

function boardUpdate() {
    const data = {
        uid: $('#bid').val(),
        subject: $('#subject').val(),
        content: $('#content').val()
    }
//  확인창에서 확인을 눌렀을때(true) 밑 if문 실행
    const con_check = confirm("수정하시겠습니까?");
    if (con_check === true) {
        if (!data.subject || data.subject.trim() === "" || !data.content || data.content.trim() === "") {
            alert("공백 또는 입력하지 않은 부분이 있습니다.");
            return false;
        } else {
            $.ajax({
                type: 'PUT',
                url: '/api/boards/' + data.uid,
                dataType: 'JSON',
                contentType: 'application/json; charset=utf-8',
                data: JSON.stringify(data)
            }).done(function () {
                alert("수정되었습니다.");
                window.location.href = '/boards/' + data.uid;
            }).fail(function (error) {
                alert(JSON.stringify(error));
            });
        }
    }
};

/** 글 삭제 */
function boardDelete() {
    const boardId = $('#boardId').val();
    const con_check = confirm("정말 삭제하시겠습니까?");

    if(con_check == true) {
        $.ajax({
            type: 'DELETE',
            url: '/api/boards/'+ boardId,
            dataType: 'JSON',
            contentType: 'application/json; charset=utf-8'

        }).done(function () {
            alert("삭제되었습니다.");
            window.location.href = '/boards';
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });
    } else {
        return false;
    }
}