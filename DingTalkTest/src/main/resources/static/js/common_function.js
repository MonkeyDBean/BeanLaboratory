//发送群消息的path
const sendMessagePath = "/test/chat/text/send";

//已选择的内部会话id
var pickedChatId;

/**
 * ajax请求封装
 *
 * @param url 请求url
 * @param type 请求类型
 * @param data 请求参数
 * @param success 成功回调函数
 */
function doAjax(url, type, data, success) {
    $.ajax({
        type: type,
        url: url,
        data: data,
        dataType: "json",
        timeout: 20000,
        success: function (data) {
            if (!!data.code) {
                console.log("doAjax code not success, is: " + data.code);
                return;
            }
            success(data.data);
        },
        error: function (err) {
            console.log("doAjax, unknown error, error status: " + err.status);
        }
    });
}

/**
 * ajax get请求
 */
function doGetAjax(url, data, success) {
    doAjax(url, "get", data, success)
}

/**
 * ajax post请求
 */
function doPostAjax(url, data, success) {
    doAjax(url, "post", data, success)
}

/**
 * 通过corpId选取会话, 仅内部会话
 */
function chooseConversation() {
    if (!$("#corpId").val()) {
        $("#showResult").html("<b>Param Lack</b>");
        return;
    }
    $("#showResult").html("Request <b>Waiting...</b>");
    dd.biz.chat.chooseConversationByCorpId({
        corpId: $("#corpId").val(),
        isAllowCreateGroup: false,
        filterNotOwnerGroup: false,
        onSuccess: function (result) {
            alertAfterChooseConversation(result);
        },
        onFail: function (err) {
            alertAfterChooseConversation(err);
        }
    });
}

/**
 * 选取会话后的弹窗
 */
function alertAfterChooseConversation(data) {
    alertMsg(data, function () {
        $("#showResult").append("-> Response Success, Content: " + JSON.stringify(data));
        pickedChatId = data.chatId;
        $("#conversationTextInput").css("display", "block");
    }, function (err) {
        $("#showResult").append("-> Response error, Content: " + JSON.stringify(err));
    })
}

/**
 * 弹窗
 */
function alertMsg(data, success, fail) {
    dd.device.notification.alert({
        message: JSON.stringify(data),
        title: "请求成功",
        buttonName: "确定",
        onSuccess: success,
        onFail: fail
    });
}

/**
 * 发送会话消息
 */
function sendConversionMessage() {
    if (!pickedChatId) {
        return;
    }
    var nowUrl = window.location.href;
    var nowUrlArray = nowUrl.split("/");
    var protocol = nowUrlArray[0];
    var domain = window.location.host;
    var reqUrl = protocol + "//" + domain + sendMessagePath;
    var content = $("#conversationTextInput").val();
    var data = {"id": pickedChatId, "content": content};
    doPostAjax(reqUrl, data);
}