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
        <c:if test="${not empty message}">
            <div class="alert alert-success">${message}</div>
        </c:if>
        <c:if test="${not empty error}">
            <div class="alert alert-danger">${error}</div>
        </c:if>

        <form method="post" action="FlightAdmin1">
            <input type="hidden" name="action" value="updateFlight">
            <input type="hidden" name="id" value="${flight.flightId}">
            <div class="form-group">
                <label for="flightNumber">Số hiệu chuyến bay</label>
                <input type="text" class="form-control" id="flightNumber" name="flightNumber" value="${flight.flightNumber}" required>
            </div>
            <div class="form-group">
                <label for="routeFrom">Điểm đi</label>
                <input type="text" class="form-control" id="routeFrom" name="routeFrom" value="${flight.routeFrom}" required>
            </div>
            <div class="form-group">
                <label for="routeTo">Điểm đến</label>
                <input type="text" class="form-control" id="routeTo" name="routeTo" value="${flight.routeTo}" required>
            </div>
            <div class="form-group">
                <label for="departureTime">Thời gian khởi hành</label>
                <input type="datetime-local" class="form-control" id="departureTime" name="departureTime"
                       value="<fmt:formatDate value='${flight.departureTime}' pattern='yyyy-MM-dd\'T\'HH:mm'/>" required>
            </div>
            <div class="form-group">
                <label for="arrivalTime">Thời gian đến</label>
                <input type="datetime-local" class="form-control" id="arrivalTime" name="arrivalTime"
                       value="<fmt:formatDate value='${flight.arrivalTime}' pattern='yyyy-MM-dd\'T\'HH:mm'/>" required>
            </div>
            <div class="form-group">
                <label for="price">Giá vé (ECO)</label>
                <input type="number" class="form-control" id="price" name="price" step="0.01" value="${flight.price}" required>
            </div>
            <div class="form-group">
                <label for="aircraft">Loại máy bay</label>
                <input type="text" class="form-control" id="aircraft" name="aircraft" value="${flight.aircraft}" required>
            </div>
            <div class="form-group">
                <label for="status">Trạng thái</label>
                <select class="form-control" id="status" name="status">
                    <option value="Scheduled" ${flight.status == 'Scheduled' ? 'selected' : ''}>Lên lịch</option>
                    <option value="Completed" ${flight.status == 'Completed' ? 'selected' : ''}>Hoàn tất</option>
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
</body>
</html>