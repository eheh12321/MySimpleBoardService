function create_user() {
    if(confirm("등록하시겠습니까?")) {
        const create_user_data = {
            userId: $("#userId").val(),
            userPassword: $("#userPassword").val(),
            nickname: $("#nickname").val(),
            phoneNumber: $("#phoneNumber").val(),
            email: $("#email").val(),
            auth: $("input[name='auth']:checked").val()
        };

        $(".field-error").text(""); // 미리 Error 필드 초기화

        $.ajax({
            type: 'POST',
            url: '/user/sign-up',
            data: JSON.stringify(create_user_data),
            datatype: 'JSON',
            contentType: 'application/json; charset=utf-8',
            success: function (result) {
                alert("환영합니다!");
                window.location.replace("/login");
            },
            error: function (result) {
                markingErrorField(result);
            }
        });
    }
}


function markingErrorField(response) {
    const errorFields = response.responseJSON.errors;
    if(!errorFields) {
        alert(response.response.message);
        return;
    }

    var error;
    for(var i = 0, length = errorFields.length; i < length; i++) {
        error = errorFields[i];
        $('#' + error.field + '-field-error').text(error.message);
    }
}