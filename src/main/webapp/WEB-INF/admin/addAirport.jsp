<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
  <meta charset="UTF-8">
  <title>Thêm Sân Bay</title>
  <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
</head>
<body class="bg-light">
<div class="container py-4" style="max-width:800px">
  <h3 class="mb-3">Thêm Sân Bay</h3>

  <c:if test="${not empty error}">
    <div class="alert alert-danger">${error}</div>
  </c:if>

  <form method="post" action="${pageContext.request.contextPath}/AirportAdmin">
    <input type="hidden" name="action" value="addAirport"/>

    <div class="form-row">
      <div class="form-group col-md-3">
        <label>IATA <span class="text-danger">*</span></label>
        <input name="iata" maxlength="3" class="form-control" required placeholder="SGN">
      </div>
      <div class="form-group col-md-3">
        <label>ICAO</label>
        <input name="icao" maxlength="4" class="form-control" placeholder="VVTS">
      </div>
      <div class="form-group col-md-6">
        <label>Tên sân bay <span class="text-danger">*</span></label>
        <input name="name" class="form-control" required placeholder="Tân Sơn Nhất">
      </div>
    </div>

    <div class="form-row">
      <div class="form-group col-md-4">
        <label>Thành phố <span class="text-danger">*</span></label>
        <select name="city" class="form-control" required>
          <option value="">-- Chọn thành phố --</option>
          <c:forEach var="c" items="${cities}">
            <option value="${c}">${c}</option>
          </c:forEach>
        </select>
      </div>
      <div class="form-group col-md-4">
        <label>Quốc gia <span class="text-danger">*</span></label>
        <input name="country" class="form-control" required value="Việt Nam">
      </div>
      <div class="form-group col-md-4">
        <label>Timezone</label>
        <input name="timezone" class="form-control" placeholder="Asia/Ho_Chi_Minh">
      </div>
    </div>

    <div class="form-group form-check">
      <input type="checkbox" class="form-check-input" id="active" name="active" checked>
      <label for="active" class="form-check-label">Kích hoạt (Active)</label>
    </div>

    <div class="d-flex justify-content-between">
      <a href="${pageContext.request.contextPath}/AirportAdmin" class="btn btn-secondary">Quay lại</a>
      <button class="btn btn-primary" type="submit">Lưu</button>
    </div>
  </form>
</div>
</body>
</html>
