<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
  <meta charset="UTF-8">
  <title>Quản lý Sân Bay</title>
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <link rel="stylesheet"
        href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
</head>
<body class="bg-light">
<div class="container py-4">
  <h3 class="mb-3">Quản Lý Sân Bay</h3>

  <!-- Alerts -->
  <c:if test="${not empty msg}">
    <div class="alert alert-success mb-3">${msg}</div>
  </c:if>
  <c:if test="${not empty error}">
    <div class="alert alert-danger mb-3">${error}</div>
  </c:if>

  <div class="mb-3 text-right">
    <a class="btn btn-success"
       href="${pageContext.request.contextPath}/AirportAdmin?action=showAddForm">
      + Thêm sân bay
    </a>
  </div>

  <div class="table-responsive">
    <table class="table table-bordered table-hover bg-white">
      <thead class="thead-light">
      <tr>
        <th>ID</th>
        <th>IATA</th>
        <th>ICAO</th>
        <th>Tên sân bay</th>
        <th>Thành phố</th>
        <th>Quốc gia</th>
        <th>Timezone</th>
        <th>Trạng thái</th>
        <th style="width: 160px;">Thao tác</th>
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
               href="${pageContext.request.contextPath}/AirportAdmin?action=editAirport&id=${a.airportId}">
              Sửa
            </a>

            <!-- XÓA bằng POST -->
            <form method="post"
                  action="${pageContext.request.contextPath}/AirportAdmin"
                  onsubmit="return confirm('Xoá sân bay này?');"
                  style="display:inline">
              <input type="hidden" name="action" value="deleteAirport"/>
              <input type="hidden" name="id" value="${a.airportId}"/>
              <button type="submit" class="btn btn-sm btn-danger">Xoá</button>
            </form>
          </td>
        </tr>
      </c:forEach>
      </tbody>
    </table>
  </div>
</div>

<!-- Optional JS (Bootstrap’s JS needs jQuery + Popper if you use dropdowns/modals) -->
<script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.1/dist/umd/popper.min.js"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
</body>
</html>
