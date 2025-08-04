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
        <title>JSP Page</title>
    </head>
    <body>
        <h2>Chi tiết trạng thái chuyến bay: ${flightStatus}</h2>
        <table border="1">
            <thead>
                <tr>
                    <th>Mã chuyến bay</th>
                    <th>Tuyến bay</th>
                    <th>Giờ khởi hành</th>
                    <th>Giờ đến</th>
                    <th>Tên máy bay</th>
                    <th>Giá thường</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="f" items="${flightList}">
                    <tr>
                        <td>${f.flightNumber}</td>
                        <td>${f.routeFrom} → ${f.routeTo}</td>
                        <td><fmt:formatDate value="${f.departureTime}" pattern="dd/MM/yyyy HH:mm" /></td>
                        <td><fmt:formatDate value="${f.arrivalTime}" pattern="dd/MM/yyyy HH:mm" /></td>
                        <td>${f.aircraft}</td>
                        <td>${f.price}</td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
        <br/>
        <a href="Stats">Quay lại thống kê</a>
    </body>
</html>
