package likelion.matching.domain.member.entity;

public enum MemberStatus {
    READY,      // 회원가입 완료, 답변 미제출
    WAITING,    // 답변 제출 완료, 매칭 대기중
    MATCHED     // 매칭 완료 (추후 사용)
}
