
<%-- 
    Document   : Login
    Created on : 23 Jun 2025, 22:13:28
    Author     : Khoa
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Đăng nhập - Flight Booking Web</title>

    <!-- Google Fonts -->
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Montserrat:wght@400;500;700&display=swap" rel="stylesheet">

    <!-- Font Awesome Icons -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css"/>

    <style>
        :root {
            --primary-color: #007bff;
            --secondary-color: #ff6f61;
            --text-color: #333;
            --light-gray: #f8f9fa;
            --white: #ffffff;
            --shadow: 0 4px 12px rgba(0,0,0,0.1);
            --error-color: #dc3545; /* Màu đỏ cho lỗi */
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
            min-height: 100vh;
            display: flex;
            flex-direction: column;
        }
        .hero-section {
            position: relative;
            flex: 1;
            display: flex;
            align-items: center;
            justify-content: center;
            text-align: center;
            padding: 40px 20px;
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
            background-color: rgba(0, 0, 0, 0.5);
            z-index: 2;
        }
        .login-container {
            position: relative;
            z-index: 3;
            max-width: 500px; /* Tăng từ 400px lên 500px */
            width: 100%;
            background: rgba(255,255,255,0.9);
            padding: 40px;
            border-radius: 12px;
            box-shadow: var(--shadow);
            text-align: center;
            backdrop-filter: blur(5px);
        }
        h2 {
            font-size: 1.8em;
            font-weight: 600;
            margin-bottom: 10px;
            color: var(--text-color);
        }
        .form-group {
            margin: 15px 0;
            text-align: left;
        }
        .form-group label {
            font-size: 0.95em;
            font-weight: 500;
            color: var(--text-color);
            margin-bottom: 8px;
            display: block;
        }
        .form-group label i {
            margin-right: 6px;
            color: var(--primary-color);
        }
        input[type="text"], input[type="password"] {
            width: 100%;
            padding: 12px;
            border: 1px solid #ddd;
            border-radius: 6px;
            font-size: 0.95em;
            font-family: 'Montserrat', sans-serif;
            transition: border-color 0.3s ease;
        }
        input:focus {
            outline: none;
            border-color: var(--primary-color);
        }
        .btn {
            width: 100%;
            padding: 14px;
            background-color: var(--primary-color);
            color: var(--white);
            border: none;
            border-radius: 6px;
            font-size: 1em;
            font-weight: 500;
            cursor: pointer;
            transition: background-color 0.3s ease;
        }
        .btn:hover {
            background-color: #0056b3;
        }
        .error {
            color: var(--error-color);
            font-size: 0.9em;
            margin: 10px 0;
            text-align: center;
            display: block;
        }
        .message {
            color: #28a745;
            font-size: 0.9em;
            margin: 10px 0;
            text-align: center;
        }
        .link {
            margin-top: 20px;
            font-size: 0.9em;
            color: var(--text-color);
        }
        .link a {
            color: var(--primary-color);
            text-decoration: none;
            font-weight: 500;
        }
        .link a:hover {
            text-decoration: underline;
        }
    </style>
</head>
<body>
    <jsp:include page="/WEB-INF/user/components/header.jsp" />
    <section class="hero-section">
        <div class="slider">
            <div class="slide active" style="background-image: url('https://images.unsplash.com/photo-1530521954074-e64f6810b32d?q=80&w=2070');"></div>
            <div class="slide" style="background-image: url('https://images.unsplash.com/photo-1436491865332-7a61a109cc05?q=80&w=2074');"></div>
            <div class="slide" style="background-image: url('https://images.unsplash.com/photo-1569154941061-e231b4725ef1?q=80&w=2070');"></div>
        </div>
        <div class="hero-overlay"></div>
        <div class="login-container">
            <h2><i class="fa-solid fa-right-to-bracket"></i> Đăng nhập</h2>
            <c:if test="${not empty error}">
                <div class="error">${error}</div>
            </c:if>
            <c:if test="${not empty message}">
                <div class="message">${message}</div>
            </c:if>

            <form method="post" action="<c:url value='/login'/>" id="loginForm">
                <div class="form-group">
                    <label for="username"><i class="fa-solid fa-user"></i> Tên tài khoản</label>
                    <input type="text" id="username" name="username" placeholder="Tên tài khoản" required value="${param.username}">
                </div>
                <div class="form-group">
                    <label for="password"><i class="fa-solid fa-lock"></i> Mật khẩu</label>
                    <input type="password" id="password" name="password" placeholder="Mật khẩu" required>
                </div>
                <button type="submit" class="btn">Đăng nhập</button>
                <div class="link">
                    Chưa có tài khoản? <a href="<c:url value='/register'/>">Đăng ký ngay</a>
                </div>
            </form>
        </div>
    </section>

    <script>
        document.addEventListener('DOMContentLoaded', function () {
            // Slider
            const slides = document.querySelectorAll('.slider .slide');
            if (slides.length > 0) {
                let currentSlide = 0;
                function showSlide(index) {
                    slides.forEach((slide, i) => {
                        slide.style.opacity = (i === index) ? '1' : '0';
                    });
                }
                function nextSlide() {
                    currentSlide = (currentSlide + 1) % slides.length;
                    showSlide(currentSlide);
                }
                showSlide(0);
                setInterval(nextSlide, 5000);
            }
        });
    </script>
</body>
</html>