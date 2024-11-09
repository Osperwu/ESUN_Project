package com.esun.vo;

import com.esun.dto.CommentDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

import static com.esun.util.tool.getNow;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Comment {
    private int commentId;         // 留言 ID (Primary Key)
    private int userId;            // 使用者 ID (Foreign Key)
    private int postId;            // 發文 ID (Foreign Key)
    private String content;        // 留言內容
    private Timestamp createdAt;   // 留言時間

    public Comment(CommentDTO dto) {
        this.postId = dto.getPostId();
        this.content = dto.getContent();
        this.createdAt = getNow();
    }
}