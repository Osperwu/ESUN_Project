package com.esun.controller;

import com.esun.dto.PostDTO;
import com.esun.service.PostService;
import com.esun.vo.Post;
import com.esun.util.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;
    private final JwtUtils jwtUtils;

    @Autowired
    public PostController(PostService postService, JwtUtils jwtUtils) {
        this.postService = postService;
        this.jwtUtils = jwtUtils;
    }

    // 創建發文功能
    @PostMapping("/create")
    public ResponseEntity<Object> createPost(@Valid @RequestBody PostDTO postDTO, @RequestHeader(value = "Authorization", required = false) String token) {
        // 使用 JwtUtils 來驗證 Token 是否有效
        ResponseEntity<Object> tokenValidationResponse = jwtUtils.checkTokenValid(token);
        if (tokenValidationResponse != null) {
            return tokenValidationResponse;  // 如果 Token 無效，返回錯誤回應
        }

        // 檢查 JWT 是否有效
        Integer userId = jwtUtils.getUserIdFromToken(token);
        System.err.println("useriddddd=" + userId);

//        // 呼叫 service 層來創建發文，並將 userId 傳遞
        postService.createPost(userId, postDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body("Post created successfully.");
    }

    // 獲取所有發文
    @GetMapping("/getAll")
    public ResponseEntity<List<Post>> getAllPosts() {
        List<Post> posts = postService.getAllPosts();
        return ResponseEntity.ok(posts);
    }

    // 編輯發文功能
    @PutMapping("/{postId}")
    public ResponseEntity<Object> updatePost(@PathVariable int postId, @RequestBody PostDTO postDTO, @RequestHeader(value = "Authorization", required = false) String token) {
        // 使用 JwtUtils 來驗證 Token 是否有效
        ResponseEntity<Object> tokenValidationResponse = jwtUtils.checkTokenValid(token);
        if (tokenValidationResponse != null) {
            return tokenValidationResponse;  // 如果 Token 無效，返回錯誤回應
        }

        Integer userId = jwtUtils.getUserIdFromToken(token);
        // 呼叫 service 層來更新發文
        boolean isUpdated = postService.updatePost(postId, userId, postDTO.getContent());
        if (isUpdated) {
            return ResponseEntity.ok("Post updated successfully.");
        } else {
            return ResponseEntity.badRequest().body("Failed to update post.");
        }
    }

    // 刪除發文功能
    @DeleteMapping("/{postId}")
    public ResponseEntity<Object> deletePost(@PathVariable int postId, @RequestHeader(value = "Authorization", required = false) String token) {
        // 使用 JwtUtils 來驗證 Token 是否有效
        ResponseEntity<Object> tokenValidationResponse = jwtUtils.checkTokenValid(token);
        if (tokenValidationResponse != null) {
            return tokenValidationResponse;  // 如果 Token 無效，返回錯誤回應
        }

        // 從有效的 Token 中解析出 userId
        Integer userId = jwtUtils.getUserIdFromToken(token);
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Please log in first.");
        }

        // 呼叫 service 層來刪除發文
        boolean isDeleted = postService.deletePost(postId, userId);
        if (isDeleted) {
            return ResponseEntity.ok("Post deleted successfully.");
        } else {
            return ResponseEntity.badRequest().body("Failed to delete post.");
        }
    }
}