package com.esun.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Null
    private int user_id; // 使用者 ID

    @NotNull(message = "Phone number is required")
    private String phoneNumber; // 手機號碼 (INT 類型)

    @NotNull(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String password; // 加密後的密碼

    @NotNull(message = "User name is required")
    @Size(min = 3, max = 50, message = "User name must be between 3 and 50 characters")
    private String userName; // 使用者名稱

    @Email(message = "Email should be valid")
    private String email; // 使用者電子郵件

    private String coverImage; // 封面照片（可選）
    private String biography; // 自我介紹（可選）
}

