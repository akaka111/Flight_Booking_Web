<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Quản Lý Đặt Vé</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        .table { margin-top: 20px; }
        .form-group { margin-bottom: 15px; }
        .alert { position: fixed; top: 20px; left: 50%; transform: translateX(-50%); z-index: 1000; }
        .btn-info { background-color: #0d6efd !important; color: #fff !important; border-color: #0d6efd !important; }
        .btn-info:hover { background-color: #0b5ed7 !important; border-color: #0a58ca !important; }
    </style>
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script>
        $(document).ready(function() {
            // Kiểm tra xem trang có đang trong iframe không
            if (window.self !== window.top) {
                console.log("Trang bookingList.jsp đang trong iframe, không gọi loadBookings.");
            } else {
                console.log("Trang bookingList.jsp không trong iframe, gọi loadBookings.");
                loadBookings();
            }

            // Xử lý thông báo ban đầu từ server
            <c:if test="${not empty error}">
                showAlert("${error}", "danger");
            </c:if>
            <c:if test="${not empty success}">
                showAlert("${success}", "success");
            </c:if>

            function showAlert(message, type) {
                var alertDiv = $('<div class="alert alert-' + type + ' alert-dismissible fade show" role="alert">' +
                    message +
                    '<button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>' +
                    '</div>');
                $("body").prepend(alertDiv);
                setTimeout(function() {
                    alertDiv.alert('close');
                }, 3000);
            }

            function loadBookings(statusFilter = '') {
                console.log("Gọi AJAX tới /staff/booking/list với statusFilter: " + statusFilter);
                $.ajax({
                    url: '${pageContext.request.contextPath}/staff/booking/list',
                    type: 'GET',
                    data: { statusFilter: statusFilter },
                    dataType: 'html',
                    success: function(response) {
                        console.log("Phản hồi AJAX thành công, cập nhật tbody.");
                        $("#bookingListTable tbody").html($(response).find("#bookingListTable tbody").html());
                        showAlert("Tải danh sách booking thành công!", "success");
                    },
                    error: function(xhr) {
                        console.log("Lỗi AJAX: " + xhr.status + " - " + xhr.responseText);
                        showAlert("Tải danh sách booking thất bại: " + xhr.responseText, "danger");
                    }
                });
            }

            // Xử lý form lọc
            $("form[action='${pageContext.request.contextPath}/staff/booking/list']").submit(function(e) {
                e.preventDefault();
                console.log("Form lọc được gửi, gọi AJAX.");
                var formData = $(this).serialize();
                $.ajax({
                    url: '${pageContext.request.contextPath}/staff/booking/list',
                    type: 'GET',
                    data: formData,
                    dataType: 'html',
                    success: function(response) {
                        console.log("Lọc thành công, cập nhật tbody.");
                        $("#bookingListTable tbody").html($(response).find("#bookingListTable tbody").html());
                        showAlert("Lọc thành công!", "success");
                    },
                    error: function(xhr) {
                        console.log("Lỗi lọc AJAX: " + xhr.status + " - " + xhr.responseText);
                        showAlert("Lọc thất bại: " + xhr.responseText, "danger");
                    }
                });
            });

            // Xử lý form tìm kiếm
            $("form[action='${pageContext.request.contextPath}/staff/booking/search']").submit(function(e) {
                e.preventDefault();
                console.log("Form tìm kiếm được gửi, gọi AJAX.");
                var formData = $(this).serialize();
                $.ajax({
                    url: '${pageContext.request.contextPath}/staff/booking/search',
                    type: 'GET',
                    data: formData,
                    dataType: 'html',
                    success: function(response) {
                        console.log("Tìm kiếm thành công, cập nhật tbody.");
                        $("#bookingListTable tbody").html($(response).find("#bookingListTable tbody").html());
                        showAlert("Tìm kiếm thành công!", "success");
                    },
                    error: function(xhr) {
                        console.log("Lỗi tìm kiếm AJAX: " + xhr.status + " - " + xhr.responseText);
                        showAlert("Tìm kiếm thất bại: " + xhr.responseText, "danger");
                    }
                });
            });
        });
    </script>
</head>
<body>
    <div class="container">
        <h2 class="mt-4">Quản Lý Đặt Vé</h2>
        <form action="${pageContext.request.contextPath}/staff/booking/list" method="get" class="form-inline mb-3">
            <div class="form-group me-2">
                <label for="statusFilter" class="me-2">Lọc theo trạng thái:</label>
                <select name="statusFilter" id="statusFilter" class="form-select">
                    <option value="">Tất cả</option>
                    <option value="CONFIRMED" ${param.statusFilter == 'CONFIRMED' ? 'selected' : ''}>Đã xác nhận</option>
                    <option value="CANCELLED" ${param.statusFilter == 'CANCELLED' ? 'selected' : ''}>Đã hủy</option>
                    <option value="CHECKED-IN" ${param.statusFilter == 'CHECKED-IN' ? 'selected' : ''}>Đã check-in</option>
                    <option value="BOARDED" ${param.statusFilter == 'BOARDED' ? 'selected' : ''}>Đã lên máy bay</option>
                </select>
            </div>
            <button type="submit" class="btn btn-primary">Lọc</button>
        </form>
        <form action="${pageContext.request.contextPath}/staff/booking/search" method="get" class="form-inline mb-3">
            <div class="form-group me-2">
                <label for="searchTerm" class="me-2">Tìm kiếm theo Mã Booking, Tên, hoặc Hộ chiếu:</label>
                <input type="text" name="searchTerm" id="searchTerm" class="form-control" value="${param.searchTerm}">
            </div>
            <button type="submit" class="btn btn-primary">Tìm kiếm</button>
        </form>
        <table class="table table-bordered" id="bookingListTable">
            <thead>
                <tr>
                    <th>Mã Đặt Chỗ</th>
                    <th>Tên Người Dùng</th>
                    <th>Số Hiệu Chuyến Bay</th>
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
                        <td>${booking.bookingCode}</td>
                        <td>${booking.userFullName}</td>
                        <td>${booking.flightNumber}</td>
                        <td>${booking.bookingDate}</td>
                        <td>
                            <c:choose>
                                <c:when test="${booking.status == 'CONFIRMED'}">Đã xác nhận</c:when>
                                <c:when test="${booking.status == 'CANCELLED'}">Đã hủy</c:when>
                                <c:when test="${booking.status == 'CHECKED-IN'}">Đã check-in</c:when>
                                <c:when test="${booking.status == 'BOARDED'}">Đã lên máy bay</c:when>
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