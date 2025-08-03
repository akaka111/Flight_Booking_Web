<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Quản Lý Chuyến Bay - Hệ Thống Đặt Vé Máy Bay</title>
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
            }
            .table-container {
                background-color: #ffffff;
                padding: 20px;
                margin: 30px auto;
                border-radius: 8px;
                box-shadow: 0 5px 15px rgba(0,0,0,0.1);
                max-width: 100%;
            }
            .btn-add {
                padding: 10px 20px;
                font-size: 16px;
                background-color: #28a745;
                color: white;
                border: none;
                border-radius: 6px;
                text-decoration: none;
                font-weight: 500;
            }
            .action-buttons {
                display: flex;
                gap: 8px;
            }
            .action-buttons .btn {
                font-size: 14px;
                padding: 4px 10px;
            }
            table th {
                background-color: #007bff;
                color: white;
            }
        </style>
    </head>
    <body>
        <div class="container">
            <div class="table-container">
                <h2 class="mb-4">Quản Lý Chuyến Bay</h2>

                <!-- Thông báo -->
                <c:if test="${not empty message}">
                    <div class="alert alert-success">${message}</div>
                </c:if>
                <c:if test="${not empty error}">
                    <div class="alert alert-danger">${error}</div>
                </c:if>

                <div class="text-right mb-3">
                    <a href="FlightAdmin1?action=showAddForm" class="btn-add">
                        <i class="fa fa-plus"></i> Thêm Chuyến Bay
                    </a>
                </div>

                <table class="table table-bordered">
                    <thead>
                        <tr>
                            <th>Số hiệu</th>
                            <th>Điểm đi</th>
                            <th>Điểm đến</th>
                            <th>Khởi hành</th>
                            <th>Đến nơi</th>
                            <th>Máy bay</th>
                            <th>Trạng thái</th>
                            <th>Thao tác</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="f" items="${flights}">
                            <tr>
                                <td>${f.flightNumber}</td>
                                <td>${f.routeFrom}</td>
                                <td>${f.routeTo}</td>
                                <td>${f.departureTime}</td>
                                <td>${f.arrivalTime}</td>
                                <td>${f.aircraft}</td>
                                <td>${f.status}</td>
                                <td class="action-buttons">
                                    <a href="FlightAdmin1?action=editFlight&id=${f.flightId}" class="btn btn-sm btn-warning">
                                        <i class="fa fa-edit"></i> Sửa
                                    </a>
                                    <form method="post" action="FlightAdmin1" onsubmit="return confirm('Xác nhận xóa chuyến bay này?')">
                                        <input type="hidden" name="action" value="deleteFlight"/>
                                        <input type="hidden" name="id" value="${f.flightId}"/>
                                        <button type="submit" class="btn btn-sm btn-danger">
                                            <i class="fa fa-trash"></i> Xóa
                                        </button>
                                    </form>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>

        <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
        <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
    </body>
</html>