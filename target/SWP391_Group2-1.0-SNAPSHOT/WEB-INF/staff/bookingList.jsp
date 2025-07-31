<%-- 
    Document   : bookingList
    Created on : Jul 19, 2025, 2:02:41 PM
    Author     : Khoa
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Quản Lý Đặt Vé</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        .table { margin-top: 20px; }
        .form-group { margin-bottom: 15px; }
        .error { color: red; }
        .success { color: green; }
    </style>
</head>
<body>
    <div class="container">
        <h2 class="mt-4">Quản Lý Đặt Vé</h2>

        <!-- Thông báo -->
        <c:if test="${not empty error}">
            <div class="alert alert-danger">${error}</div>
        </c:if>
        <c:if test="${not empty success}">
            <div class="alert alert-success">${success}</div>
        </c:if>

        <!-- Form Lọc -->
        <form action="${pageContext.request.contextPath}/staff/booking/list" method="get" class="form-inline mb-3">
            <div class="form-group me-2">
                <label for="statusFilter" class="me-2">Lọc theo trạng thái:</label>
                <select name="statusFilter" id="statusFilter" class="form-select">
                    <option value="">Tất cả</option>
                    <option value="CONFIRMED">Đã xác nhận</option>
                    <option value="CANCELLED">Đã hủy</option>
                    <option value="CHECKED-IN">Đã check-in</option>
                    <option value="BOARDED">Đã lên máy bay</option>
                </select>
            </div>
            <button type="submit" class="btn btn-primary">Lọc</button>
        </form>

        <!-- Form Tìm Kiếm -->
        <form action="${pageContext.request.contextPath}/staff/booking/search" method="get" class="form-inline mb-3">
            <div class="form-group me-2">
                <label for="searchTerm" class="me-2">Tìm kiếm theo Mã Booking, Tên, hoặc Hộ chiếu:</label>
                <input type="text" name="searchTerm" id="searchTerm" class="form-control" value="${param.searchTerm}">
            </div>
            <button type="submit" class="btn btn-primary">Tìm kiếm</button>
        </form>

        <!-- Danh sách Booking -->
        <table class="table table-bordered">
            <thead>
                <tr>
                    <th>Mã Booking</th>
                    <th>Mã Người Dùng</th>
                    <th>Mã Chuyến Bay</th>
                    <th>Thời Gian Đặt</th>
                    <th>Trạng Thái</th>
                    <th>Hạng Ghế</th>
                    <th>Tổng Giá</th>
                    <th>Trạng Thái Check-in</th>
                    <th>Ghi Chú</th>
                    <th>Thao Tác</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="booking" items="${bookings}">
                    <tr>
                        <td>${booking.bookingId}</td>
                        <td>${booking.userId}</td>
                        <td>${booking.flightId}</td>
                        <td>${booking.bookingDate}</td>
                        <td>
                            <c:choose>
                                <c:when test="${booking.status == 'CONFIRMED'}">Đã xác nhận</c:when>
                                <c:when test="${booking.status == 'CANCELLED'}">Đã hủy</c:when>
                                <c:when test="${booking.status == 'CHANGED'}">Đã thay đổi</c:when>
                                <c:otherwise>${booking.status}</c:otherwise>
                            </c:choose>
                        </td>
                        <td>${booking.seatClass}</td>
                        <td>${booking.totalPrice}</td>
                        <td>
                            <c:choose>
                                <c:when test="${booking.checkinStatus == 'NOT CHECKED-IN'}">Chưa check-in</c:when>
                                <c:when test="${booking.checkinStatus == 'CHECKED-IN'}">Đã check-in</c:when>
                                <c:when test="${booking.checkinStatus == 'BOARDED'}">Đã lên máy bay</c:when>
                                <c:otherwise>${booking.checkinStatus}</c:otherwise>
                            </c:choose>
                        </td>
                        <td>${booking.staffNote}</td>
                        <td>
                            <a href="${pageContext.request.contextPath}/staff/booking/details?bookingId=${booking.bookingId}" class="btn btn-info btn-sm">Xem chi tiết</a>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
