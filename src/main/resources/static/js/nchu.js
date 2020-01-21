/**
 * 提交回复
 */
function post() {
    var postId = $("#post_id").val();
    var content = $("#comment_content").val();
    comment2target(postId, 1, content);
}

function comment2target(targetId, type, content) {
    if (!content) {
        alert("不能回复空内容~~~");
        return;
    }

    $.ajax({
        type: "POST",
        url: "/comment/add",
        contentType: 'application/json',
        data: JSON.stringify({
            "postId": targetId,
            "content": content,
            "type": type
        }),
        success: function (response) {
            if (response.code == 200) {
                window.location.reload();
            } else {
                if (response.code == 2003) {
                    var isAccepted = confirm(response.message);
                    if (isAccepted) {
                        window.open("https://github.com/login/oauth/authorize?client_id=2859958f9f059979ed3a&redirect_uri=" + document.location.origin + "/callback&scope=user&state=1");
                        window.localStorage.setItem("closable", true);
                    }
                } else {
                    alert(response.message);
                }
            }
        },
        dataType: "json"
    });
}

/**
 * 添加评论
 */
function addComment(){
    var postId = $("#postId").val();
    var content = $("#commentContent").val();
    if (!content) {
        alert("不能回复空内容~~~");
        return;
    }
    $.ajax({
        type: "POST",
        url: "/comment/add",
        contentType: 'application/json',
        data: JSON.stringify({
            "postId": postId,
            "content": content,
        }),
        success: function (response) {
            if (response.code == 200) {
                alert("200-通过");
                window.location.reload();
            } else {
                if (response.code == 2003) {
                    var isAccepted = confirm(response.message);
                    if (isAccepted) {
                        /*window.open( "");
                        window.localStorage.setItem("closable", true);*/
                        alert("emmm")
                    }
                } else {
                    alert(response.message);
                }
            }
        },
        dataType: "json"
    });
}
/**
 * 评论回复输入框
 */
function showReply(commentId) {
    var replyList = document.getElementById("replyList"+commentId);
    if(replyList.style.display== 'none'){
        replyList.style.display = 'block';
    } else {
        replyList.style.display = 'none';
    }
}

/**
 * 查看回复
 */
function commentReply(id,commentId){
    var commentReplyForm = document.getElementById("commentReplyForm"+commentId)
    if(commentReplyForm.style.display== 'none'){
        commentReplyForm.style.display = 'block'
    } else {
        commentReplyForm.style.display = 'none'
    }
    var toId = document.getElementById('toId');
    toId.value = id;
}
function changeMusic() {
    var a = document.getElementById('changeMusic');
    a.src='https://api.uomg.com/api/rand.music';
}

/**
 * 修改个人信息按钮
 */
function changeInfo() {
    var visibility = document.getElementById('changeInfo');
    if (visibility.style.display=='none'){
        visibility.style.display='block';
    }else  {
        visibility.style.display='none'
    }
}