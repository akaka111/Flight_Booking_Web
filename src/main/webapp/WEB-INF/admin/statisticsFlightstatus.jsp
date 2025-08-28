<%-- 
    Document   : statisticsFlightstatus
    Created on : Jul 21, 2025, 7:29:59 PM
    Author     : ADMIN
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>chi tiết chuyến bay </title>
    </head>
    <body>
        <form action="StatsStatus" method="get">
            <input type="hidden" name="status" value="${param.status}"/>
            <label>Chọn tháng:</label>
            <select name="month">
                <option value="">Tất cả</option>
                <c:forEach var="m" begin="1" end="12">
                    <option value="${m}" <c:if test="${param.month == m}">selected</c:if>>${m}</option>
                </c:forEach>
            </select>
            <label>Chọn năm:</label>
            <select name="year">
                <option value="">Tất cả</option>
                <c:forEach var="y" begin="2025" end="2030">
                    <option value="${y}" <c:if test="${param.year == y}">selected</c:if>>${y}</option>
                </c:forEach>
            </select>

            <label>Lọc theo ít/nhiều nhất</label>
            <select name="airlineFilter">
                <option value="max" <c:if test="${airlineFilter == 'max'}">selected</c:if>>Nhiều nhất</option>
                <option value="min" <c:if test="${airlineFilter == 'min'}">selected</c:if>>Ít nhất</option>
                </select>
                <button type="submit">Lọc</button>
            </form>
            <h2>Danh sách chuyến bay: ${flightStatus}</h2>
        <table border="1">
            <tr>
                <th>Mã chuyến bay</th>
                <th>Số hiệu</th>
                <th>Hãng hàng không</th>
                <th>Mã tuyến bay (route_id)</th>
                <th>Giờ khởi hành</th>
                <th>Giờ đến</th>
                <th>Tình trạng</th>
            </tr>
            <c:forEach var="f" items="${flightList}">
                <tr>
                    <td>${f.flightId}</td>
                    <td>${f.flightNumber}</td>
                    <td>${f.airlineId}</td>
                    <td>${f.routeId}</td>
                    <td><fmt:formatDate value="${f.departureTime}" pattern="dd/MM/yyyy HH:mm"/></td>
                    <td><fmt:formatDate value="${f.arrivalTime}" pattern="dd/MM/yyyy HH:mm"/></td>
                    <td>${f.status}</td>
                </tr>
            </c:forEach>
        </table>
        <br/>
        <a href="Stats">Quay lại thống kê</a>
    </body>
</html>
