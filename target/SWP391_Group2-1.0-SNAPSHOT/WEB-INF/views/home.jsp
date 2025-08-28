
<%-- 
    Document   : home
    Created on : 17-Jun-2025
    Author     : Admin
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
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
                min-height: 100vh;
                display: flex;
                flex-direction: column;
            }
            .container {
                max-width: 1200px;
                margin: 0 auto;
                padding: 0 20px;
            }
            .main-content {
                flex: 1;
            }
            /* Hero Section (Slider + Search) */
            .hero-section {
                position: relative;
                height: 60vh;
                min-height: 450px;
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
                text-shadow: 0 2px 4px rgba(0,0,0,0.5);
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
                flex-wrap: wrap;
                justify-content: center;
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
            /* Featured Deals Section */
            .featured-deals-section {
                padding: 60px 0;
                background-color: #e3f2fd;
                position: relative;
                overflow: hidden;
            }
            .cloud-border {
                position: absolute;
                top: 0;
                left: 0;
                width: 100%;
                height: 40px;
                background-image: radial-gradient(circle at 50% 100%, transparent 20px, #4a2d6e 21px);
                background-size: 50px 40px;
                background-repeat: repeat-x;
                opacity: 0.8;
            }
            .section-title {
                text-align: center;
                font-size: 2.2em;
                margin-bottom: 40px;
                color: var(--text-color);
                font-weight: 700;
            }
            .deals-container {
                display: flex;
                justify-content: center;
                gap: 40px;
                flex-wrap: wrap;
            }
            .deal-card {
                text-decoration: none;
                color: var(--text-color);
                transition: transform 0.3s ease;
            }
            .deal-card:hover {
                transform: scale(1.05);
            }
            .deal-card-inner {
                width: 200px;
                background-color: #ffeb3b;
                border-radius: 40px 40px 10px 10px;
                padding: 15px;
                text-align: center;
                box-shadow: 0 4px 12px rgba(0,0,0,0.15);
                position: relative;
            }
            .deal-card-inner::after {
                content: '';
                position: absolute;
                bottom: -15px;
                left: 50%;
                transform: translateX(-50%);
                width: 0;
                height: 0;
                border-left: 15px solid transparent;
                border-right: 15px solid transparent;
                border-top: 15px solid #ffeb3b;
            }
            .from-location {
                font-size: 1em;
                font-weight: 500;
                margin-bottom: 5px;
            }
            .from-location span {
                color: #e53935;
                font-weight: 700;
            }
            .price-tag {
                font-size: 0.8em;
                color: #555;
            }
            .price-amount {
                font-size: 2.2em;
                font-weight: 700;
                color: #e53935;
                line-height: 1;
            }
            .price-currency {
                font-size: 0.9em;
                color: #333;
            }
            .to-location {
                margin-top: 25px;
                font-size: 1em;
                font-weight: 500;
                text-align: center;
            }
            .to-location span {
                color: #e53935;
                font-weight: 700;
            }
            .no-flights {
                text-align: center;
                padding: 40px;
                font-size: 1.2em;
                color: #6c757d;
            }
        </style>
    </head>
    <body>
        <jsp:include page="/WEB-INF/user/components/header.jsp" />
        <div class="main-content">
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
                        <form action="<c:url value='/search'/>" method="get" class="search-form">
                            <div class="form-group">
                                <label for="from"><i class="fa-solid fa-plane-departure"></i> Điểm đi</label>
                                <input type="text" id="from" name="from" placeholder="Thành phố, sân bay..." required>
                            </div>
                            <div class="form-group">
                                <label for="to"><i class="fa-solid fa-plane-arrival"></i> Điểm đến</label>
                                <input type="text" id="to" name="to" placeholder="Thành phố, sân bay..." required>
                            </div>
                            <div class="form-group">
                                <label for="departureDate"><i class="fa-solid fa-calendar-days"></i> Ngày đi</label>
                                <%
                                    java.text.SimpleDateFormat todayFormat = new java.text.SimpleDateFormat("yyyy-MM-dd");
                                    String today = todayFormat.format(new java.util.Date());
                                %>
                                <input type="date" id="departureDate" name="departureDate" required min="<%= today%>">

                            </div>
                            <button type="submit" class="btn-search"><i class="fa-solid fa-magnifying-glass"></i> Tìm kiếm</button>
                        </form>
                    </div>
                </div>
            </section>

           
        </div>
        <footer>
            <jsp:include page="/WEB-INF/user/components/footer.jsp" /> 
        </footer>

        <script>
            document.addEventListener('DOMContentLoaded', function () {
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