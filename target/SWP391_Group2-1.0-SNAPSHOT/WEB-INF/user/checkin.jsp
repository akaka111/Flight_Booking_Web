<%-- 
    Document   : checkin
    Created on : 1 Aug 2025, 02:35:27
    Author     : Admin
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="model.Booking" %>
<%@ page import="model.Flight" %>
<%@ page import="java.time.LocalDateTime" %>
<%@ page import="java.time.format.DateTimeFormatter" %>

<%
    Booking booking = (Booking) request.getAttribute("booking");
    Flight flight = (Flight) request.getAttribute("flight");
    String error = (String) request.getAttribute("error");
    String message = (String) request.getAttribute("message");

    LocalDateTime now = LocalDateTime.now();
    LocalDateTime departureTime = (flight != null) ? flight.getDepartureTime().toLocalDateTime() : null;
    LocalDateTime openTime = (departureTime != null) ? departureTime.minusHours(24) : null;
    LocalDateTime closeTime = (departureTime != null) ? departureTime.minusHours(2) : null;
%>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Check-in Online</title>

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

        .error-message {
            color: red;
            text-align: center;
            margin-top: 15px;
            font-weight: 500;
        }

        .alert {
            padding: 15px;
            border-radius: 5px;
            margin-top: 20px;
            font-weight: 500;
        }

        .alert-success {
            background-color: #d4edda;
            color: #155724;
            border: 1px solid #c3e6cb;
        }

        .alert-error {
            background-color: #f8d7da;
            color: #721c24;
            border: 1px solid #f5c6cb;
        }

        .alert-warning {
            background-color: #fff3cd;
            color: #856404;
            border: 1px solid #ffeeba;
        }

        .checkin-table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
            font-size: 1em;
        }

        .checkin-table th, .checkin-table td {
            padding: 12px 15px;
            border: 1px solid #dee2e6;
            text-align: left;
        }

        .checkin-table th {
            background-color: #f1f1f1;
            font-weight: 600;
            color: #333;
            width: 35%;
        }

        .checkin-table tr:nth-child(even) {
            background-color: #f9f9f9;
        }

        .checkin-table tr:hover {
            background-color: #f1f8ff;
        }

        .btn {
            margin-top: 20px;
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

        .btn:hover {
            background-color: #e65c50;
        }
    </style>
</head>
<body>
<jsp:include page="/WEB-INF/user/components/header.jsp" />

<section class="checkin-section">
    <div class="checkin-container">
        <h2><i class="fa-solid fa-plane"></i> Check-in Online</h2>

        <c:if test="${not empty error}">
            <div class="alert alert-error">${error}</div>
        </c:if>
        <c:if test="${not empty message}">
            <div class="alert alert-success">${message}</div>
        </c:if>

        <c:choose>
            <c:when test="${booking != null && flight != null}">
                <table class="checkin-table">
                    <tr><th>Mã đặt chỗ</th><td>${booking.bookingCode}</td></tr>
                    <tr><th>Chuyến bay</th><td>${flight.flightNumber}</td></tr>
                    <tr><th>Điểm đi</th><td>${flight.routeFrom}</td></tr>
                    <tr><th>Điểm đến</th><td>${flight.routeTo}</td></tr>
                    <tr><th>Giờ khởi hành</th><td><fmt:formatDate value="${flight.departureTime}" pattern="yyyy-MM-dd HH:mm" /></td></tr>
                    <tr><th>Trạng thái check-in</th><td>${booking.checkinStatus}</td></tr>
                </table>

                <% if (now.isBefore(openTime)) { %>
                    <div class="alert alert-warning">
                        Chưa đến giờ check-in. Hệ thống sẽ mở vào lúc <strong><%= openTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")) %></strong>
                    </div>
                <% } else if (now.isAfter(closeTime)) { %>
                    <div class="alert alert-error">
                        Đã hết hạn check-in. Check-in đóng lúc <strong><%= closeTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")) %></strong>
                    </div>
                <% } else if ("Checked-in".equalsIgnoreCase(booking.getCheckinStatus())) { %>
                    <div class="alert alert-success">Bạn đã check-in thành công.</div>
                <% } else { %>
                    <form action="checkinController" method="get">
                        <input type="hidden" name="step" value="confirm" />
                        <input type="hidden" name="bookingId" value="<%= booking.getBookingId() %>" />
                        <button type="submit" class="btn"><i class="fa-solid fa-circle-right"></i> Tiếp tục</button>
                    </form>
                <% } %>
            </c:when>
            <c:otherwise>
                <div class="alert alert-error">Không tìm thấy thông tin đặt chỗ phù hợp.</div>
            </c:otherwise>
        </c:choose>
    </div>
</section>

<jsp:include page="/WEB-INF/user/components/footer.jsp" />
</body>
</html>
