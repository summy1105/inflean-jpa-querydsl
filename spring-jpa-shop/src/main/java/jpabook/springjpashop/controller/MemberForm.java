package jpabook.springjpashop.controller;

import jakarta.annotation.Nonnull;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberForm {

    // @NotEmpty는 빈 문자열("")은 비허용 but 공백(" ")은 허용, @NotBlank는 공백도 비허용
    @NotBlank(message = "회원 이름은 필수 입니다.")
    private String name;

    private String city;
    private String street;
    private String zipcode;
}
