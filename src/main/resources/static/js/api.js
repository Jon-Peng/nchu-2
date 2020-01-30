
//加载执行
window.onload = function () {

    console.log("on...")
    backgroundImage();  //背景图
    cibaApi();          //词霸图
    getPoem();          //诗词
    getNews();          //新闻

}

//背景图
function backgroundImage() {
    var nowDate = new Date().getFullYear() + '_' + new Date().getMonth()+1 + '_' + new Date().getDate();
    console.log("背景图api时间--"+nowDate);
    document.getElementById("body").style="background-attachment:fixed ;background-image: url('https://api.sanv.org/api/bingtodayimg/timthumb.php?src=https://api.sanv.org/api/bingtodayimg/savebingimg_cn/"+
        nowDate + ".jpg&h=900&w=1400&zc=1')"
}

//词霸api
function cibaApi() {
    $.ajax({
        url:"https://api.ooopn.com/ciba/api.php",
        dataType: "json",
        success:function (res) {
            console.log("---词霸---"+res);
            document.getElementById("ciba").src=res.imgurl;
        }
    })
}


//news api
function getNews() {

    $.ajax({
        url:"https://news.topurl.cn/api",
        dataType: "json",
        success:function (res) {
            console.log(res);
            var newsList = res.data.newsList;
            var historyList = res.data.historyList;
            var aNew = "";
            var history = "";

            for (var num in newsList){
                aNew += "<li><a target='_blank' href='" + newsList[num].url + "'>" + newsList[num].title + "</a></li>";
            }
            document.getElementById("news").innerHTML = aNew;

            for (var num2 in historyList){
                history += "<li>" + historyList[num2].event + "</li>";
            }
            document.getElementById("history").innerHTML = history;

        }
    })
}

//今日诗词 api
function getPoem() {

    var xhr = new XMLHttpRequest();
    xhr.open('get', 'https://v1.jinrishici.com/all.json');
    xhr.onreadystatechange = function () {
        if (xhr.readyState === 4) {
            var data = JSON.parse(xhr.responseText);
            document.getElementById('gushiContent').innerHTML = data.content;
            document.getElementById('gushiAuthor').innerHTML = data.author;
            document.getElementById('gushiName').innerHTML = data.origin;
        }
    };
    xhr.send()
}