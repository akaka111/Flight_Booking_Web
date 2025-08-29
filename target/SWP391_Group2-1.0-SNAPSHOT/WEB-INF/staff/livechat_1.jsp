<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<html>
    <head>
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <meta charset="UTF-8">
        <title>Live Chat - Staff</title>
        <style>
            body{
                font-family:Arial,sans-serif;
                margin:0;
                height:100vh;
                display:flex;
                flex-direction:column;
            }
            .header{
                background:#007bff;
                color:white;
                display:flex;
                justify-content:space-between;
                align-items:center;
                padding:12px 20px;
                position:relative;
            }
            .header a{
                background:#0056b3;
                color:white;
                text-decoration:none;
                padding:6px 14px;
                border-radius:5px;
                font-size:14px;
            }
            .header a:hover{
                background:#003d80;
            }
            .main{
                flex:1;
                display:flex;
            }
            .sidebar{
                width:260px;
                background:#f8f9fa;
                border-right:1px solid #ddd;
                padding:10px;
            }
            .sidebar h3{
                margin:10px 0;
                font-size:16px;
                color:#333;
                border-bottom:1px solid #ccc;
                padding-bottom:4px;
            }
            .sidebar select{
                width:100%;
                padding:6px;
                margin-bottom:15px;
                border-radius:4px;
                border:1px solid #ccc;
            }
            .chat-window{
                flex:1;
                display:flex;
                flex-direction:column;
                background:#fdfdfd;
            }
            .chat-header{
                padding:12px;
                border-bottom:1px solid #ccc;
                font-weight:bold;
                background:#f1f1f1;
            }
            #chatBox{
                flex:1;
                overflow-y:auto;
                padding:15px;
                background:#fafafa;
            }
            .message{
                margin:6px 0;
                padding:8px 12px;
                border-radius:8px;
                max-width:70%;
                clear:both;
                font-size:14px;
            }
            .guest{
                background:#d1e7dd;
                align-self:flex-start;
            }
            .staff{
                background:#f8d7da;
                align-self:flex-end;
            }
            .user{
                background:#d5f7fd;
                align-self:flex-start;
            }
            .message i{
                font-size:11px;
                color:#555;
                display:block;
                margin-top:4px;
            }
            #chatForm{
                display:flex;
                border-top:1px solid #ccc;
            }
            #chatForm input[type="text"]{
                flex:1;
                padding:10px;
                border:none;
                border-right:1px solid #ccc;
                font-size:14px;
            }
            #chatForm button{
                padding:0 20px;
                border:none;
                background:#007bff;
                color:white;
                cursor:pointer;
                font-size:14px;
            }
            #chatForm button:hover{
                background:#0056b3;
            }
            #chatNotify{
                display:none;
                background:red;
                color:white;
                font-size:10px;
                padding:3px 6px;
                border-radius:50%;
                position:absolute;
                top:12px;
                right:20px;
                font-weight:bold;
            }
        </style>
    </head>
    <body>
        <div class="header">
            <h2>Live Chat - Staff</h2>
            <a href="staff">Quay về Staff</a>
            <span id="chatNotifyChat"></span>
        </div>

        <div class="main">
            <div class="sidebar">
                <h3>Guests</h3>
                <label for="guestSelect">Guests</label>
                <select id="guestSelect" onchange="selectChat('GUEST', this.value)">
                    <option value="">-- Select Guest --</option>
                    <c:forEach var="g" items="${chatGuests}">
                        <option value="${g}" <c:if test="${selectedGuest==g}">selected</c:if>>${g}</option>
                    </c:forEach>
                    <c:if test="${empty chatGuests}">
                        <option disabled>No guests available</option>
                    </c:if>
                </select>

                <h3>Users</h3>
                <label for="userSelect">Users</label>
                <select id="userSelect" onchange="selectChat('USER', this.value)">
                    <option value="">-- Select User --</option>
                    <c:forEach var="u" items="${chatUsers}">
                        <option value="${u.userId}" <c:if test="${selectedUser==u.userId}">selected</c:if>>${u.username}</option>
                    </c:forEach>
                    <c:if test="${empty chatUsers}">
                        <option disabled>No users available</option>
                    </c:if>
                </select>
            </div>

            <!--boxchat-->
            <div class="chat-window">
                <div class="chat-header">
                    Đang chat với: 
                    <c:choose>
                        <c:when test="${not empty selectedGuest}">
                            ${selectedGuest}
                        </c:when>
                        <c:when test="${not empty selectedUser}">
                            ${userSelect.username}
                             <c:forEach var="u" items="${chatUsers}">
                                <c:if test="${u.userId == selectedUser}">
                                    ${u.username}
                                </c:if>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                            Chưa chọn cuộc chat
                        </c:otherwise>
                    </c:choose>
                </div>
                <div id="chatBox">
                    <c:forEach var="m" items="${messages}">
                        <div class="message ${m.senderType}">
                            <b>
                                <c:choose>
                                    <c:when test="${m.senderType=='staff'}">Staff</c:when>
                                    <c:when test="${m.senderType=='user'}">${m.senderName}</c:when>
                                    <c:otherwise>${m.guest_label}</c:otherwise>
                                </c:choose>
                            </b>: ${fn:escapeXml(m.content)}
                            <i>${m.sentTime}</i>
                        </div>
                    </c:forEach>
                    <c:if test="${empty messages}">
                        <div class="message guest">No messages available</div>
                    </c:if>
                </div>

                <form id="chatForm" onsubmit="return sendMessage(event)">
                    <input type="hidden" id="selectedChat" name="selectedChat" value="${selectedChat}"/>
                    <input type="text" id="chatInput" name="content" placeholder="Type your message..." required/>
                    <button type="submit">Send</button>
                </form>
            </div>
        </div>

        <script>
            let chatMessagesMap = {};
            let lastMessageIdMap = {};
            console.log("lastMessageIdMap:", lastMessageIdMap);
            async function selectChat(type, value) {
                if (!value)
                    return;

                // Chỉ chọn 1 chat
                if (type === 'GUEST') {
                    document.getElementById('userSelect').value = ""; // reset user select
                } else if (type === 'USER') {
                    document.getElementById('guestSelect').value = ""; // reset guest select
                }

                const chatId = type + ':' + value;
                document.getElementById('selectedChat').value = chatId;
                const chatBox = document.getElementById("chatBox");
                chatBox.innerHTML = "";
                chatMessagesMap[chatId] = new Set();
                lastMessageIdMap[chatId] = 0;

                // Gọi servlet để assign conversation cho staff
                try {
                    await fetch("livechatFunc11?action=assign&selectedChat=" + encodeURIComponent(chatId));
                } catch (err) {
                    console.error("Assign error", err);
                }
                markChatRead(chatId);
                fetchMessages();
            }

            async function sendMessage(e) {
                e.preventDefault();
                const chatId = document.getElementById("selectedChat").value;
                const content = document.getElementById("chatInput").value.trim();
                if (!content)
                    return;
                try {
                    const res = await fetch("livechatFunc11", {
                        method: "POST",
                        headers: {"Content-Type": "application/x-www-form-urlencoded"},
                        body: "selectedChat=" + encodeURIComponent(chatId) + "&content=" + encodeURIComponent(content)
                    });
                    if (res.ok) {
                        document.getElementById("chatInput").value = "";
                        fetchMessages(chatId);
                    } else {
                        console.error("Server returned status", res.status);
                    }
                } catch (err) {
                    console.error(err);
                    alert("Lỗi kết nối server!");
                }

            }

            function appendMessage(message, chatId) {
                if (!chatMessagesMap[chatId])
                    chatMessagesMap[chatId] = new Set();
                const chatBox = document.getElementById("chatBox");
                const div = document.createElement("div");
                div.className = 'message ' + (message.senderType || 'guest');
                const senderName = message.senderName || message.guest_label || 'Unknown';
                const time = message.sentTime ? new Date(message.sentTime).toLocaleString() : '';
                // Tạo phần tên người gửi
                const b = document.createElement("b");
                b.textContent = senderName; // an toàn, escape XML/HTML
                // Tạo nội dung tin nhắn
                const textNode = document.createTextNode(": " + message.content);
                // Tạo thời gian
                const i = document.createElement("i");
                i.textContent = time;
                // Gắn các node vào div
                div.appendChild(b);
                div.appendChild(textNode);
                div.appendChild(i);
                chatBox.appendChild(div);
                chatBox.scrollTop = chatBox.scrollHeight;
                const msgId = message.id;
                lastMessageIdMap[chatId] = Math.max(lastMessageIdMap[chatId] || 0, msgId);
                chatMessagesMap[chatId].add(msgId);
            }

            async function fetchMessages() {
                let chatId = document.getElementById("selectedChat").value;
                if (!chatId)
                    return;
                try {
                    const lastId = lastMessageIdMap[chatId] || 0;
                    const res = await fetch("livechatFunc11?action=getMessages&selectedChat=" + encodeURIComponent(chatId) + "&afterId=" + lastId);

                    if (!res.ok)
                        return;
                    const msgs = await res.json();
                    console.log("Fetched messages:", msgs);
                    if (!chatMessagesMap[chatId])
                        chatMessagesMap[chatId] = new Set();

                    msgs.forEach(m => {
                        console.log(m.id + " " + m.content);
                        const msgKey = m.id || new Date(m.sentTime).getTime();
                        if (!chatMessagesMap[chatId].has(msgKey))
                            appendMessage(m, chatId);
                    });

                } catch (err) {
                    console.error("Error fetching messages:", err);
                }
            }

            function markChatRead(chatId) {
                if (!chatId)
                    return;
                fetch("livechatFunc11?action=markRead&selectedChat=" + encodeURIComponent(chatId));
            }

            async function checkUnread() {
                const res = await fetch("livechatFunc11?action=checkUnread");
                if (res.ok) {
                    const data = await res.json();
                    console.log("Message just sent:", data);
                    let total = 0;
                    Object.values(data).forEach(function (value) {
                        total += value;
                    });
                    const badge = document.getElementById("chatNotifySidebar");
                    if (total > 0) {
                        badge.style.display = "inline-block";
                        badge.textContent = total;
                    } else
                        badge.style.display = "none";
                }
            }

            window.onload = function () {
                const chatBox = document.getElementById('chatBox');
                let selectedChat = document.getElementById('selectedChat').value;
                const guestSelect = document.getElementById('guestSelect');
                const userSelect = document.getElementById('userSelect');
                if (!selectedChat) {
                    if (guestSelect.value)
                        selectedChat = 'GUEST:' + guestSelect.value;
                    else if (userSelect.value)
                        selectedChat = 'USER:' + userSelect.value;
                    document.getElementById('selectedChat').value = selectedChat;
                }

                if (selectedChat) {
                    markChatRead(selectedChat);
                    fetchMessages();
                }
            }
            ;
            setInterval(fetchMessages, 2000);
            setInterval(checkUnread, 2000);
        </script>
    </body>
</html>
