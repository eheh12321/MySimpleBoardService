function create_board() {

    if(confirm("등록하시겠습니까?")) {
        var form = $("#boardCreateForm")[0];
        var totalFileSize = 0;
        var maxFileSize = 1024 * 1024 * 30; // 30MB
        var regex = new RegExp("(.*?)\.(exe|zip|sh)$"); // 확장자 검사
        var formData = new FormData(form);
        var inputFiles = formData.getAll("files");

        for(var i = 0; i < inputFiles.length; i++) {
            if(inputFiles[i].size >= 1024 * 1024 * 5) {
                alert("개별 파일의 크기가 5MB를 초과할 수 없습니다"); return;
            }
            if(regex.test(inputFiles[i].name)) {
                alert("해당 확장자는 업로드할 수 없습니다."); return;
            }
            totalFileSize += inputFiles[i].size;
        }

        if(maxFileSize <= totalFileSize) {
            alert("전체 파일의 크기가 30MB를 초과할 수 없습니다"); return;
        }

        $.ajax({
            type: "POST",
            enctype: 'multipart/form-data',
            url: "/api/boards",
            data: formData,
            processData: false,
            contentType: false,
            cache: false,
            timeout: 600000,
            success: function (data) {
                window.location.replace('/boards/' + data);
            },
            error: function (e) {
                console.log("ERROR : ", e);
            }
        });
    }
}

function update_board(boardId) {
    if (confirm("수정하시겠습니까?")) {
        const board_update_data = {
            title: $("#title").val(),
            content: $("#content").val()
        };

        $.ajax({
            type: 'PATCH',
            url: '/api/boards/' + boardId,
            datatype: 'JSON',
            data: JSON.stringify(board_update_data),
            contentType: 'application/json; charset=utf-8',
            success: function (result) {
                window.location.replace('/boards/' + boardId);
            },
            error: function (result) {
                alert("수정에 실패했습니다");
            }
        })
    }
}

function delete_board(boardId) {
    if (confirm("삭제하시겠습니까?")) {
        $.ajax({
            type: 'DELETE',
            url: '/api/boards/' + boardId,
            success: function (result) {
                window.location.replace('/boards');
            },
            error: function (result) {
                alert("삭제에 실패했습니다");
            }
        })
    }
}

function add_reply(boardId) {
    const reply_data = {
        boardId: boardId,
        editor: $("#replyEditor").val(),
        content: $("#replyContent").val()
    }

    $.ajax({
        type: 'POST',
        url: '/api/replies',
        datatype: 'JSON',
        data: JSON.stringify(reply_data),
        contentType: 'application/json; charset=utf-8',
        success: function (result) {
            window.location.replace('/boards/' + boardId);
        },
        error: function(result) {
            alert("등록에 실패했습니다");
        }
    });
}

function delete_reply(replyId) {
    if (confirm("삭제하시겠습니까?")) {
        $.ajax({
            type: 'DELETE',
            url: '/api/replies/' + replyId,
            success: function (result) {
                window.location.reload();
            },
            error: function (result) {
                alert("삭제에 실패했습니다");
            }
        })
    }
}