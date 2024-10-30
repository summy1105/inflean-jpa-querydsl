package jpabook.springjpashop.domain;

import jakarta.persistence.Embeddable;
import lombok.Getter;

@Embeddable
@Getter
public class Address {
    private String city;
    private String street;
    private String zipcode;

    //jpa에서 사용하는 생성자로, public대신 protected
    protected Address() {
    }

    // 값타입은  immutable 하게 관리되어야 함, Setter대신 생성자를 사용해서 변경X
    public Address(String city, String street, String zipcode) {
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
    }
}
