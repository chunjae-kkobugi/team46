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

/*
$.ajax(
    {
        type: "POST",
        url: "/post/hello",
        success: function(data){
            console.log(data);
            alert("SUCCESS")
        }
    }
)
*/

function postAddBtn(form) {
    var formData = new FormData();

    if (form.postFile.files[0] !== undefined) {
        formData.append('postFile', form.postFile.files[0]);

        // AJAX 요청을 Promise로 감싸서 처리합니다.
        return new Promise(function(resolve, reject) {
            $.ajax({
                type: "post",
                method: "post",
                enctype: "multipart/form-data",
                url: "/post/addPro",
                cache: false,
                contentType: false,
                processData: false,
                data: formData,
                success: function(data) {
                    console.log(data);
                    alert("SUCCESS");
                    confirm("DATA SUCCESS?");
                    console.log(data);
                    resolve(data); // 성공 시 Promise를 이용해 데이터 반환
                },
                error: function(e) {
                    alert("포스트잇 추가 에러");
                    console.log("포스트잇 추가 에러");
                    console.log(e);
                    reject(e); // 에러 발생 시 Promise를 이용해 에러 반환
                }
            });
        });
    } else {
        // 파일이 없는 경우 에러를 반환하는 Promise를 reject합니다.
        return Promise.reject("No file selected");
    }
}
