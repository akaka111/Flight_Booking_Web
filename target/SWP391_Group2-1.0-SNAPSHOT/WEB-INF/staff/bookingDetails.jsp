
<%-- 
    Document   : bookingDetails
    Created on : Jul 19, 2025, 1:55:40 PM
    Author     : Khoa
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<html>
    <head>
        <title>Chi Tiết Booking</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css" rel="stylesheet">
        <style>
            .section {
                margin-bottom: 30px;
            }
            .error {
                color: red;
            }
            .success {
                color: green;
            }
            .form-group {
                margin-bottom: 15px;
            }
            .form-group label {
                display: inline-block;
                width: 150px;
            }
            .form-group input, .form-group select {
                padding: 5px;
                width: 200px;
            }
            .editable {
                display: none;
            }
            .read-only {
                display: table-cell;
            }
            .edit-btn {
                margin-right: 10px;
            }
            .alert {
                position: fixed;
                top: 20px;
                left: 50%;
                transform: translateX(-50%);
                z-index: 1000;
            }
            .disabled-section {
                pointer-events: none;
                opacity: 0.6;
            }
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
            .cancel-reason {
                margin-left: 10px;
                font-style: italic;
            }
            .cancel-btn {
                margin-left: 10px;
            }
        </style>
        <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
        <script>
            $(document).ready(function () {
                // Khởi tạo biến với kiểm tra null chặt chẽ
                let bookingStatus = "${booking != null && booking.status != null ? booking.status : ''}";
                let isBookingCancelled = bookingStatus === "CANCELLED";
                let bookingId = "${booking != null && booking.bookingId != null ? booking.bookingId : ''}";
                let flightId = "${flightId != null ? flightId : ''}";
                let staffNote = "${booking != null && booking.staffNote != null ? booking.staffNote : ''}";
                let userId = "${sessionScope.userId}";

              

                // Xử lý trạng thái ban đầu
                if (isBookingCancelled) {
                    $("#bookingSection, #passengerSection, #checkInSection").addClass("disabled-section");
                    $("#editBookingBtn, #updateBookingBtn, #cancelBookingEditBtn, #editCheckInBtn, #updateCheckInBtn, #cancelCheckInEditBtn").prop("disabled", true).hide();
                    $("#cancelBookingBtn").prop("disabled", true).addClass("btn-secondary").text("Đã Hủy");
                    $("#cancelReasonDisplay").text(staffNote ? "Lý do: " + staffNote : "Không có lý do").show();
                } else {
                    $("#cancelBookingBtn").prop("disabled", false).addClass("btn-danger").text("Hủy Booking");
                    $("#cancelReasonDisplay").hide();
                }

                // Xử lý chỉnh sửa và hủy thao tác cho Booking
                $("#editBookingBtn").click(function () {
                    if (!isBookingCancelled) {
                        $("#bookingSection .read-only").hide();
                        $("#bookingSection .editable").show();
                        $("#updateBookingBtn").show();
                        $("#cancelBookingEditBtn").show();
                        $("#editBookingBtn").hide();
                    }
                });

                $("#cancelBookingEditBtn").click(function () {
                    $("#bookingSection .editable").hide();
                    $("#bookingSection .read-only").show();
                    $("#updateBookingBtn").hide();
                    $("#cancelBookingEditBtn").hide();
                    $("#editBookingBtn").show();
                });

                $("#updateBookingBtn").click(function () {
                    if (!isBookingCancelled) {
                        var formData = {
                            bookingId: $("#bookingId").val(),
                            status: $("#status").val()
                        };
                        $.ajax({
                            url: '${pageContext.request.contextPath}/staff/booking/update',
                            type: 'POST',
                            data: formData,
                            success: function (response) {
                                showAlert("Cập nhật booking thành công!", "success");
                                $("#bookingSection .editable").hide();
                                $("#bookingSection .read-only").show();
                                $("#updateBookingBtn").hide();
                                $("#cancelBookingEditBtn").hide();
                                $("#editBookingBtn").show();
                                $("#statusReadOnly").text($("#status option:selected").text());
                            },
                            error: function (xhr) {
                                var errorMsg = xhr.responseText || "Lỗi không xác định";
                                showAlert("Cập nhật booking thất bại: " + errorMsg, "danger");
                            }
                        });
                    }
                });

                // Xử lý chỉnh sửa và hủy thao tác cho Check-in
                $("#editCheckInBtn").click(function () {
                    if (!isBookingCancelled) {
                        $("#checkInSection .read-only").hide();
                        $("#checkInSection .editable").show();
                        $("#updateCheckInBtn").show();
                        $("#cancelCheckInEditBtn").show();
                        $("#editCheckInBtn").hide();
                    }
                });

                $("#cancelCheckInEditBtn").click(function () {
                    $("#checkInSection .editable").hide();
                    $("#checkInSection .read-only").show();
                    $("#updateCheckInBtn").hide();
                    $("#cancelCheckInEditBtn").hide();
                    $("#editCheckInBtn").show();
                });

                $("#updateCheckInBtn").click(function () {
                    if (!isBookingCancelled) {
                        var checkInStatus = $("#checkInStatus").val();
                        if (!checkInStatus) {
                            showAlert("Vui lòng chọn trạng thái check-in!", "danger");
                            return;
                        }
                        var formData = {
                            bookingId: $("#bookingId").val(),
                            passengerId: $("#passengerId").val(),
                            flightId: $("#flightId").val(),
                            checkInStatus: checkInStatus
                        };
                        $.ajax({
                            url: '${pageContext.request.contextPath}/staff/booking/checkin',
                            type: 'POST',
                            data: formData,
                            success: function (response) {
                                showAlert("Cập nhật check-in thành công!", "success");
                                $("#checkInSection .editable").hide();
                                $("#checkInSection .read-only").show();
                                $("#updateCheckInBtn").hide();
                                $("#cancelCheckInEditBtn").hide();
                                $("#editCheckInBtn").show();
                                $("#checkinStatusReadOnly").text($("#checkInStatus option:selected").text());
                                var currentTime = new Date().toLocaleString('vi-VN', {day: '2-digit', month: '2-digit', year: 'numeric', hour: '2-digit', minute: '2-digit', second: '2-digit'});
                                $("#checkInSection td").last().text(currentTime);
                            },
                            error: function (xhr) {
                                var errorMsg = xhr.responseText || "Lỗi không xác định";
                                showAlert("Cập nhật check-in thất bại: " + errorMsg, "danger");
                            }
                        });
                    }
                });

                // Xử lý hủy booking
                $("#cancelBookingBtn").click(function () {
                    if (!isBookingCancelled && bookingId) {
                        $("#cancelModal").modal('show');
                    } else {
                        console.log("Không mở modal - isBookingCancelled:", isBookingCancelled, "bookingId:", bookingId);
                    }
                });

                $("#confirmCancelBtn").click(function () {
                    var reason = $("#cancelReason").val().trim();
                    if (!reason) {
                        showAlert("Vui lòng nhập lý do hủy!", "danger");
                        return;
                    }
                    if (!confirm("Bạn có chắc chắn muốn hủy booking này? Hành động này không thể hoàn tác!")) {
                        return;
                    }
                    var formData = {
                        bookingId: bookingId,
                        reason: reason
                    };
                    $.ajax({
                        url: '${pageContext.request.contextPath}/staff/booking/cancel',
                        type: 'POST',
                        data: formData,
                        success: function (response) {
                            showAlert("Hủy booking thành công!", "success");
                            $("#cancelModal").modal('hide');
                            isBookingCancelled = true;
                            $("#bookingSection, #passengerSection, #checkInSection").addClass("disabled-section");
                            $("#editBookingBtn, #updateBookingBtn, #cancelBookingEditBtn, #editCheckInBtn, #updateCheckInBtn, #cancelCheckInEditBtn").prop("disabled", true).hide();
                            $("#cancelBookingBtn").prop("disabled", true).addClass("btn-secondary").text("Đã Hủy");
                            $("#cancelReasonDisplay").text("Lý do: " + reason).show();
                            setTimeout(function () {
                                window.location.href = '${pageContext.request.contextPath}/staff/booking/flight/bookings?flightId=' + flightId;
                            }, 3000);
                        },
                        error: function (xhr) {
                            var errorMsg = xhr.responseText || "Lỗi không xác định";
                            showAlert("Hủy booking thất bại: " + errorMsg, "danger");
                        }
                    });
                });

                function showAlert(message, type) {
                    var alertDiv = $('<div class="alert alert-' + type + ' alert-dismissible fade show" role="alert">' +
                            message +
                            '<button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>' +
                            '</div>');
                    $(".container").prepend(alertDiv);
                    setTimeout(function () {
                        alertDiv.alert('close');
                    }, 5000);
                }
            });
        </script>
    </head>
    <body>
        <div class="container">

            <h2 class="mt-4">Chi Tiết Booking: <c:out value="${booking != null && booking.userFullName != null ? booking.userFullName : 'N/A'}" /></h2>

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

            <div class="section" id="bookingSection">
                <h4>Thông Tin Booking</h4>
                <table class="table table-bordered">
                    <tr><th>Mã Booking</th><td class="read-only"><span id="bookingIdReadOnly">${booking != null && booking.bookingId != null ? booking.bookingId : 'N/A'}</span></td><td class="editable"><input type="text" id="bookingId" name="bookingId" value="${booking != null && booking.bookingId != null ? booking.bookingId : ''}" readonly></td></tr>
                    <tr><th>Mã Người Dùng</th><td class="read-only"><span>${booking != null && booking.userId != null ? booking.userId : 'N/A'}</span></td><td class="editable"><input type="text" value="${booking != null && booking.userId != null ? booking.userId : ''}" readonly></td></tr>
                    <tr><th>Mã Chuyến Bay</th><td class="read-only"><span id="flightIdReadOnly">${booking != null && booking.flightId != null ? booking.flightId : 'N/A'}</span></td><td class="editable"><input type="text" id="flightId" name="flightId" value="${booking != null && booking.flightId != null ? booking.flightId : ''}" readonly></td></tr>
                    <tr><th>Thời Gian Đặt</th>
                        <td class="read-only">
                            <span><c:if test="${booking != null && booking.bookingDate != null}"><fmt:formatDate value="${booking.bookingDate}" pattern="dd-MM-yyyy HH:mm:ss" /></c:if><c:if test="${booking == null || booking.bookingDate == null}">N/A</c:if></span>
                        </td>
                        <td class="editable"><input type="text" value="${booking != null && booking.bookingDate != null ? booking.bookingDate : ''}" readonly></td>
                    </tr>
                    <tr><th>Trạng Thái</th><td class="read-only"><span id="statusReadOnly">
                            <c:choose>
                                <c:when test="${booking != null && booking.status == 'CONFIRMED'}">Đã xác nhận</c:when>
                                <c:when test="${booking != null && booking.status == 'CANCELLED'}">Đã hủy</c:when>
                                <c:when test="${booking != null && booking.status == 'COMPLETED'}">Hoàn thành</c:when>
                                <c:when test="${booking != null && booking.status == 'PENDING'}">Chờ xử lý</c:when>
                                <c:otherwise>N/A</c:otherwise>
                            </c:choose>
                        </span></td><td class="editable"><select id="status" name="status" class="form-select">
                            <option value="CONFIRMED" ${booking != null && booking.status == 'CONFIRMED' ? 'selected' : ''}>Đã xác nhận</option>
                            <option value="CANCELLED" ${booking != null && booking.status == 'CANCELLED' ? 'selected' : ''}>Đã hủy</option>
                            <option value="COMPLETED" ${booking != null && booking.status == 'COMPLETED' ? 'selected' : ''}>Hoàn thành</option>
                            <option value="PENDING" ${booking != null && booking.status == 'PENDING' ? 'selected' : ''}>Chờ xử lý</option>
                        </select></td></tr>
                    <tr><th>Ghế</th><td class="read-only"><span id="seatReadOnly">${booking != null && booking.seat != null && booking.seat.seatNumber != null ? booking.seat.seatNumber : 'N/A'} (${booking != null && booking.seat != null && booking.seat.seatClass != null ? booking.seat.seatClass.name : 'N/A'})</span></td><td class="editable"><select id="seatId" name="seatId" class="form-select" disabled>
                            <c:if test="${not empty availableSeats}">
                                <c:forEach var="seat" items="${availableSeats}">
                                    <option value="${seat.seatId}" ${booking != null && booking.seat != null && booking.seat.seatId == seat.seatId ? 'selected' : ''}>${seat.seatNumber} (${seat.seatClass != null ? seat.seatClass.name : 'N/A'})</option>
                                </c:forEach>
                            </c:if>
                            <c:if test="${empty availableSeats}">
                                <option value="">Không có ghế</option>
                            </c:if>
                        </select></td></tr>
                    <tr><th>Tổng Giá</th><td class="read-only"><span id="totalPriceReadOnly">${booking != null && booking.totalPrice != null ? booking.totalPrice : 'N/A'}</span></td><td class="editable"><input type="number" id="totalPrice" name="totalPrice" value="${booking != null && booking.totalPrice != null ? booking.totalPrice : ''}" step="0.01" readonly></td></tr>
                </table>
                <button id="editBookingBtn" class="btn btn-primary edit-btn">Chỉnh Sửa Booking</button>
                <button id="updateBookingBtn" class="btn btn-primary mt-2 ms-2" style="display: none;">Cập Nhật Booking</button>
                <button id="cancelBookingEditBtn" class="btn btn-secondary mt-2 ms-2 cancel-btn" style="display: none;">Hủy</button>
            </div>

            <div class="section" id="checkInSection">
                <h4>Thông Tin Check-in</h4>
                <table class="table table-bordered">
                    <thead>
                        <tr><th>Mã Hành Khách</th><th>Trạng Thái Check-in</th><th>Thời Gian Check-in</th></tr>
                    </thead>
                    <tbody>
                        <c:choose>
                            <c:when test="${not empty checkIns}">
                                <c:forEach var="checkIn" items="${checkIns}">
                                    <tr>
                                        <td><c:out value="${checkIn != null && checkIn.passengerId != null ? checkIn.passengerId : 'N/A'}" /></td>
                                        <td class="read-only"><span id="checkinStatusReadOnly">
                                                <c:choose>
                                                    <c:when test="${checkIn != null && checkIn.status == 'NOT CHECKED-IN'}">Chưa check-in</c:when>
                                                    <c:when test="${checkIn != null && checkIn.status == 'CHECKED-IN'}">Đã check-in</c:when>
                                                    <c:when test="${checkIn != null && checkIn.status == 'BOARDED'}">Đã lên máy bay</c:when>
                                                    <c:otherwise>Chưa check-in</c:otherwise>
                                                </c:choose>
                                            </span></td>
                                        <td class="editable"><select id="checkInStatus" name="checkInStatus" class="form-select" required>
                                                <option value="" disabled ${checkIn == null || checkIn.status == null ? 'selected' : ''}>Chọn trạng thái</option>
                                                <option value="NOT CHECKED-IN" ${checkIn.status == 'NOT CHECKED-IN' ? 'selected' : ''}>Chưa check-in</option>
                                                <option value="CHECKED-IN" ${checkIn.status == 'CHECKED-IN' ? 'selected' : ''}>Đã check-in</option>
                                                <option value="BOARDED" ${checkIn.status == 'BOARDED' ? 'selected' : ''}>Đã lên máy bay</option>
                                            </select></td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${checkIn != null && checkIn.checkinTime != null}">
                                                    <fmt:formatDate value="${checkIn.checkinTime}" pattern="dd-MM-yyyy HH:mm:ss" />
                                                </c:when>
                                                <c:otherwise>N/A</c:otherwise>
                                            </c:choose>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </c:when>
                            <c:otherwise>
                                <tr>
                                    <td><c:out value="${passenger != null && passenger.passengerId != null ? passenger.passengerId : 'N/A'}" /></td>
                                    <td class="read-only"><span id="checkinStatusReadOnly">Chưa check-in</span></td>
                                    <td class="editable"><select id="checkInStatus" name="checkInStatus" class="form-select" required>
                                            <option value="" disabled selected>Chọn trạng thái</option>
                                            <option value="CHECKED-IN">Đã check-in</option>
                                            <option value="BOARDED">Đã lên máy bay</option>
                                        </select></td>
                                    <td>N/A</td>
                                </tr>
                            </c:otherwise>
                        </c:choose>
                    </tbody>
                </table>
                <button id="editCheckInBtn" class="btn btn-primary edit-btn">Chỉnh Sửa Check-in</button>
                <button id="updateCheckInBtn" class="btn btn-primary mt-2" style="display: none;">Cập Nhật Check-in</button>
                <button id="cancelCheckInEditBtn" class="btn btn-secondary mt-2 cancel-btn" style="display: none;">Hủy</button>
            </div>

            <div class="section" id="passengerSection">
                <h4>Thông Tin Hành Khách</h4>
                <table class="table table-bordered">
                    <tr><th>Mã Hành Khách</th><td><span id="passengerIdReadOnly">${passenger != null && passenger.passengerId != null ? passenger.passengerId : 'N/A'}</span><input type="hidden" id="passengerId" value="${passenger != null && passenger.passengerId != null ? passenger.passengerId : ''}"></td></tr>
                    <tr><th>Họ Tên</th><td><span id="fullNameReadOnly">${passenger != null && passenger.fullName != null ? passenger.fullName : 'N/A'}</span></td></tr>
                    <tr><th>Số Hộ Chiếu</th><td><span id="passportNumberReadOnly">${passenger != null && passenger.passportNumber != null ? passenger.passengerId : 'N/A'}</span></td></tr>
                    <tr><th>Ngày Sinh</th><td><span id="dobReadOnly">${passenger != null && passenger.dob != null ? passenger.dob : 'N/A'}</span></td></tr>
                    <tr><th>Giới Tính</th><td><span id="genderReadOnly">${passenger != null && passenger.gender != null ? passenger.gender : 'N/A'}</span></td></tr>
                    <tr><th>Số Điện Thoại</th><td><span id="phoneNumberReadOnly">${passenger != null && passenger.phoneNumber != null ? passenger.phoneNumber : 'N/A'}</span></td></tr>
                    <tr><th>Email</th><td><span id="emailReadOnly">${passenger != null && passenger.email != null ? passenger.email : 'N/A'}</span></td></tr>
                    <tr><th>Quốc Gia</th><td><span id="countryReadOnly">${passenger != null && passenger.country != null ? passenger.country : 'N/A'}</span></td></tr>
                    <tr><th>Địa Chỉ</th><td><span id="addressReadOnly">${passenger != null && passenger.address != null ? passenger.address : 'N/A'}</span></td></tr>
                </table>
            </div>

            <div class="section">
                <h4>Chi Tiết Chuyến Bay</h4>
                <table class="table table-bordered">
                    <tr><th>Mã Chuyến Bay</th><td><span>${flight != null && flight.flightNumber != null ? flight.flightNumber : 'N/A'}</span></td></tr>
                    <tr><th>Điểm Đi</th><td><span>${flight != null && flight.route != null && flight.route.originIata != null ? flight.route.originIata : 'N/A'} (${flight != null && flight.route != null && flight.route.originName != null ? flight.route.originName : 'N/A'})</span></td></tr>
                    <tr><th>Điểm Đến</th><td><span>${flight != null && flight.route != null && flight.route.destIata != null ? flight.route.destIata : 'N/A'} (${flight != null && flight.route != null && flight.route.destName != null ? flight.route.destName : 'N/A'})</span></td></tr>
                    <tr><th>Thời Gian Cất Cánh</th><td><span><c:if test="${flight != null && flight.departureTime != null}"><fmt:formatDate value="${flight.departureTime}" pattern="dd-MM-yyyy HH:mm:ss" /></c:if><c:if test="${flight == null || flight.departureTime == null}">N/A</c:if></span></td></tr>
                    <tr><th>Thời Gian Hạ Cánh</th><td><span><c:if test="${flight != null && flight.arrivalTime != null}"><fmt:formatDate value="${flight.arrivalTime}" pattern="dd-MM-yyyy HH:mm:ss" /></c:if><c:if test="${flight == null || flight.arrivalTime == null}">N/A</c:if></span></td></tr>
                    <tr><th>Trạng Thái</th><td><span>${flight != null && flight.status != null ? flight.status : 'N/A'}</span></td></tr>
                    <tr><th>Máy Bay</th><td><span>${flight != null && flight.aircraftType != null && flight.aircraftType.aircraftTypeName != null ? flight.aircraftType.aircraftTypeName : 'N/A'}</span></td></tr>
                </table>
            </div>

            <div class="section">
                <h4>Hủy Booking</h4>
                <button id="cancelBookingBtn" class="btn btn-danger mt-2">Hủy Booking</button>
                <span id="cancelReasonDisplay" class="cancel-reason" style="display: none;"></span>
            </div>

            <!-- Modal để nhập lý do hủy -->
            <div class="modal fade" id="cancelModal" tabindex="-1" aria-labelledby="cancelModalLabel" aria-hidden="true">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title" id="cancelModalLabel">Hủy Booking</h5>
                            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                        </div>
                        <div class="modal-body">
                            <div class="form-group">
                                <label for="cancelReason">Lý do hủy:</label>
                                <input type="text" id="cancelReason" name="reason" class="form-control" placeholder="Nhập lý do hủy" required>
                            </div>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Đóng</button>
                            <button type="button" id="confirmCancelBtn" class="btn btn-danger">Xác Nhận Hủy</button>
                        </div>
                    </div>
                </div>
            </div>

            <div class="section">
                <h4>Lịch Sử Xử Lý</h4>
                <table class="table table-bordered">
                    <thead>
                        <tr>
                            <th>STT</th>
                            <th>Vai Trò</th>
                            <th>Tên Nhân Viên</th>
                            <th>Hành Động</th>
                            <th>Mô Tả</th>
                            <th>Thời Gian</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:if test="${not empty history}">
                            <c:forEach var="history" items="${history}" varStatus="loop">
                                <tr>
                                    <td>${loop.index + 1}</td>
                                    <td>${history != null && history.role != null ? history.role : 'N/A'}</td>
                                    <td>${history != null && history.userName != null ? history.userName : 'N/A'}</td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${history != null && history.action == 'CẬP NHẬT'}">Cập nhật</c:when>
                                            <c:when test="${history != null && history.action == 'HỦY'}">Hủy</c:when>
                                            <c:when test="${history != null && history.action == 'CHECKED-IN'}">Check-in</c:when>
                                            <c:when test="${history != null && history.action == 'BOARDED'}">Lên máy bay</c:when>
                                            <c:when test="${history != null && history.action == 'CẬP NHẬT HÀNH KHÁCH'}">Cập nhật hành khách</c:when>
                                            <c:otherwise>${history != null && history.action != null ? history.action : 'N/A'}</c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>${history != null && history.description != null ? history.description : 'N/A'}</td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${history != null && history.actionTime != null}">
                                                <fmt:formatDate value="${history.actionTime}" pattern="dd-MM-yyyy HH:mm:ss" />
                                            </c:when>
                                            <c:otherwise>N/A</c:otherwise>
                                        </c:choose>
                                    </td>
                                </tr>
                            </c:forEach>
                        </c:if>
                        <c:if test="${empty history}">
                            <tr><td colspan="6">Chưa có lịch sử xử lý</td></tr>
                        </c:if>
                    </tbody>
                </table>
            </div>

            <a href="${pageContext.request.contextPath}/staff/booking/flight/bookings?flightId=${flightId != null ? flightId : ''}" class="fixed-back-icon" title="Quay Lại Danh Sách Booking">
                <i class="bi bi-arrow-left-circle"></i>
            </a>
        </div>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>
