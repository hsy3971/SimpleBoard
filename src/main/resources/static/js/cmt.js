const main = {
    init : function() {
        const _this = this;
        document.querySelectorAll('#btn-comment-update').forEach(function (item) {
            item.addEventListener('click', function () {
                const form = this.closest('#form1');
                _this.commentUpdate(form);
            });
        });
        document.querySelectorAll('#btn-comment-reply').forEach(function (item) {
            item.addEventListener('click', function () {
                const form2 = this.closest('#form2');
                _this.commentReply(form2);
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

    commentReply : function (form2) {
        const data = {
            postsId: form2.querySelector('#boardsId').value,
            comment: form2.querySelector('#comment-content2').value,
            id: form2.querySelector('#parentsId').value,
        }
        if (!data.comment || data.comment.trim() === "") {
            alert("공백 또는 입력하지 않은 부분이 있습니다.");
            return false;
        }
        const con_check = confirm("답글 작성을 완료하시겠습니까?");
        if (con_check === true) {
            $.ajax({
                type: 'POST',
                url: '/api/boards/' + data.postsId + '/comments/reply',
                contentType: 'application/json; charset=utf-8',
                data: JSON.stringify(data)
            }).done(function () {
                alert('답글 작성이 완료되었습니다.');
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
