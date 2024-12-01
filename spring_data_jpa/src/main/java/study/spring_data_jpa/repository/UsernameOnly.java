package study.spring_data_jpa.repository;

import org.springframework.beans.factory.annotation.Value;

public interface UsernameOnly {

    @Value("#{target.username + ' ' + target.age}") // open projection : 엔티티의 값을 전부 가져와서 value값을 처리
    String getUsername();
}
