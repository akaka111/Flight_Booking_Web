<%-- 
    Document   : checkinSearch
    Created on : 1 Aug 2025
    Author     : Admin
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <title>Check-in Online - Flight Booking Web</title>

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

            .main-header {
                background-color: var(--white);
                box-shadow: var(--shadow);
                padding: 15px 0;
                position: sticky;
                top: 0;
                z-index: 1000;
            }

            .main-header .container {
                max-width: 1200px;
                margin: 0 auto;
                padding: 0 20px;
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

            /* === Check-in Section === */
            .checkin-section {
                padding: 80px 20px;
            }

            .checkin-container {
                max-width: 600px;
                margin: 0 auto;
                padding: 40px;
                background-color: var(--white);
                border-radius: 10px;
                box-shadow: var(--shadow);
                border: 1px solid #dee2e6;
            }

            .checkin-container h2 {
                text-align: center;
                color: var(--primary-color);
                margin-bottom: 25px;
            }

            label {
                display: block;
                margin-bottom: 8px;
                font-weight: 500;
            }

            input[type="text"] {
                width: 100%;
                padding: 12px;
                border: 1px solid #ccc;
                border-radius: 5px;
                margin-bottom: 20px;
                font-size: 1em;
            }

            input[type="submit"] {
                width: 100%;
                padding: 12px;
                background-color: var(--secondary-color);
                color: var(--white);
                border: none;
                border-radius: 5px;
                font-size: 1em;
                font-weight: 600;
                cursor: pointer;
                transition: background-color 0.3s ease;
            }

            input[type="submit"]:hover {
                background-color: #e65c50;
            }

            .error-message {
                color: red;
                text-align: center;
                margin-top: 15px;
                font-weight: 500;
            }
        </style>
    </head>
    <body>

        <!-- ===== HEADER ===== -->
        <header>
            <jsp:include page="/WEB-INF/user/components/header.jsp" /> 
        </header>

        <!-- ===== CHECK-IN SEARCH SECTION ===== -->
        <section class="checkin-section">
            <div class="checkin-container">
                <h2>Check-in Online</h2>
                <form action="checkin-search" method="get">
                    <label for="bookingCode"><i class="fa-solid fa-ticket"></i> Nhập mã đặt chỗ (Booking Code):</label>
                    <input type="text" name="bookingCode" id="bookingCode" required placeholder="VD: ABC123" />
                    <input type="submit" value="Tìm kiếm đặt chỗ" />
                </form>

                <c:if test="${not empty error}">
                    <p class="error-message">${error}</p>
                </c:if>
            </div>
        </section>

        <!-- ===== FOOTER (optional) ===== -->
        <footer>
            <jsp:include page="/WEB-INF/user/components/footer.jsp" />
        </footer>
    </body>
</html>
