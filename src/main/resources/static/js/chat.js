function connect() {
    var socket = new SockJS('/stomp');

    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        stompClient.subscribe('/stomp-receive/'+bno, function (chatListVO) {
            showChat(JSON.parse(chatListVO.body));
        });
    });
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }

    console.log("Disconnected");
}

function postAdd(){
    let sendUrl = "/stomp-send/add/"+bno;
}

function postEdit(){
    let sendUrl = "/stomp-send/edit/"+bno;
}

function postRemove(){
    let sendUrl = "/stomp-send/remove/"+bno;
}

function postMove(){
    let sendUrl = "/stomp-send/move/"+bno;
    stompClient.send(
        sendUrl, // destination (String 필수)
        {}, // 헤더 (Object 선택)
        JSON.stringify({ // body (String 선택)
            'message' : $("#message").val()
    }));
}


//html 에서 입력값, bno 를 받아서 Controller 로 전달
function sendChat() {
    if($("#message").val() === ''){
        return false;
    }

    stompClient.send("/stomp-send/add/"+bno, {},
        JSON.stringify({
            'message' : $("#message").val()
    }));
}

//저장된 채팅 불러오기
function loadChat(chatList){
    if(chatList != null) {
        for(chat in chatList) {
            if(chatList[chat].senderId == loginId){
                $("#chatting").append(
                    "<div class='row right'><div class='col'></div><div class='col-md-auto align-self-end task-tooltip me-3 mt-3 p-3'>" + chatList[chat].message + "</div></div>"
                );
            } else {
                $("#chatting").append(
                    "<div class='row left'><div class='col-md-auto align-self-star task-tooltip ms-3 mt-3 p-3'>" + "[" + chatList[chat].userName + "] " + chatList[chat].message + "</div><div class='col'></div>"
                );
            }
        }
    }
    updateScroll();
}

//보낸 채팅 보기
function showChat(chatListVO) {
    if(chatListVO.senderId == loginId){
        $("#chatting").append(
            "<div class='row right'><div class='col'></div><div class='col-md-auto align-self-end task-tooltip me-3 mt-3 p-3'>" + chatListVO.message + "</div></div>"
        );
    } else {
        $("#chatting").append(
            "<div class='row left'><div class='col-md-auto align-self-star task-tooltip ms-3 mt-3 p-3'>" + "[" + chatListVO.userName + "] " + chatListVO.message + "</div><div class='col'></div></div>"
        );
    }

    //this.scrollTo(0, $("#chatting").height());

    $("#message").val("");
    updateScroll();
}

$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $( "#connect" ).click(function() { connect(); });
    //$( "#disconnect" ).click(function() { disconnect(); });
    $( "#send" ).click(function() { sendChat(); });
});

//창 키면 바로 연결
window.onload = function (){
    connect();
}

window.BeforeUnloadEvent = function (){
    disconnect();
}

function updateScroll(){
    var chattingDiv = document.getElementById('chatting');
    chattingDiv.scrollTop = chattingDiv.scrollHeight;
}
