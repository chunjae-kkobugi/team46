package com.memomo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageDTO<E, T> {
    private static final ModelMapper modelMapper = new ModelMapper();

    private int pageScreen = 5; // 한 화면 당 페이지 개수
    private int pageStart; // 현재 화면의 시작 페이지 번호
    private int pageLast; // 현재 화면의 마지막 페이지 번호
    private int pageNow = 1; // 현재 화면의 현재 페이지 번호
    private int pageTotal; // 전체 페이지 개수

    private int postScreen = 9; // 한 화면(페이지) 당 게시글 개수
    private Long postTotal; // 전체 게시글 개수 (Long 타입 반환)

    private String type; // 검색 종류(복수 선택 시 반점(,)으로 구분
    private String keyword; // 검색어

    private String teacher = "";
    private String status = "";

    private List<T> listDTO; // 페이지 결과

    // 검색 종류가 여러 개일 경우, 반점(,)으로 구분
    public String[] getTypes(){
        // 검색 설정하지 않으면 null 반환
        if(type==null || type.isEmpty()){
            return null;
        }
        String[] types = type.split(",");

        // 공백 있을 것을 대비해 공백 제거
        for(int i=0; i<types.length; i++){
            types[i] = types[i].trim();
        }

        return types;
    }

    public Pageable getPageable(){
        return PageRequest.of(this.pageNow-1, this.postScreen);
    }

    public void build(Page<E> result){
        this.postTotal = result.getTotalElements(); // 이 함수가 long 반환
        this.pageTotal = result.getTotalPages();

        if(this.pageNow<=1){
            // 현재 페이지가 0 또는 1인 경우 시작 페이지를 1로 제한 (DivideByZeroException 방지)
            this.pageStart = 1;
        } else {
            this.pageStart = ((this.pageNow - 1) / this.pageScreen) * this.pageScreen + 1;
        }

        // ex. 시작페이지가 1이면, 마지막페이지는 5
        this.pageLast = this.pageStart + this.pageScreen - 1;
        if(this.pageLast > this.pageTotal) {
            // ex. 만약 전체 페이지가 32고, 시작 페이지가 31이라면, 마지막 페이지는 35가 아니라 32가 되어야 한다.
            this.pageLast = this.pageTotal;
        }
    }

    public void entity2dto(Page<E> result, Class<T> dto){
        // 기존 엔티티 페이징 결과를 DTO 타입으로 변환
        this.listDTO = result.getContent().stream().map(entity -> modelMapper.map(entity, dto)).collect(Collectors.toList());
    }
}
