package com.esun.repository;

import com.esun.vo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // 儲存用戶資料，並包含手機號碼
    public int saveUser(User user) {
        String sql = "INSERT INTO User (phone_number, user_name, email, password, cover_image, biography) VALUES (?, ?, ?, ?, ?, ?)";
        return jdbcTemplate.update(sql, user.getPhoneNumber(), user.getUserName(), user.getEmail(),
                user.getPassword(), user.getCoverImage(), user.getBiography());
    }

    // 根據手機號碼查詢用戶
    public User findByPhoneNumber(String phoneNumber) {
        String sql = "SELECT * FROM User WHERE phone_number = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{phoneNumber}, new BeanPropertyRowMapper<>(User.class));
        } catch (Exception e) {
            return null;  // 如果沒有找到用戶，返回 null
        }
    }

    public List<User> findAllUser() {
        String sql = "SELECT * FROM User";  // 可以根據需要調整查詢條件
        try {
            return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(User.class));  // 這裡會返回一個 List
        } catch (Exception e) {
            e.printStackTrace();  // 可以將錯誤詳細訊息紀錄到日志中
            return null;  // 若發生異常則返回 null
        }
    }
}