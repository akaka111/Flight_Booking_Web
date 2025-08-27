<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
  <meta charset="UTF-8">
  <title>Sửa tuyến bay</title>
  <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css"> 
</head>
<body class="bg-light">
<div class="container py-4" style="max-width:720px">
  <h3 class="mb-3">Sửa tuyến bay #${route.routeId}</h3>

  <c:if test="${not empty error}">
    <div class="alert alert-danger">${error}</div>
  </c:if>

  <form method="post" action="${pageContext.request.contextPath}/RouteAdmin" id="routeForm" novalidate>
    <input type="hidden" name="action" value="updateRoute"/>
    <input type="hidden" name="id" value="${route.routeId}"/>

    <!-- Điểm đi -->
    <div class="form-group">
      <label>Điểm đi (IATA) <span class="text-danger">*</span></label>
      <select name="originIata" id="originIata" class="form-control" required>
        <c:forEach var="a" items="${airports}">
          <c:set var="sel" value=""/>
          <c:if test="${a.iata ne null and a.iata eq route.originIata}">
            <c:set var="sel" value=' selected="selected"'/>
          </c:if>
          <option value="${a.iata}"${sel}>${a.iata} - ${a.name} (${a.city})</option>
        </c:forEach>
      </select>
      <div class="invalid-feedback" id="originErr"></div>
    </div>

    <!-- Điểm đến -->
    <div class="form-group">
      <label>Điểm đến (IATA) <span class="text-danger">*</span></label>
      <select name="destIata" id="destIata" class="form-control" required>
        <c:forEach var="a" items="${airports}">
          <c:set var="sel" value=""/>
          <c:if test="${a.iata ne null and a.iata eq route.destIata}">
            <c:set var="sel" value=' selected="selected"'/>
          </c:if>
          <option value="${a.iata}"${sel}>${a.iata} - ${a.name} (${a.city})</option>
        </c:forEach>
      </select>
      <div class="invalid-feedback" id="destErr"></div>
    </div>

    <div class="form-row">
      <div class="form-group col-md-6">
        <label>Khoảng cách (km)</label>
        <input type="number" name="distanceKm" class="form-control" min="0" value="${route.distanceKm}">
      </div>
      <div class="form-group col-md-6">
        <label>Thời lượng (phút)</label>
        <input type="number" name="durationMinutes" class="form-control" min="0" value="${route.durationMinutes}">
      </div>
    </div>

    <div class="form-group form-check">
      <input type="checkbox" class="form-check-input" id="active" name="active"
             <c:if test="${route.active}">checked</c:if>>
      <label class="form-check-label" for="active">Kích hoạt (Active)</label>
    </div>

    <div class="d-flex justify-content-between">
      <a href="${pageContext.request.contextPath}/RouteAdmin" class="btn btn-secondary">Quay lại</a>
      <button type="submit" class="btn btn-primary">Cập nhật</button>
    </div>
  </form>
</div>

<script>
(function () {
  const o = document.getElementById('originIata');
  const d = document.getElementById('destIata');
  const form = document.getElementById('routeForm');
  function validateDifferent() {
    const same = o.value && d.value && o.value === d.value;
    const msg = 'Điểm đi và điểm đến không được trùng nhau.';
    o.setCustomValidity(same ? msg : '');
    d.setCustomValidity(same ? msg : '');
    document.getElementById('originErr').textContent = same ? msg : '';
    document.getElementById('destErr').textContent   = same ? msg : '';
    o.classList.toggle('is-invalid', same);
    d.classList.toggle('is-invalid', same);
  }
  o.addEventListener('change', validateDifferent);
  d.addEventListener('change', validateDifferent);
  form.addEventListener('submit', function (e) {
    validateDifferent();
    if (!form.checkValidity()) { e.preventDefault(); e.stopPropagation(); }
    form.classList.add('was-validated');
  });
})();
</script>
</body>
</html>
