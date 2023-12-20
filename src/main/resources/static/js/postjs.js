$("#draggable").draggable({
    stop: function( event, ui ) {
        layoutDrag(ui.position.left, ui.position.top);
    }
});

$("#sortable").sortable({
    stop: function (event, ui){
        let changed = ui.item[0];
        let pno = changed.getAttribute("data-pno");
        let priority = $("#sortable > li").index(changed);
        layoutSort(pno, priority);
    }
});

$("#sortable").disableSelection();

$(".timeline__items").sortable({
    stop: function (event, ui){
        let changed = ui.item[0];
        let pno = changed.getAttribute("data-pno");
        let priority = $(".timeline__items > .timeline__item").index(changed);
        console.log(priority);
        layoutSort(pno, priority);
    }
});

$(".timeline__items").disableSelection();


$(document).on('submit', '.postEditForm', function(e){
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

    form.reset();
});

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

    // AJAX 요청을 Promise로 감싸서 처리합니다.
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

$(document).on('click', '[id^=postModifyBtn]', function(){
    var index = this.id.replace('postModifyBtn','');
    var modify = $("#modify" + index);
    var original = $("#original" + index);

    $(original).addClass('register');
    $(modify).removeClass('register');
});