<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Thêm chuyến bay mới</title>
        <style>
            body {
                font-family: Arial;
                padding: 40px;
                background-color: #f9f9f9;
            }
            h2 {
                margin-bottom: 20px;
            }
            form label {
                display: block;
                margin-top: 10px;
            }
            form input, form select {
                width: 300px;
                padding: 8px;
                margin-top: 4px;
                border: 1px solid #ccc;
                border-radius: 4px;
            }
            button {
                margin-top: 20px;
                padding: 10px 20px;
                background-color: #28a745;
                color: white;
                border: none;
                border-radius: 5px;
                cursor: pointer;
            }
            a {
                display: inline-block;
                margin-top: 20px;
                text-decoration: none;
                color: #007bff;
            }
        </style>
    </head>
    <body>
        <h2>Thêm chuyến bay mới</h2>
        <form method="post" action="FlightAdmin1">
            <input type="hidden" name="action" value="addFlight" />
            <label>Số hiệu chuyến bay:</label>
            <input type="text" name="flightNumber" required>

            <label>Điểm đi:</label>
            <input type="text" name="routeFrom" required>

            <label>Điểm đến:</label>
            <input type="text" name="routeTo" required>

            <label>Thời gian khởi hành:</label>
            <input type="datetime-local" name="departureTime" required>

            <label>Thời gian đến:</label>
            <input type="datetime-local" name="arrivalTime" required>

            <label>Giá vé (ECO):</label>
            <input type="number" step="0.01" name="price" required>

            <label>Loại máy bay:</label>
            <input type="text" name="aircraft" required>

            <label>Trạng thái:</label>
            <select name="status">
                <option value="Scheduled">Lên lịch</option>
                <option value="Completed">Hoàn tất</option>
            </select>

            <button type="submit">Lưu chuyến bay</button>
        </form>

        <a href="FlightAdmin1">← Quay lại danh sách</a>
    </body>
</html>
