// 포스트잇 한 장에 대한 동적인 html 코드를 리턴
// 포스트잇 추가, 수정 등에서 일률적인 포스트잇 적용을 위해 따로 분리한 코드
function postLayout(p){
    let post = `
<li class="col-2 mb-3 mt-2 ui-sortable-handle ui-state-default" data-pno="${p.pno}" style="width: 275px;">    
    <div class="m-2">
        <div class="card shadow-sm">
            <!-- 포스트잇 내용-->
            <div class="card-body" id="post${p.pno}" style="${(p.bgImage==null)? 'background-color: '+p.bgColor : null}">
                <div class="original" id="original${p.pno}">
                    <div id="postMenuList${p.pno}" class="pe-2"
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
                            <li class="postRemoveBtn" data-pno="${p.pno}" onclick="postRemove(this.getAttribute('data-pno'))"
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
                    <form class="postEditForm" method="post" enctype="multipart/form-data">
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
</li>`;

    return post;
}