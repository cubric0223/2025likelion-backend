package likelion.matching.domain.entity;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "member")
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(unique = true)
    private String instarId;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Answer> answers = new ArrayList<>();

    @OneToOne
    @JoinColumn(name = "matched_member_id")
    @JsonIgnoreProperties({"answers", "matchedMember"})
    private Member matchedMember;

    @Enumerated(EnumType.STRING)
    private MemberStatus status = MemberStatus.READY;
}