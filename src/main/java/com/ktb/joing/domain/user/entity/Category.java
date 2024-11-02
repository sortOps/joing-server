package com.ktb.joing.domain.user.entity;

public enum Category {
    GAME("게임"),
    TECH("과학기술"),
    EDUCATION("교육"),
    KNOWHOW_STYLE("노하우/스타일"),
    NEWS_POLITICS("뉴스/정치"),
    SPORTS("스포츠"),
    NONPROFIT_SOCIAL("비영리/사회운동"),
    PETS_ANIMALS("애완동물/동물"),
    ENTERTAINMENT("엔터테인먼트"),
    TRAVEL_EVENTS("여행/이벤트"),
    MOVIE_ANIMATION("영화/애니메이션"),
    MUSIC("음악"),
    PEOPLE_BLOG("인물/블로그"),
    AUTO_TRANSPORT("자동차/교통"),
    COMEDY("코미디"),
    ETC("기타");

    private final String value;

    Category(String value) {
        this.value = value;
    }
}
