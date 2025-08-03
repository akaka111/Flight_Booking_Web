<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Quản lý Booking - Flight Booking Web</title>

    <!-- Google Fonts & Font Awesome -->
    <link href="https://fonts.googleapis.com/css2?family=Montserrat:wght@400;500;700&display=swap" rel="stylesheet">
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

        body {
            font-family: 'Montserrat', sans-serif;
            background-color: var(--light-gray);
            color: var(--text-color);
            margin: 0;
            padding: 0;
        }

        .container {
            max-width: 1100px;
            margin: 40px auto;
            background: var(--white);
            box-shadow: var(--shadow);
            border-radius: 10px;
            padding: 30px;
        }

        h2 {
            text-align: center;
            margin-bottom: 30px;
        }

        table {
            width: 100%;
            border-collapse: collapse;
        }

        th, td {
            padding: 12px 15px;
            border: 1px solid #ddd;
            text-align: center;
        }

        th {
            background-color: var(--primary-color);
            color: var(--white);
        }

        tr:nth-child(even) {
            background-color: #f2f2f2;
        }

        .btn-cancel {
            background-color: var(--secondary-color);
            color: white;
            border: none;
            padding: 8px 16px;
            border-radius: 5px;
            cursor: pointer;
            transition: background-color 0.3s;
        }

        .btn-cancel:hover {
            background-color: #e65c50;
        }

        .status {
            font-weight: 600;
        }

        .status.Cancelled {
            color: red;
        }

        .status.Confirmed {
            color: green;
        }
    </style>
</head>
<body>

<jsp:include page="/WEB-INF/user/components/header.jsp" />

<div class="container">
    <h2><i class="fa-solid fa-ticket"></i> Danh sách đặt chỗ của bạn</h2>
    <c:choose>
        <c:when test="${not empty bookings}">
            <table>
                <thead>
                    <tr>
                        <th>Mã đặt chỗ</th>
                        <th>Ngày đặt</th>
                        <th>Trạng thái</th>
                        <th>Giá</th>
                        <th>Hạng ghế</th>
                        <th>Điểm đi</th>
                        <th>Điểm đến</th>
                        <th>Thao tác</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="b" items="${bookings}">
                        <c:set var="flight" value="${flightMap[b.flightId]}" />
                        
                        <tr>
                            <td>${b.bookingCode}</td>
                            <td>
                                <fmt:formatDate value="${b.bookingDate}" pattern="dd-MM-yyyy HH:mm" />
                            </td>
                            <td class="status ${b.status}">${b.status}</td>
                            <td>
                                <fmt:formatNumber value="${b.totalPrice}" type="number" maxFractionDigits="0" groupingUsed="true"/> VND
                            </td>
                            <td>${b.seatClass}</td>
                            <td>${flight.routeFrom}</td>
                            <td>${flight.routeTo}</td>
                            <td>
                                <c:if test="${b.status != 'Cancelled'}">
                                    <form method="post" action="manageBooking">
                                        <input type="hidden" name="bookingId" value="${b.bookingId}" />
                                        <button type="submit" class="btn-cancel">Hủy</button>
                                    </form>
                                </c:if>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </c:when>
        <c:otherwise>
            <p style="text-align:center; font-size: 1.2em; color: #6c757d;">Bạn chưa có đặt chỗ nào.</p>
        </c:otherwise>
    </c:choose>
</div>

<jsp:include page="/WEB-INF/user/components/footer.jsp" />

</body>
</html>
