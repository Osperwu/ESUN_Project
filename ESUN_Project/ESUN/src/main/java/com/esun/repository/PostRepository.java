package com.esun.repository;

import com.esun.vo.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class PostRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // 創建發文
    public int createPost(Post post) {
        String sql = "INSERT INTO Post (user_id, content, image, created_at) VALUES (?, ?, ?, ?)";
        return jdbcTemplate.update(sql, post.getUserId(), post.getContent(), post.getImage(), post.getCreatedAt());
    }

    // 查詢所有發文
    public List<Post> findAllPosts() {
        String sql = "SELECT * FROM Post";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Post.class));
    }

    // 查詢單一發文
    public Optional<Post> findPostById(int postId) {
        String sql = "SELECT * FROM Post WHERE post_id = ?";
        List<Post> posts = jdbcTemplate.query(sql, new Object[]{postId}, new BeanPropertyRowMapper<>(Post.class));
        return posts.isEmpty() ? Optional.empty() : Optional.of(posts.get(0));
    }

    // 更新發文
    public int updatePost(int postId, String content) {
        String sql = "UPDATE Post SET content = ? WHERE post_id = ?";
        return jdbcTemplate.update(sql, content, postId);
    }

    // 刪除發文
    public int deletePost(int postId) {
        String sql = "DELETE FROM Post WHERE post_id = ?";
        return jdbcTemplate.update(sql, postId);
    }

    // 根據 userId 查詢該使用者的所有發文
    public List<Post> findPostsByUserId(int userId) {
        String sql = "SELECT * FROM Post WHERE user_id = ?";
        return jdbcTemplate.query(sql, new Object[]{userId}, new BeanPropertyRowMapper<>(Post.class));
    }

    // 檢查該發文是否屬於指定的 userId
    public boolean isPostOwnedByUser(int postId, int userId) {
        String sql = "SELECT COUNT(*) FROM Post WHERE post_id = ? AND user_id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, postId, userId);
        return count != null && count > 0;
    }
}