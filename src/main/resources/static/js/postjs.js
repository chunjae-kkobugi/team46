/* 게시판 공통 함수 */

// 포스트 글쓰기 영역
$(".xBtn i").click(function(){
    $("#postRegister").addClass("register")
})
$("#submit").click(function(){
    $("#postRegister").addClass("register")
})



/* 포스트 공통 함수 */
// 포스트 추가
$(document).on('submit', '#postAddForm', function(e){
    let form = $(this)[0];

    e.preventDefault();
    var formData = new FormData();

    if (form.postFile.files[0] !== undefined) {
        formData.append('postFile', form.postFile.files[0]);
    }

    formData.append('bgColor', form.bgColor.value);
    formData.append('content', form.content.value);
    formData.append('bno', form.bno.value);

    $.ajax({
        type: "post",
        method: "post",
        enctype: "multipart/form-data",
        url: "/post/add",
        cache: false,
        contentType: false,
        processData: false,
        data: formData,
        success: function(data) {
            postAdd(data);
        },
        error: function(e) {
            alert("포스트잇 추가 에러");
            console.log("포스트잇 추가 에러");
            console.log(e);
        }
    });

    form.reset();
});

// 포스트 수정
$(document).on('submit', '#postEditForm', function(e){
    e.preventDefault();
    let form = $(this)[0];

    var formData = new FormData();

    if (form.postFile.files[0] !== undefined) {
        formData.append('postFile', form.postFile.files[0]);
    }

    formData.append('bgColor', form.bgColor.value);
    formData.append('content', form.content.value);
    formData.append('pno', form.pno.value);

    $.ajax({
        type: "post",
        method: "post",
        enctype: "multipart/form-data",
        url: "/post/edit",
        cache: false,
        contentType: false,
        processData: false,
        data: formData,
        success: function(data) {
            postEdit(data);
        },
        error: function(e) {
            alert("포스트잇 추가 에러");
            console.log("포스트잇 추가 에러");
            console.log(e);
        }
    });

    // 수정 후 모달 창 닫기
    $('#postEditModal .btn-close').click();
});

// 포스트 좋아요
$(document).on('click', '.myLike', function(){
    let pno = $(this)[0].getAttribute('data-pno');
    toggleLike(pno, sid);
    let myLike = $(`.myLike[data-pno=${pno}]`)
    if(myLike.hasClass('fa-regular')){
        myLike.removeClass('fa-regular').addClass('fa-solid');
    } else{
        myLike.addClass('fa-regular').removeClass('fa-solid');
    }
});

// 포스트 상세 보기
function getPostModal(pno){
    $.ajax({
        type: 'GET',
        url: /*realpath +  */'/post/getPost/' + pno,
        success: function(data) {
            // 상세보기
            let postHTML = `<p style="font-size: 13px;">${data.author}</p><p>${data.content}</p>`
            $('.getPost').html(postHTML);

            // 댓글 입력 pno
            // let cnoHTML = `<input type="hidden" name="pno" value="${data.pno}">`
            // $('.addComment').append(cnoHTML);
            $("#commentAddPno").val(data.pno);

            // 댓글 목록
            let commentHTML = ''; // 변수를 밖에서 초기화

            if (data.commentList.length == 0 ) {
                commentHTML += '<p class="text-center noComment">등록된 댓글이 없습니다</p>'
            } else {
                for (let i = 0; i < data.commentList.length; i++) {
                    commentHTML += '<p class="m-0" style="font-size: 13px;">' + data.commentList[i].author + '</p>';
                    commentHTML += '<p>' + data.commentList[i].content + '</p>';
                    commentHTML += '<hr>'
                }
            }

            $('.comment').html(commentHTML); // 반복문이 끝난 후 한 번에 HTML 추가
        },
        error: function(err) {
            console.log(err);
        }
    });
}

// 댓글 입력 처리
$(document).on('submit', '#commentAddForm', function(e){
    let form = $(this)[0];
    e.preventDefault(); // ajax 로 폼 보내는 경우 반드시 제출을 막아야 함.
    // 안 막으면 post 라 해도 get 으로 가게됨

    let pno = form.pno.value;
    let content = form.content.value;
    let params = {"pno" : pno, "content" : content};
    $.ajax({
        type:'POST',
        url: '/post/addComment',
        data: params,
        success:function(data) {
            let commentHTML = '';
            commentHTML += '<p class="m-0" style="font-size: 13px;">' + data.author + '</p>';
            commentHTML += '<p>' + data.content + '</p>';
            commentHTML += '<hr>'
            $('.comment').prepend(commentHTML);
            $('.noComment').remove();
            $('#commentContent').val('');
            commentCount(pno);
        },
        error: function(err){
            console.log(err);
        }
    })
});

// 포스트 수정 모달
function editPostModal(pno){
    $.ajax({
        type: 'GET',
        url: /*realpath +  */'/post/getPost/' + pno,
        success: function(data) {
            let form = $("#postEditForm")[0];
            form.pno.setAttribute('value', pno);
            $(form.content).text(data.content);
            form.bgColor.setAttribute('value', data.bgColor);

            $(`#postEditForm select option[value=${data.gno}]`).prop("selected", true);
        },
        error: function(err) {
            console.log(err);
        }
    });
}

// 그룹 추가
$(document).on('submit', '#groupRegisterForm', function(e){
    e.preventDefault();
    let form = $(this)[0];
    groupAdd(form);

    $('#groupRegisterModal .btn-close').click();
});

// 그룹 추가
$(document).on('submit', '#groupModifyForm', function(e){
    e.preventDefault();
    let form = $(this)[0];
    groupEdit(form);

    $('#groupModifyModal .btn-close').click();
});