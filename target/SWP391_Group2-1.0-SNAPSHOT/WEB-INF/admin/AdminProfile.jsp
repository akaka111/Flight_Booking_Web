<%-- 
    Document   : adminProfile
    Created on : Jul 31, 2025, 4:13:19 PM
    Author     : ADMIN
--%>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="model.Account" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>

<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Hỗ Trợ - Hệ Thống Đặt Vé Máy Bay</title>
        <link rel="preconnect" href="https://fonts.googleapis.com">
        <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
        <link href="https://fonts.googleapis.com/css2?family=Montserrat:wght@400;500;700&display=swap" rel="stylesheet">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css"/>
        <style>
            :root {
                --primary-color: #007bff;
                --secondary-color: #6c757d;
                --text-color: #343a40;
                --light-gray: #f8f9fa;
                --white: #ffffff;
                --shadow: 0 5px 15px rgba(0,0,0,0.1);
                --active-bg: #0056b3;
            }

            * {
                box-sizing: border-box;
                margin: 0;
                padding: 0;
            }

            body {
                font-family: 'Montserrat', sans-serif;
                background-color: var(--light-gray);
                color: var(--text-color);
                display: flex;
            }

            .sidebar {
                width: 250px;
                background-color: var(--primary-color);
                color: var(--white);
                height: 100vh;
                position: fixed;
                padding-top: 20px;
                transition: all 0.3s ease;
            }

            .sidebar .logo {
                font-size: 1.7em;
                font-weight: 700;
                text-align: center;
                margin-bottom: 25px;
            }

            .sidebar ul {
                list-style: none;
            }

            .sidebar ul li {
                padding: 18px 20px;
                cursor: pointer;
            }

            .sidebar ul li:hover {
                background-color: rgba(255, 255, 255, 0.1);
            }

            .sidebar ul li a {
                color: var(--white);
                text-decoration: none;
                display: flex;
                align-items: center;
                font-size: 1.1em;
            }

            .sidebar ul li i {
                margin-right: 12px;
                font-size: 1.3em;
            }

            .sidebar ul li.active {
                background-color: var(--active-bg);
            }

            .content {
                margin-left: 250px;
                padding: 20px;
                width: calc(100% - 250px);
            }

            .header {
                display: flex;
                justify-content: space-between;
                align-items: center;
                background-color: var(--white);
                padding: 10px 20px;
                border-bottom: 1px solid var(--light-gray);
                box-shadow: var(--shadow);
                margin-bottom: 20px;
                position: relative;
                z-index: 100;
            }

            .header .greeting {
                font-size: 1.2em;
                color: var(--text-color);
            }

            .header .actions {
                display: flex;
                align-items: center;
                gap: 10px;
            }

            .header .actions .btn-export {
                padding: 8px 15px;
                background-color: var(--primary-color);
                color: var(--white);
                border: none;
                border-radius: 5px;
                cursor: pointer;
                text-decoration: none;
                font-size: 1em;
            }

            .header .actions .btn-export:hover {
                background-color: #0056b3;
            }

            .header .dropdown {
                position: relative;
            }

            .header .dropdown button {
                background: none;
                border: none;
                color: var(--primary-color);
                cursor: pointer;
                display: flex;
                align-items: center;
                font-size: 1.1em;
                padding: 8px 15px;
                border-radius: 5px;
                transition: background-color 0.3s;
            }

            .header .dropdown button:hover {
                background-color: var(--light-gray);
            }

            .header .dropdown button i {
                margin-right: 5px;
                font-size: 1.2em;
            }

            .header .dropdown-content {
                display: none;
                position: absolute;
                right: 0;
                background-color: var(--white);
                min-width: 150px;
                box-shadow: var(--shadow);
                z-index: 101;
                border-radius: 5px;
                top: 100%;
            }

            .header .dropdown:hover .dropdown-content {
                display: block;
            }

            .header .dropdown-content a {
                color: var(--text-color);
                padding: 10px 20px;
                text-decoration: none;
                display: block;
            }

            .header .dropdown-content a:hover {
                background-color: var(--light-gray);
            }

            .hero-section {
                position: relative;
                height: 300px;
                margin-bottom: 20px;
            }

            .slider {
                position: absolute;
                top: 0;
                left: 0;
                width: 100%;
                height: 100%;
                z-index: 1;
            }

            .slide {
                position: absolute;
                width: 100%;
                height: 100%;
                background-size: cover;
                background-position: center;
                opacity: 0;
                transition: opacity 1.5s ease-in-out;
            }

            .slide.active {
                opacity: 1;
            }

            .hero-overlay {
                position: absolute;
                top: 0;
                left: 0;
                width: 100%;
                height: 100%;
                background-color: rgba(0, 0, 0, 0.3);
                z-index: 2;
            }

            .hero-content {
                position: relative;
                z-index: 3;
                color: var(--white);
                text-align: center;
                padding-top: 60px;
            }

            .stats-grid {
                display: grid;
                grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
                gap: 20px;
                margin-top: 20px;
            }

            .stat-card {
                background-color: var(--white);
                padding: 20px;
                border-radius: 8px;
                box-shadow: var(--shadow);
                text-align: center;
            }

            .stat-card h3 {
                color: var(--secondary-color);
                margin-bottom: 10px;
            }

            .stat-card p {
                font-size: 1.5em;
                font-weight: 700;
                color: var(--primary-color);
            }

            .iframe-container {
                width: 100%;
                height: calc(100vh - 100px);
                border: none;
                display: none;
            }

            /* Modal Styles */
            .modal {
                display: none;
                position: fixed;
                top: 0;
                left: 0;
                width: 100%;
                height: 100%;
                background-color: rgba(0, 0, 0, 0.5);
                z-index: 1000;
            }

            .modal-content {
                background-color: var(--white);
                margin: 15% auto;
                padding: 20px;
                border-radius: 8px;
                width: 300px;
                text-align: center;
                box-shadow: var(--shadow);
            }

            .modal-content h3 {
                margin-bottom: 15px;
            }
            .modal-content p {
                margin-bottom: 30px;
            }
            .modal-content .btn {
                padding: 10px 20px;
                margin: 0 10px;

                border-radius: 5px;
                text-decoration: none;
                transition: all 0.3s ease;
            }
            .btn-confirm {
                background-color: var(--primary-color);
                color: var(--white);
            }
            .btn-confirm:hover {
                background-color: #0056b3;
            }
            .btn-cancel {
                background-color: transparent;
                color: var(--primary-color);
                border: 1px solid var(--primary-color);
            }
            .btn-cancel:hover {
                background-color: #e9ecef;
            }
        </style>
    </head>
    <%
        Account user = (Account) session.getAttribute("user");
    %>
    <body>
        <jsp:include page="/WEB-INF/admin/sidebar.jsp" />
        <div class="content">
            <div class="header">
                <div class="greeting">Xin Chào, <%= user.getFullname()%></div>
                <div class="actions">
                    <a href="exportDashboard.jsp" class="btn-export">Xuất PDF</a>
                    <div class="dropdown">
                        <button><i class="fa-solid fa-user"></i> Hồ Sơ</button>
                        <div class="dropdown-content">
                            <a href="AdminProfileServlet">Xem Hồ Sơ Admin</a>
                            <a href="<c:url value='/logout'/>">Đăng Xuất</a>
                        </div>
                    </div>
                </div>
            </div>

            <div class="hero-section">
                <div class="slider">
                    <div class="slide active" style="background-image: url('https://images.unsplash.com/photo-1530521954074-e64f6810b32d?q=80&w=2070');"></div>
                </div>
                <div class="hero-overlay"></div>
                <div class="hero-content">
                    <h1>Thông Tin Hồ Sơ Admin</h1>
                </div>
            </div>

            <div class="stats-grid">
                <div class="stat-card">
                    <h3>Họ và Tên</h3>
                    <p><%= user.getFullname()%></p>
                </div>
                <div class="stat-card">
                    <h3>Tên Đăng Nhập</h3>
                    <p><%= user.getUsername()%></p>
                </div>
                <div class="stat-card">
                    <h3>Email</h3>
                    <p><%= user.getEmail()%></p>
                </div>
                <div class="stat-card">
                    <h3>Vai Trò</h3>
                    <p>Quản Trị Viên</p>
                </div>
            </div>

            <div style="margin-top: 20px;">
                <a href="admin" class="btn-export"><i class="fa-solid fa-arrow-left icon"></i> Quay lại Dashboard</a>
            </div>
        </div>
    </body>
</html>
