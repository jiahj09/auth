//相关url地址
const host = "http://localhost:40000/auth"; // 授权api域名地址
const support_api = "/support/";
const init_api = "/init/";
const status_api = "/status/";
const input_api = "/input/";
//一下为相关状态标识
const status_input = "INPUT";
const status_doing = "DOING";
const status_error = "ERROR";
const status_done = "DONE";
const status_success = "SUCCESS";
var resObj = {}; // 用户接收返回的信息对象


function setCookie(key, value, t) {
    var oDate = new Date();
    oDate.setDate(oDate.getDate() + t);
    document.cookie = key + "=" + value + "; expires=" + oDate.toDateString();
}

function getCookie(key) {
    var arr1 = document.cookie.split("; ");//由于cookie是通过一个分号+空格的形式串联起来的，所以这里需要先按分号空格截断,变成[name=Jack,pwd=123456,age=22]数组类型；
    for (var i = 0; i < arr1.length; i++) {
        var arr2 = arr1[i].split("=");//通过=截断，把name=Jack截断成[name,Jack]数组；
        if (arr2[0] == key) {
            return decodeURI(arr2[1]);
        }
    }
}

/**
 * [removeCookie 移除cookie]
 */
function removeCookie(key) {
    setCookie(key, "", -1); // 把cookie设置为过期
};


function sleep(delay) {
    let start = (new Date()).getTime();
    while((new Date()).getTime() - start < delay) {
        continue;
    }
}