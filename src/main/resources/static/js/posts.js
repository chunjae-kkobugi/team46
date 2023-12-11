function connect() {
    var socket = new SockJS('/stomp');

    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        stompClient.subscribe(
            '/stomp-receive/'+bno, // destination (String 필수)
            function (message) { // 콜백, 서버에서 받은 메시지 처리 function (message)
                let postDTO = JSON.parse(message.body)
            },
            {}  // 헤더 (Object 선택)
        );
    });
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }

    console.log("Disconnected");
}

function postAdd(content){
    let sendUrl = "/stomp-send/add/"+bno;
    stompClient.send(
        sendUrl, // destination (String 필수)
        {}, // 헤더 (Object 선택)
        JSON.stringify({ // body (String 선택)
            'content' : content,
        })
    );
}

function postEdit(){
    let sendUrl = "/stomp-send/edit/"+bno;
    stompClient.send(
        sendUrl, // destination (String 필수)
        {}, // 헤더 (Object 선택)
        JSON.stringify({ // body (String 선택)

        })
    );
}

function postRemove(){
    let sendUrl = "/stomp-send/remove/"+bno;
    stompClient.send(
        sendUrl, // destination (String 필수)
        {}, // 헤더 (Object 선택)
        JSON.stringify({ // body (String 선택)
            'content' : "Hello STOMP websocket",
            'layout': {
                'priority': 3
            }
        })
    );
}

function postMove(layout){
    let sendUrl = "/stomp-send/move/"+bno;
    stompClient.send(
        sendUrl, // destination (String 필수)
        {}, // 헤더 (Object 선택)
        JSON.stringify({ // body (String 선택)
            'pno': layout.pno,
            'layout': layout,
            'action': "SORT",
        })
    );
}

function layoutDrag(pno, x, y, z){
    let layout = {
        'pno': pno,
        'x': x,
        'y': y,
        'z': z,
    };
    postMove(layout);
}

function layoutSort(pno, priority){
    let sendUrl = "/stomp-send/sort/"+bno;
    stompClient.send(
        sendUrl, // destination (String 필수)
        {}, // 헤더 (Object 선택)
        JSON.stringify({ // body (String 선택)
            'pno': pno,
            'gPriority': priority,
            'action': "SORT",
        })
    );
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

    $("#message").val("");
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