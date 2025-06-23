<%-- 
    Document   : addUser
    Created on : 24 Jun 2025, 00:25:06
    Author     : Admin
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Thêm Người Dùng Mới</title>
    <style>
        /* Thêm một vài style cơ bản cho form */
        body { font-family: 'Montserrat', sans-serif; background-color: #f8f9fa; }
        .form-container { max-width: 600px; margin: 50px auto; padding: 30px; background: #fff; border-radius: 8px; box-shadow: 0 5px 15px rgba(0,0,0,0.1); }
        h2 { text-align: center; margin-bottom: 20px; }
        .form-group { margin-bottom: 15px; }
        .form-group label { display: block; margin-bottom: 5px; font-weight: 500; }
        .form-group input, .form-group select { width: 100%; padding: 10px; border: 1px solid #ccc; border-radius: 4px; box-sizing: border-box; }
        .form-actions { text-align: right; }
        .form-actions button { padding: 10px 20px; border: none; background-color: #007bff; color: white; border-radius: 4px; cursor: pointer; }
        .form-actions a { padding: 10px 20px; text-decoration: none; color: #333; background-color: #f1f1f1; border-radius: 4px; margin-right: 10px; }
    </style>
</head>
<body>
    <div class="form-container">
        <h2>Thêm Người Dùng Mới</h2>
        <form action="manageAccountController" method="post">
            <input type="hidden" name="action" value="create">
            <div class="form-group">
                <label for="username">Tên đăng nhập:</label>
                <input type="text" id="username" name="username" required>
            </div>
            <div class="form-group">
                <label for="password">Mật khẩu:</label>
                <input type="password" id="password" name="password" required>
            </div>
            <div class="form-group">
                <label for="fullname">Họ và Tên:</label>
                <input type="text" id="fullname" name="fullname" required>
            </div>
            <div class="form-group">
                <label for="email">Email:</label>
                <input type="email" id="email" name="email" required>
            </div>
            <div class="form-group">
                <label for="phone">Số điện thoại:</label>
                <input type="tel" id="phone" name="phone">
            </div>
            <div class="form-group">
                <label for="dob">Ngày sinh:</label>
                <input type="date" id="dob" name="dob">
            </div>
            <div class="form-group">
                <label for="role">Vai trò:</label>
                <select id="role" name="role">
                    <option value="CUSTOMER">Customer</option>
                    <option value="STAFF">Staff</option>
                    <option value="ADMIN">Admin</option>
                </select>
            </div>
            <div class="form-group">
                <label>Trạng thái:</label>
                <input type="radio" id="statusActive" name="status" value="true" checked> <label for="statusActive">Active</label>
                <input type="radio" id="statusInactive" name="status" value="false"> <label for="statusInactive">Inactive</label>
            </div>
            <div class="form-actions">
                <a href="manageAccountController">Hủy</a>
                <button type="submit">Lưu</button>
            </div>
        </form>
    </div>
</body>
</html>