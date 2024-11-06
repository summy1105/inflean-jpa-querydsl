package jpabook.springjpashop.service;

import jpabook.springjpashop.domain.Member;
import jpabook.springjpashop.repository.MemberRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true) //읽기에는 추가하는것이 성능상에 좋다
//@AllArgsConstructor
@RequiredArgsConstructor
public class MemberService {

//    @Autowired // not recommended
    private final MemberRepository memberRepository;

//    @Autowired // mock repository를 주입할 수 있음, but 빈이 다 생성한 후 변경되는 위험성이 있음
//    public void setMemberRepository(MemberRepository memberRepository) {
//        this.memberRepository = memberRepository;
//    }

//    @Autowired // annotation 없어도 동작함
//    public MemberService(MemberRepository memberRepository) {
//        this.memberRepository = memberRepository;
//    }


    /**
     * 회원가입
     */
    @Transactional // 클래스 레벨로 readOnly를 추가해서, write하는 메소드에 추가로 붙여줌
    public Long join(Member member) {
        validateDuplicateMember(member); // 중복 회원 검증
        memberRepository.save(member);
        return member.getId();
    }

    private void validateDuplicateMember(Member member) {
        // 중복이면 Exception
        List<Member> findMembers = memberRepository.findByName(member.getName());
        if (!findMembers.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    //회원 전체 조회
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    public Member findOne(Long memberId) {
        return memberRepository.findOne(memberId);
    }
}
