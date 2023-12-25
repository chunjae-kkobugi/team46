// 포스트잇 한 장에 대한 동적인 html 코드를 리턴
// 포스트잇 추가, 수정 등에서 일률적인 포스트잇 적용을 위해 따로 분리한 코드

function postLayout(p, sid){
    let post = `    
    <div class="m-2">
        <div class="card shadow-sm post-style">
            <div class="timeline__content content card-body" id="post${p.pno}">
            <div class="bg" style="${(p.bgImage==null)? 'background-color: '+p.bgColor : 'background-repeat : repeat-y; background-position : center; background-image: url(\'/fileImages/' + p.file.savePath + '/' + p.file.saveName + '\');'}"></div>    
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
                    
                    <!-- 포스트잇 내용 -->
                    <div class="text-body" style="height: 210px;" data-bs-toggle="modal" data-bs-target="#postGetModal" id="getPost${p.pno}" data-pno="${p.pno}" onclick="getPostModal(this.getAttribute('data-pno'))">
                        <p class="card-text pt-3 color post-text-style" style="font-size: 20px;">${p.content}</p>
                    </div>
                    <!-- 포스트 정보(작성자, 좋아요 수, 댓글 수)-->
                    <div class="d-flex justify-content-between"> 
                        <p class="card-text text-end mb-0 color">${p.author}</p>
                        <p class="card-text text-end mb-0"><i class="myLike fa-regular fa-heart" data-pno="${p.pno}"></i> <span class="color">${p.likes===null?0:p.likes}</span></p>
                        <p class="card-text text-end mb-0 color"><i class="comments fa-regular fa-comment" data-pno="${p.pno}"></i> <span>${p.comments===null?0:p.comments}</span></p>
                    </div>
                </div>


            </div>
        </div>
    </div>
`;
    return post;
}

function gridLayout(p, sid){
    let newPost = `<li class="col-2 mb-3 mt-2 ui-sortable-handle ui-state-defaul" data-pno="${p.pno}">`;
    newPost += postLayout(p, sid);
    newPost += `</li>`
    return newPost
}

function timelineLayout(p, sid){
    let newPost = `<div class="timeline__item ui-sortable-handle ui-state-default" data-pno="${p.pno}">`;
    newPost += postLayout(p, sid);
    newPost += `</div>`
    return newPost;
}

function groupLayout(g) {
    let group = `
<div class="groupLayout" data-gno="${g.gno}">
    <div class="card card-border-primary">
        <div class="card-header">
            <div class="card-actions float-end" id="groupMenuBtn${g.gno}" data-gno="${g.gno}" style="cursor: pointer;">
                <i class="fa-solid fa-ellipsis"></i>
                <div class="dropdown-menu dropdown-menu-right" id="groupMenuList${g.gno}" style="display: none;">
                    <button class="dropdown-item" data-bs-toggle="modal" data-bs-target="#postRegisterModal">포스트잇 추가</button>
                    <button class="dropdown-item" data-bs-toggle="modal" data-bs-target="#groupModifyModal" data-gno="${g.gno}" data-title="${g.title}" data-gColor="${g.gColor}" onclick="groupModifyModalSet(this.getAttribute('data-gno'), this.getAttribute('data-title'), this.getAttribute('g.gColor'))">그룹 수정</button>
                    <a class="dropdown-item" href="javascript:groupRemove(${g.gno})">그룹 삭제</a>
                </div>
            </div>
            <h5 class="card-title mt-2">${g.title}</h5>
        </div>
        <div class="card-body p-1 groupPostList" data-gno="${g.gno}">
        </div>
    </div>
</div>
    `;

    return group;
}

function groupPost(p, sid) {
    let groupP = `<div data-pno="${p.pno}">`;
    groupP+=postLayout(p, sid);
    groupP += `</div>`;
    return groupP;
}