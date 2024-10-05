package jpabook.jpashop.domain;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Item {

    @Id @GeneratedValue
    @Column(name="ITEM_ID")
    private Long id;

    private String name;
    private int price;
    private int stockQuantity;

    @ManyToMany(mappedBy = "items")
//    @JoinTable(name = "CATEGORY_ITEM"
//            , joinColumns = @JoinColumn(name = "ITEM_ID")
//            , inverseJoinColumns = @JoinColumn(name = "CATEGORY_ID")
//    )
    private List<Category> categories = new ArrayList<>();
}
