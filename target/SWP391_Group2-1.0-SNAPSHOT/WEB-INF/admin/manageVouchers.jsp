<%-- 
    Document   : manageVouchers
    Created on : Jun 21, 2025, 12:46:30 PM
    Author     : Khoa
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Quản Lý Voucher - Hệ Thống Đặt Vé Máy Bay</title>
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
        .table-container { background-color: var(--white); padding: 20px; border-radius: 8px; box-shadow: var(--shadow); }
        table { width: 100%; border-collapse: collapse; }
        table th, table td { padding: 12px; text-align: left; border-bottom: 1px solid var(--light-gray); }
        table th { background-color: var(--primary-color); color: var(--white); }
        table td { color: var(--text-color); }
        .actions a { margin-right: 10px; color: var(--primary-color); text-decoration: none; }
        .actions a:hover { text-decoration: underline; }
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

        <div class="table-container">
            <h2>Quản Lý Voucher</h2>
            <table>
                <tr>
                    <th>Code</th>
                    <th>Discount</th>
                    <th>Expiry Date</th>
                    <th>Status</th>
                    <th>Actions</th>
                </tr>
                <tr>
                    <td>VCH001</td>
                    <td>10%</td>
                    <td>2025-07-01</td>
                    <td>Active</td>
                    <td class="actions"><a href="#">Edit</a> <a href="#">Delete</a></td>
                </tr>
                <tr>
                    <td>VCH002</td>
                    <td>15%</td>
                    <td>2025-06-30</td>
                    <td>Active</td>
                    <td class="actions"><a href="#">Edit</a> <a href="#">Delete</a></td>
                </tr>
            </table>
        </div>
    </div>
</body>
</html>