<%-- 
    Document   : SupportPage
    Created on : Jul 15, 2025, 2:26:08 PM
    Author     : ADMIN
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
      <!-- Thông báo người dùng sau khi đã gửi form -->
    <%
        String message = (String) request.getAttribute("message");
        if (message != null) {
    %>
    <p style="color: green;"><%= message%></p>
    <%
        }
    %>
    <body>
        <h2>📚 Câu hỏi thường gặp (FAQ)</h2>
        <div class="faq">
            <div class="question">1. Làm sao để đặt vé máy bay?</div>
            <div class="answer">Bạn có thể đặt vé bằng cách đăng nhập, chọn chuyến bay và thanh toán trực tuyến.</div>
            <div class="question">2. Tôi quên mật khẩu thì phải làm sao?</div>
            <div class="answer">Bạn có thể sử dụng chức năng "Quên mật khẩu" ở trang đăng nhập để lấy lại.</div>
            <div class="question">3. Làm sao để hủy vé?</div>
            <div class="answer">Liên hệ bộ phận hỗ trợ hoặc gửi yêu cầu trong hộp thư hỗ trợ.</div>
        </div>
        <!-- Nút liên hệ hỗ trợ -->
        <form action="ContactSupport" method="post">
            <input type="hidden" name="action" value="openForm" />
            <button type="submit">💬 Liên hệ hỗ trợ</button>
        </form>
        <form action="ContactSupport" method="post">
            <input type="hidden" name="action" value="inbox" />
            <button type="submit">💬 hòm thư</button>
        </form>
    </body>
</html>
