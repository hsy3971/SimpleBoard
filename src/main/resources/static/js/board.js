function boardCreate() {
    const createForm = $('#createForm');
    const boardAddForm = new FormData(createForm[0]);
    const con_check = confirm("등록하시겠습니까?");
    if (con_check === true) {
        if(!boardAddForm.get('subject') || !boardAddForm.get('content')) {
            alert("공백 또는 입력하지 않은 부분이 있습니다.");
            return false;
        }
        $.ajax({
            type: 'POST',
            url: '/api/boards',
            processData: false,
            contentType: false,
    //      List<MultipartFile> = 다중파일로 넘기기
            enctype: 'multipart/form-data',
            data: boardAddForm,
        }).done(function () {
            alert('게시글이 등록되었습니다.');
            window.location.href = "/boards";
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });
    }
};

function removeBtn(id, attachmentType) {
    var fileName = id;
//  removeId를 통해 각 id별로 클래스명을 달리한다.
    var removeId = "removeButton"+id;
    var className = document.getElementsByClassName(removeId);
    var fileType = attachmentType;

    $.post('/removeFile',{fileName : fileName, fileType : fileType}, function(result){
        console.log(result);
        if(result === true){
            className[0].style.display = 'none';
        }
    })
};

function removeBtn2(id, attachmentType) {
    var fileName = id;
    var removeId = "removeButton2"+id;
    var className = document.getElementsByClassName(removeId);
    var fileType = attachmentType;

    $.post('/removeFile',{fileName : fileName, fileType : fileType}, function(result){
        console.log(result);
        if(result === true){
            className[0].style.display = 'none';
        }
    })
};

function boardUpdate() {
    const uid = $("#bid").val();
    const updateForm = $('#updateForm');
    const boardAddForm = new FormData(updateForm[0]);
    const con_check = confirm("수정하시겠습니까?");
    if (con_check === true) {
        if(!boardAddForm.get('subject') || !boardAddForm.get('content')) {
            alert("공백 또는 입력하지 않은 부분이 있습니다.");
            return false;
        } else {
            $.ajax({
                type: 'PUT',
                url: '/api/boards/' + uid,
                processData: false,
                contentType: false,
        //      List<MultipartFile> = 다중파일로 넘기기
                enctype: 'multipart/form-data',
                data: boardAddForm,
            }).done(function () {
                alert('수정되었습니다.');
                window.location.href = "/boards";
            }).fail(function (error) {
                alert(JSON.stringify(error));
            });
        }
    }
};
// 글 삭제
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
};