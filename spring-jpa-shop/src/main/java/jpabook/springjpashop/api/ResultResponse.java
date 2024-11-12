package jpabook.springjpashop.api;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
class ResultResponse<T> {
    private long count;
    private T data;
}