package com.memomo.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Layout {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // @Id와 @OneToOne 은 분리하여 고유성 보장
    private Long lno;               // 레이아웃 고유번호
    private Long pno;               // 레이아웃의 포스트잇

    private Integer gno;            // 해당 포스트잇이 속한 그룹
    @JsonProperty("gPriority")
    private Integer gPriority;      // 그룹 내 우선순위
    private Integer x;
    private Integer y;
    private Integer z;
    @Column(columnDefinition = "BIGINT DEFAULT 0")
    private Long priority = 0L;
}