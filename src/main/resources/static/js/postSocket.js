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
        stompClient.subscribe(
            '/stomp-receive/edit/'+bno, // destination (String 필수)
            function (message) { // 콜백, 서버에서 받은 메시지 처리 function (message)
                let newPost = JSON.parse(message.body);
                receiveEdit(newPost);
            },
            {}  // 헤더 (Object 선택)
        );
        stompClient.subscribe(
            '/stomp-receive/remove/'+bno, // destination (String 필수)
            function (message) { // 콜백, 서버에서 받은 메시지 처리 function (message)
                receiveRemove(message.body);
            },
            {}  // 헤더 (Object 선택)
        );
        stompClient.subscribe(
            '/stomp-receive/likes/'+bno, // destination (String 필수)
            function (message) { // 콜백, 서버에서 받은 메시지 처리 function (message)
                receiveLikes(JSON.parse(message.body));
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
    let sendUrl = "/stomp-send/add/"+bno;
    stompClient.send(
        sendUrl, // destination (String 필수)
        {}, // 헤더 (Object 선택)
        JSON.stringify({ // body (String 선택)
            'pno' : pno,
        })
    );
    // 등록 버튼 누르면 바로 모달 창 닫기
    $('#postRegisterModal .btn-close').click();
}
function postEdit(pno){
    let sendUrl = "/stomp-send/edit/"+bno;
    stompClient.send(
        sendUrl, // destination (String 필수)
        {}, // 헤더 (Object 선택)
        JSON.stringify({ // body (String 선택)
            'pno' : pno,
        })
    );
}

function postRemove(pno){
    if(confirm("정말로 삭제하시겠습니까?")){
        let sendUrl = "/stomp-send/remove/"+bno;
        stompClient.send(
            sendUrl, // destination (String 필수)
            {}, // 헤더 (Object 선택)
            JSON.stringify({ // body (String 선택)
                'pno' : pno
            })
        );
    }
    return false;
}

function layoutSort(pno, priority){
    let sendUrl = "/stomp-send/sort/"+bno;
    stompClient.send(
        sendUrl, // destination (String 필수)
        {}, // 헤더 (Object 선택)
        JSON.stringify({ // body (String 선택)
            'pno': pno,
            'gPriority': priority,
        })
    );
}

function toggleLike(pno, sid){
    let sendUrl = "/stomp-send/likes/"+bno;
    stompClient.send(
        sendUrl, // destination (String 필수)
        {}, // 헤더 (Object 선택)
        JSON.stringify({ // body (String 선택)
            'pno': pno,
            'author': sid,
        })
    );
}

function receiveLikes(post){
    let myLike = $(`.myLike[data-pno=${post.pno}]`).next();
    myLike.text(post.likes);
}

// 다른 사람이 순서 바꿨을 때 나한테도 적용
function receiveSort(newOrder){
    if(layoutNow==='GRID'){
        let fragment = document.createDocumentFragment();
        newOrder.forEach(function(order){
            let pnoLi = document.querySelector(`#sortable > li[data-pno=${order}]`);
            fragment.appendChild(pnoLi);
        });
        document.getElementById('sortable').appendChild(fragment);
    }
    else if(layoutNow === 'TIMELINE'){
        let fragment = document.createDocumentFragment();
        newOrder.forEach(function(order){
            let pnoLi = $(`.timeline__items > .timeline__item[data-pno=${order}]`)[0];
            console.log(pnoLi);
            fragment.appendChild(pnoLi);
        });
        document.getElementsByClassName('timeline__items').appendChild(fragment);
        alert("TIMELINE");
    }
}
function receiveAdd(p){
    if(layoutNow === 'GRID'){
        let postT = postLayout(p);
        $("#sortable").append(postT);
    } else if(layoutNow==='TIMELINE'){
        let newPost = timelineLayout(p);
        $(".timeline__items").append(newPost);
    }

}

function receiveEdit(newPost){
    if(layoutNow==='GRID'){
        let postT = postLayout(newPost);
        $(`li[data-pno=${newPost.pno}]`).replaceWith(postT);
    } else if(layoutNow==='TIMELINE'){
        let postT = timelineLayout(newPost);
        $(`.timeline__item[data-pno=${newPost.pno}]`).replaceWith(postT);
    }
}

function receiveRemove(pno){
    if(layoutNow==='GRID'){
        $(`li[data-pno=${pno}]`).remove();
    } else if(layoutNow==='TIMELINE'){
        $(`.timeline__item[data-pno=${pno}]`).remove();
    }
}

//창 키면 바로 연결
window.onload = function (){
    connect();


}
window.BeforeUnloadEvent = function (){
    disconnect();
}