<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Chỉnh Sửa Chuyến Bay - Hệ Thống Đặt Vé Máy Bay</title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Montserrat:wght@400;500;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css"/>
    <style>
        body {
            font-family: 'Montserrat', sans-serif;
            background-color: #f8f9fa;
            color: #343a40;
            padding: 40px;
        }
        .form-container {
            max-width: 900px;
            margin: auto;
            background: #fff;
            padding: 30px;
            border-radius: 10px;
            box-shadow: 0 5px 15px rgba(0,0,0,0.1);
        }
        h2 {
            text-align: center;
            margin-bottom: 30px;
            color: #007bff;
        }
        .form-actions {
            text-align: right;
        }
        .form-actions .btn {
            min-width: 120px;
        }
    </style>
</head>
<body>
<div class="container">
    <div class="form-container">
        <h2><i class="fa fa-plane"></i> Chỉnh Sửa Chuyến Bay</h2>
        <!-- Thông báo -->
        <c:if test="${not empty msg}">
            <div class="alert alert-success">${msg}</div>
        </c:if>
        <c:if test="${not empty error}">
            <div class="alert alert-danger">${error}</div>
        </c:if>
        <form method="post" action="FlightAdmin1" onsubmit="return validateForm()">
            <input type="hidden" name="action" value="updateFlight">
            <input type="hidden" name="flightId" value="${flight.flightId}">
            <div class="form-group">
                <label for="airlineId">Hãng bay</label>
                <select class="form-control" id="airlineId" name="airlineId" required>
                    <option value="">Chọn hãng bay</option>
                    <c:forEach var="airline" items="${airlines}">
                        <option value="${airline.airlineId}" ${airline.airlineId == flight.airline.airlineId ? 'selected' : ''}>${airline.name} (${airline.code})</option>
                    </c:forEach>
                </select>
            </div>
            <div class="form-group">
                <label for="flightNumber">Số hiệu chuyến bay</label>
                <input type="text" class="form-control" id="flightNumber" name="flightNumber" value="${flight.flightNumber}" required pattern="[A-Z0-9]{4,10}">
            </div>
            <div class="form-group">
                <label for="routeId">Tuyến đường</label>
                <select class="form-control" id="routeId" name="routeId" required>
                    <option value="">Chọn tuyến đường</option>
                    <c:forEach var="route" items="${routes}">
                        <option value="${route.routeId}" ${route.routeId == flight.route.routeId ? 'selected' : ''}>${route.originCity} (${route.originIata}) -> ${route.destCity} (${route.destIata})</option>
                    </c:forEach>
                </select>
            </div>
            <div class="form-group">
                <label for="departureTime">Thời gian khởi hành</label>
                <input type="datetime-local" class="form-control" id="departureTime" name="departureTime" value="${flight.departureTime.toString().replace(' ', 'T').substring(0,16)}" required min="${today}">
            </div>
            <div class="form-group">
                <label for="arrivalTime">Thời gian đến</label>
                <input type="datetime-local" class="form-control" id="arrivalTime" name="arrivalTime" value="${flight.arrivalTime.toString().replace(' ', 'T').substring(0,16)}" required min="${today}">
            </div>
            <div class="form-group">
                <label for="aircraftTypeId">Loại máy bay</label>
                <select class="form-control" id="aircraftTypeId" name="aircraftTypeId" required>
                    <option value="">Chọn loại máy bay</option>
                    <c:forEach var="aircraftType" items="${aircraftTypes}">
                        <option value="${aircraftType.aircraftTypeId}" ${aircraftType.aircraftTypeId == flight.aircraftType.aircraftTypeId ? 'selected' : ''}>${aircraftType.aircraftTypeName} (${aircraftType.aircraftTypeCode})</option>
                    </c:forEach>
                </select>
            </div>
            <div class="form-group">
                <label for="status">Trạng thái</label>
                <select class="form-control" id="status" name="status" required>
                    <option value="Scheduled" ${flight.status == 'Scheduled' ? 'selected' : ''}>Đang hoạt động</option>
                    <option value="ON TIME" ${flight.status == 'ON TIME' ? 'selected' : ''}>Đúng giờ</option>
                    <option value="Delayed" ${flight.status == 'Delayed' ? 'selected' : ''}>Hoãn</option>
                    <option value="Cancelled" ${flight.status == 'Cancelled' ? 'selected' : ''}>Hủy</option>
                </select>
            </div>
            <div class="form-actions mt-4">
                <a href="FlightAdmin1" class="btn btn-secondary">
                    <i class="fa fa-arrow-left"></i> Hủy
                </a>
                <button type="submit" class="btn btn-success">
                    <i class="fa fa-save"></i> Lưu
                </button>
            </div>
        </form>
    </div>
</div>
<script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
<script>
    function validateForm() {
        const flightNumber = document.getElementById('flightNumber').value;
        const departureTime = document.getElementById('departureTime').value;
        const arrivalTime = document.getElementById('arrivalTime').value;
        const airlineId = document.getElementById('airlineId').value;
        const routeId = document.getElementById('routeId').value;
        const aircraftTypeId = document.getElementById('aircraftTypeId').value;
        const status = document.getElementById('status').value;

        if (!flightNumber || !departureTime || !arrivalTime || !airlineId || !routeId || !aircraftTypeId || !status) {
            alert('Vui lòng điền đầy đủ thông tin.');
            return false;
        }

        if (!flightNumber.match(/^[A-Z0-9]{4,10}$/)) {
            alert('Số hiệu chuyến bay phải từ 4-10 ký tự, chỉ chứa chữ cái in hoa và số.');
            return false;
        }

        const departure = new Date(departureTime);
        const arrival = new Date(arrivalTime);
        const now = new Date();
        if (departure < now) {
            alert('Thời gian khởi hành phải từ hiện tại trở đi.');
            return false;
        }
        if (arrival <= departure) {
            alert('Thời gian đến phải sau thời gian khởi hành.');
            return false;
        }

        return true;
    }
</script>
</body>
</html>