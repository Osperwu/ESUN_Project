package com.esun.controller;

import com.esun.service.UserService;
import com.esun.dto.UserDTO;
import com.esun.vo.User;
import com.esun.util.JwtUtils;  // 引入 Jwt 工具類別
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.esun.util.tool.generateErrorResponse;
import static com.esun.util.tool.isNumeric;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;
    private final JwtUtils jwtUtils;

    // 使用 Constructor Injection
    @Autowired
    public UserController(UserService userService, JwtUtils jwtUtils) {
        this.userService = userService;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/register") // http://localhost:8080/api/user/register
    public ResponseEntity<String> registerUser(@Valid @RequestBody User user) {
        // 驗證手機號碼格式
        ResponseEntity<Map<String, Object>> badRequest = checkPhone(user.getPhoneNumber());
        if (badRequest != null) return ResponseEntity.badRequest().body("phoneNumber err.");

        // 註冊用戶
        boolean isRegistered = userService.registerUser(user);
        if (isRegistered) {
            return ResponseEntity.ok("User registered successfully.");
        } else {
            return ResponseEntity.badRequest().body("User registration failed.");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> loginUser(@Valid @RequestBody UserDTO loginRequest) {
        String phoneNumber = loginRequest.getPhoneNumber();

        // 驗證手機號碼格式
        ResponseEntity<Map<String, Object>> badRequest = checkPhone(phoneNumber);
        if (badRequest != null) return badRequest;

        String password = loginRequest.getPassword();
        User user = userService.getUserByPhoneNumber(phoneNumber);
        List<User> allUser = userService.getAllUser();

        // 檢查用戶是否存在
        if (user == null) {
            return generateErrorResponse("User not found.", HttpStatus.NOT_FOUND);
        }

        // 使用 BCrypt 驗證密碼
        if (!BCrypt.checkpw(password, user.getPassword())) {
            return generateErrorResponse("Invalid password.", HttpStatus.UNAUTHORIZED);
        }

        // 使用 JwtUtils 生成 JWT Token
        String token = jwtUtils.generateToken(String.valueOf(user.getUser_id()));

        // 準備回應資料
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("rc", "200");
        responseBody.put("rm", "success");
        responseBody.put("token", token);// 返回 token

        responseBody.put("allUser", allUser);

        return ResponseEntity.ok(responseBody);
    }

    private ResponseEntity<Map<String, Object>> checkPhone(String phoneNumber) {
        // 驗證手機號碼格式邏輯
        if (!isNumeric(phoneNumber) || !phoneNumber.startsWith("09") || phoneNumber.length() != 10) {
            return generateErrorResponse("phoneNumber err.", HttpStatus.BAD_REQUEST);
        }
        return null;
    }
}