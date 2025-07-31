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
<html>
    <head>
        <meta charset="UTF-8">
        <title>Check-in Online</title>
        <link href="https://fonts.googleapis.com/css2?family=Montserrat:wght@400;600&display=swap" rel="stylesheet">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css"/>

        <style>
            body {
                font-family: 'Montserrat', sans-serif;
                background-color: #f8f9fa;
                color: #343a40;
                padding: 50px 20px;
            }

            .checkin-container {
                max-width: 700px;
                margin: 0 auto;
                background: #fff;
                padding: 30px;
                border-radius: 10px;
                box-shadow: 0 10px 30px rgba(0,0,0,0.1);
            }

            h2 {
                text-align: center;
                margin-bottom: 30px;
                color: #007bff;
            }

            table {
                width: 100%;
                border-collapse: collapse;
                margin-bottom: 20px;
            }

            td {
                padding: 12px;
                border: 1px solid #dee2e6;
            }

            .alert {
                padding: 15px;
                border-radius: 5px;
                margin-bottom: 20px;
            }

            .alert-error {
                background-color: #f8d7da;
                color: #721c24;
            }

            .alert-success {
                background-color: #d4edda;
                color: #155724;
            }

            .alert-warning {
                background-color: #fff3cd;
                color: #856404;
            }

            .btn-checkin {
                display: block;
                width: 100%;
                background-color: #28a745;
                color: #fff;
                padding: 12px;
                font-size: 1.1em;
                border: none;
                border-radius: 5px;
                cursor: pointer;
                transition: background-color 0.3s ease;
            }

            .btn-checkin:hover {
                background-color: #218838;
            }

            .text-center {
                text-align: center;
            }
        </style>
    </head>
    <body>
        <header>
            <jsp:include page="/WEB-INF/user/components/header.jsp" /> 
        </header>

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
                    <table>
                        <tr><td>Mã đặt chỗ:</td><td><%= booking.getBookingCode()%></td></tr>
                        <tr><td>Chuyến bay:</td><td><%= flight.getFlightNumber()%></td></tr>
                        <tr><td>Điểm đi:</td><td><%= flight.getRouteFrom()%></td></tr>
                        <tr><td>Điểm đến:</td><td><%= flight.getRouteTo()%></td></tr>
                        <tr><td>Giờ khởi hành:</td>
                            <td><%= departureTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))%></td>
                        </tr>
                        <tr><td>Trạng thái check-in:</td><td><%= booking.getCheckinStatus()%></td></tr>
                    </table>

                    <%
                        if (now.isBefore(openTime)) {
                    %>
                    <div class="alert alert-warning">Chưa đến giờ check-in. Hệ thống sẽ mở vào lúc <strong><%= openTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))%></strong></div>
                    <%
                    } else if (now.isAfter(closeTime)) {
                    %>
                    <div class="alert alert-error">Đã hết hạn check-in. Check-in đóng lúc <strong><%= closeTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))%></strong></div>
                    <%
                    } else if ("Checked-in".equalsIgnoreCase(booking.getCheckinStatus())) {
                    %>
                    <div class="alert alert-success">Bạn đã check-in thành công.</div>
                    <%
                    } else {
                    %>
                    <form action="checkinController" method="post">
                        <input type="hidden" name="bookingId" value="<%= booking.getBookingId()%>"/>
                        <button type="submit" class="btn-checkin">Check-in ngay</button>
                    </form>
                    <%
                        }
                    %>
                </c:when>
                <c:otherwise>
                    <div class="alert alert-error">Không tìm thấy thông tin đặt chỗ phù hợp.</div>
                </c:otherwise>
            </c:choose>
        </div>

</html>
