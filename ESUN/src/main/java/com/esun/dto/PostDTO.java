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
public class PostDTO {

    @NotNull(message = "Content cannot be null")  // 驗證 content 不可以為 null
    @Size(max = 50)
    @SafeHtml // 過濾 HTML 標籤以防範 XSS 攻擊
    private String content;

    private String image;  // 圖片的 URL，這裡不設驗證，因為可以為 null

    @Override
    public String toString() {
        return "PostDTO{" +
                "content='" + content + '\'' +
                ", image='" + image + '\'' +
                '}';
    }
}
