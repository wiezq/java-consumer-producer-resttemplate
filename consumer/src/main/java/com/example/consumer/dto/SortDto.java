package com.example.consumer.dto;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
class SortDto {
    private boolean empty;
    private boolean sorted;
    private boolean unsorted;
}