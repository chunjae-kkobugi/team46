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
    formData.append('color', form.bgColor.value);
    formData.append('content', form.content.value);
    formData.append('bno', form.bno.value);


    $.ajax({
        type : "POST",
        enctype : "multipart/form-data",
        url : "/post/add",
        cache : false,
        contentType : false,
        processData : false,
        data : formData,
        success : function(data){
            console.log(data);
        },
        error : function(){
            alert('에러');
        }
    });
}