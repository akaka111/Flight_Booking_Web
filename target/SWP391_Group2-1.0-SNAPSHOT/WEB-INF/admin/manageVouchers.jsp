<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Quản Lý Voucher - Hệ Thống Đặt Vé Máy Bay</title>
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
        <h2 class="mb-4">Quản Lý Voucher</h2>

        <!-- Thông báo -->
        <c:if test="${not empty message}">
            <div class="alert alert-success">${message}</div>
        </c:if>
        <c:if test="${not empty error}">
            <div class="alert alert-danger">${error}</div>
        </c:if>

        <div class="text-right mb-3">
            <a href="manageVouchers?action=create" class="btn-add">
                <i class="fa fa-plus"></i> Thêm Voucher
            </a>
        </div>

        <c:choose>
            <c:when test="${empty list}">
                <div class="alert alert-warning">Hiện chưa có voucher nào.</div>
            </c:when>
            <c:otherwise>
                <table class="table table-bordered">
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Code</th>
                            <th>Discount %</th>
                            <th>Valid From</th>
                            <th>Expiry</th>
                            <th>Use Time</th>
                            <th>Thao tác</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="v" items="${list}">
                            <tr>
                                <td>${v.voucher_id}</td>
                                <td>${v.code}</td>
                                <td>${v.discount_percent}</td>
                                <td>${v.valid_from}</td>
                                <td>${v.valid_to}</td>
                                <td>${v.usage_limit}</td>
                                <td class="action-buttons">
                                    <a href="manageVouchers?action=edit&id=${v.voucher_id}" class="btn btn-sm btn-warning">
                                        <i class="fa fa-edit"></i> Sửa
                                    </a>
                                    <form action="manageVouchers" method="post" style="display:inline;" onsubmit="return confirm('Xác nhận xóa voucher này?');">
                                        <input type="hidden" name="action" value="delete">
                                        <input type="hidden" name="id" value="${v.voucher_id}">
                                        <button type="submit" class="btn btn-sm btn-danger">
                                            <i class="fa fa-trash"></i> Xóa
                                        </button>
                                    </form>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </c:otherwise>
        </c:choose>
    </div>
</div>

<script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
</body>
</html>