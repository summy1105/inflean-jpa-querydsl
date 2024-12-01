package study.spring_data_jpa.dto;

import lombok.Data;
import study.spring_data_jpa.entity.Member;

@Data
public class MemberDto {

    private Long id;
    private String username;
    private String teamName;

    public MemberDto(Long id, String username, String teamName) {
        this.id = id;
        this.username = username;
        this.teamName = teamName;
    }

    public MemberDto(Member member) {
        id = member.getId();
        username = member.getUsername();
        if (member.getTeam() != null) {
            teamName = member.getTeam().getName();
        }
    }
}
