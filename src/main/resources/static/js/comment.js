function commentCreate() {
    //댓글 저장
    const data = {
        postsId: $('#postsId').val(),
        comment: $('#cmt').val()
    }
    // 공백 및 빈 문자열 체크
    if (!data.comment || data.comment.trim() === "") {
        alert("댓글을 입력해주세요!!");
        return false;
    }
    const con_check = confirm("등록하시겠습니까?");
    if (con_check == true) {
        $.ajax({
            type: 'POST',
            url: '/api/boards/' + data.postsId + '/comments',
            contentType: 'application/json; charset=utf-8',
            data: JSON.stringify(data)
        }).done(function () {
            alert('댓글이 등록되었습니다.');
            window.location.reload();
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });
    }
};
