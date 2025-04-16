package likelion.matching.domain.member.entity;

//이게 밸런스게임
public enum Choice {
    FIRST("첫번째"),SECOND("두번째");

    private final String description;
    Choice(String description){
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}