package com.esun.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

public class tool {

    public static boolean isNumeric(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }
        return str.matches("-?\\d+(\\.\\d+)?");
    }

    // 輔助方法：生成錯誤回應
    public static ResponseEntity<Map<String, Object>> generateErrorResponse(String message, HttpStatus status) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("rc", String.valueOf(status.value())); // 使用狀態碼
        errorResponse.put("rm", message); // 錯誤訊息

        return ResponseEntity.status(status).body(errorResponse);
    }

    public static Timestamp getNow(){
        return new Timestamp(System.currentTimeMillis());
    }
}
