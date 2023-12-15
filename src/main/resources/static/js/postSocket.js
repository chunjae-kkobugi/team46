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
                receiveAdd(message.body);
            },
            {}  // 헤더 (Object 선택)
        );
        stompClient.subscribe(
            '/stomp-receive/edit/'+bno, // destination (String 필수)
            function (message) { // 콜백, 서버에서 받은 메시지 처리 function (message)
                receiveEdit(message.body);
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
    let sendUrl = "/stomp-send/remove/"+bno;
    stompClient.send(
        sendUrl, // destination (String 필수)
        {}, // 헤더 (Object 선택)
        JSON.stringify({ // body (String 선택)
            'pno' : pno
        })
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
    let p = JSON.parse(newPost);
    // 문자열로 정의된 HTML 코드를 HTML 엘리먼트로 변환

    let postT = `
<li class="col-2 mb-3 mt-2 ui-sortable-handle ui-state-default" data-pno="${p.pno}">    
    <div class="m-2">
        <div class="card shadow-sm">
            <!-- 포스트잇 내용-->
            <div class="card-body" id="post${p.pno}" style="${(p.bgImage==null)? 'background-color: '+p.bgColor : null}">
                <div class="original" id="original${p.pno}">
                    <div id="postMenuList+${p.pno}" class="pe-2"
                         style="position: absolute; right: 0; height: auto; z-index: 10; ">
                        <ul class="p-0" style="list-style-type: none">

                            <!-- 수정버튼 -->
                            <li class="me-2" id="postModifyBtn${p.pno}"
                                style="${(p.bgImage==null)? 'cursor: pointer; background-color : ' + p.bgColor : '#eeeeee'}">
                                <span style="${(p.bgColor=='#ffffff' || p.bgColor == null) ? 'color : #333333' : 'mix-blend-mode: difference; color : #eeeeee'}">
                                    <i class="fa-pen-to-square fa-solid"></i>
                                </span>
                            </li>

                            <!-- 삭제 버튼 -->
                            <li id="postRemoveBtn${p.pno}"
                                style="${ p.bgImage==null ? 'cursor: pointer; background-color: ' + p.bgColor : '#eeeeee' }">
                                <span style="${p.bgColor=='#ffffff' || p.bgColor==null ? 'color : #333333' : 'mix-blend-mode: difference; color : #eeeeee'}">
                                    <i class="fa-solid fa-trash-can"></i></span>
                            </li>
                        </ul>
                    </div>
                    <div class="text-body" style="height: 185px;">
                        <p class="card-text pt-3" style="mix-blend-mode: difference; color: #eeeeee; font-size: 20px;">${p.content}</p>
                    </div>
                    <div class="d-flex justify-content-between row">
                        <p class="card-text text-end mb-0" style="mix-blend-mode: exclusion; color: #ffffff">${p.author}</p>
                    </div>
                </div>

                <!-- 포스트잇 수정 -->
                <div class="modify register" id="modify${p.pno}">
                    <form method="post" enctype="multipart/form-data" onsubmit="postEdit(this)">
                        <div class="justify-content-center row">
                            <textarea name="content" cols="30" rows="5" class="form-control" style="resize: none;height: 104px;">${p.content}</textarea>
                            <input type="hidden" name="pno" id="${p.pno}" value="${p.pno}">
                            <input type="hidden" name="bno" id="${p.bno}" value="${p.bno}">
                            <div class="justify-content-between row">
                                <input type="color" class="col-2 form-control mt-1" name="bgColor" value="${p.bgColor}">
                                <input type="file" class="col-9 form-control mb-2 me-2 mt-2 uploadFiles" name="postFile" style="height: auto">
                            </div>
                            <div class="btn-group d-flex">
                                <button type="button" class="btn btn-main" id="reset${p.pno}"> 취소 </button>
                                <button type="submit" class="btn btn-main" id="modSubmit${p.pno}"> 수정 </button>
                            </div>
                        </div>
                    </form>
                </div>
                <!-- 포스트잇 수정 끝 -->
            </div>
        </div>
    </div>
</li>`;;
    console.log(postT)
    if(layoutNow === 'GRID'){
        $("#sortable").append(postT);
    }

}

function receiveEdit(newPost){
    let p = JSON.parse(newPost);
    alert(p.content+" 이 부분은 구현 예정입니다. 소켓연결은 되고 있습니다.");
}

function receiveRemove(pno){
    alert(pno+"번 포스트 삭제. 구현 예정. 소켓 연결 성공")
}

//창 키면 바로 연결
window.onload = function (){
    connect();


}
window.BeforeUnloadEvent = function (){
    disconnect();

}
