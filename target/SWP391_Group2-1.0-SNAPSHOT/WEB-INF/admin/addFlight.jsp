<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Thêm Chuyến Bay Mới - Hệ Thống Đặt Vé Máy Bay</title>
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
                <h2><i class="fa fa-plane"></i> Thêm Chuyến Bay Mới</h2>

                <!-- Thông báo -->
                <c:if test="${not empty message}">
                    <div class="alert alert-success">${message}</div>
                </c:if>
                <c:if test="${not empty error}">
                    <div class="alert alert-danger">${error}</div>
                </c:if>

                <form method="post" action="FlightAdmin1">
                    <input type="hidden" name="action" value="addFlight">
                    <div class="form-group">

                        <label for="airline">Hãng bay</label>
                        <select class="form-control" id="airline" name="airline"  onchange="updateTicketOptions()" required>
                            <option value="" disabled selected hidden>Chọn hãng bay</option>
                            <option value="VJ">VietJet Air (VJ)</option>
                            <option value="VN">Vietnam Airlines (VN)</option>
                            <option value="QH">Bamboo Airways (QH)</option>


                        </select>
                    </div>
                    <div class="form-group">
                        <label for="flightNumber">Số hiệu chuyến bay</label>
                        <input type="text" class="form-control" id="flightNumber" name="flightNumber" required>
                    </div>
                    <div class="form-group">
                        <label for="routeFrom">Điểm khởi hành</label>
                        <input type="text" class="form-control" id="routeFrom" name="routeFrom" required>
                    </div>
                    <div class="form-group">
                        <label for="routeTo">Điểm đến</label>
                        <input type="text" class="form-control" id="routeTo" name="routeTo" required>
                    </div>           
                    <div class="form-group">
                        <label for="departureTime">Thời gian khởi hành</label>
                        <input type="datetime-local" class="form-control" id="departureTime" name="departureTime" required>
                    </div>
                    <div class="form-group">
                        <label for="arrivalTime">Thời gian đến</label>
                        <input type="datetime-local" class="form-control" id="arrivalTime" name="arrivalTime" required>
                    </div>
                    <div class="form-group">
                        <label for="routeFrom">Số ghế</label>
                        <input type="text" class="form-control" id="routeFrom" name="routeFrom" required>
                    </div>
                    <div class="form-group">
                        <label for="ticketType">Loại vé</label>
                        <select class="form-control" id="ticketType" name="ticketType" disabled required>
                            <option value="" disabled selected hidden>Chọn loại vé</option>
                            <option value="SKYBOSS">Sky Boss</option>
                            <option value="BUSINESS">Thương gia (Business)</option>
                            <option value="DELUXE">Deluxe</option>
                            <option value="ECO">Phổ thông (Eco)</option>
                        </select>
                    </div>
                    <div class="form-group">
                        <label for="price">Giá vé</label>
                        <input type="number" class="form-control" id="price" name="price" step="0.01" required>
                    </div>      
                    <div class="form-group">
                        <label for="status">Trạng thái</label>
                        <select class="form-control" id="status" name="status">
                            <option value="" disabled selected hidden>Xác nhận</option>
                            <option value="ON TIME">ON TIME</option>
                            <option value="DELAYED">DELAYED</option>
                            <option value="CANCELLED">CANCELLED</option>
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
        <script>
            function updateTicketOptions() {
                const airline = document.getElementById("airline").value;
                const ticketSelect = document.getElementById("ticketType");

                
                if (!airline) {
                    ticketSelect.disabled = true;
                    ticketSelect.innerHTML = '<option value="" disabled selected hidden>Chọn loại vé</option>';
                    return;
                }

                
                ticketSelect.disabled = false;
                ticketSelect.innerHTML = '<option value="" disabled selected hidden>Chọn loại vé</option>';

                let options = [];

                if (airline === "VN") {
                    options = [
                        {value: "BUSINESS_FLEX", text: "Hạng Thương gia - Business Flex"},
                        {value: "BUSINESS_LITE", text: "Hạng Thương gia - Business Lite"},
                        {value: "PREMIUM_FLEX", text: "Phổ thông đặc biệt - Premium Flex"},
                        {value: "PREMIUM_LITE", text: "Phổ thông đặc biệt - Premium Lite"},
                        {value: "ECONOMY_FLEX", text: "Phổ thông - Economy Flex"},
                        {value: "ECONOMY_STANDARD", text: "Phổ thông - Economy Standard"},
                        {value: "ECONOMY_LITE", text: "Phổ thông - Economy Lite"}
                    ];
                } else if (airline === "VJ") {
                    options = [
                        {value: "SKYBOSS", text: "Hạng SkyBoss"},
                        {value: "BUSINESS", text: "Hạng Thương gia (Business)"},
                        {value: "DELUXE", text: "Hạng Deluxe"},
                        {value: "ECO", text: "Hạng Phổ thông (Eco)"}
                    ];
                } else if (airline === "QH") {
                    options = [
                        {value: "BAMBOO_BUSINESS", text: "Hạng Thương gia (Business)"},
                        {value: "BAMBOO_SMART", text: "Hạng Smart"},
                        {value: "BAMBOO_SAVER", text: "Hạng Tiết kiệm (Saver)"}
                    ];
                }

                options.forEach(opt => {
                    const option = document.createElement("option");
                    option.value = opt.value;
                    option.textContent = opt.text;
                    ticketSelect.appendChild(option);
                });
            }
        </script>
        <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
        <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
    </body>
</html>