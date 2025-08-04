<%-- 
    Document   : checkin-passenger
    Created on : 3 Aug 2025, 15:52:21
    Author     : Admin
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="model.Passenger" %>
<%@ page import="model.Booking" %>

<%
    Booking booking = (Booking) request.getAttribute("booking");
    Passenger passenger = (Passenger) request.getAttribute("passenger");
%>

<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <title>Xác nhận hành khách</title>

        <!-- Google Fonts -->
        <link href="https://fonts.googleapis.com/css2?family=Montserrat:wght@400;500;700&display=swap" rel="stylesheet">

        <!-- Font Awesome (nếu cần) -->
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
                padding: 0;
            }
            .container {
                max-width: 1000px;
                margin: 50px auto;
                background-color: var(--white);
                box-shadow: var(--shadow);
                border-radius: 8px;
                padding: 30px;
            }
            h2 {
                font-size: 1.8em;
                margin-bottom: 20px;
                color: var(--primary-color);
            }
            p {
                font-size: 1em;
                margin-bottom: 20px;
            }
            table {
                width: 100%;
                border-collapse: collapse;
                margin-bottom: 30px;
            }
            th, td {
                padding: 14px;
                border-bottom: 1px solid #ddd;
                text-align: left;
            }
            th {
                background-color: #f0f0f0;
                font-weight: 600;
            }
            .btn {
                padding: 12px 30px;
                background-color: var(--secondary-color);
                color: var(--white);
                border: none;
                border-radius: 5px;
                font-size: 1em;
                cursor: pointer;
                text-decoration: none;
                transition: background-color 0.3s ease;
            }
            .btn:hover {
                background-color: #e65c50;
            }
        </style>
    </head>
    <body>

        <!-- Header đồng bộ nếu có -->
        <header>
            <jsp:include page="/WEB-INF/user/components/header.jsp" />
        </header>

        <div class="container">
            <h2>Xác nhận hành khách</h2>
            <p><strong>Mã đặt chỗ:</strong> <%= booking.getBookingCode()%></p>

            <table>
                <thead>
                    <tr>
                        <th>Họ và tên</th>
                        <th>Giới tính</th>
                        <th>Ngày sinh</th>
                        <th>Quốc tịch</th>
                        <th>Số hộ chiếu</th>
                        <th>Số điện thoại</th>
                        <th>Email</th>
                        <th>Địa chỉ</th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <td><%= passenger.getFullName()%></td>
                        <td><%= passenger.getGender()%></td>
                        <td><%= passenger.getDob()%></td>
                        <td><%= passenger.getCountry()%></td>
                        <td><%= passenger.getPassportNumber()%></td>
                        <td><%= passenger.getPhoneNumber()%></td>
                        <td><%= passenger.getEmail()%></td>
                        <td><%= passenger.getAddress()%></td>
                    </tr>
                </tbody>
            </table>


            <form action="checkinController" method="post">
                <input type="hidden" name="step" value="complete" />
                <input type="hidden" name="bookingId" value="<%= booking.getBookingId()%>" />
                <button type="submit" class="btn"><i class="fa-solid fa-circle-check"></i> Xác nhận & Check-in</button>
            </form>
        </div>

        <!-- Footer đồng bộ nếu có -->
        <footer>
            <jsp:include page="/WEB-INF/user/components/footer.jsp" />
        </footer>

    </body>
</html>
