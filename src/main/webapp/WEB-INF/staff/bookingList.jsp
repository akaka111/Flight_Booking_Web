<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
    <head>
        <title>Quản Lý Đặt Vé</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css" rel="stylesheet">
        <style>
            body {
                background-color: #f8f9fa;
            }
            .card-custom {
                border-radius: 15px;
                box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
                border: none;
            }
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
            .btn-info, .btn-primary {
                background-color: #0d6efd !important;
                color: #fff !important;
                border-color: #0d6efd !important;
            }
            .btn-info:hover, .btn-primary:hover {
                background-color: #0b5ed7 !important;
                border-color: #0a58ca !important;
            }
            .fixed-back-icon {
                position: fixed;
                top: 20px;
                left: 5px;
                z-index: 1000;
                font-size: 60px;
                color: #0d6efd;
                background-color: rgba(255, 255, 255, 0.9);
                border-radius: 50%;
                padding: 10px;
                transition: transform 0.2s, color 0.2s;
            }
            .fixed-back-icon:hover {
                transform: scale(1.1);
                color: #ff6f61;
            }
            h2.mt-4 {
                text-align: center;
                color: #0d6efd;
                font-weight: bold;
            }
            .table thead {
                background-color: #0d6efd;
                color: white;
                text-align: center;
            }
            .table tbody tr:hover {
                background-color: #e9f3ff;
            }
        </style>
        <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
        <script>
            $(document).ready(function () {
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

                $("form[action='${pageContext.request.contextPath}/staff/booking/flight/bookings']").submit(function (e) {
                    e.preventDefault();
                    var formData = $(this).serialize();
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
        <div class="container mt-4">
            <div class="card card-custom p-4">
                <h2 class="text-center mb-4">Quản Lý Đặt Vé</h2>
                <h2 class="text-center mb-4">
                    Tuyến 
                    <c:choose>
                        <c:when test="${not empty flight.route.originIata and not empty flight.route.originName}">
                            ${flight.route.originIata} (${flight.route.originName})
                        </c:when>
                        <c:otherwise>N/A</c:otherwise>
                    </c:choose>
                    →
                    <c:choose>
                        <c:when test="${not empty flight.route.destIata and not empty flight.route.destName}">
                            ${flight.route.destIata} (${flight.route.destName})
                        </c:when>
                        <c:otherwise>N/A</c:otherwise>
                    </c:choose>
                    | Hãng
                    <c:choose>
                        <c:when test="${not empty flight.airline.name}">
                            ${flight.airline.name}
                        </c:when>
                        <c:otherwise>N/A</c:otherwise>
                    </c:choose>
                </h2>

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

                <!-- Bộ lọc -->
                <form action="${pageContext.request.contextPath}/staff/booking/flight/bookings" method="get" class="row g-3 mb-3 align-items-center">
                    <input type="hidden" name="flightId" value="${flightId}">
                    <div class="col-auto">
                        <label for="statusFilter" class="col-form-label">Trạng thái:</label>
                    </div>
                    <div class="col-auto">
                        <select name="statusFilter" id="statusFilter" class="form-select">
                            <option value="">Tất cả</option>
                            <option value="CONFIRMED" ${param.statusFilter == 'CONFIRMED' ? 'selected' : ''}>Đã xác nhận</option>
                            <option value="PENDING" ${param.statusFilter == 'PENDING' ? 'selected' : ''}>Chờ xử lí</option>
                            <option value="CANCELLED" ${param.statusFilter == 'CANCELLED' ? 'selected' : ''}>Đã hủy</option>
                            <option value="CHECKED-IN" ${param.statusFilter == 'CHECKED-IN' ? 'selected' : ''}>Đã check-in</option>
                            <option value="BOARDED" ${param.statusFilter == 'BOARDED' ? 'selected' : ''}>Đã lên máy bay</option>
                        </select>
                    </div>
                    <div class="col-auto">
                        <button type="submit" class="btn btn-primary">Lọc</button>
                    </div>
                </form>

                <!-- Tìm kiếm -->
                <form action="${pageContext.request.contextPath}/staff/booking/search" method="get" class="row g-3 mb-3 align-items-center">
                    <div class="col-auto">
                        <label for="searchTerm" class="col-form-label">Tìm kiếm:</label>
                    </div>
                    <div class="col-auto flex-grow-1">
                        <input type="text" name="searchTerm" id="searchTerm" class="form-control"
                               placeholder="Nhập Mã Booking, Tên hoặc CCCD..." value="${param.searchTerm}">
                    </div>
                    <div class="col-auto">
                        <button type="submit" class="btn btn-primary">Tìm kiếm</button>
                    </div>
                </form>

                <!-- Bảng booking -->
                <div class="table-responsive">
                    <table class="table table-bordered table-hover text-center" id="bookingListTable">
                        <thead>
                            <tr>
                                <th>Mã Đặt Chỗ</th>
                                <th>Tên Hành Khách</th>
                                <th>Số CCCD</th>
                                <th>Thời Gian Đặt</th>
                                <th>Ghế</th>
                                <th>Tổng Giá</th>
                                <th>Trạng Thái Check-in</th>
                                <th>Trạng Thái</th>
                                <th>Ghi Chú</th>
                                <th>Thao Tác</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="booking" items="${bookings}">
                                <tr>
                                    <td><c:out value="${booking.bookingCode != null ? booking.bookingCode : 'N/A'}" /></td>
                                    <td><c:out value="${booking.passenger.fullName != null ? booking.passenger.fullName : 'N/A'}" /></td>
                                    <td><c:out value="${booking.passenger.passportNumber != null ? booking.passenger.passportNumber : 'N/A'}" /></td>
                                    <td><fmt:formatDate value="${booking.bookingDate}" pattern="dd-MM-yyyy HH:mm:ss"/></td>
                                    <td>
                                        <c:out value="${booking.seat != null ? booking.seat.seatNumber : 'N/A'}" /> 
                                        (<c:out value="${booking.seat != null && booking.seat.seatClass != null ? booking.seat.seatClass.name : 'N/A'}" />)
                                    </td>
                                    <td><fmt:formatNumber value="${booking.totalPrice}" type="currency" currencySymbol="VND" maxFractionDigits="0" /></td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${booking.checkInStatus == 'CHECKED-IN'}">
                                                <span class="text-success fw-bold">Đã check-in</span>
                                            </c:when>
                                            <c:when test="${booking.checkInStatus == 'BOARDED'}">
                                                <span class="text-primary fw-bold">Đã lên máy bay</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="text-secondary fst-italic">Chưa check-in</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${booking.status == 'CONFIRMED'}">
                                                <span class="text-success fw-bold">Đã xác nhận</span>
                                            </c:when>
                                            <c:when test="${booking.status == 'CANCELLED'}">
                                                <span class="text-danger fw-bold">Đã hủy</span>
                                            </c:when>
                                            <c:when test="${booking.status == 'COMPLETED'}">
                                                <span class="text-primary fw-bold">Hoàn thành</span>
                                            </c:when>
                                            <c:when test="${booking.status == 'PENDING'}">
                                                <span class="text-warning fw-bold">Chờ xử lý</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="text-muted fst-italic">
                                                    <c:out value="${booking.status != null ? booking.status : 'N/A'}" />
                                                </span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td><c:out value="${booking.staffNote != null ? booking.staffNote : 'N/A'}" /></td>
                                    <td>
                                        <a href="${pageContext.request.contextPath}/staff/booking/details?bookingId=${booking.bookingId}" 
                                           class="btn btn-info btn-sm">Xem chi tiết</a>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>

        <!-- Nút quay lại -->
        <a href="${pageContext.request.contextPath}/staff/booking/list" class="fixed-back-icon" title="Quay Lại Danh Sách Chuyến Bay">
            <i class="bi bi-arrow-left-circle"></i>
        </a>     

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>
