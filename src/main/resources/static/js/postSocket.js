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
        receiveAdd();
        receiveEdit();
        receiveRemove();
        receiveSort();
        receiveLikes();
        receiveGroupAdd();
        receiveGroupEdit();
        receiveGroupRemove();
        receiveCount();
    });
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    console.log("Disconnected");
}

//창 키면 바로 연결
window.onload = function (){
    connect();
}

window.BeforeUnloadEvent = function (){
    disconnect();
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

function receiveAdd(){
    stompClient.subscribe(
        '/stomp-receive/add/'+bno, // destination (String 필수)
        function (message) { // 콜백, 서버에서 받은 메시지 처리 function (message)
            let newPost = JSON.parse(message.body);
            if(layoutNow === 'GRID'){
                let postT = postLayout(newPost);
                $("#sortable").append(postT);
            } else if(layoutNow==='TIMELINE'){
                let postT = timelineLayout(newPost);
                $(".timeline__items").append(postT);
            }
        },
{}  // 헤더 (Object 선택)
    );
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

function receiveEdit(){
    stompClient.subscribe(
        '/stomp-receive/edit/'+bno, // destination (String 필수)
        function (message) { // 콜백, 서버에서 받은 메시지 처리 function (message)
            let newPost = JSON.parse(message.body);
            if(layoutNow==='GRID'){
                let postT = postLayout(newPost);
                $(`li[data-pno=${newPost.pno}]`).replaceWith(postT);
            } else if(layoutNow==='TIMELINE'){
                let postT = timelineLayout(newPost);
                $(`.timeline__item[data-pno=${newPost.pno}]`).replaceWith(postT);
            }
        },
        {}  // 헤더 (Object 선택)
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

function receiveRemove(){
    stompClient.subscribe(
        '/stomp-receive/remove/'+bno, // destination (String 필수)
        function (message) { // 콜백, 서버에서 받은 메시지 처리 function (message)
            let pno = message.body;
            if(layoutNow==='GRID'){
                $(`li[data-pno=${pno}]`).remove();
            } else if(layoutNow==='TIMELINE'){
                $(`.timeline__item[data-pno=${pno}]`).remove();
            }
        },
        {}  // 헤더 (Object 선택)
    );
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

function receiveSort(){
    stompClient.subscribe(
        '/stomp-receive/sort/'+bno, // destination (String 필수)
        function (message) { // 콜백, 서버에서 받은 메시지 처리 function (message)
            let newOrder = JSON.parse(message.body);
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
        },
        {}  // 헤더 (Object 선택)
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

function receiveLikes(){
    stompClient.subscribe(
        '/stomp-receive/likes/'+bno, // destination (String 필수)
        function (message) { // 콜백, 서버에서 받은 메시지 처리 function (message)
            let post = JSON.parse(message.body);
            let myLike = $(`.myLike[data-pno=${post.pno}]`).next();
            myLike.text(post.likes);
        },
        {}  // 헤더 (Object 선택)
    );
}

function groupAdd(form){
    let sendUrl = "/stomp-send/groupAdd/"+bno;
    stompClient.send(
        sendUrl, // destination (String 필수)
        {}, // 헤더 (Object 선택)
        JSON.stringify({ // body (String 선택)
            'bno': bno,
            'title': form.title.value,
            'gColor': form.gColor.value,
        })
    );
}

function receiveGroupAdd(){
    stompClient.subscribe(
        '/stomp-receive/groupAdd/'+bno, // destination (String 필수)
        function (message) { // 콜백, 서버에서 받은 메시지 처리 function (message)
            let g = JSON.parse(message.body);
            let newGroup = groupLayout(g);
            $("#groupList").append(newGroup);
        },
        {}  // 헤더 (Object 선택)
    );
}

function groupEdit(form){
    let sendUrl = "/stomp-send/groupEdit/"+bno;
    stompClient.send(
        sendUrl, // destination (String 필수)
        {}, // 헤더 (Object 선택)
        JSON.stringify({ // body (String 선택)
            'gno': form.gno.value,
            'title': form.title.value,
            'gColor': form.gColor.value,
        })
    );
}

function receiveGroupEdit(){
    stompClient.subscribe(
        '/stomp-receive/groupEdit/'+bno, // destination (String 필수)
        function (message) { // 콜백, 서버에서 받은 메시지 처리 function (message)
            let g = JSON.parse(message.body);
            let newGroup = groupLayout(g);
            $(`.groupLayout[data-gno=${g.gno}]`).replaceWith(newGroup);
        },
        {}  // 헤더 (Object 선택)
    );
}

function groupRemove(gno){
    if(confirm("이 그룹을 정말로 삭제하겠습니까?")){
        let sendUrl = "/stomp-send/groupRemove/"+bno;
        stompClient.send(
            sendUrl, // destination (String 필수)
            {}, // 헤더 (Object 선택)
            JSON.stringify({ // body (String 선택)
                'gno': gno,
            })
        );
    }
}

function receiveGroupRemove(){
    stompClient.subscribe(
        '/stomp-receive/remove/'+bno, // destination (String 필수)
        function (message) { // 콜백, 서버에서 받은 메시지 처리 function (message)
            let gno = message.body;
            $(`.groupLayout[data-gno=${gno}]`).remove();
        },
        {}  // 헤더 (Object 선택)
    );
}

function commentCount(pno){
    let sendUrl = "/stomp-send/comments/" + pno;
    stompClient.send(
        sendUrl,
        {},
        JSON.stringify({
            'pno' : pno,
        })
    );
}

function receiveCount(){
    stompClient.subscribe(
        '/stomp-receive/comments/' + pno,
        function (message){
            let count = JSON.parse(message.count);
            let comments = $('.comments[data-pno='+pno+']').next();
            comments.text(count)
            $()
        }
    )
}