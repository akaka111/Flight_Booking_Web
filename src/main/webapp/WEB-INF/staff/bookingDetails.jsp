<%-- 
    Document   : bookingDetails
    Created on : Jul 19, 2025, 1:55:40 PM
    Author     : Khoa
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Chi Tiết Booking</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        .section { margin-bottom: 30px; }
        .error { color: red; }
        .success { color: green; }
        .modal-body .form-group { margin-bottom: 15px; }
    </style>
</head>
<body>
    <div class="container">
        <h2 class="mt-4">Chi Tiết Booking #${booking.bookingId}</h2>

        <!-- Thông báo -->
        <c:if test="${not empty error}">
            <div class="alert alert-danger">${error}</div>
        </c:if>
        <c:if test="${not empty success}">
            <div class="alert alert-success">${success}</div>
        </c:if>

        <!-- Thông tin Booking -->
        <div class="section">
            <h4>Thông Tin Booking</h4>
            <table class="table table-bordered">
                <tr><th>Mã Booking</th><td>${booking.bookingId}</td></tr>
                <tr><th>Mã Người Dùng</th><td>${booking.userId}</td></tr>
                <tr><th>Mã Chuyến Bay</th><td>${booking.flightId}</td></tr>
                <tr><th>Thời Gian Đặt</th><td>${booking.bookingDate}</td></tr>
                <tr><th>Trạng Thái</th><td>
                    <c:choose>
                        <c:when test="${booking.status == 'CONFIRMED'}">Đã xác nhận</c:when>
                        <c:when test="${booking.status == 'CANCELLED'}">Đã hủy</c:when>
                        <c:when test="${booking.status == 'CHANGED'}">Đã thay đổi</c:when>
                        <c:otherwise>${booking.status}</c:otherwise>
                    </c:choose>
                </td></tr>
                <tr><th>Hạng Ghế</th><td>${booking.seatClass}</td></tr>
                <tr><th>Tổng Giá</th><td>${booking.totalPrice}</td></tr>
                <tr><th>Trạng Thái Check-in</th><td>
                    <c:choose>
                        <c:when test="${booking.checkinStatus == 'NOT CHECKED-IN'}">Chưa check-in</c:when>
                        <c:when test="${booking.checkinStatus == 'CHECKED-IN'}">Đã check-in</c:when>
                        <c:when test="${booking.checkinStatus == 'BOARDED'}">Đã lên máy bay</c:when>
                        <c:otherwise>${booking.checkinStatus}</c:otherwise>
                    </c:choose>
                </td></tr>
                <tr><th>Ghi Chú Nhân Viên</th><td>${booking.staffNote}</td></tr>
            </table>
        </div>

        <!-- Thông tin Hành Khách -->
        <div class="section">
            <h4>Thông Tin Hành Khách</h4>
            <table class="table table-bordered">
                <tr><th>Họ Tên</th><td>${passenger.fullName}</td></tr>
                <tr><th>Số Hộ Chiếu</th><td>${passenger.passportNumber}</td></tr>
                <tr><th>Ngày Sinh</th><td>${passenger.dob}</td></tr>
                <tr><th>Giới Tính</th><td>${passenger.gender}</td></tr>
                <tr><th>Số Điện Thoại</th><td>${passenger.phoneNumber}</td></tr>
                <tr><th>Email</th><td>${passenger.email}</td></tr>
                <tr><th>Qu Quốc Gia</th><td>${passenger.country}</td></tr>
                <tr><th>Địa Chỉ</th><td>${passenger.address}</td></tr>
            </table>
        </div>

        <!-- Chi tiết Chuyến Bay -->
        <div class="section">
            <h4>Chi Tiết Chuyến Bay</h4>
            <table class="table table-bordered">
                <tr><th>Mã Chuyến Bay</th><td>${flight.flightNumber}</td></tr>
                <tr><th>Điểm Đi</th><td>${flight.routeFrom}</td></tr>
                <tr><th>Điểm Đến</th><td>${flight.routeTo}</td></tr>
                <tr><th>Thời Gian Cất Cánh</th><td>${flight.departureTime}</td></tr>
                <tr><th>Thời Gian Hạ Cánh</th><td>${flight.arrivalTime}</td></tr>
                <tr><th>Máy Bay</th><td>${flight.aircraft}</td></tr>
            </table>
        </div>

        <!-- Nút mở các Form -->
        <div class="section">
            <h4>Thao Tác</h4>
            <button type="button" class="btn btn-primary me-2" data-bs-toggle="modal" data-bs-target="#updateBookingModal">Cập Nhật Booking</button>
            <button type="button" class="btn btn-primary me-2" data-bs-toggle="modal" data-bs-target="#checkInModal">Thực Hiện Check-in</button>
            <button type="button" class="btn btn-primary me-2" data-bs-toggle="modal" data-bs-target="#updatePassengerModal">Cập Nhật Hành Khách</button>
            <button type="button" class="btn btn-danger" data-bs-toggle="modal" data-bs-target="#cancelBookingModal">Hủy Booking</button>
        </div>

        <!-- Modal Cập Nhật Booking -->
        <div class="modal fade" id="updateBookingModal" tabindex="-1" aria-labelledby="updateBookingModalLabel" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="updateBookingModalLabel">Cập Nhật Booking</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body">
                        <form action="${pageContext.request.contextPath}/staff/booking/update" method="post">
                            <input type="hidden" name="bookingId" value="${booking.bookingId}">
                            <div class="form-group">
                                <label for="status">Trạng Thái:</label>
                                <select name="status" id="status" class="form-select">
                                    <option value="CONFIRMED" ${booking.status == 'CONFIRMED' ? 'selected' : ''}>Đã xác nhận</option>
                                    <option value="CANCELLED" ${booking.status == 'CANCELLED' ? 'selected' : ''}>Đã hủy</option>
                                    <option value="CHANGED" ${booking.status == 'CHANGED' ? 'selected' : ''}>Đã thay đổi</option>
                                </select>
                            </div>
                            <div class="form-group">
                                <label for="seatClass">Hạng Ghế:</label>
                                <select name="seatClass" id="seatClass" class="form-select">
                                    <option value="Economy" ${booking.seatClass == 'Economy' ? 'selected' : ''}>Economy</option>
                                    <option value="Deluxe" ${booking.seatClass == 'Deluxe' ? 'selected' : ''}>Deluxe</option>
                                    <option value="SkyBoss" ${booking.seatClass == 'SkyBoss' ? 'selected' : ''}>SkyBoss</option>
                                    <option value="Business" ${booking.seatClass == 'Business' ? 'selected' : ''}>Business</option>
                                </select>
                            </div>
                            <div class="form-group">
                                <label for="totalPrice">Tổng Giá:</label>
                                <input type="number" step="0.01" name="totalPrice" id="totalPrice" class="form-control" value="${booking.totalPrice}" required>
                            </div>
                            <div class="form-group">
                                <label for="staffNote">Ghi Chú Nhân Viên:</label>
                                <input type="text" name="staffNote" id="staffNote" class="form-control" value="${booking.staffNote}">
                            </div>
                            <div class="form-group">
                                <label for="checkinStatus">Trạng Thái Check-in:</label>
                                <select name="checkinStatus" id="checkinStatus" class="form-select">
                                    <option value="NOT CHECKED-IN" ${booking.checkinStatus == 'NOT CHECKED-IN' ? 'selected' : ''}>Chưa check-in</option>
                                    <option value="CHECKED-IN" ${booking.checkinStatus == 'CHECKED-IN' ? 'selected' : ''}>Đã check-in</option>
                                    <option value="BOARDED" ${booking.checkinStatus == 'BOARDED' ? 'selected' : ''}>Đã lên máy bay</option>
                                </select>
                            </div>
                            <button type="submit" class="btn btn-primary mt-3">Cập Nhật Booking</button>
                        </form>
                    </div>
                </div>
            </div>
        </div>

        <!-- Modal Thực Hiện Check-in -->
        <div class="modal fade" id="checkInModal" tabindex="-1" aria-labelledby="checkInModalLabel" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="checkInModalLabel">Thực Hiện Check-in</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body">
                        <form action="${pageContext.request.contextPath}/staff/booking/checkin" method="post">
                            <input type="hidden" name="bookingId" value="${booking.bookingId}">
                            <div class="form-group">
                                <label for="passengerId">Mã Hành Khách:</label>
                                <input type="number" name="passengerId" id="passengerId" class="form-control" required>
                            </div>
                            <div class="form-group">
                                <label for="flightId">Mã Chuyến Bay:</label>
                                <input type="number" name="flightId" id="flightId" class="form-control" value="${booking.flightId}" required>
                            </div>
                            <div class="form-group">
                                <label for="checkinStatus">Trạng Thái Check-in:</label>
                                <select name="checkinStatus" id="checkinStatus" class="form-select">
                                    <option value="CHECKED-IN">Đã check-in</option>
                                    <option value="BOARDED">Đã lên máy bay</option>
                                </select>
                            </div>
                            <button type="submit" class="btn btn-primary mt-3">Thực Hiện Check-in</button>
                        </form>
                    </div>
                </div>
            </div>
        </div>

        <!-- Modal Cập Nhật Hành Khách -->
        <div class="modal fade" id="updatePassengerModal" tabindex="-1" aria-labelledby="updatePassengerModalLabel" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="updatePassengerModalLabel">Cập Nhật Hành Khách</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body">
                        <form action="${pageContext.request.contextPath}/staff/booking/updatePassenger" method="post">
                            <input type="hidden" name="bookingId" value="${booking.bookingId}">
                            <div class="form-group">
                                <label for="passengerId">Mã Hành Khách:</label>
                                <input type="number" name="passengerId" id="passengerId" class="form-control" required>
                            </div>
                            <div class="form-group">
                                <label for="fullName">Họ Tên:</label>
                                <input type="text" name="fullName" id="fullName" class="form-control" value="${passenger.fullName}" required>
                            </div>
                            <div class="form-group">
                                <label for="passportNumber">Số Hộ Chiếu:</label>
                                <input type="text" name="passportNumber" id="passportNumber" class="form-control" value="${passenger.passportNumber}" required>
                            </div>
                            <div class="form-group">
                                <label for="dob">Ngày Sinh:</label>
                                <input type="date" name="dob" id="dob" class="form-control" value="${passenger.dob}" required>
                            </div>
                            <div class="form-group">
                                <label for="gender">Giới Tính:</label>
                                <select name="gender" id="gender" class="form-select">
                                    <option value="Nam" ${passenger.gender == 'Nam' ? 'selected' : ''}>Nam</option>
                                    <option value="Nữ" ${passenger.gender == 'Nữ' ? 'selected' : ''}>Nữ</option>
                                </select>
                            </div>
                            <div class="form-group">
                                <label for="phoneNumber">Số Điện Thoại:</label>
                                <input type="text" name="phoneNumber" id="phoneNumber" class="form-control" value="${passenger.phoneNumber}">
                            </div>
                            <div class="form-group">
                                <label for="email">Email:</label>
                                <input type="email" name="email" id="email" class="form-control" value="${passenger.email}">
                            </div>
                            <div class="form-group">
                                <label for="country">Quốc Gia:</label>
                                <input type="text" name="country" id="country" class="form-control" value="${passenger.country}">
                            </div>
                            <div class="form-group">
                                <label for="address">Địa Chỉ:</label>
                                <input type="text" name="address" id="address" class="form-control" value="${passenger.address}">
                            </div>
                            <button type="submit" class="btn btn-primary mt-3">Cập Nhật Hành Khách</button>
                        </form>
                    </div>
                </div>
            </div>
        </div>

        <!-- Modal Hủy Booking -->
        <div class="modal fade" id="cancelBookingModal" tabindex="-1" aria-labelledby="cancelBookingModalLabel" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="cancelBookingModalLabel">Hủy Booking</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body">
                        <form action="${pageContext.request.contextPath}/staff/booking/cancel" method="post">
                            <input type="hidden" name="bookingId" value="${booking.bookingId}">
                            <div class="form-group">
                                <label for="reason">Lý Do Hủy:</label>
                                <input type="text" name="reason" id="reason" class="form-control" required>
                            </div>
                            <button type="submit" class="btn btn-danger mt-3">Hủy Booking</button>
                        </form>
                    </div>
                </div>
            </div>
        </div>

        <!-- Lịch Sử Xử Lý -->
        <div class="section">
            <h4>Lịch Sử Xử Lý</h4>
            <table class="table table-bordered">
                <thead>
                    <tr>
                        <th>Mã Lịch Sử</th>
                        <th>Hành Động</th>
                        <th>Mô Tả</th>
                        <th>Mã Nhân Viên</th>
                        <th>Thời Gian</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="history" items="${history}">
                        <tr>
                            <td>${history.historyId}</td>
                            <td>
                                <c:choose>
                                    <c:when test="${history.action == 'CẬP NHẬT'}">Cập nhật</c:when>
                                    <c:when test="${history.action == 'HỦY'}">Hủy</c:when>
                                    <c:when test="${history.action == 'CHECKED-IN'}">Check-in</c:when>
                                    <c:when test="${history.action == 'BOARDED'}">Lên máy bay</c:when>
                                    <c:when test="${history.action == 'CẬP NHẬT HÀNH KHÁCH'}">Cập nhật hành khách</c:when>
                                    <c:otherwise>${history.action}</c:otherwise>
                                </c:choose>
                            </td>
                            <td>${history.description}</td>
                            <td>${history.staffId}</td>
                            <td>${history.actionTime}</td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>

        <a href="${pageContext.request.contextPath}/staff/booking/list" class="btn btn-secondary">Quay Lại Danh Sách</a>
    </div>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
