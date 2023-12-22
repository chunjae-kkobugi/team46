// 포스트잇 한 장에 대한 동적인 html 코드를 리턴
// 포스트잇 추가, 수정 등에서 일률적인 포스트잇 적용을 위해 따로 분리한 코드

function postLayout(p){
    let post = `
<li class="col-2 mb-3 mt-2 ui-sortable-handle ui-state-default" data-pno="${p.pno}" style="width 275px;">    
    <div class="m-2">
        <div class="card shadow-sm">
            <!-- 포스트잇 내용-->
            <div class="card-body" id="post${p.pno}" style="${(p.bgImage==null)? 'background-color: '+p.bgColor : 'background: no-repeat center/cover url(/images/purin.webp)'}">
                <div class="original" id="original${p.pno}">
                    <div id="postMenuList${p.pno}" class="pe-2 postMenu"
                         style="position: absolute; right: 0; height: auto; z-index: 10; ">
                         <input type="hidden" class="postAuthor" value="${p.author}">
                        <ul class="p-0" style="list-style-type: none">
                            <!-- 수정버튼 -->
                            <li class="me-2" id="postModifyBtn${p.pno}"
                                style="${(p.bgImage==null)? 'cursor: pointer; background-color : ' + p.bgColor : '#ffffff'}">
                                <span style="${(p.bgColor=='#ffffff' || p.bgColor == null) ? 'color : #333333' : 'mix-blend-mode: difference; color : #ffffff'}">
                                    <i class="fa-pen-to-square fa-solid"></i>
                                </span>
                            </li>

                            <!-- 삭제 버튼 -->
                            <li class="postRemoveBtn" data-pno="${p.pno}" onclick="postRemove(this.getAttribute('data-pno'))"
                                style="${ p.bgImage==null ? 'cursor: pointer; background-color: ' + p.bgColor : '#ffffff' }">
                                <span style="${p.bgColor=='#ffffff' || p.bgColor==null ? 'color : #333333' : 'mix-blend-mode: difference; color : #ffffff'}">
                                    <i class="fa-solid fa-trash-can"></i></span>
                            </li>
                        </ul>
                    </div>
                    <div class="text-body" style="height: 185px;" data-bs-toggle="modal" data-bs-target="#postGetModal" id="getPost${p.pno}">
                        <p class="card-text pt-3 color" style="font-size: 20px;">${p.content}</p>
                    </div>
                    <div class="d-flex justify-content-between">
                        <p class="card-text text-end mb-0 color">${p.author}</p>
                        <p class="card-text text-end mb-0"><i class="myLike fa-regular fa-heart" data-pno="${p.pno}"></i> <span class="color">${p.likes===null?0:p.likes}</span></p>
                        <p class="card-text text-end mb-0 color"><i class="comments fa-regular fa-comment" data-pno="${p.pno}"></i> ${p.comments===null?0:p.comments}</p>
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
                                <input type="file" class="col-8 form-control mb-2 me-2 mt-2 uploadFiles" name="postFile" style="height: auto">
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

function timelineLayout(p){
    let timelinePost = `
    <div class="timeline__item ui-sortable-handle ui-state-default" data-pno="${p.pno}">
        <div class="timeline__content"
             style="${ p.bgImage == null ? 'cursor: pointer; background-color : ' + p.bgColor : 'background-color : #ffffff' } ">
            <div class="original" id="original${p.pno}">
                <div id="postMenuList{p.pno}" class="pe-3"
                     style="position: absolute; right: 0; height: auto; z-index: 10; top: 10px;">
                     <input type="hidden" class="postAuthor" value="${p.author}">
                    <ul class="p-0" style="list-style-type: none">
                        <!-- 수정버튼 -->
                        <li class="me-2" id="postModifyBtn${p.pno}"
                            style="${ p.bgImage == null ? 'cursor: pointer; background-color : ' + p.bgColor : 'cursor: pointer; background-color : #ffffff' }">
                            <span style="${p.bgColor == '#ffffff' || p.bgColor == null ? 'color : #333333' : 'mix-blend-mode: difference; color : #ffffff'}">
                                <i class="fa-pen-to-square fa-solid"></i>
                            </span>
                        </li>
                        <!-- 삭제 버튼 -->
                        <li class="postRemoveBtn" data-pno="${p.pno}" onclick="postRemove(this.getAttribute('data-pno'))"
                            style="${ p.bgImage == null ? 'cursor: pointer; background-color : ' + p.bgColor : 'cursor: pointer; background-color : #ffffff' }">
                            <span style="${p.bgColor == '#ffffff' || p.bgColor == null ? 'color : #333333' : 'mix-blend-mode: difference; color : #ffffff'}">
                                <i class="fa-solid fa-trash-can"></i>
                            </span>
                        </li>
                    </ul>
                </div>
                <!-- 내용 -->
                <div style="height: 100%; cursor: pointer" data-bs-toggle="modal" data-bs-target="#postGetModal" id="getPost${p.pno}">
                     <p class="pe-3" style="${p.bgColor == '#ffffff' || p.bgColor == null ? 'font-size : 20px; color : #333333' : 'font-size : 20px; mix-blend-mode: difference; color : #ffffff'}">${p.content}</p>
                     <p class="text-end m-0" style="${p.bgColor == '#ffffff' || p.bgColor == null ? 'color : #333333' : 'mix-blend-mode: difference; color : #ffffff'}">${p.author}</p>
                </div>
            </div>
            <!-- 수정 창 -->
            <div class="modify register" id="modify${p.pno}">
                <form class="postEditForm" method="post" enctype="multipart/form-data">
                    <div class="justify-content-center row">
                        <textarea name="content" cols="30" rows="5" class="form-control" maxlength="900" style="resize: none;height: 80px;">${p.content}</textarea>
                        <input type="hidden" name="pno" id="${p.pno}" value="${p.pno}">
                        <input type="hidden" name="bno" id="${p.bno}" value="${p.bno}">
                        <div class="justify-content-between row">
                            <input type="color" class="col-2 form-control mt-1" name="bgColor" id="bgColor" value="${p.bgColor}">
                            <input type="file" class="col form-control mb-2 me-2 mt-2 uploadFiles" id="postFile" name="postFile" style="height: auto">
                        </div>
                        <div class="btn-group d-flex">
                            <button type="button" class="btn btn-main" id="reset${p.pno}"> 취소 </button>
                            <button type="submit" class="btn btn-main" id="modSubmit${p.pno}"> 수정 </button>
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </div>`;

    return timelinePost;
}

function groupLayout(g) {
    let group = `
<div class="col-md-2 groupLayout" data-gno="${g.gno}">
    <div class="card card-border-primary">
        <div class="card-header" style="'background-color : '+${g.gColor}">
            <div class="card-actions float-end" id="groupMenuBtn${g.gno}" data-gno="${g.gno}" style="cursor: pointer;">
                <i class="fa-solid fa-ellipsis color"></i>
                <div class="dropdown-menu dropdown-menu-right" id="groupMenuList${g.gno}" style="display: none;">
                    <button class="dropdown-item" data-bs-toggle="modal" data-bs-target="#postRegisterModal">포스트잇 추가</button>
                    <button class="dropdown-item" data-bs-toggle="modal" data-bs-target="#groupModifyModal" data-gno="${g.gno}" data-title="${g.title}" data-gColor="${g.gColor}" onclick="groupModifyModalSet(this.getAttribute('data-gno'), this.getAttribute('data-title'), this.getAttribute('g.gColor'))">그룹 수정</button>
                    <a class="dropdown-item" href="javascript:groupRemove(${g.gno})">그룹 삭제</a>
                </div>
            </div>
            <h5 class="card-title mt-2 color">${g.title}</h5>
        </div>
        <div class="card-body p-3 groupPostList" data-gno="${g.gno}">
        </div>
    </div>
</div>
    `;

    return group;
}

function groupPost(p) {
    let post = `
<!-- 그룹 내 포스트잇 시작 -->
<div class="card mb-3" style="${p.bgImage == null ? 'height : 220px; background-color : ' + p.bgColor : 'height : 220px;'}">
    <div class="card-body p-3 original" id="original${p.pno}">
        <!-- 포스트잇 메뉴 시작 -->
        <div id="postMenuList${p.pno}" class="pe-3" style="position: absolute; right: 0; height: auto; top: 10px; z-index: 10">
        <input type="hidden" class="postAuthor${p.pno}" value="${p.author}">
            <ul class="p-0" style="list-style-type: none">
                <!-- 수정버튼 -->
                <li class="me-2" id="postModifyBtn${p.pno}"
                    style="${ p.bgImage == null ? 'cursor: pointer; background-color : ' + p.bgColor : 'cursor: pointer; background-color : #ffffff' }">
                        <span style="${p.bgColor == '#ffffff' || p.bgColor == null ? 'color : #333333' : 'mix-blend-mode: difference; color : #ffffff'}">
                            <i class="fa-pen-to-square fa-solid"></i>
                        </span>
                </li>
                <!-- 삭제 버튼 -->
                <li id="postRemoveBtn${p.pno}"
                style="${ p.bgImage == null ? 'cursor: pointer; background-color : ' + p.bgColor : 'cursor: pointer; background-color : #ffffff' }">
                        <span style="${p.bgColor == '#ffffff' || p.bgColor == null ? 'color : #333333' : 'mix-blend-mode: difference; color : #ffffff'}">
                            <i class="fa-solid fa-trash-can"></i>
                        </span>
                </li>
            </ul>
        </div>
        <!-- 포스트잇 메뉴 끝 -->
        <div style="height: 100%; cursor: pointer" data-bs-toggle="modal" data-bs-target="#postGetModal" id="getPost${p.pno}">
            <p class="color pt-3" style="height: 70%">${p.content}</p>
            <p class="text-end mb-0 color">${p.author}</p>
        </div>
       
    </div>
    <!-- 포스트잇 수정 -->
    <div class="card-body p-3 modify register" id="modify${p.pno}">
        <form class="postEditForm" method="post" enctype="multipart/form-data">
            <div class="justify-content-center row">
                <textarea name="content" cols="30" rows="5" class="form-control" maxlength="900" style="resize: none;height: 80px;">${p.content}</textarea>
                <input type="hidden" name="pno" value="${p.pno}">
                <input type="hidden" name="bno" value="${p.bno}">
                <div class="justify-content-between row">
                    <input type="color" class="col-2 form-control mt-1" name="bgColor" id="bgColor" value="${p.bgColor}">
                    <input type="file" class="col form-control mb-2 me-2 mt-2 uploadFiles" id="postFile" name="postFile" style="height: auto">
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
<!-- 그룹 내 포스트잇 끝 -->
    `;

    return post;
}