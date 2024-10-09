package hello.jpa;

import hello.jpa.embeddable.Address;
import jakarta.persistence.*;

@Entity
@Table(name = "MEMBER_ADDRESS_HISTORY")
public class MemberAddressHistory {

    @Id @GeneratedValue
    private Long id;

    @Embedded
    private Address address;

    public MemberAddressHistory() {
    }

    public MemberAddressHistory(Address address) {
        this.address = address.copy();
    }

    public MemberAddressHistory(String city, String street, String zipcode) {
        this.address = new Address(city, street, zipcode);
    }

    public String getCity() {
        return address.getCity();
    }

    public Long getId() {
        return id;
    }

    public Address getAddress() {
        return address;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
}
