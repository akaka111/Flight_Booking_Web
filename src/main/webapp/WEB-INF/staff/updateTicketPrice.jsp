<%-- 
    Document   : updateTicketPrice
    Created on : Aug 30, 2025, 4:08:12 AM
    Author     : Khoa
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
    <title>Cập Nhật Giá Vé</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
    <style>
        body { font-family: Arial, sans-serif; margin: 0; display: flex; }
        .sidebar { width: 250px; background: #f4f4f4; padding: 20px; }
        .content { flex-grow: 1; padding: 20px; }
        .form-group { margin-bottom: 15px; }
        label { display: inline-block; width: 150px; }
        input[type="number"] { padding: 5px; width: 200px; }
        button { padding: 10px 20px; background: #4CAF50; color: white; border: none; cursor: pointer; }
        button:hover { background: #45a049; }
    </style>
</head>
<body>
    <jsp:include page="/sidebar.jsp"/>
    <div class="content">
        <h2>Cập Nhật Giá Vé Cho Chuyến Bay ID: ${flightId}</h2>
        <form action="ticketPrice" method="post">
            <input type="hidden" name="flightId" value="${flightId}"/>
            <c:forEach var="seatClass" items="${seatClasses}">
                <div class="form-group">
                    <label>Hạng Ghế: ${seatClass.name}</label>
                    <input type="hidden" name="seatClassId" value="${seatClass.seatClassId}"/>
                    <c:set var="existingPrice" value="0.0"/>
                    <c:forEach var="ticketClass" items="${ticketClasses}">
                        <c:if test="${ticketClass.className == seatClass.name}">
                            <c:set var="existingPrice" value="${ticketClass.price}"/>
                        </c:if>
                    </c:forEach>
                    <input type="number" name="price" value="${existingPrice}" step="0.01" min="0" required/>
                </div>
            </c:forEach>
            <button type="submit">Lưu Giá Vé</button>
        </form>
        <a href="ticketPrice">Quay Lại</a>
    </div>
</body>
</html>
