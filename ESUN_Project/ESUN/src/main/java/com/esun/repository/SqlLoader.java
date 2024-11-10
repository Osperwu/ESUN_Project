package com.esun.repository;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Properties;

public class SqlLoader {
    private Properties sqlProperties = new Properties();

    public SqlLoader(String filePath) {
        try {
            // 使用專案根目錄取得文件的絕對路徑
            String absolutePath = Paths.get(filePath).toAbsolutePath().toString();
            try (FileInputStream fis = new FileInputStream(absolutePath)) {
                sqlProperties.load(fis);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getSql(String key) {
        return sqlProperties.getProperty(key);
    }
}