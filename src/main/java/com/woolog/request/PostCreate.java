package com.woolog.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Setter
@Getter
public class PostCreate {

    @NotBlank(message = "타이틀을 입력해주세요.")
    private String title;
    @NotBlank(message = "컨텐츠을 입력해주세요.")
    private String content;

    /*빌더를 사용하면 장점이 많다.
    * - 가독성 상승(값 생성에 대한 유연함)
    * - 필요한 값만 받을 수 있다
    * - 객체의 불변성*/
    @Builder
    public PostCreate(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public PostCreate changeTitle(String title) {
        return PostCreate.builder()
                .title(title)
                .content(this.content)
                .build();
    }
}
