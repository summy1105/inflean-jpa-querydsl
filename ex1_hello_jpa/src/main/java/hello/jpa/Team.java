package hello.jpa;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
//@ToString : stack over flow 발생
public class Team {

    @Id @GeneratedValue
    @Column(name = "TEAM_ID")
    private Long id;

    private String name;

    @OneToMany(mappedBy = "team") // mappedBy "team": Member의 team 필드이름 => member의 team_id fk 관리는 Member 엔티티에서 관리한다는 의미이다
//    @OneToMany
//    @JoinColumn(name = "TEAM_ID")
    private List<Member> members = new ArrayList<>(); // 초기화 해주는게 관례
}
