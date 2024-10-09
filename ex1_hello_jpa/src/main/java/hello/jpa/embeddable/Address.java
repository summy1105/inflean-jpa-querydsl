package hello.jpa.embeddable;

import jakarta.persistence.Embeddable;
import lombok.With;

import java.util.Objects;

@Embeddable
@With
public class Address {
    private final String city;
    private final String street;
    private final String zipcode;

    public Address(String city, String street, String zipcode) {
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
    }

    public Address() {
        this.city = null;
        this.street = null;
        this.zipcode = null;
    }

    public String getCity() {
        return city;
    }

    private void setCity(String city) {
    }

    public String getStreet() {
        return street;
    }

    private void setStreet(String street) {
    }

    public String getZipcode() {
        return zipcode;
    }

    private void setZipcode(String zipcode) {
    }

    public Address copy() {
        return new Address(this.city, this.street, this.zipcode);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Address)) return false;
        Address address = (Address) o;
        return Objects.equals(getCity(), address.getCity()) && Objects.equals(getStreet(), address.getStreet()) && Objects.equals(getZipcode(), address.getZipcode());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCity(), getStreet(), getZipcode());
    }
}
