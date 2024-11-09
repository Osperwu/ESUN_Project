package com.esun.service;

import com.esun.repository.CommentRepository;
import com.esun.vo.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class CommentService {

    private final CommentRepository commentRepository;

    // 通過構造器注入 CommentRepository
    @Autowired
    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }
    // 新增留言
    public void addComment(Comment comment) {
        // 呼叫 repository 儲存留言
        commentRepository.addComment(comment);
    }

    // 根據發文 ID 查詢留言
    public List<Comment> getCommentsByPostId(String postId) {
        return commentRepository.findCommentsByPostId(Integer.parseInt(postId));
    }


    // 刪除留言 - 根據 postId 刪除所有相關留言
    public boolean deleteCommentsByPostId(int postId, int userId) {
        // 確保只有該發文的擁有者才可以刪除留言
        boolean isOwner = commentRepository.isPostOwner(postId, userId);
        if (!isOwner) {
            return false;  // 如果不是該發文的擁有者，則不允許刪除留言
        }
        // 刪除留言
        return commentRepository.deleteCommentsByPostId(postId);
    }

    // 刪除留言 - 根據 commentId 刪除留言
    public boolean deleteComment(String comment_id, int userId) {
        // 確保只有該發文的擁有者才可以刪除留言
        int commentId = Integer.parseInt(comment_id);
        Comment comment = commentRepository.findComment(commentId);
        if (Objects.isNull(comment)) return false;
        if (comment.getUserId() != userId) return false;

        // 刪除留言
        return commentRepository.deleteComment(Integer.parseInt(comment_id));
    }
}