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
            // Hàm lấy giá trị cookie
            function getCookie(name) {
                let cookies = document.cookie.split(';');
                for (let cookie of cookies) {
                    let [key, value] = cookie.trim().split('=');
                    if (key === name) {
                        return decodeURIComponent(value);
                    }
                }
                return null;
            }

            $(document).ready(function () {
                let isBookingCancelled = "${booking.status}" === "CANCELLED";
                let staffId = getCookie("staffId");
                let staffRole = getCookie("staffRole");
                let staffName = getCookie("staffName") ? getCookie("staffName").replace(/_/g, ' ') : null;

                // Kiểm tra cookie staffId


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
                            staffId: staffId,
                            staffRole: staffRole,
                            staffName: staffName
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
                                // Tải lại lịch sử
                                loadBookingHistory(formData.bookingId);
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
                            staffId: staffId,
                            staffRole: staffRole,
                            staffName: staffName
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
                                // Tải lại lịch sử
                                loadBookingHistory(formData.bookingId);
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
                            staffId: staffId,
                            staffRole: staffRole,
                            staffName: staffName
                        };
                        $.ajax({
                            url: '${pageContext.request.contextPath}/staff/booking/checkin',
                            type: 'POST',
                            data: formData,
                            success: function (response) {
                                showAlert("Check-in thành công!", "success");
                                $("#checkinStatusReadOnly").text("Đã check-in");
                                $("#checkinStatus").val("CHECKED-IN");
                                // Tải lại lịch sử
                                loadBookingHistory(formData.bookingId);
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
                        if (!reason) {
                            showAlert("Vui lòng nhập lý do hủy!", "danger");
                            return;
                        }
                        if (confirm("Bạn có chắc chắn muốn hủy booking này? Hành động này không thể hoàn tác!")) {
                            var formData = {
                                bookingId: $("#bookingId").val(),
                                reason: reason,
                                staffId: staffId,
                                staffRole: staffRole,
                                staffName: staffName
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
                                    // Tải lại lịch sử
                                    loadBookingHistory(formData.bookingId);
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

                // Hàm tải lại lịch sử booking
                function loadBookingHistory(bookingId) {
                    $.ajax({
                        url: '${pageContext.request.contextPath}/staff/booking/details',
                        type: 'GET',
                        data: {bookingId: bookingId},
                        success: function (response) {
                            // Cập nhật bảng lịch sử
                            $("#historyTable tbody").html($(response).find("#historyTable tbody").html());
                            showAlert("Cập nhật lịch sử thành công!", "success");
                        },
                        error: function (xhr) {
                            showAlert("Tải lịch sử thất bại: " + xhr.responseText, "danger");
                        }
                    });
                }

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
                        <td class="editable"><input type="text" value="<fmt:formatDate value='${booking.bookingDate}' pattern='yyyy-MM-dd HH:mm:ss'/>" readonly></td>
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
                <button id="checkInBtn" class="btn btn-success mt-2 ms-2">Thực Hiện Check-in</button>
            </div>

            <!-- Thông tin Hành Khách -->
            <div class="section" id="passengerSection">
                <h4>Thông Tin Hành Khách</h4>
                <table class="table table-bordered">
                    <tr><th>Mã Hành Khách</th><td class="read-only"><span id="passengerIdReadOnly">${passenger.passengerId}</span></td><td class="editable"><input type="text" id="passengerId" name="passengerId" value="${passenger.passengerId}" readonly></td></tr>
                    <tr><th>Họ Tên</th><td class="read-only"><span id="fullNameReadOnly">${passenger.fullName}</span></td><td class="editable"><input type="text" id="fullName" name="fullName" value="${passenger.fullName}"></td></tr>
                    <tr><th>Số Hộ Chiếu</th><td class="read-only"><span id="passportNumberReadOnly">${passenger.passportNumber}</span></td><td class="editable"><input type="text" id="passportNumber" name="passportNumber" value="${passenger.passportNumber}"></td></tr>
                    <tr><th>Ngày Sinh</th><td class="read-only"><span id="dobReadOnly"><fmt:formatDate value="${passenger.dob}" pattern="dd-MM-yyyy"/></span></td><td class="editable"><input type="date" id="dob" name="dob" value="<fmt:formatDate value='${passenger.dob}' pattern='yyyy-MM-dd'/>"></td></tr>
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
                    <tr><th>Mã Chuyến Bay</th><td><span>${flight.flightNumber}</span></td></tr>
                    <tr><th>Điểm Đi</th><td><span>${flight.routeFrom}</span></td></tr>
                    <tr><th>Điểm Đến</th><td><span>${flight.routeTo}</span></td></tr>
                    <tr><th>Thời Gian Khởi Hành</th><td><span><fmt:formatDate value="${flight.departureTime}" pattern="dd-MM-yyyy HH:mm:ss"/></span></td></tr>
                    <tr><th>Thời Gian Đến</th><td><span><fmt:formatDate value="${flight.arrivalTime}" pattern="dd-MM-yyyy HH:mm:ss"/></span></td></tr>
                    <tr><th>Máy Bay</th><td><span>${flight.aircraft}</span></td></tr>
                    <tr><th>Trạng Thái</th><td><span>${flight.status}</span></td></tr>
                </table>
            </div>

            <!-- Lịch Sử Booking -->
            <div class="section">
                <h4>Lịch Sử Booking</h4>
                <table class="table table-bordered" id="historyTable">
                    <thead>
                        <tr>
                            <th>Thời Gian</th>
                            <th>Hành Động</th>
                            <th>Mô Tả</th>
                            <th>Nhân Viên</th>
                            <th>Vai Trò</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="history" items="${history}">
                            <tr>
                                <td><fmt:formatDate value="${history.actionTime}" pattern="dd-MM-yyyy HH:mm:ss"/></td>
                                <td>${history.action}</td>
                                <td>${history.description}</td>
                                <td>${history.staffName}</td>
                                <td>${history.role}</td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>

            <!-- Hủy Booking -->
            <c:if test="${booking.status != 'CANCELLED'}">
                <div class="section">
                    <h4>Hủy Booking</h4>
                    <div class="form-group">
                        <label for="reason">Lý Do Hủy:</label>
                        <input type="text" id="reason" name="reason" class="form-control" style="width: 300px;" placeholder="Nhập lý do hủy">
                    </div>
                    <button id="cancelBookingBtn" class="btn btn-danger">Hủy Booking</button>
                </div>
            </c:if>

            <a href="${pageContext.request.contextPath}/staff/booking/list" class="btn btn-secondary mt-3">Quay Lại Danh Sách</a>
        </div>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>