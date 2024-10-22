function usernameCheck() {
    const username = $("#uid").val();
    if (username == "") {
        alert("아이디를 입력해주세요! 필수항목입니다.");
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
            const validationMessageElement = $('#username-validation');
            validationMessageElement.removeClass("invalid-feedback").addClass("valid-feedback");
            validationMessageElement.text("사용 가능한 아이디입니다.");
            validationMessageElement.show();
            // 성공 시 가입버튼 활성화
            $("#submit").removeAttr("disabled");
        }, error: function(error) {
            const validationMessageElement = $('#username-validation');
            validationMessageElement.removeClass("valid-feedback").addClass("invalid-feedback");
            validationMessageElement.text("이미 사용 중인 아이디입니다.");
            validationMessageElement.show();
            // 실패시 가입버튼 비활성화
            $("#submit").attr("disabled", "disabled");
        }
    });
}

