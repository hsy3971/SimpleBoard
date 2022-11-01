// 방대한 cmt.js라는 파일에 이러한 긴 코드를 한번에 실행하는 것 자체가 오류가 있었다. 그래서 다음에 할때는 밑에 리스트대로 확인을 하자.
// 1. 버튼을 클릭했을시에 ajax가 즉 js파일에 함수가 잘 동작이 되는지부터 확인을 한다.(함수안에 아무것도 없이 alert를 찍어보던지 하자)
// 2. 만든 ajax코드가 잘 동작이 되는지 확인을 하자. 예를 들어 밑에 코드라면 init함수부터 잘 동작이 되는지 확인을 하고 그 다음에 update, delete순으로 진행을 하자.
// 사실 이번에 앞에 collapse부터 시작해서 ajax까지 많이 고생을 했다. 특히 ajax은 js파일이기 때문에 출력을 시키는데 있어서 조금 생소했다. 이것도 코딩과 마찬가지로
// 잘 실행이 되는지 하나하나 찍어보면 된다. 그러고 나서 안되는건 오류구문이 출력이 될테니 그건 구글링해서 찾으면 될 문제다. 다음엔 위의 리스트대로 잘 수행하길 바란다.
const main = {
    init : function() {
        const _this = this;
        document.querySelectorAll('#btn-comment-update').forEach(function (item) {
            item.addEventListener('click', function () {
                const form = this.closest('form');
                _this.commentUpdate(form);
            });
        });
    },
    commentUpdate : function (form) {
        const data = {
            id: form.querySelector('#cId').value,
            postsId: form.querySelector('#pId').value,
            comment: form.querySelector('#comment-content').value,
        }
        if (!data.comment || data.comment.trim() === "") {
            alert("공백 또는 입력하지 않은 부분이 있습니다.");
            return false;
        }
        const con_check = confirm("수정하시겠습니까?");
        if (con_check === true) {
            $.ajax({
                type: 'PUT',
                url: '/api/boards/' + data.postsId + '/comments/' + data.id,
                contentType: 'application/json; charset=utf-8',
                data: JSON.stringify(data)
            }).done(function () {
                alert('댓글이 수정되었습니다.');
                window.location.reload();
            }).fail(function (error) {
                alert(JSON.stringify(error));
            });
        }
    },

    commentDelete : function (postsId, commentId) {
        const con_check = confirm("삭제하시겠습니까?");
        if (con_check === true) {
            $.ajax({
                type: 'DELETE',
                url: '/api/boards/' + postsId + '/comments/' + commentId,
            }).done(function () {
                alert('댓글이 삭제되었습니다.');
                window.location.reload();
            }).fail(function (error) {
                alert(JSON.stringify(error));
            });
        }
    }
};

main.init();
