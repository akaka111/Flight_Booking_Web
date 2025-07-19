<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Thêm Voucher Mới - Hệ Thống Đặt Vé Máy Bay</title>
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
        <h2><i class="fa fa-ticket"></i> Thêm Voucher Mới</h2>

        <!-- Thông báo -->
        <c:if test="${not empty message}">
            <div class="alert alert-success">${message}</div>
        </c:if>
        <c:if test="${not empty error}">
            <div class="alert alert-danger">${error}</div>
        </c:if>

        <form action="manageVouchers" method="post">
            <input type="hidden" name="action" value="create">
            <div class="form-group">
                <label for="code">Mã Voucher</label>
                <input type="text" class="form-control" id="code" name="code" required>
            </div>
            <div class="form-group">
                <label for="discount">Phần trăm giảm giá (%)</label>
                <input type="number" class="form-control" id="discount" name="discount" min="0" max="100" required>
            </div>
            <div class="form-group">
                <label for="valid_from">Ngày bắt đầu</label>
                <input type="date" class="form-control" id="valid_from" name="valid_from" required>
            </div>
            <div class="form-group">
                <label for="valid_to">Ngày kết thúc</label>
                <input type="date" class="form-control" id="valid_to" name="valid_to" required>
            </div>
            <div class="form-group">
                <label for="usage_limit">Giới hạn sử dụng</label>
                <input type="number" class="form-control" id="usage_limit" name="usage_limit" min="1" required>
            </div>
            <div class="form-actions mt-4">
                <a href="manageVouchers" class="btn btn-secondary">
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