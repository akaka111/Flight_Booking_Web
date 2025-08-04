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
        </style>
        <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
        <script>
            $(document).ready(function () {
                let isBookingCancelled = "${booking.status}" === "CANCELLED"; // Kiểm tra trạng thái ban đầu
                let staffId = "${sessionScope.staffId}"; // Lấy staffId từ session
                if (!staffId || staffId <= 0) {
                    alert("Vui lòng đăng nhập lại để tiếp tục.");
                    window.location.href = "${pageContext.request.contextPath}/login";
                    return;
                }

                // Vô hiệu hóa các nút chỉnh sửa nếu booking đã bị hủy
                if (isBookingCancelled) {
                    $("#bookingSection, #passengerSection").addClass("disabled-section");
                    $("#editBookingBtn, #updateBookingBtn, #editPassengerBtn, #updatePassengerBtn, #checkInBtn").prop("disabled", true).hide();
                }

                // Chuyển sang chế độ chỉnh sửa Booking
                $("#editBookingBtn").click(function () {
                    if (!isBookingCancelled) {
                        $("#bookingSection .read-only").hide();
                        $("#bookingSection .editable").show();
                        $("#updateBookingBtn").show();
                        $("#editBookingBtn").hide();
                    }
                });

                // Cập nhật Booking
                $("#updateBookingBtn").click(function () {
                    if (!isBookingCancelled) {
                        var formData = {
                            bookingId: $("#bookingId").val(),
                            status: $("#status").val(),
                            seatClass: $("#seatClass").val(),
                            totalPrice: $("#totalPrice").val(),
                            staffNote: $("#staffNote").val(),
                            checkinStatus: $("#checkinStatus").val(),
                            staffId: staffId
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
                                $("#editBookingBtn").show();
                                $("#statusReadOnly").text($("#status option:selected").text());
                                $("#seatClassReadOnly").text($("#seatClass option:selected").text());
                                $("#totalPriceReadOnly").text(formData.totalPrice);
                                $("#checkinStatusReadOnly").text($("#checkinStatus option:selected").text());
                                $("#staffNoteReadOnly").text(formData.staffNote);
                            },
                            error: function (xhr) {
                                showAlert("Cập nhật booking thất bại: " + xhr.responseText, "danger");
                            }
                        });
                    }
                });

                // Chuyển sang chế độ chỉnh sửa Hành Khách
                $("#editPassengerBtn").click(function () {
                    if (!isBookingCancelled) {
                        $("#passengerSection .read-only").hide();
                        $("#passengerSection .editable").show();
                        $("#updatePassengerBtn").show();
                        $("#editPassengerBtn").hide();
                    }
                });

                // Cập nhật Hành Khách
                $("#updatePassengerBtn").click(function () {
                    if (!isBookingCancelled) {
                        var formData = {
                            passengerId: $("#passengerId").val(),
                            fullName: $("#fullName").val(),
                            passportNumber: $("#passportNumber").val(),
                            dob: $("#dob").val(),
                            gender: $("#gender").val(),
                            phoneNumber: $("#phoneNumber").val(),
                            email: $("#email").val(),
                            country: $("#country").val(),
                            address: $("#address").val(),
                            bookingId: $("#bookingId").val(),
                            staffId: staffId
                        };
                        $.ajax({
                            url: '${pageContext.request.contextPath}/staff/booking/updatePassenger',
                            type: 'POST',
                            data: formData,
                            success: function (response) {
                                showAlert("Cập nhật thông tin hành khách thành công!", "success");
                                $("#passengerSection .editable").hide();
                                $("#passengerSection .read-only").show();
                                $("#updatePassengerBtn").hide();
                                $("#editPassengerBtn").show();
                                $("#fullNameReadOnly").text(formData.fullName);
                                $("#passportNumberReadOnly").text(formData.passportNumber);
                                $("#dobReadOnly").text(formData.dob);
                                $("#genderReadOnly").text(formData.gender);
                                $("#phoneNumberReadOnly").text(formData.phoneNumber);
                                $("#emailReadOnly").text(formData.email);
                                $("#countryReadOnly").text(formData.country);
                                $("#addressReadOnly").text(formData.address);
                            },
                            error: function (xhr) {
                                showAlert("Cập nhật thông tin hành khách thất bại: " + xhr.responseText, "danger");
                            }
                        });
                    }
                });

                // Xử lý Thực hiện Check-in
                $("#checkInBtn").click(function () {
                    if (!isBookingCancelled) {
                        var formData = {
                            bookingId: $("#bookingId").val(),
                            passengerId: $("#passengerId").val(),
                            flightId: $("#flightId").val(),
                            checkinStatus: $("#checkinStatus").val(),
                            staffId: staffId
                        };
                        $.ajax({
                            url: '${pageContext.request.contextPath}/staff/booking/checkin',
                            type: 'POST',
                            data: formData,
                            success: function (response) {
                                showAlert("Check-in thành công!", "success");
                                $("#checkinStatusReadOnly").text("Đã check-in");
                                $("#checkinStatus").val("CHECKED-IN");
                            },
                            error: function (xhr) {
                                showAlert("Check-in thất bại: " + xhr.responseText, "danger");
                            }
                        });
                    }
                });

                // Xử lý Hủy Booking với xác nhận
                $("#cancelBookingBtn").click(function () {
                    if (!isBookingCancelled) {
                        var reason = $("#reason").val();
                        if (confirm("Bạn có chắc chắn muốn hủy booking này? Hành động này không thể hoàn tác!")) {
                            var formData = {
                                bookingId: $("#bookingId").val(),
                                reason: reason,
                                staffId: staffId
                            };
                            $.ajax({
                                url: '${pageContext.request.contextPath}/staff/booking/cancel',
                                type: 'POST',
                                data: formData,
                                success: function (response) {
                                    showAlert("Hủy booking thành công!", "success");
                                    isBookingCancelled = true;
                                    $("#bookingSection, #passengerSection").addClass("disabled-section");
                                    $("#editBookingBtn, #updateBookingBtn, #editPassengerBtn, #updatePassengerBtn, #checkInBtn, #cancelBookingBtn").prop("disabled", true).hide();
                                    setTimeout(function () {
                                        window.location.href = '${pageContext.request.contextPath}/staff/booking/list';
                                    }, 3000);
                                },
                                error: function (xhr) {
                                    showAlert("Hủy booking thất bại: " + xhr.responseText, "danger");
                                }
                            });
                        }
                    }
                });

                // Hàm hiển thị và tự động ẩn thông báo
                function showAlert(message, type) {
                    var alertDiv = $('<div class="alert alert-' + type + ' alert-dismissible fade show" role="alert">' +
                            message +
                            '<button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>' +
                            '</div>');
                    $(".container").prepend(alertDiv);
                    setTimeout(function () {
                        alertDiv.alert('close');
                    }, 3000);
                }
            });
        </script>
    </head>
    <body>
        <div class="container">
            <h2 class="mt-4">Chi Tiết Booking: ${booking.userFullName}</h2>

            <!-- Thông báo ban đầu từ server -->
            <c:if test="${not empty error}">
                <div class="alert alert-danger alert-dismissible fade show" role="alert">
                    ${error}
                    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                </div>
            </c:if>
            <c:if test="${not empty success}">
                <div class="alert alert-success alert-dismissible fade show" role="alert">
                    ${success}
                    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                </div>
            </c:if>

            <!-- Thông tin Booking -->
            <div class="section" id="bookingSection">
                <h4>Thông Tin Booking</h4>
                <table class="table table-bordered">
                    <tr><th>Mã Booking</th><td class="read-only"><span id="bookingIdReadOnly">${booking.bookingId}</span></td><td class="editable"><input type="text" id="bookingId" name="bookingId" value="${booking.bookingId}" readonly></td></tr>
                    <tr><th>Mã Người Dùng</th><td class="read-only"><span>${booking.userId}</span></td><td class="editable"><input type="text" value="${booking.userId}" readonly></td></tr>
                    <tr><th>Mã Chuyến Bay</th><td class="read-only"><span id="flightIdReadOnly">${booking.flightId}</span></td><td class="editable"><input type="text" id="flightId" name="flightId" value="${booking.flightId}" readonly></td></tr>
                    <tr><th>Thời Gian Đặt</th>
                        <td class="read-only">
                            <span>
                                <fmt:formatDate value="${booking.bookingDate}" pattern="dd-MM-yyyy HH:mm:ss"/>
                            </span>
                        </td>
                        <td class="editable"><input type="text" value="${booking.bookingDate}" readonly></td>
                    </tr>
                    <tr><th>Trạng Thái</th><td class="read-only"><span id="statusReadOnly">
                            <c:choose>
                                <c:when test="${booking.status == 'CONFIRMED'}">Đã xác nhận</c:when>
                                <c:when test="${booking.status == 'CANCELLED'}">Đã hủy</c:when>
                                <c:when test="${booking.status == 'CHANGED'}">Đã thay đổi</c:when>
                                <c:otherwise>${booking.status}</c:otherwise>
                            </c:choose>
                        </span></td><td class="editable"><select id="status" name="status" class="form-select">
                            <option value="CONFIRMED" ${booking.status == 'CONFIRMED' ? 'selected' : ''}>Đã xác nhận</option>
                            <option value="CANCELLED" ${booking.status == 'CANCELLED' ? 'selected' : ''}>Đã hủy</option>
                            <option value="CHANGED" ${booking.status == 'CHANGED' ? 'selected' : ''}>Đã thay đổi</option>
                        </select></td></tr>
                    <tr><th>Hạng Ghế</th><td class="read-only"><span id="seatClassReadOnly">${booking.seatClass}</span></td><td class="editable"><select id="seatClass" name="seatClass" class="form-select">
                            <option value="Economy" ${booking.seatClass == 'Economy' ? 'selected' : ''}>Economy</option>
                            <option value="Deluxe" ${booking.seatClass == 'Deluxe' ? 'selected' : ''}>Deluxe</option>
                            <option value="SkyBoss" ${booking.seatClass == 'SkyBoss' ? 'selected' : ''}>SkyBoss</option>
                            <option value="Business" ${booking.seatClass == 'Business' ? 'selected' : ''}>Business</option>
                        </select></td></tr>
                    <tr><th>Tổng Giá</th><td class="read-only"><span id="totalPriceReadOnly">${booking.totalPrice}</span></td><td class="editable"><input type="number" id="totalPrice" name="totalPrice" value="${booking.totalPrice}" step="0.01"></td></tr>
                    <tr><th>Trạng Thái Check-in</th><td class="read-only"><span id="checkinStatusReadOnly">
                            <c:choose>
                                <c:when test="${booking.checkinStatus == 'NOT CHECKED-IN'}">Chưa check-in</c:when>
                                <c:when test="${booking.checkinStatus == 'CHECKED-IN'}">Đã check-in</c:when>
                                <c:when test="${booking.checkinStatus == 'BOARDED'}">Đã lên máy bay</c:when>
                                <c:otherwise>${booking.checkinStatus}</c:otherwise>
                            </c:choose>
                        </span></td><td class="editable"><select id="checkinStatus" name="checkinStatus" class="form-select">
                            <option value="NOT CHECKED-IN" ${booking.checkinStatus == 'NOT CHECKED-IN' ? 'selected' : ''}>Chưa check-in</option>
                            <option value="CHECKED-IN" ${booking.checkinStatus == 'CHECKED-IN' ? 'selected' : ''}>Đã check-in</option>
                            <option value="BOARDED" ${booking.checkinStatus == 'BOARDED' ? 'selected' : ''}>Đã lên máy bay</option>
                        </select></td></tr>
                    <tr><th>Ghi Chú Nhân Viên</th><td class="read-only"><span id="staffNoteReadOnly">${booking.staffNote}</span></td><td class="editable"><input type="text" id="staffNote" name="staffNote" value="${booking.staffNote}"></td></tr>
                </table>
                <button id="editBookingBtn" class="btn btn-primary edit-btn">Chỉnh Sửa Booking</button>
                <button id="updateBookingBtn" class="btn btn-primary mt-2 ms-2" style="display: none;">Cập Nhật Booking</button>
                <button id="checkInBtn" class="btn btn-info mt-2 ms-2">Thực Hiện Check-in</button>
            </div>

            <!-- Thông tin Hành Khách -->
            <div class="section" id="passengerSection">
                <h4>Thông Tin Hành Khách</h4>
                <table class="table table-bordered">
                    <tr><th>Mã Hành Khách</th><td class="read-only"><span id="passengerIdReadOnly">${passenger.passengerId}</span></td><td class="editable"><input type="text" id="passengerId" name="passengerId" value="${passenger.passengerId}" readonly></td></tr>
                    <tr><th>Họ Tên</th><td class="read-only"><span id="fullNameReadOnly">${passenger.fullName}</span></td><td class="editable"><input type="text" id="fullName" name="fullName" value="${passenger.fullName}"></td></tr>
                    <tr><th>Số Hộ Chiếu</th><td class="read-only"><span id="passportNumberReadOnly">${passenger.passportNumber}</span></td><td class="editable"><input type="text" id="passportNumber" name="passportNumber" value="${passenger.passportNumber}"></td></tr>
                    <tr><th>Ngày Sinh</th><td class="read-only"><span id="dobReadOnly">${passenger.dob}</span></td><td class="editable"><input type="date" id="dob" name="dob" value="${passenger.dob}"></td></tr>
                    <tr><th>Giới Tính</th><td class="read-only"><span id="genderReadOnly">${passenger.gender}</span></td><td class="editable"><select id="gender" name="gender" class="form-select">
                            <option value="Nam" ${passenger.gender == 'Nam' ? 'selected' : ''}>Nam</option>
                            <option value="Nữ" ${passenger.gender == 'Nữ' ? 'selected' : ''}>Nữ</option>
                        </select></td></tr>
                    <tr><th>Số Điện Thoại</th><td class="read-only"><span id="phoneNumberReadOnly">${passenger.phoneNumber}</span></td><td class="editable"><input type="text" id="phoneNumber" name="phoneNumber" value="${passenger.phoneNumber}"></td></tr>
                    <tr><th>Email</th><td class="read-only"><span id="emailReadOnly">${passenger.email}</span></td><td class="editable"><input type="email" id="email" name="email" value="${passenger.email}"></td></tr>
                    <tr><th>Quốc Gia</th><td class="read-only"><span id="countryReadOnly">${passenger.country}</span></td><td class="editable"><input type="text" id="country" name="country" value="${passenger.country}"></td></tr>
                    <tr><th>Địa Chỉ</th><td class="read-only"><span id="addressReadOnly">${passenger.address}</span></td><td class="editable"><input type="text" id="address" name="address" value="${passenger.address}"></td></tr>
                </table>
                <button id="editPassengerBtn" class="btn btn-primary edit-btn">Chỉnh Sửa Hành Khách</button>
                <button id="updatePassengerBtn" class="btn btn-primary mt-2" style="display: none;">Cập Nhật Hành Khách</button>
            </div>

            <!-- Chi tiết Chuyến Bay -->
            <div class="section">
                <h4>Chi Tiết Chuyến Bay</h4>
                <table class="table table-bordered">
                    <tr><th>Mã Chuyến Bay</th><td><span> ${flight.flightNumber}</span></td></tr>
                    <tr><th>Điểm Đi</th><td><span> ${flight.routeFrom}</span> </td></tr>
                    <tr><th>Điểm Đến</th><td><span>${flight.routeTo} </span></td></tr>
                    <tr><th>Thời Gian Cất Cánh</th><td><span><fmt:formatDate value="${flight.departureTime}" pattern="dd-MM-yyyy HH:mm:ss"/></span></td></tr>
                    <tr><th>Thời Gian Hạ Cánh</th><td><span><fmt:formatDate value="${flight.arrivalTime}" pattern="dd-MM-yyyy HH:mm:ss"/> </span></td></tr>
                    <tr><th>Máy Bay</th><td><span>${flight.aircraft} </span></td></tr>
                </table>
            </div>

            <!-- Hủy Booking -->
            <div class="section">
                <h4>Hủy Booking</h4>
                <div class="form-group">
                    <label>Lý Do Hủy:</label>
                    <input type="text" id="reason" name="reason" placeholder="Nhập lý do hủy">
                </div>
                <button id="cancelBookingBtn" class="btn btn-danger mt-2">Hủy Booking</button>
            </div>

            <!-- Lịch Sử Xử Lý -->
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
                        <c:forEach var="history" items="${history}" varStatus="loop">
                            <tr>
                                <td>${loop.index + 1}</td>
                                <td>${history.role}</td>
                                <td>${history.staffName}</td>
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
                                <td>
                                    <fmt:formatDate value="${history.actionTime}" pattern="dd-MM-yyyy HH:mm:ss" />
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>

            <a href="${pageContext.request.contextPath}/staff/booking/list" class="btn btn-secondary mt-3">Quay Lại Danh Sách</a>
        </div>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>