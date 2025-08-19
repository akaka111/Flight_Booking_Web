<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Quản lý tuyến bay</title>
    <link rel="stylesheet"
          href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
</head>
<body class="bg-light">
<div class="container py-4">
    <h3 class="mb-3">Quản lý tuyến bay</h3>

    <!-- Thông báo -->
    <c:if test="${not empty msg}">
        <div class="alert alert-success" role="alert">
            ${fn:escapeXml(msg)}
        </div>
    </c:if>
    <c:if test="${not empty error}">
        <div class="alert alert-danger" role="alert">
            ${fn:escapeXml(error)}
        </div>
    </c:if>

    <div class="mb-3 text-right">
        <a href="${pageContext.request.contextPath}/RouteAdmin?action=showAddForm"
           class="btn btn-success">
            + Thêm tuyến bay
        </a>
    </div>

    <c:choose>
        <c:when test="${empty routes}">
            <div class="alert alert-info">Chưa có tuyến bay nào.</div>
        </c:when>
        <c:otherwise>
            <table class="table table-bordered table-hover bg-white">
                <thead>
                <tr>
                    <th>ID</th>
                    <th>Điểm đi (IATA)</th>
                    <th>Điểm đến (IATA)</th>
                    <th>Khoảng cách (km)</th>
                    <th>Thời lượng (phút)</th>
                    <th>Trạng thái</th>
                    <th style="width: 180px;">Thao tác</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="r" items="${routes}">
                    <tr>
                        <td>${r.routeId}</td>
                        <td>${fn:escapeXml(r.originIata)}</td>
                        <td>${fn:escapeXml(r.destIata)}</td>
                        <td>
                            <c:choose>
                                <c:when test="${r.distanceKm != null && r.distanceKm > 0}">
                                    ${r.distanceKm}
                                </c:when>
                                <c:otherwise>-</c:otherwise>
                            </c:choose>
                        </td>
                        <td>
                            <c:choose>
                                <c:when test="${r.durationMinutes != null && r.durationMinutes > 0}">
                                    ${r.durationMinutes}
                                </c:when>
                                <c:otherwise>-</c:otherwise>
                            </c:choose>
                        </td>
                        <td>
                            <c:choose>
                                <c:when test="${r.active}">
                                    <span class="badge badge-success">Active</span>
                                </c:when>
                                <c:otherwise>
                                    <span class="badge badge-secondary">Inactive</span>
                                </c:otherwise>
                            </c:choose>
                        </td>
                        <td>
                            <a href="${pageContext.request.contextPath}/RouteAdmin?action=editRoute&id=${r.routeId}"
                               class="btn btn-sm btn-primary">Sửa</a>

                            <!-- XÓA: dùng POST để RouteAdmin.doPost(action=deleteRoute) xử lý -->
                            <form method="post"
                                  action="${pageContext.request.contextPath}/RouteAdmin"
                                  style="display:inline">
                                <input type="hidden" name="action" value="deleteRoute"/>
                                <input type="hidden" name="id" value="${r.routeId}"/>
                                <button type="submit" class="btn btn-sm btn-danger"
                                        onclick="return confirm('Bạn có chắc chắn muốn xóa tuyến bay #${r.routeId}?');">
                                    Xóa
                                </button>
                            </form>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </c:otherwise>
    </c:choose>
</div>
</body>
</html>
