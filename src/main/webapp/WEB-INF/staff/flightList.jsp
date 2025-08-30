<%-- 
    Document   : flightList
    Created on : Aug 23, 2025, 3:36:51 PM
    Author     : Khoa
--%>

<%@page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <title>Danh Sách Chuyến Bay</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
        <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
        <style>
            body {
                background-color: #f8f9fa;
            }
            h2 {
                color: #333;
                font-weight: 600;
            }
            .table thead {
                background-color: #0d6efd;
                color: #fff;
            }
            .btn-primary-custom {
                background-color: #0d6efd; /* xanh dương sáng */
                border: none;
                color: #fff;
            }
            .btn-primary-custom:hover {
                background-color: #1a8cff; /* xanh dương đậm hơn khi hover */
                color: #fff;
            }
        </style>
    </head>
    <body>
        <div class="container my-4">
            <h2 class="mb-4 text-center">Danh Sách Chuyến Bay</h2>

            <div class="card shadow-sm">
                <div class="card-body">
                    <table class="table table-hover align-middle text-center">
                        <thead>
                            <tr>
                                <th>Mã Chuyến Bay</th>
                                <th>Hãng Hàng Không</th>
                                <th>Tuyến Đường</th>
                                <th>Thời Gian Khởi Hành</th>
                                <th>Thời Gian Đến</th>
                                <th>Trạng Thái</th>
                                <th>Thao Tác</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="flight" items="${flights}">
                                <tr>
                                    <td><c:out value="${flight.flightNumber != null ? flight.flightNumber : 'N/A'}" /></td>
                                    <td><c:out value="${flight.airline.name != null ? flight.airline.name : 'N/A'}" /></td>
                                    <td>
                                        <c:out value="${flight.route.originIata != null ? flight.route.originIata : 'N/A'}" />
                                        →
                                        <c:out value="${flight.route.destIata != null ? flight.route.destIata : 'N/A'}" />
                                    </td>
                                    <td><fmt:formatDate value="${flight.departureTime}" pattern="dd-MM-yyyy HH:mm:ss"/></td>
                                    <td><fmt:formatDate value="${flight.arrivalTime}" pattern="dd-MM-yyyy HH:mm:ss"/></td>
                                    <td>
                                        <span class="badge bg-${flight.status == 'ON TIME' ? 'success' : flight.status == 'DELAYED' ? 'danger' : 'secondary'}">
                                            <c:out value="${flight.status != null ? flight.status : 'N/A'}" />
                                        </span>
                                    </td>
                                    <td>
                                        <a href="${pageContext.request.contextPath}/staff/booking/flight/bookings?flightId=${flight.flightId}" 
                                           class="btn btn-sm btn-primary-custom">
                                            Xem Đặt Vé
                                        </a>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>
