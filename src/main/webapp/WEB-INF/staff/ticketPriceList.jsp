<%-- 
    Document   : ticketPriceList
    Created on : Aug 30, 2025, 4:07:33 AM
    Author     : Khoa
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>
<head>
    <title>Quản Lý Giá Vé</title>
    <!-- Bootstrap 5 -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet"/>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</head>
<body class="bg-light">

<div class="container mt-4">
    <h2 class="text-center mb-4">Quản Lý Giá Vé</h2>

    <div class="card shadow-sm">
        <div class="card-body">
            <table class="table table-hover align-middle">
                <thead class="table-hover">
                    <tr>
                        <th>Mã Chuyến Bay</th>
                        <th>Hãng Hàng Không</th>
                        <th>Nơi Đi</th>
                        <th>Nơi Đến</th>
                        <th>Thời Gian Đi</th>
                        <th>Thời Gian Đến</th>
                        <th>Trạng Thái Giá</th>
                        <th>Hành Động</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="flight" items="${flights}">
                        <tr>
                            <td>${flight[1]}</td>
                            <td>${flight[2]}</td>
                            <td>${flight[3]}</td>
                            <td>${flight[4]}</td>
                            <td><fmt:formatDate value="${flight[5]}" pattern="dd-MM-yyyy HH:mm"/></td>
                            <td><fmt:formatDate value="${flight[6]}" pattern="dd-MM-yyyy HH:mm"/></td>
                            <td>
                                <c:choose>
                                    <c:when test="${flight[7] == 'Đã cập nhật'}">
                                        <span class="badge bg-success">Đã cập nhật</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="badge bg-warning text-dark">Chưa cập nhật</span>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td>
                                <a href="ticketPrice?action=update&flightId=${flight[0]}" 
                                   class="btn btn-sm ${flight[7] == 'Đã cập nhật' ? 'btn-outline-primary' : 'btn-primary'}">
                                   <i class="fa fa-edit"></i> ${flight[7] == 'Đã cập nhật' ? 'Chỉnh Sửa' : 'Cập Nhật'}
                                </a>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
</div>

</body>
</html>
