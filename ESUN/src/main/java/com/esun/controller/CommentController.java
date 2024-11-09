package com.esun.controller;

import com.esun.dto.CommentDTO;
import com.esun.service.CommentService;
import com.esun.util.JwtUtils;
import com.esun.vo.Comment;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/comments")
public class CommentController {


    private final CommentService commentService;
    private final JwtUtils jwtUtils;

    @Autowired
    public CommentController(CommentService commentService, JwtUtils jwtUtils) {
        this.commentService = commentService;
        this.jwtUtils = jwtUtils;
    }

    // 確保只有登入的用戶能發表留言
    @PostMapping("/create")
    public ResponseEntity<Object> addComment(@RequestBody CommentDTO commentDTO, @RequestHeader(value = "Authorization", required = false) String token) {
        // 使用 JwtUtils 來驗證 Token 是否有效
        ResponseEntity<Object> tokenValidationResponse = jwtUtils.checkTokenValid(token);
        if (tokenValidationResponse != null) {
            return tokenValidationResponse;  // 如果 Token 無效，返回錯誤回應
        }

        // 從有效的 Token 中解析出 userId
        Integer userId = jwtUtils.getUserIdFromToken(token);
        Comment comment = new Comment(commentDTO);
        // 設置留言的發佈者為當前登入的用戶
        comment.setUserId(userId);

        // 儲存留言
        commentService.addComment(comment);
        return ResponseEntity.ok("Comment added successfully.");
    }

    // 根據發文 ID 查詢留言
    @GetMapping("/post/{postId}")
    public ResponseEntity<List<Comment>> getCommentsByPostId(@PathVariable String postId) {
        List<Comment> comments = commentService.getCommentsByPostId(postId);
        return ResponseEntity.ok(comments);
    }

    // 刪除留言 - 透過 postId 刪除所有相關留言
    @DeleteMapping("/post/{commentId}")
    public ResponseEntity<Object> deleteComment(@PathVariable String commentId, @RequestHeader(value = "Authorization", required = false) String token) {
        // 驗證 Token
        ResponseEntity<Object> tokenValidationResponse = jwtUtils.checkTokenValid(token);
        if (tokenValidationResponse != null) {
            return tokenValidationResponse;  // 如果 Token 無效，返回錯誤回應
        }

        // 從 Token 解析出 userId
        Integer userId = jwtUtils.getUserIdFromToken(token);

        // 呼叫 Service 層刪除留言
        boolean isDeleted = commentService.deleteComment(commentId, userId);
        if (isDeleted) {
            return ResponseEntity.ok("Comments deleted successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to delete comments.");
        }
    }

}