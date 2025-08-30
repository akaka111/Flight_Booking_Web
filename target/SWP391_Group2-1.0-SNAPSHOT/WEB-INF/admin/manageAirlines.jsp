<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Quản Lý Hãng Bay - Hệ Thống Đặt Vé Máy Bay</title>
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
        <h2 class="mb-4">Quản Lý Hãng Bay</h2>

        <!-- Thông báo -->
        <c:if test="${not empty message}">
            <div class="alert alert-success">${message}</div>
        </c:if>
        <c:if test="${not empty error}">
            <div class="alert alert-danger">${error}</div>
        </c:if>

        <div class="text-right mb-3">
            <a href="${pageContext.request.contextPath}/AirlineAdmin?action=new" class="btn-add">
                <i class="fa fa-plus"></i> Thêm Hãng Bay
            </a>
        </div>

        <table class="table table-bordered">
            <thead>
                <tr>
                    <th>STT</th>
                    <th>Tên Hãng Bay</th>
                    <th>Mã</th>
                    <th>Mô Tả</th>
                    <th>Dịch Vụ</th>
                    <th>Thao tác</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="airline" items="${airlines}" varStatus="status">
                    <tr>
                        <td>${status.index + 1}</td>
                        <td>${airline.name}</td>
                        <td>${airline.code}</td>
                        <td>${airline.description}</td>
                        <td>${airline.services}</td>
                        <td class="action-buttons">
                            <a href="${pageContext.request.contextPath}/AirlineAdmin?action=edit&id=${airline.airlineId}" class="btn btn-sm btn-warning">
                                <i class="fa fa-edit"></i> Sửa
                            </a>
                            <a href="${pageContext.request.contextPath}/AirlineAdmin?action=delete&id=${airline.airlineId}"
                               class="btn btn-sm btn-danger"
                               onclick="return confirm('Bạn có chắc muốn xóa hãng bay này?')">
                                <i class="fa fa-trash"></i> Xóa
                            </a>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>

        <!-- Form Thêm/Sửa hãng bay -->
        <c:if test="${not empty airline || param.action == 'new'}">
            <div class="card mt-4">
                <div class="card-header bg-secondary text-white">
                    ${param.action == 'new' ? 'Thêm Hãng Bay' : 'Chỉnh sửa Hãng Bay'}
                </div>
                <div class="card-body">
                    <form action="${pageContext.request.contextPath}/AirlineAdmin" method="post">
                        <input type="hidden" name="action" value="${param.action == 'new' ? 'add' : 'update'}">
                        <c:if test="${not empty airline}">
                            <input type="hidden" name="id" value="${airline.airlineId}">
                        </c:if>
                        <div class="form-group">
                            <label for="name">Tên Hãng Bay</label>
                            <input type="text" id="name" name="name" class="form-control" value="${airline.name}" required>
                        </div>
                        <div class="form-group">
                            <label for="code">Mã Hãng Bay</label>
                            <input type="text" id="code" name="code" class="form-control" value="${airline.code}" required>
                        </div>
                        <div class="form-group">
                            <label for="description">Mô Tả</label>
                            <textarea id="description" name="description" class="form-control" rows="3">${airline.description}</textarea>
                        </div>
                        <div class="form-group">
                            <label for="services">Dịch Vụ</label>
                            <textarea id="services" name="services" class="form-control" rows="3">${airline.services}</textarea>
                        </div>
                        <button type="submit" class="btn btn-success">Lưu</button>
                        <a href="${pageContext.request.contextPath}/AirlineAdmin" class="btn btn-secondary ml-2">Hủy</a>
                    </form>
                </div>
            </div>
        </c:if>
    </div>
</div>

<script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
</body>
</html>