<%-- 
    Document   : home
    Created on : 17-Jun-2025
    Author     : Admin
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List, model.Flight, java.text.SimpleDateFormat" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Flight Booking Web - Đặt vé máy bay giá rẻ</title>
    
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
            --text-color: #343a40;
            --light-gray: #f8f9fa;
            --white: #ffffff;
            --shadow: 0 5px 15px rgba(0,0,0,0.1);
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
        }

        .container {
            max-width: 1200px;
            margin: 0 auto;
            padding: 0 20px;
        }

        /* --- HEADER --- */
        .main-header {
            background-color: var(--white);
            box-shadow: var(--shadow);
            padding: 15px 0;
            position: sticky;
            top: 0;
            z-index: 1000;
        }

        .main-header .container {
            display: flex;
            justify-content: space-between;
            align-items: center;
        }

        .logo {
            font-size: 1.8em;
            font-weight: 700;
            color: var(--primary-color);
            text-decoration: none;
        }
        .logo i {
            margin-right: 10px;
        }

        .main-nav a {
            color: var(--text-color);
            text-decoration: none;
            margin: 0 15px;
            font-weight: 500;
            transition: color 0.3s ease;
        }
        .main-nav a:hover {
            color: var(--primary-color);
        }

        .auth-buttons .btn {
            padding: 10px 20px;
            border-radius: 5px;
            text-decoration: none;
            margin-left: 10px;
            transition: all 0.3s ease;
            border: 1px solid transparent;
        }
        .btn-login {
            background-color: transparent;
            color: var(--primary-color);
            border-color: var(--primary-color);
        }
        .btn-login:hover {
            background-color: var(--primary-color);
            color: var(--white);
        }
        .btn-register {
            background-color: var(--primary-color);
            color: var(--white);
        }
         .btn-register:hover {
            background-color: #0056b3;
        }

        /* --- HERO SECTION (SLIDER + SEARCH) --- */
        .hero-section {
            position: relative;
            height: 60vh;
            color: var(--white);
            display: flex;
            align-items: center;
            justify-content: center;
            text-align: center;
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
        .hero-content {
            position: relative;
            z-index: 3;
        }
        .hero-content h1 {
            font-size: 3em;
            margin-bottom: 20px;
        }

        .search-form-container {
            background-color: rgba(255,255,255,0.9);
            padding: 30px;
            border-radius: 10px;
            box-shadow: 0 10px 30px rgba(0,0,0,0.2);
            backdrop-filter: blur(5px);
        }
        .search-form {
            display: flex;
            gap: 15px;
            align-items: flex-end;
        }
        .form-group {
            display: flex;
            flex-direction: column;
        }
        .form-group label {
            font-size: 0.9em;
            color: #555;
            margin-bottom: 5px;
            text-align: left;
        }
        .form-group input {
            padding: 12px;
            border: 1px solid #ccc;
            border-radius: 5px;
            font-size: 1em;
        }
        .btn-search {
            padding: 12px 30px;
            font-size: 1.1em;
            background-color: var(--secondary-color);
            color: var(--white);
            border: none;
            border-radius: 5px;
            cursor: pointer;
            transition: background-color 0.3s;
        }
        .btn-search:hover {
            background-color: #e65c50;
        }

        /* --- FLIGHT LIST SECTION --- */
        .flight-results-section {
            padding: 50px 0;
        }
        .section-title {
            text-align: center;
            font-size: 2em;
            margin-bottom: 40px;
            color: var(--text-color);
        }
        .flight-list {
            display: grid;
            grid-template-columns: 1fr; /* Default to 1 column */
            gap: 25px;
        }
        .flight-card {
            display: flex;
            background: var(--white);
            border-radius: 8px;
            box-shadow: var(--shadow);
            transition: all 0.3s ease;
        }
        .flight-card:hover {
            transform: translateY(-5px);
            box-shadow: 0 8px 20px rgba(0,0,0,0.12);
        }
        .airline-logo {
            flex: 0 0 100px;
            display: flex;
            align-items: center;
            justify-content: center;
            background-color: #e9ecef;
            border-right: 1px solid #dee2e6;
        }
         .airline-logo i {
            font-size: 2.5em;
            color: var(--primary-color);
        }
        .flight-details {
            flex-grow: 1;
            padding: 20px;
            display: flex;
            justify-content: space-between;
            align-items: center;
        }
        .flight-route, .flight-time, .flight-price {
            display: flex;
            flex-direction: column;
        }
        .flight-route {
            flex-basis: 40%;
        }
        .flight-time {
            flex-basis: 30%;
        }
        .flight-price {
            flex-basis: 30%;
            text-align: right;
        }
        .route-path {
            font-size: 1.2em;
            font-weight: 500;
        }
        .route-path .fa-plane {
            margin: 0 10px;
            color: var(--secondary-color);
        }
        .flight-time-value {
            font-size: 1.2em;
            font-weight: 500;
        }
        .price-value {
            font-size: 1.5em;
            font-weight: 700;
            color: var(--secondary-color);
        }
        .label {
            font-size: 0.8em;
            color: #6c757d;
        }
        .btn-book {
            padding: 12px 25px;
            background-color: var(--primary-color);
            color: var(--white);
            text-decoration: none;
            border-radius: 5px;
            transition: background-color 0.3s;
        }
         .btn-book:hover {
            background-color: #0056b3;
        }
    </style>
</head>
<body>

    <!-- ==================== HEADER ==================== -->
    <header class="main-header">
        <div class="container">
            <a href="#" class="logo">
                <i class="fa-solid fa-plane-up"></i>
                Flight Booking Web
            </a>
            <nav class="main-nav">
                <a href="#">Trang Chủ</a>
                <a href="#">Khuyến Mãi</a>
                <a href="#">Quản Lý Đặt Chỗ</a>
                <a href="#">Liên Hệ</a>
            </nav>
            <div class="auth-buttons">
                <a href="#" class="btn btn-login">Đăng Nhập</a>
                <a href="#" class="btn btn-register">Đăng Ký</a>
            </div>
        </div>
    </header>

    <!-- ==================== HERO SECTION ==================== -->
    <section class="hero-section">
        <div class="slider">
            <div class="slide active" style="background-image: url('https://images.unsplash.com/photo-1530521954074-e64f6810b32d?q=80&w=2070');"></div>
            <div class="slide" style="background-image: url('https://images.unsplash.com/photo-1436491865332-7a61a109cc05?q=80&w=2074');"></div>
            <div class="slide" style="background-image: url('https://images.unsplash.com/photo-1569154941061-e231b4725ef1?q=80&w=2070');"></div>
        </div>
        <div class="hero-overlay"></div>
        <div class="hero-content container">
            <h1>Chuyến đi trong mơ, trong tầm tay bạn</h1>
            <div class="search-form-container">
                <form action="SearchController" method="get" class="search-form">
                    <div class="form-group">
                        <label for="from">Điểm đi</label>
                        <input type="text" id="from" name="from" placeholder="Thành phố, sân bay..." required>
                    </div>
                    <div class="form-group">
                        <label for="to">Điểm đến</label>
                        <input type="text" id="to" name="to" placeholder="Thành phố, sân bay..." required>
                    </div>
                    <div class="form-group">
                        <label for="departureDate">Ngày đi</label>
                        <input type="date" id="departureDate" name="departureDate" required>
                    </div>
                    <button type="submit" class="btn-search"><i class="fa-solid fa-magnifying-glass"></i> Tìm kiếm</button>
                </form>
            </div>
        </div>
    </section>

    <!-- ==================== FLIGHT LIST SECTION ==================== -->
    <section class="flight-results-section">
        <div class="container">
            <h2 class="section-title">Các chuyến bay nổi bật</h2>
            
            <div class="flight-list">
                <c:choose>
                    <c:when test="${not empty flights}">
                        <c:forEach var="flight" items="${flights}">
                            <div class="flight-card">
                                <div class="airline-logo">
                                    <i class="fa-solid fa-paper-plane"></i>
                                </div>
                                <div class="flight-details">
                                    <div class="flight-route">
                                        <div class="label">${flight.flightNumber} - ${flight.airlineId}</div>
                                        <div class="route-path">
                                            <span>${flight.routeFrom}</span>
                                            <i class="fa-solid fa-plane"></i>
                                            <span>${flight.routeTo}</span>
                                        </div>
                                    </div>
                                    <div class="flight-time">
                                        <div class="label">Khởi hành</div>
                                        <div class="flight-time-value">
                                            <fmt:formatDate value="${flight.departureTime}" pattern="HH:mm, dd/MM/yyyy" />
                                        </div>
                                    </div>
                                    <div class="flight-price">
                                        <div class="label">Giá chỉ từ</div>
                                        <div class="price-value">
                                            <fmt:formatNumber value="${flight.price}" type="currency" currencyCode="VND" />
                                        </div>
                                        <a href="FlightDetailController?flightId=${flight.flightId}" class="btn-book">Đặt ngay</a>
                                    </div>
                                </div>
                            </div>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <div class="no-flights">
                            <p>Hiện không có chuyến bay nào.</p>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </section>

    <!-- JavaScript for Slider -->
    <script>
        document.addEventListener('DOMContentLoaded', function() {
            const slides = document.querySelectorAll('.slider .slide');
            let currentSlide = 0;

            function showSlide(index) {
                slides.forEach((slide, i) => {
                    slide.classList.remove('active');
                    if (i === index) {
                        slide.classList.add('active');
                    }
                });
            }

            function nextSlide() {
                currentSlide = (currentSlide + 1) % slides.length;
                showSlide(currentSlide);
            }

            setInterval(nextSlide, 5000); // Change slide every 5 seconds
        });
    </script>
</body>
</html>