<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Sửa chuyến bay</title>
    </head>
    <body>
        <h2>Sửa thông tin chuyến bay</h2>
        <form method="post" action="FlightAdmin1">
            <input type="hidden" name="action" value="updateFlight"/>
            <input type="hidden" name="id" value="${flight.flightId}"/>

            Số hiệu: <input type="text" name="flightNumber" value="${flight.flightNumber}" required><br>
            Điểm đi: <input type="text" name="routeFrom" value="${flight.routeFrom}" required><br>
            Điểm đến: <input type="text" name="routeTo" value="${flight.routeTo}" required><br>
            Khởi hành: <input type="datetime-local" name="departureTime" 
                              value="<fmt:formatDate value='${flight.departureTime}' pattern='yyyy-MM-dd\'T\'HH:mm'/>" required><br>
            Đến nơi: <input type="datetime-local" name="arrivalTime" 
                            value="<fmt:formatDate value='${flight.arrivalTime}' pattern='yyyy-MM-dd\'T\'HH:mm'/>" required><br>
            Giá: <input type="number" step="0.01" name="price" value="${flight.price}" required><br>
            Máy bay: <input type="text" name="aircraft" value="${flight.aircraft}" required><br>
            Trạng thái:
            <select name="status">
                <option value="Scheduled" ${flight.status == 'Scheduled' ? 'selected' : ''}>Lên lịch</option>
                <option value="Completed" ${flight.status == 'Completed' ? 'selected' : ''}>Hoàn tất</option>
            </select><br><br>

            <button type="submit">Lưu thay đổi</button>
        </form>
    </body>
</html>
