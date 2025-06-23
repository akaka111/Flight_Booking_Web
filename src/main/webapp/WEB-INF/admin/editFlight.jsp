<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Sửa chuyến bay</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css"/>
    <style>
        body {
            background-color: #f8f9fa;
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            padding: 40px;
        }

        .form-container {
            background-color: white;
            padding: 30px;
            border-radius: 10px;
            max-width: 1000px;
            margin: 0 auto;
            box-shadow: 0 5px 15px rgba(0,0,0,0.1);
        }

        h2 {
            color: #007bff;
            margin-bottom: 25px;
        }

        .btn-primary {
            background-color: #007bff;
            border: none;
        }

        .btn-secondary {
            margin-left: 10px;
        }

        a.back-link {
            display: inline-block;
            margin-top: 20px;
            color: #007bff;
            text-decoration: none;
        }

        a.back-link:hover {
            text-decoration: underline;
        }

        label {
            font-weight: 500;
        }
    </style>
</head>
<body>

<div class="form-container">
    <h2><i class="fa fa-edit"></i> Sửa Thông Tin Chuyến Bay</h2>

    <form method="post" action="FlightAdmin1">
        <input type="hidden" name="action" value="updateFlight"/>
        <input type="hidden" name="id" value="${flight.flightId}"/>

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
            <input type="number" class="form-control" id="price" step="0.01" name="price" value="${flight.price}" required>
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

        <button type="submit" class="btn btn-success">
            <i class="fa fa-save"></i> Lưu thay đổi
        </button>
        <a href="FlightAdmin1" class="btn btn-secondary">
            <i class="fa fa-arrow-left"></i> Quay lại danh sách
        </a>
    </form>
</div>

<!-- Bootstrap scripts -->
<script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
</body>
</html>
