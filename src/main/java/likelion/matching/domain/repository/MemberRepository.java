package likelion.matching.domain.repository;

import likelion.matching.domain.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member,Long> {
    boolean existsByInstarId(String instarId);
    Optional<Member> findByInstarId(String instarId);
}