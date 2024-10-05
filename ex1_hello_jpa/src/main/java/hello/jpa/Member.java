package hello.jpa;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Entity
//@Table(name = "MBR")
//@SequenceGenerator(name="MEMBER_SEQ_GENERATOR", sequenceName = "MEMBER_SEQ", initialValue = 1, allocationSize = 50)
//@TableGenerator(name = "MEMBER_SEQ_GENERATOR"
//        , table = "MY_SEQUENCES"
//        , pkColumnValue = "MEMBER_SEQ"
//        , allocationSize = 1
//)
@Getter
//@Setter
//@ToString
public class Member {

    @Id
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MEMBER_SEQ")
//    @GeneratedValue(strategy = GenerationType.TABLE, generator="MEMBER_SEQ_GENERATOR")
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @GeneratedValue
    @Column(name = "MEMBER_ID")
    private Long id;

//    @Column(name = "name") // hibernate.hbm2ddl.auto -> update시에는 변경안됨
    @Column(name = "USERNAME")
    private String username;

//    @Column(name = "TEAM_ID")
//    private Long teamId;

    @ManyToOne(fetch = FetchType.LAZY) // Many : member , One : team
    @JoinColumn(name = "TEAM_ID")
    private Team team;

    @OneToOne
    @JoinColumn(name = "LOCKER_ID")
    private Locker locker;

//    @ManyToMany
//    @JoinTable(name = "MEMBER_PRODUCT")
//    private List<Product> products = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<MemberProduct> memberProducts = new ArrayList<>();

//    @Column(columnDefinition = "INTEGER default -1")
//    private Integer age;
//
//    @Enumerated(EnumType.STRING)
//    private RoleType roleType;
//
//    @Temporal(TemporalType.TIMESTAMP)
//    private Date createdDate;
//
//    @Temporal(TemporalType.TIMESTAMP)
//    private Date lastModifiedDate;
//
//    @Lob
//    private String description;
//
//    @Transient // 디비랑 연관안하는 필드값(컬럼 생성X)
//    private String temp;

    public Member() {
    }

    public Member(Long id, String username) {
        this.id = id;
        this.username = username;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void changeTeam(Team team) {
        if (this.team != null) {
            this.team.getMembers().remove(this);
        }
        this.team = team;
        this.team.getMembers().add(this);
    }
}
