<%-- 
    Document   : flightList
    Created on : Aug 23, 2025, 3:36:51 PM
    Author     : Khoa
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
    <head>
        <title>Danh Sách Chuyến Bay</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
        <!-- Giữ nguyên style và script từ bookingList.jsp -->
        <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
        <!-- Có thể thêm ajax load flights nếu cần, nhưng giữ đơn giản -->
    </head>
    <body>
        <div class="container">
            <h2 class="mt-4">Danh Sách Chuyến Bay</h2>
            <!-- Có thể thêm form lọc chuyến bay nếu cần (ví dụ theo status, date) -->
            <table class="table table-bordered" id="flightListTable">
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
                            <td><c:out value="${flight.route.originIata != null ? flight.route.originIata : 'N/A'} -> ${flight.route.destIata != null ? flight.route.destIata : 'N/A'}" /></td>
                            <td><fmt:formatDate value="${flight.departureTime}" pattern="dd-MM-yyyy HH:mm:ss"/></td>
                            <td><fmt:formatDate value="${flight.arrivalTime}" pattern="dd-MM-yyyy HH:mm:ss"/></td>
                            <td><c:out value="${flight.status != null ? flight.status : 'N/A'}" /></td>
                            <td>
                                <a href="${pageContext.request.contextPath}/staff/booking/flight/bookings?flightId=${flight.flightId}" class="btn btn-info btn-primary" style="background-color: #007bff; color: white; border: none;">Xem Đặt Vé</a>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>
