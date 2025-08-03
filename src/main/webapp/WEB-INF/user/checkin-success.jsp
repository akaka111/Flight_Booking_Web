<%-- 
    Document   : checkin-success
    Created on : 3 Aug 2025, 16:42:26
    Author     : Admin
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Check-in Thành Công</title>

        <!-- Google Fonts & Font Awesome -->
        <link rel="preconnect" href="https://fonts.googleapis.com">
        <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
        <link href="https://fonts.googleapis.com/css2?family=Montserrat:wght@400;500;700&display=swap" rel="stylesheet">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css"/>

        <!-- Bootstrap (giữ lại nếu dùng layout của Bootstrap) -->
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/bootstrap.min.css">

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
                max-width: 800px;
                background-color: var(--white);
                padding: 40px;
                margin: 60px auto;
                border-radius: 10px;
                box-shadow: var(--shadow);
            }

            h2.text-success {
                color: var(--secondary-color);
                text-align: center;
                font-weight: 700;
            }

            h4 {
                margin-top: 30px;
                color: var(--primary-color);
            }

            table th {
                background-color: var(--primary-color);
                color: white;
            }

            a {
                display: inline-block;
                margin-top: 20px;
                color: var(--primary-color);
                text-decoration: none;
                font-weight: 600;
            }

            a:hover {
                text-decoration: underline;
            }
        </style>
    </head>

    <body>
        <jsp:include page="/WEB-INF/user/components/header.jsp" />

        <main class="container">
            <h2 class="text-success"><i class="fa-solid fa-circle-check"></i> Check-in thành công!</h2>
            <hr>

            <h4>Thông tin chuyến bay</h4>
            <p><strong>Mã đặt chỗ:</strong> ${booking.bookingCode}</p>
            <p><strong>Số hiệu chuyến bay:</strong> ${flight.flightNumber}</p>
            <p><strong>Ngày bay:</strong> <fmt:formatDate value="${flight.departureTime}" pattern="dd/MM/yyyy HH:mm"/></p>
            <p><strong>Điểm đi:</strong> ${flight.routeFrom}</p>
            <p><strong>Điểm đến:</strong> ${flight.routeTo}</p>

            <hr>

            <h4>Danh sách hành khách đã check-in</h4>
            <table class="table table-bordered">
                <thead>
                    <tr>
                        <th>Họ tên</th>
                        <th>Giới tính</th>
                        <th>Ngày sinh</th>
                        <th>Quốc tịch</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="p" items="${passengers}">
                        <tr>
                            <td>${p.fullName}</td>
                            <td>${p.gender}</td>
                            <td><fmt:formatDate value="${p.dob}" pattern="dd/MM/yyyy"/></td>
                            <td>${p.nationality}</td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>

            <a href="home"><i class="fa-solid fa-house"></i> Về Trang Chủ</a>
        </main>

        <jsp:include page="/WEB-INF/user/components/footer.jsp" />
    </body>

</html>
