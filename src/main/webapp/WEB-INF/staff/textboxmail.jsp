<%-- 
    Document   : textboxmail
    Created on : Jul 15, 2025, 1:43:04 PM
    Author     : ADMIN
--%>

<%@ page import="java.util.*, model.Message" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
    <head>
        <title>Mailbox - Staff</title>
    </head>
    <body>
        <h2>📬 Hộp thư hỗ trợ</h2>
        <table border="1">
            <tr>
                <th>Email người gửi</th>
                <th>Chủ đề</th>
                <th>Thời gian gửi</th>
                <th>Chi tiết</th>
            </tr>
            <%
                List<Message> messages = (List<Message>) request.getAttribute("messages");
                if (messages != null && !messages.isEmpty()) {
                    for (Message msg : messages) {
            %>
            <tr>
                <td><%= msg.getSenderEmail()%></td>
                <td><%= msg.getSubject()%></td>
                <td><%= msg.getSentTime()%></td>
                <td><a href="messageDetail?id=<%= msg.getId()%>">Xem chi tiết</a></td>
            </tr>
            <% }
            } else {
            %>
            <tr><td colspan="4">Không có tin nhắn nào</td></tr>
            <%
                }
            %>
        </table>
    </body>
</html>

