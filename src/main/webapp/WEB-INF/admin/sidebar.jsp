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
        <li><a href="admin.jsp"><i class="fa-solid fa-tachometer-alt"></i> Bảng Điều Khiển</a></li>
        <li>
            <a href="${pageContext.request.contextPath}/manageAccountController">
                <i class="fa fa-sitemap fa-fw"></i> Quản Lý Người Dùng
            </a>
        </li>


        <li><a href="${pageContext.request.contextPath}/FlightAdmin1"><i class="fa-solid fa-plane-departure"></i> Quản Lý Chuyến Bay</a></li>
        <li><a href="${pageContext.request.contextPath}/AirlineAdmin"><i class="fa-solid fa-building"></i> Quản Lý Hãng Bay</a></li>
        <li><a href="${pageContext.request.contextPath}/manageVouchers"><i class="fa-solid fa-ticket"></i> Quản Lý Voucher</a></li>
        <li><a href="statistics.jsp"><i class="fa-solid fa-chart-line"></i> Thống Kê</a></li>
        <li><a href="settings.jsp"><i class="fa-solid fa-cog"></i> Cài Đặt</a></li>
        <li><a href="support.jsp"><i class="fa-solid fa-headset"></i> Hỗ Trợ</a></li>
    </ul>
</div>

