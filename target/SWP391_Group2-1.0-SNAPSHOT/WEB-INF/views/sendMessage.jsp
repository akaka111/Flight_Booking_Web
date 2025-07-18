<%-- 
    Document   : sendMessage
    Created on : Jul 15, 2025, 2:41:43 PM
    Author     : ADMIN
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <h2>✉️ Gửi yêu cầu hỗ trợ</h2>
        <form action="ContactSupport" method="post">
            <input type="hidden" name="action" value="send" />
            <label>Chủ đề:</label><br>
            <input type="text" name="subject" required><br><br>
            <label>Nội dung:</label><br>
            <textarea name="content" rows="5" cols="50" required></textarea><br><br>
            <input type="hidden" name="recipient" value="support@staffexample.com">
            <button type="submit">Gửi</button>
        </form>
        <%
            String notify = (String) request.getAttribute("notify");
            if (notify != null) {
        %>
        <p style="color: green;"><%= notify%></p>
        <%
            }
        %>
    </body>
</html>
