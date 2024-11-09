package com.esun.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Post {

    private int postId;            // 發文 ID (Primary Key)
    private int userId;            // 使用者 ID (Foreign Key to User)
    private String content;        // 發文內容
    private String image;          // 圖片 URL (非必要欄位)
    private Timestamp createdAt;   // 發布時間
}
