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
