package com.esun.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.SafeHtml;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentDTO {

    @NotNull(message = "postId cannot be null")  // 驗證 content 不可以為 null
    private int postId;

    @NotNull(message = "Content cannot be null")  // 驗證 content 不可以為 null
    @Size(max = 250)
    @SafeHtml // 過濾 HTML 標籤以防範 XSS 攻擊
    private String content;

    @Override
    public String toString() {
        return "CommentDTO{" +
                "content='" + content + '\'' +
                ", postId='" + postId + '\'' +
                '}';
    }
}
