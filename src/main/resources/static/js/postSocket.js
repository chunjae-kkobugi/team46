function connect() {
    var socket = new SockJS('/stomp');

    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        stompClient.subscribe(
            '/stomp-receive/'+bno, // destination (String 필수)
            function (message) { // 콜백, 서버에서 받은 메시지 처리 function (message)
                console.log(message);
            },
            {}  // 헤더 (Object 선택)
        );
        stompClient.subscribe(
            '/stomp-receive/sort/'+bno, // destination (String 필수)
            function (message) { // 콜백, 서버에서 받은 메시지 처리 function (message)
                let newOrder = JSON.parse(message.body);
                receiveSort(newOrder);
            },
            {}  // 헤더 (Object 선택)
        );
        stompClient.subscribe(
            '/stomp-receive/add/'+bno, // destination (String 필수)
            function (message) { // 콜백, 서버에서 받은 메시지 처리 function (message)
                let newPost = JSON.parse(message.body);
                receiveAdd(newPost);
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

function postAdd(pno){
    alert("post ADD of postSocket");
    let sendUrl = "/stomp-send/add/"+bno;
    stompClient.send(
        sendUrl, // destination (String 필수)
        {}, // 헤더 (Object 선택)
        JSON.stringify({ // body (String 선택)
            'pno' : pno,
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

// 다른 사람이 순서 바꿨을 때 나한테도 적용
function receiveSort(newOrder){
    let fragment = document.createDocumentFragment();
    newOrder.forEach(function(order){
        let pnoLi = document.querySelector(`#sortable > li[pno='${order}']`);
        fragment.appendChild(pnoLi);
    });

    if(layoutNow === 'GRID'){
        document.getElementById('sortable').appendChild(fragment);
    }
}

function receiveAdd(newPost){
    let fragment = `<div th:replace="~{include/postLayout :: post-layout(p=${newPost})}" class="mb-0"></div>`;

    if(layoutNow === 'GRID'){
        document.getElementById('sortable').appendChild(fragment);
    }
}

//창 키면 바로 연결
window.onload = function (){
    connect();
}


window.BeforeUnloadEvent = function (){
    disconnect();
}