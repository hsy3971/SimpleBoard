function usernameCheck() {
    const username = $("#uid").val();
    if (username == "") {
        alert("아이디를 입력해주세요!. 필수항목입니다.");
        $("#uid").focus();
        return false;
    }
    $("#uid").keyup(function(){
         $("#submit").attr("disabled", "disabled");
    });
    $.ajax({
        url:'/api/users-ids/' + username + '/exists',
        type: 'GET',
        contentType: 'application/json',
        headers: {
            // 스프링 시큐리티를 위한 헤더 설정
            "X-CSRF-TOKEN": $("meta[name='_csrf']").attr("content")
        },
        data: {
            uid: $('#uid').val()
        },

        success: function (result) {
            // 성공 시 실패 메시지 hide, 성공 메시지 show
            $('#idNotAvailable').hide();
            //성공시 가입버튼 활성화
            $("#submit").removeAttr("disabled");
            $('#idAvailable').show().text(result).append($('<br />'));
        }, error: function(error) {
            // 실패 시 실패 메시지 show, 성공 메시지 hide
            $('#idAvailable').hide();
            //실패시 가입버튼 비활성화
            $("#submit").attr("disabled","disabled");
            $('#idNotAvailable').show().text('이미 사용중인 아이디 입니다.').append($('<br />'));
        }
    });
}

