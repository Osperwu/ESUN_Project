package com.esun.repository;

import com.esun.vo.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CommentRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // 新增留言
    public int addComment(Comment comment) {
        String sql = "INSERT INTO Comment (user_id, post_id, content, created_at) VALUES (?, ?, ?, ?)";
        return jdbcTemplate.update(sql, comment.getUserId(), comment.getPostId(), comment.getContent(), comment.getCreatedAt());
    }

    // 根據 postId 查詢留言
    public List<Comment> findCommentsByPostId(int postId) {
        String sql = "SELECT * FROM Comment WHERE post_id = ?";
        return jdbcTemplate.query(sql, new Object[]{postId}, new BeanPropertyRowMapper<>(Comment.class));
    }

    // 根據 commentId 查詢單一留言
    public Comment findComment(int commentId) {
        String sql = "SELECT * FROM Comment WHERE comment_id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{commentId}, new BeanPropertyRowMapper<>(Comment.class));
        } catch (Exception e) {
            return null;  // 如果找不到該 commentId，返回 null
        }
    }

    // 確認用戶是否擁有該發文
    public boolean isPostOwner(int postId, int userId) {
        String sql = "SELECT COUNT(*) FROM Post WHERE post_id = ? AND user_id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, postId, userId);
        return count != null && count > 0;
    }

    // 根據 postId 刪除留言
    public boolean deleteCommentsByPostId(int postId) {
        String sql = "DELETE FROM Comment WHERE post_id = ?";
        int rowsAffected = jdbcTemplate.update(sql, postId);
        return rowsAffected > 0;
    }

    public boolean deleteComment(int comment_id) {
        String sql = "DELETE FROM Comment WHERE comment_id = ?";
        int rowsAffected = jdbcTemplate.update(sql, comment_id);
        return rowsAffected > 0;
    }
}