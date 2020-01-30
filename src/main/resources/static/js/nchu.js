
window.onload = function () {
    console.log("页面加载完成");
}

//监测是否需要刷新页面
window.addEventListener('pageshow', function(event) {
    if(event.persisted) { // ios 有效, android 和 pc 每次都是 false
        location.reload();
    } else { // ios 除外
        if(sessionStorage.getItem('refresh') === 'true') {
            console.log("页面刷新！")
            location.reload();
        }
    }
    sessionStorage.removeItem('refresh');
});

/**
 * 登录
 */
function login() {
    var stuId = $("#stuId").val();
    var password = $("#password").val();
    $.ajax({
        type:"POST",
        url:"/user/login",
        data:JSON.stringify({
            "stuId":stuId,
            "password":password
        }),
        contentType: 'application/json',
        dataType:"json",
        success:function (res) {
            if (res.code==200){
                // alert("ajax成功");
                sessionStorage.setItem('refresh', 'true');
                window.history.go(-1);
                //window.history.go(-1);        //返回+刷新
                //window.history.forward();  //前进
                //window.history.back();       //返回
                console.log(res)
            }else {
                alert(res.message);
            }
        }

    })
}

/**
 * 退出登錄
 */
function logout() {
    $.ajax({
        dataType:"json",
        url:"/user/logout",
        success:function (res) {
            // 2 window.location.go(-1) 是刷新上一页
            //1 window.history.go(-1) 是返回上一页
            // window.history.go(-1);
            location.reload();
            console.log("退出成功")
        }
    })
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
                console.log("评论添加成功");
                window.location.reload();
                // window.location.reload();
            } else {
                    alert(response.message);
            }
        },
        dataType: "json"
    });
}

/**
 * 评论回复输入框（显示/隐藏）
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
 * 查看回复（显示/隐藏）
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

/**
 * 修改个人信息按钮（显示/隐藏）
 */
function changeInfo() {
    var visibility = document.getElementById('changeInfo');
    if (visibility.style.display=='none'){
        visibility.style.display='block';
    }else  {
        visibility.style.display='none'
    }
}

/**
 * 切歌
 */
function changeMusic() {
    var a = document.getElementById('changeMusic');
    a.src='https://api.uomg.com/api/rand.music';
}