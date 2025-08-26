<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Giá vé theo chuyến</title>
    <style>
        body { font-family: Arial, sans-serif; }
        .container { max-width: 1000px; margin: 24px auto; }
        .top { display:flex; justify-content:space-between; align-items:center; margin-bottom:12px; }
        table { border-collapse: collapse; width: 100%; }
        th, td { border: 1px solid #ddd; padding: 8px; }
        th { background: #f4f6f8; text-align: left; }
        .msg { color: green; margin-bottom:10px; }
        .err { color: #c00; margin-bottom:10px; }
        .btn { background:#1976d2; color:#fff; padding:6px 10px; text-decoration:none; border-radius:4px; }
        .btn-danger { background:#c62828; }
        .btn-secondary { background:#546e7a; }
        form.inline { display:inline; margin:0; }
    </style>
</head>
<body>
<div class="container">
    <div class="top">
        <h2>Giá vé – Flight ID: ${flightId}</h2>
        <a class="btn" href="${pageContext.request.contextPath}/TicketClassAdmin?action=showAddForm&flight_id=${flightId}">
            + Thêm giá cho chuyến này
        </a>
    </div>

    <c:if test="${not empty msg}"><div class="msg">${msg}</div></c:if>
    <c:if test="${not empty error}"><div class="err">${error}</div></c:if>

    <table>
        <thead>
        <tr>
            <th>ClassID</th>
            <th>SeatClass</th>
            <th>SeatClassID</th>
            <th>Giá</th>
            <th style="width:160px;">Thao tác</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="r" items="${prices}">
            <tr>
                <td>${r.classId}</td>
                <td>${r.seatClassName}</td>
                <td>${r.seatClassId}</td>
                <td><fmt:formatNumber value="${r.price}" type="currency" currencySymbol="₫" maxFractionDigits="0"/></td>
                <td>
                    <a class="btn btn-secondary"
                       href="${pageContext.request.contextPath}/TicketClassAdmin?action=edit&id=${r.classId}">Sửa</a>
                    <form class="inline" method="post" action="${pageContext.request.contextPath}/TicketClassAdmin">
                        <input type="hidden" name="action" value="delete"/>
                        <input type="hidden" name="id" value="${r.classId}"/>
                        <button class="btn btn-danger" type="submit"
                                onclick="return confirm('Xoá giá #${r.classId}?')">Xoá</button>
                    </form>
                </td>
            </tr>
        </c:forEach>
        <c:if test="${empty prices}">
            <tr><td colspan="5" style="text-align:center;color:#666;">Chưa có giá cho chuyến này.</td></tr>
        </c:if>
        </tbody>
    </table>

    <div style="margin-top:14px;">
        <a class="btn" href="${pageContext.request.contextPath}/TicketClassAdmin?action=flights">← Quay lại danh sách chuyến</a>
    </div>
</div>
</body>
</html>
