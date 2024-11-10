package com.esun.service;

import com.esun.repository.UserRepository;
import com.esun.vo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // 使用手機號碼註冊
    public boolean registerUser(User user) {
        // 先檢查手機號碼是否已經註冊過
        if (user != null  && userRepository.findByPhoneNumber(user.getPhoneNumber()) != null) {
            return false;  // 如果已經有相同的手機號碼，則註冊失敗
        }

        // 密碼加鹽並進行雜湊
        String salt = BCrypt.gensalt();
        String hashedPassword = BCrypt.hashpw(user.getPassword(), salt);
        user.setPassword(hashedPassword);

        // 儲存用戶資料
        userRepository.saveUser(user);
        return true;
    }

    // 根據手機號碼查詢用戶
    public User getUserByPhoneNumber(String phoneNumber) {
        return userRepository.findByPhoneNumber(phoneNumber);
    }

    public List<User> getAllUser() {
        return userRepository.findAllUser();
    }


}