package hello.jpa.inheritance;

import jakarta.persistence.*;

@Entity
//@Inheritance(strategy = InheritanceType.SINGLE_TABLE) // @DiscriminatorColumn "DTYPE"이 자동
//@Inheritance(strategy = InheritanceType.JOINED)
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
//@DiscriminatorColumn(name = "DIS_TYPE")
public abstract class Item extends BaseEntity{

    @Id
    @GeneratedValue
    private Long id;

    private String name;
    private int price;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
