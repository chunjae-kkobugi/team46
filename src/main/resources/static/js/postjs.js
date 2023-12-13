$("#draggable").draggable({
    stop: function( event, ui ) {
        layoutDrag(ui.position.left, ui.position.top);
    }
});

$("#sortable").sortable({
    stop: function (event, ui){
        let changed = ui.item[0];
        let pno = changed.getAttribute("pno");
        let priority = $("#sortable > li").index(changed);
        layoutSort(pno, priority);
    }
});

$("#sortable").disableSelection();

function postAddBtn(form){
    var formData = new FormData();

    formData.append('postFile', form.postFile.files[0]);
    formData.append('bgColor', form.bgColor.value);
    formData.append('content', form.content.value);
    formData.append('bno', form.bno.value);

    $.ajax({
        // type : "post",
        method: "post",
        enctype : "multipart/form-data",
        url : "/post/addPro",
        cache : false,
        contentType : false,
        processData : false,
        data : formData,
        xhrFields: {
            withCredentials: true
        },
        success : function(data){
            alert("SUCCESS");
            console.log(data);
            confirm("DATA SUCCESS?");
            console.log(data);
            // let newPost = JSON.parse(data);
            // alert(data);
            // alert(data.pno);
            // postAdd(newPost.pno);
        },
        error : function(e){
            console.log("포스트잇 추가 에러");
            console.log(e);
        }
    });
}