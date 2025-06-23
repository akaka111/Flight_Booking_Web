<%-- 
    Document   : editUser
    Created on : 24 Jun 2025, 00:25:28
    Author     : Admin
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Chỉnh Sửa Người Dùng</title>
    <style>
        /* Dùng chung style với addUser.jsp */
        body { font-family: 'Montserrat', sans-serif; background-color: #f8f9fa; }
        .form-container { max-width: 600px; margin: 50px auto; padding: 30px; background: #fff; border-radius: 8px; box-shadow: 0 5px 15px rgba(0,0,0,0.1); }
        h2 { text-align: center; margin-bottom: 20px; }
        .form-group { margin-bottom: 15px; }
        .form-group label { display: block; margin-bottom: 5px; font-weight: 500; }
        .form-group input, .form-group select { width: 100%; padding: 10px; border: 1px solid #ccc; border-radius: 4px; box-sizing: border-box; }
        .form-group input[readonly] { background-color: #e9ecef; }
        .form-actions { text-align: right; }
        .form-actions button { padding: 10px 20px; border: none; background-color: #007bff; color: white; border-radius: 4px; cursor: pointer; }
        .form-actions a { padding: 10px 20px; text-decoration: none; color: #333; background-color: #f1f1f1; border-radius: 4px; margin-right: 10px; }
    </style>
</head>
<body>
    <div class="form-container">
        <h2>Chỉnh Sửa Thông Tin Người Dùng</h2>
        <form action="manageAccountController" method="post">
            <input type="hidden" name="action" value="update">
            <input type="hidden" name="userId" value="${user.userId}">
            
            <div class="form-group">
                <label for="username">Tên đăng nhập:</label>
                <input type="text" id="username" name="username" value="${user.username}" readonly>
            </div>
            <div class="form-group">
                <label for="fullname">Họ và Tên:</label>
                <input type="text" id="fullname" name="fullname" value="${user.fullname}" required>
            </div>
            <div class="form-group">
                <label for="email">Email:</label>
                <input type="email" id="email" name="email" value="${user.email}" required>
            </div>
            <div class="form-group">
                <label for="phone">Số điện thoại:</label>
                <input type="tel" id="phone" name="phone" value="${user.phone}">
            </div>
            <div class="form-group">
                <label for="dob">Ngày sinh:</label>
                <fmt:formatDate value="${user.dob}" pattern="yyyy-MM-dd" var="formattedDob" />
                <input type="date" id="dob" name="dob" value="${formattedDob}">
            </div>
            <div class="form-group">
                <label for="role">Vai trò:</label>
                <select id="role" name="role">
                    <option value="CUSTOMER" ${user.role == 'CUSTOMER' ? 'selected' : ''}>Customer</option>
                    <option value="STAFF" ${user.role == 'STAFF' ? 'selected' : ''}>Staff</option>
                    <option value="ADMIN" ${user.role == 'ADMIN' ? 'selected' : ''}>Admin</option>
                </select>
            </div>
            <div class="form-group">
                <label>Trạng thái:</label>
                <input type="radio" id="statusActive" name="status" value="true" ${user.status ? 'checked' : ''}> <label for="statusActive">Active</label>
                <input type="radio" id="statusInactive" name="status" value="false" ${!user.status ? 'checked' : ''}> <label for="statusInactive">Inactive</label>
            </div>
            <div class="form-actions">
                <a href="manageAccountController">Hủy</a>
                <button type="submit">Cập Nhật</button>
            </div>
        </form>
    </div>
</body>
</html>