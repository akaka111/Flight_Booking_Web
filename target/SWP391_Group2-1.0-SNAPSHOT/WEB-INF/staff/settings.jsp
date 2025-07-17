<%-- 
    Document   : settings
    Created on : Jun 21, 2025, 12:48:18 PM
    Author     : Khoa
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Cài Đặt - Hệ Thống Đặt Vé Máy Bay</title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Montserrat:wght@400;500;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css"/>
    <style>
        :root { --primary-color: #007bff; --secondary-color: #6c757d; --text-color: #343a40; --light-gray: #f8f9fa; --white: #ffffff; --shadow: 0 5px 15px rgba(0,0,0,0.1); }
        * { box-sizing: border-box; margin: 0; padding: 0; }
        body { font-family: 'Montserrat', sans-serif; background-color: var(--light-gray); color: var(--text-color); display: flex; }
        .sidebar { display: none; } /* Hide sidebar in iframe */
        .content { margin-left: 0; padding: 20px; width: 100%; }
        .header { display: none; } /* Hide header in iframe */
        .settings-container { background-color: var(--white); padding: 20px; border-radius: 8px; box-shadow: var(--shadow); }
    </style>
</head>
<body>
    <div class="sidebar">
        <div class="logo">FBAir <i class="fa-solid fa-plane"></i></div>
        <ul>
            <li><a href="admin.jsp"><i class="fa-solid fa-tachometer-alt"></i> Bảng Điều Khiển</a></li>
            <li><a href="manageUsers.jsp"><i class="fa-solid fa-users"></i> Quản Lý Người Dùng</a></li>
            <li><a href="manageFlights.jsp"><i class="fa-solid fa-plane-departure"></i> Quản Lý Chuyến Bay</a></li>
            <li><a href="manageAirlines.jsp"><i class="fa-solid fa-building"></i> Quản Lý Hãng Bay</a></li>
            <li><a href="manageVouchers.jsp"><i class="fa-solid fa-ticket"></i> Quản Lý Voucher</a></li>
            <li><a href="statistics.jsp"><i class="fa-solid fa-chart-line"></i> Thống Kê</a></li>
            <li><a href="settings.jsp"><i class="fa-solid fa-cog"></i> Cài Đặt</a></li>
            <li><a href="support.jsp"><i class="fa-solid fa-headset"></i> Hỗ Trợ</a></li>
        </ul>
    </div>

    <div class="content">
        <div class="header">
            <div class="greeting">Xin Chào, Admin</div>
            <div class="actions">
                <div class="dropdown">
                    <button><i class="fa-solid fa-user"></i> Hồ Sơ</button>
                    <div class="dropdown-content">
                        <a href="viewProfile.jsp">Xem Hồ Sơ Admin</a>
                        <a href="LogoutController">Đăng Xuất</a>
                    </div>
                </div>
            </div>
        </div>

        <div class="settings-container">
            <h2>Cài Đặt</h2>
            <p>Configure system settings here.</p>
        </div>
    </div>
</body>
</html>