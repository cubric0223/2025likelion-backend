package likelion.matching.domain.service;

import likelion.matching.domain.entity.Answer;
import likelion.matching.domain.entity.Choice;
import likelion.matching.domain.entity.Member;
import likelion.matching.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MatchingService {
    private final MemberRepository memberRepository;

    @Transactional
    public void runMatching(){
        //전체 멤버 조회
        List<Member> members = memberRepository.findAll();

        List<Member> unmatched = new ArrayList<>();
        for (Member member : members) {
            if(member.getMatchedMember() == null){
                unmatched.add(member);
            }
        }

        for (int i = 0; i < unmatched.size(); i++) {
            Member m1 = unmatched.get(i);
            if(m1.getMatchedMember() != null){
                continue;
            }
            for (int j = i+1; j< unmatched.size();j++){
                Member m2 = unmatched.get(j);
                if(m2.getMatchedMember() != null){
                    continue;
                }

                //실제로 매칭 시키기
                if(isMatch(m1,m2,5)){
                    m1.setMatchedMember(m2);
                    m2.setMatchedMember(m1);
                    break;
                }

            }
        }


        unmatched = new ArrayList<>();
        for (Member member : members) {
            if(member.getMatchedMember() == null){
                unmatched.add(member);
            }
        }

        for (int i = 0; i < unmatched.size(); i++) {
            Member m1 = unmatched.get(i);
            if(m1.getMatchedMember() != null){
                continue;
            }
            for (int j = i+1; j< unmatched.size();j++){
                Member m2 = unmatched.get(j);
                if(m2.getMatchedMember() != null){
                    continue;
                }

                //실제로 매칭 시키기
                if(isMatch(m1,m2,4)){
                    m1.setMatchedMember(m2);
                    m2.setMatchedMember(m1);
                    break;
                }

            }
        }

        unmatched = new ArrayList<>();
        for (Member member : members) {
            if(member.getMatchedMember() == null){
                unmatched.add(member);
            }
        }

        for (int i = 0; i < unmatched.size(); i++) {
            Member m1 = unmatched.get(i);
            if(m1.getMatchedMember() != null){
                continue;
            }
            for (int j = i+1; j< unmatched.size();j++){
                Member m2 = unmatched.get(j);
                if(m2.getMatchedMember() != null){
                    continue;
                }

                //실제로 매칭 시키기
                if(isMatch(m1,m2,3)){
                    m1.setMatchedMember(m2);
                    m2.setMatchedMember(m1);
                    break;
                }

            }
        }
    }

    //매칭조건 확인 n은 매칭 숫자
    private boolean isMatch(Member m1 , Member m2,int n){
        if(m1.getGender() == m2.getGender()){
            return false;
        }
        Map<Integer, Choice> answerMap = new HashMap<>();



        for (Answer m1Answer : m1.getAnswers()) {
            answerMap.put(m1Answer.getQuestionNumber(),m1Answer.getChoice());
        }
        int count = 0;

        for (Answer m2Answer : m2.getAnswers()) {
            Choice m1Choice = answerMap.get(m2Answer.getQuestionNumber());

            if(m1Choice!= null&&m1Choice == m2Answer.getChoice()){
                count++;
            }
        }

        return count >=n;

    }
}