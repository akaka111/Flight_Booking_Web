<%-- 
    Document   : messageDetail
    Created on : Jul 17, 2025, 2:44:45 PM
    Author     : ADMIN
--%>

<%@page import="model.Message"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Chi tiết tin nhắn</title>
    </head>
    <body>
        <div class="chatbox">
            <div class="chat-header">📨 Chi tiết tin nhắn</div>
            <%
                Message msg = (Message) request.getAttribute("message");
                if (msg != null) {
            %>
            <div class="message-info">
                <strong>Người gửi:</strong> <%= msg.getSenderEmail()%><br/>
                <strong>Thời gian gửi:</strong> <%= msg.getSentTime()%>
            </div>
            <div class="message-content">
                <strong>Chủ đề:</strong> <%= msg.getSubject()%><br/><br/>
                <%= msg.getContent().replaceAll("\n", "<br/>")%>
            </div>
            <a class="back-link" href="staffMessageList">⬅ Quay lại hộp thư</a>
            <% } else { %>
            <p>Không tìm thấy tin nhắn.</p>
            <% }%>
        </div>
    </body>
</html>
