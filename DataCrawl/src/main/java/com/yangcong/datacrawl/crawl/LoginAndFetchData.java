package com.yangcong.datacrawl.crawl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

public class LoginAndFetchData {

    public static void main(String[] args) throws IOException {
        // 登录页面的URL
        String loginUrl = "https://www.fpe-store.com/Login.asp";
        // 需要登录获取数据的页面或接口URL
        String dataUrl = "https://www.fpe-store.com/category-s/278.htm?searching=Y&sort=2&cat=278&show=1500&page=1";

        // 用户名和密码
        String username = "1772748735@qq.com";
        String password = "yangcong98.";

        // 发送登录请求
        String cookie = loginAndGetCookie(loginUrl, username, password);

        // 使用Cookie发送请求获取数据
        String responseData = fetchDataWithCookie(dataUrl, cookie);

        // 处理获取的数据
        System.out.println("Response Data:");
        System.out.println(responseData);
    }

    // 发送登录请求并返回Cookie
    private static String loginAndGetCookie(String loginUrl, String username, String password) throws IOException {
        // 构造登录请求的参数
        String postData = "email=" + username + "&password=" + password;

        // 创建URL对象
        URL url = new URL(loginUrl);
        // 打开连接
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        // 设置请求方法为POST
        conn.setRequestMethod("POST");
        // 允许输入输出
        conn.setDoOutput(true);

        // 发送登录请求
        try (OutputStream os = conn.getOutputStream()) {
            os.write(postData.getBytes());
        }

        // 读取服务器响应
        StringBuilder response = new StringBuilder();
        try (BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
            String line;
            while ((line = in.readLine()) != null) {
                response.append(line);
            }
        }

        // 获取Cookie
        Map<String, List<String>> headerFields = conn.getHeaderFields();
        List<String> cookies = headerFields.get("Set-Cookie");
        List<String> cookiess = headerFields.get("Cookie");
        if (cookies != null) {
            for (String cookie : cookies) {
                if (cookie.startsWith("JSESSIONID")) {
                    // 提取JSESSIONID保存为Cookie
                    return cookie.split(";")[0];
                }
            }
        }
        return null;
    }

    // 使用Cookie发送请求获取数据
    private static String fetchDataWithCookie(String dataUrl, String cookie) throws IOException {
        // 创建URL对象
        URL url = new URL(dataUrl);
        // 打开连接
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        // 设置请求方法为GET
        conn.setRequestMethod("GET");
        // 设置Cookie
        conn.setRequestProperty("Cookie", cookie);

        // 读取服务器响应
        StringBuilder response = new StringBuilder();
        try (BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
            String line;
            while ((line = in.readLine()) != null) {
                response.append(line);
            }
        }
        return response.toString();
    }
}

