<%-- 
    Document   : header
    Created on : 23 Jun 2025, 00:31:33
    Author     : Admin
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<style>
    /* CSS cho Header */
    .main-header {
        background-color: #ffffff;
        box-shadow: 0 2px 5px rgba(0,0,0,0.1);
        padding: 15px 0;
        position: sticky;
        top: 0;
        z-index: 1000;
        width: 100%;
    }

    .main-header .container {
        display: flex;
        justify-content: space-between;
        align-items: center;
        max-width: 1200px;
        margin: 0 auto;
        padding: 0 20px;
    }

    .logo {
        font-size: 1.8em;
        font-weight: 700;
        color: #007bff;
        text-decoration: none;
    }

    .logo i {
        margin-right: 10px;
        transform: rotate(-45deg);
    }

    .main-nav a {
        color: #343a40;
        text-decoration: none;
        margin: 0 15px;
        font-weight: 500;
        transition: color 0.3s ease;
        position: relative;
    }

    .main-nav a:hover {
        color: #007bff;
    }

    .main-nav a::after {
        content: '';
        position: absolute;
        width: 0;
        height: 2px;
        display: block;
        margin-top: 5px;
        right: 0;
        background: #007bff;
        transition: width 0.3s ease;
    }

    .main-nav a:hover::after {
        width: 100%;
        left: 0;
    }


    .auth-buttons .btn {
        padding: 10px 22px;
        border-radius: 20px;
        text-decoration: none;
        margin-left: 10px;
        transition: all 0.3s ease;
        border: 2px solid transparent;
        font-weight: 500;
    }

    .btn-login {
        background-color: transparent;
        color: #007bff;
        border-color: #007bff;
    }

    .btn-login:hover {
        background-color: #007bff;
        color: #ffffff;
    }

    .btn-register {
        background-color: #007bff;
        color: #ffffff;
    }

    .btn-register:hover {
        background-color: #0056b3;
        border-color: #0056b3;
    }
</style>

<header class="main-header">
    <div class="container">
        <%-- Sử dụng c:url để tạo link an toàn, tự động thêm context path --%>
        <a href="<c:url value='/home'/>" class="logo">
            <i class="fa-solid fa-plane-up"></i>
            Flight Booking Web
        </a>
        <nav class="main-nav">
            <a href="home">Trang Chủ</a>
            <a href="#">Khuyến Mãi</a>
            <a href="#">Quản Lý Đặt Chỗ</a>
            <a href="#">Liên Hệ</a>
        </nav>
        <%-- ================== PHẦN HIỂN THỊ ĐỘNG ================== --%>
        <div class="auth-buttons">
            <c:choose>
                <%-- TRƯỜNG HỢP: ĐÃ ĐĂNG NHẬP (user không rỗng) --%>
                <c:when test="${not empty sessionScope.user}">
                    <div class="user-info">
                        <a href="#">
                            <i class="fa-solid fa-user-circle"></i> 
                            Chào, ${sessionScope.user.username}
                        </a>
                        <a href="<c:url value='/logout'/>" class="btn btn-logout">Đăng Xuất</a>
                    </div>
                </c:when>

                <%-- TRƯỜNG HỢP: CHƯA ĐĂNG NHẬP --%>
                <c:otherwise>
                    <a href="<c:url value='/login'/>" class="btn btn-login">Đăng Nhập</a>
                    <a href="<c:url value='/register'/>" class="btn btn-register">Đăng Ký</a>
                </c:otherwise>
            </c:choose>
        </div>
        <%-- ======================================================== --%>

    </div>
</header>
