function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#conversation").show();
    }
    else {
        $("#conversation").hide();
    }
    $("#chatting").html("");
}

function connect() {
    var socket = new SockJS('/stomp');

    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        setConnected(true);

        loadChat(chatList)  //저장된 채팅 불러오기
        stompClient.subscribe('/stomp-receive/'+roomId, function (chatListVO) {
            showChat(JSON.parse(chatListVO.body));
        });
    });
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

//html 에서 입력값, roomId 를 받아서 Controller 로 전달
function sendChat() {
    if($("#message").val() === ''){
        return false;
    }

    stompClient.send("/chat/send/"+roomId, {},
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
