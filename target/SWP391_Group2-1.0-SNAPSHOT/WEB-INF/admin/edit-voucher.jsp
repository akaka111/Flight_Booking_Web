<%-- 
    Document   : edit-voucher
    Created on : Jun 22, 2025, 1:58:58 PM
    Author     : bao
--%>
<%@ page import="model.Voucher" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%
    Voucher v = (Voucher) request.getAttribute("voucher");
%>
<html>
    <head>
        <title>edit voucher</title>
    </head>
    <body>
        <form action="manageVouchers" method="post">
            <input type="hidden" name="action" value="update">
            <input type="hidden" name="id" value="<%= v.getVoucher_id()%>">
            <label>code voucher:</label><br>
            <input type="text" name="code" value="<%= v.getCode()%>" required><br><br>
            <label>Phần trăm giảm giá (%):</label><br>
            <input type="number" name="discount" value="<%= v.getDiscount_percent()%>" min="0" max="100" required><br><br>
            <label>Ngày bắt đầu:</label><br>
            <input type="date" name="valid_from" value="<%= v.getValid_from()%>" required><br><br>
            <label>Ngày kết thúc:</label><br>
            <input type="date" name="valid_to" value="<%= v.getValid_to()%>" required><br><br>
            <label>Giới hạn sử dụng:</label><br>
            <input type="number" name="usage_limit" value="<%= v.getUsage_limit()%>" min="1" required><br><br>
            <input type="submit" value="Lưu thay đổi"><br><br>
        </form>
        <a href="manageVouchers">← Quay lại danh sách voucher</a>
    </body>
</html>