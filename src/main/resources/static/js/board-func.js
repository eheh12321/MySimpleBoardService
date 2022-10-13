function create_board() {
    if (confirm("등록하시겠습니까?")) {
        const board_data = {
            title: $("#title").val(),
            editor: $("#editor").val(),
            content: $("#content").val()
        };

        $.ajax({
            type: 'POST',
            url: '/api/boards',
            datatype: 'JSON',
            data: JSON.stringify(board_data),
            contentType: 'application/json; charset=utf-8',
            success: function (result) {
                alert("게시글이 등록되었습니다");
                window.location.replace('/boards/' + result);
            },
            error: function(result) {
                alert("등록에 실패했습니다");
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