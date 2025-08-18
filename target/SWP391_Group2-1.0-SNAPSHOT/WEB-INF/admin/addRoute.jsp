<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
  <meta charset="UTF-8">
  <title>Thêm tuyến bay</title>
  <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
</head>
<body class="bg-light">
<div class="container py-4" style="max-width:720px">
  <h3 class="mb-3">Thêm tuyến bay</h3>

  <c:if test="${not empty error}">
    <div class="alert alert-danger">${error}</div>
  </c:if>

  <form method="post" action="${pageContext.request.contextPath}/RouteAdmin">
    <input type="hidden" name="action" value="addRoute"/>

    <div class="form-group">
      <label>Điểm đi (IATA) <span class="text-danger">*</span></label>
      <select name="originIata" class="form-control" required>
        <option value="">-- Chọn sân bay --</option>
        <c:forEach var="a" items="${airports}">
          <option value="${a.iata}">${a.iata} - ${a.name} (${a.city})</option>
        </c:forEach>
      </select>
    </div>

    <div class="form-group">
      <label>Điểm đến (IATA) <span class="text-danger">*</span></label>
      <select name="destIata" class="form-control" required>
        <option value="">-- Chọn sân bay --</option>
        <c:forEach var="a" items="${airports}">
          <option value="${a.iata}">${a.iata} - ${a.name} (${a.city})</option>
        </c:forEach>
      </select>
    </div>

    <div class="form-row">
      <div class="form-group col-md-6">
        <label>Khoảng cách (km) (tuỳ chọn)</label>
        <input type="number" name="distanceKm" class="form-control" min="0" placeholder="VD: 1140">
      </div>
      <div class="form-group col-md-6">
        <label>Thời lượng (phút) (tuỳ chọn)</label>
        <input type="number" name="durationMinutes" class="form-control" min="0" placeholder="VD: 90">
      </div>
    </div>

    <div class="form-group form-check">
      <input type="checkbox" class="form-check-input" id="active" name="active" checked>
      <label class="form-check-label" for="active">Kích hoạt (Active)</label>
    </div>

    <div class="d-flex justify-content-between">
      <a href="${pageContext.request.contextPath}/RouteAdmin" class="btn btn-secondary">Quay lại</a>
      <button type="submit" class="btn btn-primary">Lưu</button>
    </div>
  </form>
</div>
</body>
</html>
