package com.esun.service;

import com.esun.dto.PostDTO;
import com.esun.repository.CommentRepository;
import com.esun.repository.PostRepository;
import com.esun.vo.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.esun.util.tool.getNow;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final CommentService commentService;

    @Autowired
    public PostService(PostRepository postRepository,CommentService commentService) {
        this.postRepository = postRepository;
        this.commentService = commentService;
    }

    // 創建發文
    public void createPost(int userId, PostDTO postDTO) {
        Post post = new Post();
        post.setUserId(userId); // 設定發文者
        post.setContent(postDTO.getContent());
        post.setImage(postDTO.getImage());
        post.setCreatedAt(getNow()); // 可以通過 DTO 中設置時間
        postRepository.createPost(post);
    }

    // 獲取所有發文
    public List<Post> getAllPosts() {
        return postRepository.findAllPosts();
    }

    // 更新發文
    public boolean updatePost(int postId, int userId, String content) {
        Optional<Post> postOptional = postRepository.findPostById(postId);
        if (postOptional.isPresent() && postRepository.isPostOwnedByUser(postId, userId)) {
            // 更新發文內容
            postRepository.updatePost(postId, content);
            return true;
        }
        return false;
    }

    // 刪除發文
    public boolean deletePost(int postId, int userId) {
        if (postRepository.isPostOwnedByUser(postId, userId)) {
            postRepository.deletePost(postId);
            commentService.deleteCommentsByPostId(postId, userId);
            return true;
        }
        return false;
    }
}