<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Chỉnh Sửa Người Dùng - Hệ Thống Đặt Vé Máy Bay</title>
        <link rel="preconnect" href="https://fonts.googleapis.com">
        <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
        <link href="https://fonts.googleapis.com/css2?family=Montserrat:wght@400;500;700&display=swap" rel="stylesheet">
        <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css"/>
        <style>
            body {
                font-family: 'Montserrat', sans-serif;
                background-color: #f8f9fa;
                color: #343a40;
                padding: 40px;
            }
            .form-container {
                max-width: 900px;
                margin: auto;
                background: #fff;
                padding: 30px;
                border-radius: 10px;
                box-shadow: 0 5px 15px rgba(0,0,0,0.1);
            }
            h2 {
                text-align: center;
                margin-bottom: 30px;
                color: #007bff;
            }
            .form-group input[readonly] {
                background-color: #e9ecef;
            }
            .form-actions {
                text-align: right;
            }
            .form-actions .btn {
                min-width: 120px;
            }
        </style>
    </head>
    <body>
        <div class="container">
            <div class="form-container">
                <h2><i class="fa fa-user-pen"></i> Chỉnh Sửa Người Dùng</h2>

                <!-- Thông báo -->
                <c:if test="${not empty message}">
                    <div class="alert alert-success">${message}</div>
                </c:if>
                <c:if test="${not empty error}">
                    <div class="alert alert-danger">${error}</div>
                </c:if>

                <form action="ManageCustomerController" method="post">
                    <input type="hidden" name="action" value="update">
                    <input type="hidden" name="userId" value="${user.userId}">
                    <div class="form-group">
                        <label for="username">Tên đăng nhập</label>
                        <input type="text" class="form-control" id="username" name="username" value="${user.username}" readonly>
                    </div>
                    <div class="form-group">
                        <label for="fullname">Họ và Tên</label>
                        <input type="text" class="form-control" id="fullname" name="fullname" value="${user.fullname}" required>
                    </div>
                    <div class="form-group">
                        <label for="email">Email</label>
                        <input type="email" class="form-control" id="email" name="email" value="${user.email}" required>
                    </div>
                    <div class="form-group">
                        <label for="phone">Số điện thoại</label>
                        <input type="tel" class="form-control" id="phone" name="phone" value="${user.phone}">
                    </div>
                    <div class="form-group">
                        <label for="dob">Ngày sinh</label>
                        <fmt:formatDate value="${user.dob}" pattern="yyyy-MM-dd" var="formattedDob"/>
                        <input type="date" class="form-control" id="dob" name="dob" value="${formattedDob}">
                    </div>
                    <div class="form-group">
                        <label for="role">Vai trò</label>
                        <select class="form-control" id="role" name="role">
                            <option value="CUSTOMER" ${user.role == 'CUSTOMER' ? 'selected' : ''}>Customer</option>
                            <option value="STAFF" ${user.role == 'STAFF' ? 'selected' : ''}>Staff</option>
                            <option value="ADMIN" ${user.role == 'ADMIN' ? 'selected' : ''}>Admin</option>
                        </select>
                    </div>
                    <div class="form-group">
                        <label>Trạng thái</label><br>
                        <div class="form-check form-check-inline">
                            <input class="form-check-input" type="radio" id="statusActive" name="status" value="true" ${user.status ? 'checked' : ''}>
                            <label class="form-check-label" for="statusActive">Active</label>
                        </div>
                        <div class="form-check form-check-inline">
                            <input class="form-check-input" type="radio" id="statusInactive" name="status" value="false" ${!user.status ? 'checked' : ''}>
                            <label class="form-check-label" for="statusInactive">Inactive</label>
                        </div>
                    </div>
                    <div class="form-actions mt-4">
                        <a href="ManageCustomerController" class="btn btn-secondary">
                            <i class="fa fa-arrow-left"></i> Hủy
                        </a>
                        <button type="submit" class="btn btn-success">
                            <i class="fa fa-save"></i> Lưu
                        </button>
                    </div>
                </form>
            </div>
        </div>

        <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
        <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
    </body>
</html>