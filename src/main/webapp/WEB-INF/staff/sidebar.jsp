<%-- 
    Document   : sidebar
    Created on : 23 Jun 2025, 22:56:50
    Author     : Admin
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="sidebar">
    <div class="logo">FBS <i class="fa-solid fa-plane"></i></div>
    <ul>
        <li><a href="staff"><i class="fa-solid fa-tachometer-alt"></i> Bảng Điều Khiển</a></li>
        <li><a href="#" onclick="loadPage('${pageContext.request.contextPath}/staff/booking/list'); return false;"><i class="fa-solid fa-ticket"></i> Quản Lý Booking</a></li>
        <li><a href="ticketPrice"><i class="fa-solid fa-ticket"></i> Quản Lý Giá Vé</a></li>

        <li><a href="StaffChangePassword"><i class="fa-solid fa-cog"></i>Đổi Mật Khẩu</a></li>
        <li><a href="textboxmailMessage"><i class="fa-solid fa-headset"></i>hộp thư hỗ trợ</a></li>

        <li><a href="manageVouchers"><i class="fa-solid fa-ticket"></i> Quản Lý Voucher</a></li>
        <li id="liveChatTab" style="position:relative;">
            <a href="livechatFunc11"> 
                <i class="fa-solid fa-comments"></i> Live Chat
                <span id="chatNotify" 
                      <span id="chatNotifySidebar" 
                      style="display:none; position:absolute; top:-5px; right:-5px; background:red; color:white; font-size:10px; padding:3px 6px; border-radius:50%; font-weight:bold;">
                </span>
            </a>
        </li>
    </ul>
</div>

<script>
    async function checkUnread() {
        const res = await fetch("livechatFunc11?action=checkUnread");
        if (res.ok) {
            const data = await res.json();
            let total = 0;
            Object.values(data).forEach(v => total += v);
            const badge = document.getElementById("chatNotifySidebar");
            if (total > 0) {
                badge.style.display = "inline-block";
                badge.textContent = total;
            } else {
                badge.style.display = "none";
            }
        }
    }

// Chạy định kỳ
    setInterval(checkUnread, 2000);
    window.onload = checkUnread;
</script>

