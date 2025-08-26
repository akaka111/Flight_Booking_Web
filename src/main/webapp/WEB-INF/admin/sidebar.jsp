<%-- 
    Document   : sidebar
    Created on : 23 Jun 2025, 22:56:50
    Author     : Admin
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="sidebar">
    <div class="logo">Admin <i class="fa-solid fa-plane"></i></div>
    <ul>
        <!-- Dashboard (đi qua servlet DashboardAdmin) -->
        <li>
            <a href="${pageContext.request.contextPath}/admin">
                <i class="fa-solid fa-tachometer-alt"></i> Bảng Điều Khiển
            </a>
        </li>

        <!-- Quản lý người dùng -->
        <li>
            <a href="${pageContext.request.contextPath}/manageAccountController">
                <i class="fa fa-users"></i> Quản Lý Người Dùng
            </a>
        </li>

        <!-- Quản lý chuyến bay -->
        <li>
            <a href="${pageContext.request.contextPath}/FlightAdmin1">
                <i class="fa-solid fa-plane-departure"></i> Quản Lý Chuyến Bay
            </a>
        </li>

        <!-- ✅ Quản lý tuyến bay (MỚI) -->
        <li>
            <a href="${pageContext.request.contextPath}/RouteAdmin">
                <i class="fa-solid fa-route"></i> Quản Lý Tuyến Bay
            </a>
        </li>
     <!-- ✅ Quản lý tuyến bay (MỚI) -->
        <li>
            <a href="${pageContext.request.contextPath}/AirportAdmin">
                <i class="fa-solid fa-route"></i> Quản Lý Sân Bay
            </a>
        </li>

        <!-- Quản lý hãng bay -->
        <li>
            <a href="${pageContext.request.contextPath}/AirlineAdmin">
                <i class="fa-solid fa-building"></i> Quản Lý Hãng Bay
            </a>
        </li>
        <!-- Quản lý vé -->
<li>
  <a href="${pageContext.request.contextPath}/TicketClassAdmin?action=flights">
    <i class="fa-solid fa-ticket-simple"></i> Quản Lý Giá Vé
  </a>
</li>

        <!-- Quản lý voucher -->
        <li>
            <a href="${pageContext.request.contextPath}/manageVouchers">
                <i class="fa-solid fa-ticket"></i> Quản Lý Voucher
            </a>
        </li>

        <!-- Thống kê -->
        <li>
            <a href="${pageContext.request.contextPath}/Stats">
                <i class="fa-solid fa-chart-line"></i> Thống Kê
            </a>
        </li>

        <!-- Đổi mật khẩu -->
        <li>
            <a href="${pageContext.request.contextPath}/AdminChangePassword">
                <i class="fa-solid fa-cog"></i> Đổi Mật Khẩu
            </a>
        </li>

        <!-- Hỗ trợ (nên đi qua 1 servlet/route, KHÔNG gọi trực tiếp JSP trong WEB-INF) -->
        <li>
            <a href="${pageContext.request.contextPath}/admin?action=support">
                <i class="fa-solid fa-headset"></i> Hỗ Trợ
            </a>
        </li>
    </ul>
</div>
