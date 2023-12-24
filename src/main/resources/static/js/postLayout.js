// 포스트잇 한 장에 대한 동적인 html 코드를 리턴
// 포스트잇 추가, 수정 등에서 일률적인 포스트잇 적용을 위해 따로 분리한 코드

function postLayout(p, sid){
    let post = `    
    <div class="m-2">
        <div class="card shadow-sm">
            <div class="timeline__content content card-body" id="post${p.pno}" style="${(p.bgImage==null)? 'background-color: '+p.bgColor : 'background: no-repeat center/cover url(/images/purin.webp)'}">
            <div class="bg" style="${(p.bgImage==null)? 'background-color: '+p.bgColor : 'background-repeat : repeat-y; background-position : center; background-image: url(/fileImages/' + p.file.savePath + '/' + p.file.saveName + ');'}"></div>    
            <div class="original content" id="original${p.pno}">
                    <!-- 포스트잇 메뉴 -->
                    <div id="postMenuList${p.pno}" class="pe-2 postMenu"
                         style="position: absolute; right: 0; height: auto; z-index: 300; ${(sid===p.author)? '':'display: none;'}">
                         <input type="hidden" class="postAuthor" value="${p.author}">
                        <ul class="p-0" style="list-style-type: none">
                            <!-- 수정버튼 -->
                            <li class="me-2" id="postModifyBtn${p.pno}" 
                            data-bs-toggle="modal" data-bs-target="#postEditModal" data-pno="${p.pno}" onclick="editPostModal(this.getAttribute('data-pno'))">
                                <span><i class="fa-pen-to-square fa-solid color"></i>
                            </li>

                            <!-- 삭제 버튼 -->
                            <li class="postRemoveBtn" data-pno="${p.pno}" onclick="postRemove(this.getAttribute('data-pno'))">
                                <span><i class="fa-solid fa-trash-can color"></i></span>
                            </li>
                        </ul>
                    </div>
                    <div class="text-body" style="height: 185px;" data-bs-toggle="modal" data-bs-target="#postGetModal" id="getPost${p.pno}" data-pno="${p.pno}" onclick="getPostModal(this.getAttribute('data-pno'))">
                        <p class="card-text pt-3 color post-text-style" style="font-size: 20px;">${p.content}</p>
                    </div>
                    <!-- 포스트 정보(작성자, 좋아요 수, 댓글 수 - 일단 뺌)-->
                    <div class="d-flex justify-content-between"> 
                        <p class="card-text text-end mb-0 color">${p.author}</p>
                        <p class="card-text text-end mb-0"><i class="myLike fa-regular fa-heart" data-pno="${p.pno}"></i> <span class="color">${p.likes===null?0:p.likes}</span></p>
                        <p class="card-text text-end mb-0 color"><i class="comments fa-regular fa-comment" data-pno="${p.pno}"></i> ${p.comments===null?0:p.comments}</p>
                    </div>
                </div>


            </div>
        </div>
    </div>
`;
    return post;
}

function gridLayout(p, sid){
    let newPost = `<li class="col-2 mb-3 mt-2 ui-sortable-handle ui-state-default post-style" data-pno="${p.pno}">`;
    newPost += postLayout(p, sid);
    newPost += `</li>`
    return newPost
}

function timelineLayout(p, sid){
    let newPost = `<div class="timeline__item ui-sortable-handle ui-state-default" data-pno="${p.pno}"><div class="post-style">`;
    newPost += postLayout(p, sid);
    newPost += `</div></div>`
    return newPost;
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
<div class="card mb-3">
<div class="bg" style="${(p.bgImage==null)? 'background-color: '+p.bgColor : 'background-repeat : repeat-y; background-position : center; background-image: url(/fileImages/' + p.file.savePath + '/' + p.file.saveName + ');'}"></div>
    <div class="card-body p-3 original content" id="original${p.pno}">
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
        <div style="height: 100%; cursor: pointer" data-bs-toggle="modal" data-bs-target="#postGetModal" id="getPost${p.pno}" data-pno="${p.pno}" onclick="getPostModal(this.getAttribute('data-pno'))">
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