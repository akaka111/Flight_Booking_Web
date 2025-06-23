<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Quản lý Hãng Bay</title>
    <link href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<div class="container mt-5">
    <h2 class="mb-4">Quản lý Hãng Bay</h2>

    <!-- Thông báo -->
    <c:if test="${not empty message}">
        <div class="alert alert-success">${message}</div>
    </c:if>
    <c:if test="${not empty error}">
        <div class="alert alert-danger">${error}</div>
    </c:if>

    <!-- Nút thêm hãng bay -->
    <div class="mb-3">
        <a href="${pageContext.request.contextPath}/AirlineAdmin?action=new" class="btn btn-primary">+ Thêm Hãng Bay</a>
    </div>

    <!-- Danh sách hãng bay -->
    <div class="card mb-4">
        <div class="card-header bg-info text-white">Danh sách Hãng Bay</div>
        <div class="card-body p-0">
            <table class="table table-bordered mb-0">
                <thead class="thead-light">
                <tr>
                    <th>STT</th>
                    <th>Tên Hãng Bay</th>
                    <th>Mã</th>
                    <th>Mô Tả</th>
                    <th>Dịch Vụ</th>
                    <th>Hành Động</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="airline" items="${airlines}" varStatus="status">
                    <tr>
                        <td>${status.index + 1}</td> <!-- STT tự động -->
                        <td>${airline.name}</td>
                        <td>${airline.code}</td>
                        <td>${airline.description}</td>
                        <td>${airline.services}</td>
                        <td>
                            <a href="${pageContext.request.contextPath}/AirlineAdmin?action=edit&id=${airline.airlineId}" class="btn btn-sm btn-warning">Sửa</a>
                            <a href="${pageContext.request.contextPath}/AirlineAdmin?action=delete&id=${airline.airlineId}"
                               class="btn btn-sm btn-danger"
                               onclick="return confirm('Bạn có chắc muốn xóa hãng bay này?')">Xóa</a>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </div>

    <!-- Form Thêm/Sửa hãng bay -->
    <c:if test="${not empty airline || param.action == 'new'}">
        <div class="card">
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

<!-- Bootstrap scripts -->
<script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
</body>
</html>