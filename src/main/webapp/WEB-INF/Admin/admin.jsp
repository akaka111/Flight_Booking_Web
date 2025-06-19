<%-- 
    Document   : admin
    Created on : Jun 17, 2025, 3:29:17 PM
    Author     : Khoa
--%>


<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List, model.Flight, model.Airline,  model.Voucher, java.text.SimpleDateFormat, java.util.Date" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admin Panel - Flight Booking System</title>
    
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
        .btn-logout {
            background-color: transparent;
            color: var(--primary-color);
            border-color: var(--primary-color);
        }
        .btn-logout:hover {
            background-color: var(--primary-color);
            color: var(--white);
        }

        /* --- HERO SECTION (SLIDER + DASHBOARD) --- */
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
        .dashboard-stats {
            display: flex;
            justify-content: center;
            gap: 20px;
            margin-top: 20px;
        }
        .stat-card {
            background: rgba(255, 255, 255, 0.9);
            padding: 15px;
            border-radius: 8px;
            box-shadow: var(--shadow);
            width: 150px;
            text-align: center;
        }
        .stat-card h3 {
            font-size: 1.2em;
            color: var(--text-color);
        }
        .stat-card p {
            font-size: 1.5em;
            font-weight: 700;
            color: var(--secondary-color);
        }

        /* --- MANAGEMENT SECTION --- */
        .management-section {
            padding: 50px 0;
        }
        .section-title {
            text-align: center;
            font-size: 2em;
            margin-bottom: 40px;
            color: var(--text-color);
        }
        .management-panel {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
            gap: 25px;
        }
        .management-card {
            background: var(--white);
            border-radius: 8px;
            box-shadow: var(--shadow);
            padding: 20px;
            text-align: center;
            transition: all 0.3s ease;
            cursor: pointer;
        }
        .management-card:hover {
            transform: translateY(-5px);
            box-shadow: 0 8px 20px rgba(0,0,0,0.12);
        }
        .management-card i {
            font-size: 2.5em;
            color: var(--primary-color);
            margin-bottom: 10px;
        }
        .management-card a {
            text-decoration: none;
            color: var(--primary-color);
            font-size: 1.2em;
            font-weight: 500;
            display: block;
        }
        .management-card a:hover {
            color: var(--secondary-color);
        }

        /* --- MODAL --- */
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
<body>

    <!-- ==================== HEADER ==================== -->
    <header class="main-header">
        <div class="container">
            <a href="#" class="logo">
                <i class="fa-solid fa-plane-up"></i>
                Admin Panel
            </a>
            <nav class="main-nav">
                <a href="#airlines">Quản lý Hãng bay</a>
                <a href="#accounts">Quản lý Tài khoản</a>
                <a href="#flights">Quản lý Chuyến bay</a>
                <a href="#vouchers">Quản lý Voucher</a>
            </nav>
            <div class="auth-buttons">
                <a href="#" class="btn btn-logout" id="logoutBtn">Đăng xuất</a>
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
            <h1>Quản lý hệ thống đặt vé máy bay</h1>
            <div class="dashboard-stats">
                <div class="stat-card">
                    <h3>Tổng Hãng bay</h3>
                    <p><c:out value="${airlinesCount}" default="0"/></p>
                </div>
                <div class="stat-card">
                    <h3>Tổng Tài khoản</h3>
                    <p><c:out value="${accountsCount}" default="0"/></p>
                </div>
                <div class="stat-card">
                    <h3>Tổng Chuyến bay</h3>
                    <p><c:out value="${flightsCount}" default="0"/></p>
                </div>
            </div>
        </div>
    </section>

    <!-- ==================== MANAGEMENT SECTION ==================== -->
    <section class="management-section">
        <div class="container">
            <h2 class="section-title">Các chức năng quản lý</h2>
            <div class="management-panel">
                <div class="management-card">
                    <i class="fa-solid fa-plane"></i>
                    <a href="ManageAirlineController">Quản lý Hãng bay</a>
                </div>
                <div class="management-card">
                    <i class="fa-solid fa-users"></i>
                    <a href="ManageAccountController">Quản lý Tài khoản</a>
                </div>
                <div class="management-card">
                    <i class="fa-solid fa-calendar-days"></i>
                    <a href="ManageFlightController">Quản lý Chuyến bay</a>
                </div>
                <div class="management-card">
                    <i class="fa-solid fa-ticket"></i>
                    <a href="ManageVoucherController">Quản lý Voucher</a>
                </div>
            </div>
        </div>
    </section>

    <!-- ==================== LOGOUT MODAL ==================== -->
    <div id="logoutModal" class="modal">
        <div class="modal-content">
            <h3>Xác nhận đăng xuất</h3>
            <p>Bạn có chắc muốn đăng xuất?</p>
            <a href="LogoutController" class="btn btn-confirm">Có</a>
            <a href="#" class="btn btn-cancel" id="cancelLogout">Không</a>
        </div>
    </div>

    <!-- JavaScript for Slider and Modal -->
    <script>
        document.addEventListener('DOMContentLoaded', function() {
            // Slider
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

            setInterval(nextSlide, 5000);

            // Modal
            const logoutBtn = document.getElementById('logoutBtn');
            const logoutModal = document.getElementById('logoutModal');
            const cancelLogout = document.getElementById('cancelLogout');

            logoutBtn.addEventListener('click', function(e) {
                e.preventDefault();
                logoutModal.style.display = 'block';
            });

            cancelLogout.addEventListener('click', function(e) {
                e.preventDefault();
                logoutModal.style.display = 'none';
            });

            window.addEventListener('click', function(e) {
                if (e.target === logoutModal) {
                    logoutModal.style.display = 'none';
                }
            });
        });
    </script>
</body>
</html>