package com.esun.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtils {

    private final SecretKey secretKey;
    private final long expiration;

    public JwtUtils(@Value("${jwt.secret}") String secret, @Value("${jwt.expiration}") long expiration) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes());
        this.expiration = expiration;
    }

    // 生成 JWT Token，帶有 subject（用戶識別）和過期時間
    public String generateToken(String userId) {
        return Jwts.builder()
                .setSubject(userId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(secretKey, SignatureAlgorithm.HS512)
                .compact();
    }

    // 解析 JWT Token 并返回 Claims，验证是否有效
    public Claims parseToken(String token) {
        try {
            return Jwts.parserBuilder()  // 使用解析器解析 Token
                    .setSigningKey(secretKey)  // 设置签名秘钥
                    .build()
                    .parseClaimsJws(token)  // 解析 token 并返回 Claims
                    .getBody();
        } catch (JwtException | IllegalArgumentException e) {
            // 打印错误信息并返回 null，表示解析失败
            System.err.println("JWT validation error: " + e.getMessage());
            return null;
        }
    }

    // 驗證 Token 是否有效並返回是否有效
    public ResponseEntity<Object> checkTokenValid(String token) {
        // 如果沒有傳遞 Token
        if (token == null || token.trim().isEmpty()) {
            // 回傳錯誤訊息及狀態碼
            Map<String, String> response = new HashMap<>();
            response.put("rc", "1996");
            response.put("rm", "Please login first.token is null");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        // 驗證 Token 是否有效
        try {
            Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);
        } catch (JwtException e) {
            // 如果 Token 解析失敗或無效
            Map<String, String> response = new HashMap<>();
            response.put("rc", "1996");
            response.put("rm", "bad token");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
        return null;  // 如果 Token 有效，返回 null 表示驗證通過
    }


    // 從 JWT token 中解析出 userId
    public Integer getUserIdFromToken(String token) {
        try {
            Claims claims = parseToken(token);  // 使用 JwtUtils 解析 token
            return Integer.parseInt(claims.getSubject());  // 取得 userId (這裡假設 JWT 的 subject 是用戶 ID)
        } catch (Exception e) {
            return null;  // 無效的 token 或解析錯誤
        }
    }
}