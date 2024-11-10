package com.esun.repository;

import com.esun.vo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.List;

//@Repository
public class UserRepositoryDML {

//    @Autowired
    private JdbcTemplate jdbcTemplate;

    private SqlLoader sqlLoader;

//    @PostConstruct
    public void init() {
        // 初始化 SqlLoader，載入 SQL 文件
        sqlLoader = new SqlLoader("ESUN/DB/user_queries.sql");
    }

    // 儲存用戶資料
    public int saveUser(User user) {
        String sql = sqlLoader.getSql("saveUser");
        return jdbcTemplate.update(sql, user.getPhoneNumber(), user.getUserName(), user.getEmail(),
                user.getPassword(), user.getCoverImage(), user.getBiography());
    }

    // 根據手機號碼查詢用戶
    public User findByPhoneNumber(String phoneNumber) {
        String sql = sqlLoader.getSql("findByPhoneNumber");
        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{phoneNumber}, new BeanPropertyRowMapper<>(User.class));
        } catch (Exception e) {
            return null;  // 如果沒有找到用戶，返回 null
        }
    }

    // 查詢所有用戶
    public List<User> findAllUser() {
        String sql = sqlLoader.getSql("findAllUser");
        try {
            return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(User.class));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}