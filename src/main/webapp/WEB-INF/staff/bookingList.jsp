
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
    <head>
        <title>Quản Lý Đặt Vé</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css" rel="stylesheet">
        <style>
            .table {
                margin-top: 20px;
            }
            .form-group {
                margin-bottom: 15px;
            }
            .alert {
                position: fixed;
                top: 20px;
                left: 50%;
                transform: translateX(-50%);
                z-index: 1000;
            }
            .btn-info {
                background-color: #0d6efd !important;
                color: #fff !important;
                border-color: #0d6efd !important;
            }
            .btn-info:hover {
                background-color: #0b5ed7 !important;
                border-color: #0a58ca !important;
            }
            /* Style cho biểu tượng quay lại */
            .fixed-back-icon {
                position: fixed;
                top: 20px;
                left: 5px;
                z-index: 1000;
                font-size: 60px;
                color: #0f5132;
                background-color: rgba(255, 255, 255, 0.8);
                border-radius: 50%;
                padding: 10px;
                transition: transform 0.2s;
            }
            .fixed-back-icon:hover {
                transform: scale(1.1);
                color: #ff6f61;
            }
        </style>
        <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
        <script>
            $(document).ready(function () {
                // Chỉ gọi loadBookings nếu không trong iframe
                if (window.self === window.top) {
                    var flightId = "${flightId}";
                    if (flightId) {
                        loadFlightBookings(flightId, '');
                    } else {
                        loadBookings();
                    }
                }



                function showAlert(message, type) {
                    var alertDiv = $('<div class="alert alert-' + type + ' alert-dismissible fade show" role="alert">' +
                            message +
                            '<button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>' +
                            '</div>');
                    $("body").prepend(alertDiv);
                    setTimeout(function () {
                        alertDiv.alert('close');
                    }, 3000);
                }

                function loadBookings(statusFilter = '') {
                    $.ajax({
                        url: '${pageContext.request.contextPath}/staff/booking/list',
                        type: 'GET',
                        data: {statusFilter: statusFilter},
                        dataType: 'html',
                        success: function (response) {
                            $("#bookingListTable tbody").html($(response).find("#bookingListTable tbody").html());
                            showAlert("Tải danh sách booking thành công!", "success");
                        },
                        error: function (xhr) {
                            showAlert("Tải danh sách booking thất bại: " + (xhr.responseText || "Lỗi không xác định"), "danger");
                        }
                    });
                }

                function loadFlightBookings(flightId, statusFilter = '') {
                    $.ajax({
                        url: '${pageContext.request.contextPath}/staff/booking/flight/bookings',
                        type: 'GET',
                        data: {flightId: flightId, statusFilter: statusFilter},
                        dataType: 'html',
                        success: function (response) {
                            $("#bookingListTable tbody").html($(response).find("#bookingListTable tbody").html());
                            showAlert("Tải danh sách booking thành công!", "success");
                        },
                        error: function (xhr) {
                            showAlert("Tải danh sách booking thất bại: " + (xhr.responseText || "Lỗi không xác định"), "danger");
                        }
                    });
                }

                // Xử lý form lọc
                $("form[action='${pageContext.request.contextPath}/staff/booking/flight/bookings']").submit(function (e) {
                    e.preventDefault();
                    var formData = $(this).serialize();
                    var flightId = $(this).find('input[name="flightId"]').val();
                    $.ajax({
                        url: '${pageContext.request.contextPath}/staff/booking/flight/bookings',
                        type: 'GET',
                        data: formData,
                        dataType: 'html',
                        success: function (response) {
                            $("#bookingListTable tbody").html($(response).find("#bookingListTable tbody").html());
                            showAlert("Lọc thành công!", "success");
                        },
                        error: function (xhr) {
                            showAlert("Lọc thất bại: " + (xhr.responseText || "Lỗi không xác định"), "danger");
                        }
                    });
                });

                // Xử lý form tìm kiếm
                $("form[action='${pageContext.request.contextPath}/staff/booking/search']").submit(function (e) {
                    e.preventDefault();
                    var formData = $(this).serialize();
                    var flightId = "${flightId}";
                    if (flightId) {
                        formData += "&flightId=" + flightId;
                    }
                    $.ajax({
                        url: '${pageContext.request.contextPath}/staff/booking/search',
                        type: 'GET',
                        data: formData,
                        dataType: 'html',
                        success: function (response) {
                            $("#bookingListTable tbody").html($(response).find("#bookingListTable tbody").html());
                            showAlert("Tìm kiếm thành công!", "success");
                        },
                        error: function (xhr) {
                            showAlert("Tìm kiếm thất bại: " + (xhr.responseText || "Lỗi không xác định"), "danger");
                        }
                    });
                });
            });
        </script>
    </head>
    <body>
        <div class="container">
            <h2 class="mt-4">Quản Lý Đặt Vé</h2>
            <c:if test="${not empty error}">
                <div class="alert alert-danger alert-dismissible fade show" role="alert">
                    ${error}
                    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                </div>
                <c:remove var="error" scope="session"/>
            </c:if>
            <c:if test="${not empty success}">
                <div class="alert alert-success alert-dismissible fade show" role="alert">
                    ${success}
                    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                </div>
                <c:remove var="success" scope="session"/>
            </c:if>
            <form action="${pageContext.request.contextPath}/staff/booking/flight/bookings" method="get" class="form-inline mb-3">
                <input type="hidden" name="flightId" value="${flightId}">
                <div class="form-group me-2">
                    <label for="statusFilter" class="me-2">Lọc theo trạng thái:</label>
                    <select name="statusFilter" id="statusFilter" class="form-select">
                        <option value="">Tất cả</option>
                        <option value="CONFIRMED" ${param.statusFilter == 'CONFIRMED' ? 'selected' : ''}>Đã xác nhận</option>
                        <option value="CANCELLED"   ${param.statusFilter == 'CANCELLED' ? 'selected' : ''}>Đã hủy</option>
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
                        <th>Tuyến Đường</th>
                        <th>Thời Gian Đặt</th>
                        <th>Trạng Thái</th>
                        <th>Ghế</th>
                        <th>Tổng Giá</th>
                        <th>Trạng Thái Check-in</th>
                        <th>Ghi Chú</th>
                        <th>Thao Tác</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="booking" items="${bookings}">
                        <tr>
                            <td><c:out value="${booking.bookingCode != null ? booking.bookingCode : 'N/A'}" /></td>
                            <td><c:out value="${booking.userFullName != null ? booking.userFullName : 'N/A'}" /></td>
                            <td><c:out value="${booking.flight != null && booking.flight.route != null ? booking.flight.route.originIata : 'N/A'} -> ${booking.flight != null && booking.flight.route != null ? booking.flight.route.destIata : 'N/A'} (${booking.flight != null ? booking.flight.flightNumber : 'N/A'})" /></td>
                            <td><fmt:formatDate value="${booking.bookingDate}" pattern="dd-MM-yyyy HH:mm:ss"/></td>
                            <td>
                                <c:choose>
                                    <c:when test="${booking.status == 'CONFIRMED'}">Đã xác nhận</c:when>
                                    <c:when test="${booking.status == 'CANCELLED'}">Đã hủy</c:when>
                                    <c:when test="${booking.status == 'COMPLETED'}">Hoàn thành</c:when>
                                    <c:when test="${booking.status == 'PENDING'}">Chờ xử lý</c:when>
                                    <c:otherwise><c:out value="${booking.status != null ? booking.status : 'N/A'}" /></c:otherwise>
                                </c:choose>
                            </td>
                            <td><c:out value="${booking.seat != null ? booking.seat.seatNumber : 'N/A'} (${booking.seat != null && booking.seat.seatClass != null ? booking.seat.seatClass.name : 'N/A'})" /></td>
                            <td><fmt:formatNumber value="${booking.totalPrice}" type="currency" currencySymbol="VND" maxFractionDigits="0" /></td>
                            <td>
                                <c:choose>
                                    <c:when test="${booking.checkInStatus == 'CHECKED-IN'}">Đã check-in</c:when>
                                    <c:when test="${booking.checkInStatus == 'BOARDED'}">Đã lên máy bay</c:when>
                                    <c:otherwise>Chưa check-in</c:otherwise>
                                </c:choose>
                            </td>
                            <td><c:out value="${booking.staffNote != null ? booking.staffNote : 'N/A'}" /></td>
                            <td>
                                <a href="${pageContext.request.contextPath}/staff/booking/details?bookingId=${booking.bookingId}" class="btn btn-info btn-sm">Xem chi tiết</a>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
            <!-- Biểu tượng quay lại cố định -->
            <a href="${pageContext.request.contextPath}/staff/booking/list" class="fixed-back-icon" title="Quay Lại Danh Sách Chuyến Bay">
                <i class="bi bi-arrow-left-circle"></i>
            </a>     
        </div>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>
