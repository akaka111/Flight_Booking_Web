<%-- 
    Document   : statistics
    Created on : Jun 21, 2025, 12:47:55 PM
    Author     : Khoa
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Thống Kê - Hệ Thống Đặt Vé Máy Bay</title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Montserrat:wght@400;500;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css"/>
    <style>
        :root { --primary-color: #007bff; --secondary-color: #6c757d; --text-color: #343a40; --light-gray: #f8f9fa; --white: #ffffff; --shadow: 0 5px 15px rgba(0,0,0,0.1); }
        * { box-sizing: border-box; margin: 0; padding: 0; }
        body { font-family: 'Montserrat', sans-serif; background-color: var(--light-gray); color: var(--text-color); display: flex; }

        .content { margin-left: 0; padding: 20px; width: 100%; }
        .header { display: none; } /* Hide header in iframe */
        .stats-grid { display: grid; grid-template-columns: repeat(auto-fit, minmax(250px, 1fr)); gap: 20px; margin-top: 20px; }
        .stat-card { background-color: var(--white); padding: 20px; border-radius: 8px; box-shadow: var(--shadow); text-align: center; }
        .stat-card h3 { color: var(--secondary-color); margin-bottom: 10px; }
        .stat-card p { font-size: 1.5em; font-weight: 700; color: var(--primary-color); }
    </style>
</head>
<body>
     
         
    

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

        <div class="stats-grid">
            <div class="stat-card">
                <h3>Tổng Doanh Thu</h3>
                <p>500,000,000 VND</p>
            </div>
            <div class="stat-card">
                <h3>Số Vé Đã Bán</h3>
                <p>1,200</p>
            </div>
            <div class="stat-card">
                <h3>Tỷ Lệ Đặt Chỗ</h3>
                <p>85%</p>
            </div>
        </div>
    </div>
</body>
</html>