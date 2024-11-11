package jpabook.springjpashop.service;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UpdateItemDto {
    private Long id;

    private String name;
    private int price;
    private int stockQuantity;
}
