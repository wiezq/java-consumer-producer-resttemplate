package com.example.consumer.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
class PageableDto {
    private int pageNumber;
    private int pageSize;
    private boolean paged;
    private boolean unpaged;

}