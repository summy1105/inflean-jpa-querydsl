package hello.jpa;

import hello.jpa.embeddable.Address;

import java.util.Objects;

public class JavaBasic {
    // (' D')
    public static void main(String[] args) {
        System.out.println(new Address("city", "street", "zipcode").hashCode());
        System.out.println(new Address("city", "street", "zipcode").hashCode());
        System.out.println(new BasicInner("city", "street", "zipcode").hashCode());

        System.out.println("test".hashCode());
        System.out.println("test".hashCode());
    }

    static class BasicInner {
        final String city;
        final String street;
        final String zipcode;

        public BasicInner(String city, String street, String zipcode) {
            this.city = city;
            this.street = street;
            this.zipcode = zipcode;
        }

        public String getCity() {
            return city;
        }

        public String getStreet() {
            return street;
        }

        public String getZipcode() {
            return zipcode;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof BasicInner)) return false;
            BasicInner that = (BasicInner) o;
            return Objects.equals(getCity(), that.getCity()) && Objects.equals(getStreet(), that.getStreet()) && Objects.equals(getZipcode(), that.getZipcode());
        }

        @Override
        public int hashCode() {
            return Objects.hash(getCity(), getStreet(), getZipcode());
        }
    }
}
