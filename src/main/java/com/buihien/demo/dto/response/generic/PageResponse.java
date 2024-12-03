package com.buihien.demo.dto.response.generic;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;

@Getter
@Builder
public class PageResponse<T> implements Serializable {
    private int pageNo;
    private int pageSize;
    private int totalPage;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T items;
}