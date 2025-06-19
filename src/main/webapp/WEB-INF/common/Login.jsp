<%-- 
    Document   : Login
    Created on : 19 Jun 2025, 16:55:10
    Author     : Admin
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
    <title>Login</title>
    <style>
        body {
            font-family: sans-serif;
            background-color: #f5f5f5;
        }
        .login-container {
            width: 400px;
            margin: 100px auto;
            background: #fff;
            padding: 30px;
            border-radius: 8px;
            box-shadow: 0px 0px 10px #ccc;
        }
        h2 {
            text-align: center;
        }
        .form-group {
            margin: 15px 0;
        }
        input[type="text"], input[type="password"] {
            width: 100%;
            padding: 8px 10px;
            border-radius: 4px;
            border: 1px solid #ccc;
        }
        .btn {
            width: 100%;
            padding: 10px;
            background-color: #4CAF50;
            color: white;
            border: none;
            margin-top: 10px;
            border-radius: 4px;
            cursor: pointer;
        }
        .google-btn {
            background-color: #dd4b39;
        }
        .error {
            color: red;
            text-align: center;
            margin-top: 10px;
        }
    </style>
</head>
<body>
<div class="login-container">
    <h2>Login</h2>

    <!-- Hiển thị thông báo lỗi nếu có -->
    <c:if test="${not empty error}">
        <div class="error">${error}</div>
    </c:if>

    <!-- Form login thông thường -->
    <form action="LoginController" method="post">
        <div class="form-group">
            <input type="text" name="username" placeholder="Username" required/>
        </div>
        <div class="form-group">
            <input type="password" name="password" placeholder="Password" required/>
        </div>
        <button type="submit" class="btn">Login</button>
    </form>

    <hr style="margin: 20px 0;">

    <!-- Nút đăng nhập bằng Google -->
    <form action="LoginController" method="get">
        <input type="hidden" name="action" value="google"/>
        <button type="submit" class="btn google-btn">Login with Google</button>
    </form>
</div>
</body>
</html>
