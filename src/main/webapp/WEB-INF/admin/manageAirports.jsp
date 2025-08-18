<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
  <meta charset="UTF-8">
  <title>Quản lý Sân Bay</title>
  <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
</head>
<body class="bg-light">
<div class="container py-4">
  <h3 class="mb-3">Quản Lý Sân Bay</h3>

  <c:if test="${not empty msg}">
    <div class="alert alert-success">${msg}</div>
  </c:if>
  <c:if test="${not empty error}">
    <div class="alert alert-danger">${error}</div>
  </c:if>

  <div class="mb-3 text-right">
    <a class="btn btn-success" href="${pageContext.request.contextPath}/AirportAdmin?action=showAddForm">+ Thêm sân bay</a>
  </div>

  <table class="table table-bordered table-hover bg-white">
    <thead class="thead-dark">
      <tr>
        <th>ID</th>
        <th>IATA</th>
        <th>ICAO</th>
        <th>Tên sân bay</th>
        <th>Thành phố</th>
        <th>Quốc gia</th>
        <th>Timezone</th>
        <th>Trạng thái</th>
        <th>Thao tác</th>
      </tr>
    </thead>
    <tbody>
      <c:forEach var="a" items="${airports}">
        <tr>
          <td>${a.airportId}</td>
          <td>${a.iataCode}</td>
          <td><c:out value="${a.icaoCode != null ? a.icaoCode : '-'}"/></td>
          <td>${a.name}</td>
          <td>${a.city}</td>
          <td>${a.country}</td>
          <td><c:out value="${a.timezone != null ? a.timezone : '-'}"/></td>
          <td>
            <c:choose>
              <c:when test="${a.active}">
                <span class="badge badge-success">Active</span>
              </c:when>
              <c:otherwise>
                <span class="badge badge-secondary">Inactive</span>
              </c:otherwise>
            </c:choose>
          </td>
          <td>
            <a class="btn btn-sm btn-primary"
               href="${pageContext.request.contextPath}/AirportAdmin?action=editAirport&id=${a.airportId}">Sửa</a>
            <a class="btn btn-sm btn-danger"
               href="${pageContext.request.contextPath}/AirportAdmin?action=deleteAirport&id=${a.airportId}"
               onclick="return confirm('Xoá sân bay này?');">Xoá</a>
          </td>
        </tr>
      </c:forEach>
    </tbody>
  </table>
</div>
</body>
</html>
