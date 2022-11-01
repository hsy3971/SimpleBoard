function usernameCheck() {
    const username = $("#uid").val();
    if (username == "") {
        alert("아이디를 입력해주세요!. 필수항목입니다.");
        $("#uid").focus();
        return false;
    }
//    https://kuzuro.blogspot.com/2018/04/16.html
// 문제점 : 편법으로 사용가능한 아이디를 치고 중복체크를 한다음에 다시 중복되는 아이디를 치고 가입을 하면 가입이 완료
// 위의 문제점을 막기위해 keyup을 통해서 값을 치기만 하더라도 회원가입 버튼을 막는다.
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

