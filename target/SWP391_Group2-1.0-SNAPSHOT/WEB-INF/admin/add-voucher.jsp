<%-- 
    Document   : add-voucher
    Created on : Jun 21, 2025, 6:00:08 PM
    Author     : bao
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <title>add voucher</title>
    </head>
    <body>
        <h2>Thêm Voucher Mới</h2>
        <form action="manageVouchers" method="post">
            <input type="hidden" name="action" value="create">
            <label>Mã Voucher (code):</label><br>
            <input type="text" name="code" required><br><br>
            <label>Phần trăm giảm giá (%):</label><br>
            <input type="number" name="discount" min="0" max="100" required><br><br>
            <label>Ngày bắt đầu (valid_from):</label><br>
            <input type="date" name="valid_from" required><br><br>
            <label>Ngày kết thúc (valid_to):</label><br>
            <input type="date" name="valid_to" required><br><br>
            <label>Giới hạn sử dụng (usage_limit):</label><br>
            <input type="number" name="usage_limit" min="1" required><br>
            <input type="submit" value="Thêm Voucher"><br><br>
        </form>
        <a href="manageVouchers">← Quay lại danh sách voucher</a>
    </body>
</html>
