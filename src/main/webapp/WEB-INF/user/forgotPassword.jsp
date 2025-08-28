<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Quên mật khẩu</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background: #f5f5f5;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
        }
        .container {
            width: 400px;
            background: #fff;
            padding: 25px;
            border-radius: 10px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.2);
        }
        h2 {
            text-align: center;
            color: #333;
        }
        .form-group {
            margin-bottom: 15px;
        }
        .form-group label {
            display: block;
            font-weight: bold;
            margin-bottom: 5px;
        }
        .form-group input {
            width: 100%;
            padding: 10px;
            border: 1px solid #ccc;
            border-radius: 5px;
        }
        .btn {
            width: 100%;
            background: #007bff;
            color: #fff;
            border: none;
            padding: 10px;
            border-radius: 5px;
            cursor: pointer;
            font-weight: bold;
        }
        .btn:hover {
            background: #0056b3;
        }
        .message {
            margin: 10px 0;
            padding: 10px;
            border-radius: 5px;
            font-size: 14px;
        }
        .error {
            background: #f8d7da;
            color: #842029;
        }
        .success {
            background: #d1e7dd;
            color: #0f5132;
        }
    </style>
</head>
<body>
<div class="container">
    <h2>Quên mật khẩu</h2>

    <!-- Hiển thị thông báo -->
    <c:if test="${not empty error}">
        <div class="message error">${error}</div>
    </c:if>
    <c:if test="${not empty message}">
        <div class="message success">${message}</div>
    </c:if>

    <!-- STEP 1: Nhập email/username -->
    <c:if test="${empty step || step eq 'sendOtp'}">
        <form method="post" action="ForgotPassword">
            <input type="hidden" name="action" value="sendOtp">
            <div class="form-group">
                <label for="identifier">Email hoặc Username:</label>
                <input type="text" id="identifier" name="identifier" required>
            </div>
            <button type="submit" class="btn">Gửi OTP</button>
        </form>
    </c:if>

    <!-- STEP 2: Nhập OTP + mật khẩu mới -->
    <c:if test="${step eq 'verifyOtp'}">
        <form method="post" action="ForgotPassword">
            <input type="hidden" name="action" value="resetPassword">
            <input type="hidden" name="userId" value="${userId}">

            <div class="form-group">
                <label for="otp">Mã OTP:</label>
                <input type="text" id="otp" name="otp" required>
            </div>

            <div class="form-group">
                <label for="newPassword">Mật khẩu mới:</label>
                <input type="password" id="newPassword" name="newPassword" minlength="6" required>
            </div>

            <div class="form-group">
                <label for="confirmPassword">Xác nhận mật khẩu:</label>
                <input type="password" id="confirmPassword" name="confirmPassword" minlength="6" required>
            </div>

            <button type="submit" class="btn">Đặt lại mật khẩu</button>
        </form>
    </c:if>
</div>
</body>
</html>
