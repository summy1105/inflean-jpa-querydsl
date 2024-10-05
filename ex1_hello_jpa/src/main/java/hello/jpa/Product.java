package hello.jpa;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Product {

    @Id @GeneratedValue
    private Long id;

//    @ManyToMany(mappedBy = "products")
//    private List<Member> members = new ArrayList<>();

    @OneToMany(mappedBy = "product")
    private List<MemberProduct> memberProducts = new ArrayList<>();
}
