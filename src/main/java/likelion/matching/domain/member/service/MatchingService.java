package likelion.matching.domain.member.service;

import likelion.matching.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MatchingService {
    private final MemberRepository memberRepository;


}
