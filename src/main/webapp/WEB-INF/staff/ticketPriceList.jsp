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
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
    <style>
        body { font-family: Arial, sans-serif; margin: 0; display: flex; }
        .sidebar { width: 250px; background: #f4f4f4; padding: 20px; }
        .content { flex-grow: 1; padding: 20px; }
        table { width: 100%; border-collapse: collapse; }
        th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }
        th { background-color: #f2f2f2; }
        .action-btn { padding: 5px 10px; text-decoration: none; color: white; border-radius: 3px; }
        .update-btn { background-color: #4CAF50; }
        .edit-btn { background-color: #FFA500; }
    </style>
</head>
<body>
    <jsp:include page="/sidebar.jsp"/>
    <div class="content">
        <h2>Quản Lý Giá Vé</h2>
        <table>
            <thead>
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
                        <td>${flight[7]}</td>
                        <td>
                            <a href="ticketPrice?action=update&flightId=${flight[0]}" 
                               class="action-btn ${flight[7] == 'Đã cập nhật' ? 'edit-btn' : 'update-btn'}">
                               ${flight[7] == 'Đã cập nhật' ? 'Chỉnh Sửa' : 'Cập Nhật'}
                            </a>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>
</body>
</html>