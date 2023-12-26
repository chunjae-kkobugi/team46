function connect() {
    // 운영 서버로 돌릴 떄는 '/team46/stomp'로 바꾸기
    var socket = new SockJS('/team46/stomp');
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
        receiveCommentCount();
        receiveLayout();
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

function receiveAdd(){
    stompClient.subscribe(
        '/stomp-receive/add/'+bno, // destination (String 필수)
        function (message) { // 콜백, 서버에서 받은 메시지 처리 function (message)
            let newPost = JSON.parse(message.body);
            if(layoutNow === 'GRID'){
                let postT = gridLayout(newPost, sid);
                $("#sortable").append(postT);
            } else if(layoutNow==='TIMELINE'){
                let postT = timelineLayout(newPost, sid);
                console.log(postT);
                $(".timeline__items").append(postT);
                let timeLen = $(".timeline__item").length
                if((timeLen-1)%2==0){
                    $(`.timeline__item[data-pno=${newPost.pno}]`).addClass("timeline__item--left d-flex justify-content-end");
                } else {
                    $(`.timeline__item[data-pno=${newPost.pno}]`).addClass("timeline__item--right");
                }
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
            let myLike = $(`.myLike[data-pno=${newPost.pno}]`);

            if(layoutNow==='GRID'){
                let postT = gridLayout(newPost, sid);
                $(`li[data-pno=${newPost.pno}]`).replaceWith(postT);
            } else if(layoutNow==='TIMELINE'){
                let postT = timelineLayout(newPost, sid);
                var selectedClasses = $(`.timeline__item[data-pno=${newPost.pno}]`).attr('class');
                $(`.timeline__item[data-pno=${newPost.pno}]`).replaceWith(postT).addClass(selectedClasses);
            }

            $(`.myLike[data-pno=${newPost.pno}]`).replaceWith(myLike);
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
                // 삭제된 요소 이후부터 왼쪽, 오른쪽 바꾸기
                let changedIdx = $(`.timeline__item[data-pno=${pno}]`).index();
                $(`.timeline__item[data-pno=${pno}]`).remove();
                let itemsLen = $(`.timeline__item`).length;
                var elements= $(".timeline__item");
                for(var i=changedIdx; i<itemsLen; i++){
                    elements[i].classList.remove('timeline__item--left', 'd-flex', 'justify-content-end', 'timeline__item--right');
                    if(i%2==0){
                        elements[i].classList.add('timeline__item--left', 'd-flex', 'justify-content-end');
                    } else {
                        elements[i].classList.add('timeline__item--right');
                    }
                }
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
                let fragment = $(document.createDocumentFragment());
                newOrder.forEach(function(order){
                    let pnoLi = $(`li.ui-sortable-handle[data-pno=${order}]`);
                    fragment.append(pnoLi);
                });
                $('#sortable').append(fragment);
            }

            else if(layoutNow === 'TIMELINE'){
                let originalTimelineItems = document.querySelector('.timeline__items');
                let cloned = originalTimelineItems.cloneNode(true);
                var i = 0;
                $(".timeline__items").html("");

                newOrder.forEach(function(order){
                    let pnoLi = $(cloned).find(`.timeline__item[data-pno=${order}]`)[0];
                    $(pnoLi).removeClass('timeline__item--left d-flex justify-content-end timeline__item--right');
                    if(i%2==0){
                        $(pnoLi).addClass('timeline__item--left d-flex justify-content-end');
                    } else {
                        $(pnoLi).addClass('timeline__item--right');
                    }

                    $(".timeline__items").append(pnoLi);
                    $(`.timeline__item[data-pno=${order}]`).removeAttr("style");
                    i++;
                });
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
            // console.log("RECEIVE LIKE");
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
    let sendUrl = "/stomp-send/comments/" + bno;
    stompClient.send(
        sendUrl,
        {},
        JSON.stringify({
            'pno' : pno,
        })
    );
}

function receiveCommentCount(){
    stompClient.subscribe(
        '/stomp-receive/comments/' + bno,
        function (message){
            let c = JSON.parse(message.body);
            let comments = $(`.comments[data-pno=${c.pno}]`).next().text(c.cno);
        },
    {}
    )
}

function layoutChange(){
    let sendUrl = "/stomp-send/layout/" + bno;
    stompClient.send(
        sendUrl,
        {},
        {}
    );
}

function receiveLayout(){
    stompClient.subscribe(
        '/stomp-receive/layout/' + bno,
        function (message){
            location.reload();
        },
        {}
    )
}