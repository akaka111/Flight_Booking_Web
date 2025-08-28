
<%-- 
    Document   : home
    Created on : 17-Jun-2025
    Author     : Admin
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Flight Booking Web - Đặt vé máy bay giá rẻ</title>

        <!-- Google Fonts -->
        <link rel="preconnect" href="https://fonts.googleapis.com">
        <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
        <link href="https://fonts.googleapis.com/css2?family=Montserrat:wght@400;500;700&display=swap" rel="stylesheet">

        <!-- Font Awesome Icons -->
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css"/>

        <style>
            :root {
                --primary-color: #007bff;
                --secondary-color: #ff6f61;
                --text-color: #343a40;
                --light-gray: #f8f9fa;
                --white: #ffffff;
                --shadow: 0 5px 15px rgba(0,0,0,0.1);
            }
            * {
                box-sizing: border-box;
                margin: 0;
                padding: 0;
            }
            body {
                font-family: 'Montserrat', sans-serif;
                background-color: var(--light-gray);
                color: var(--text-color);
                min-height: 100vh;
                display: flex;
                flex-direction: column;
            }
            .container {
                max-width: 1200px;
                margin: 0 auto;
                padding: 0 20px;
            }
            .main-content {
                flex: 1;
            }
            /* Hero Section (Slider + Search) */
            .hero-section {
                position: relative;
                height: 60vh;
                min-height: 450px;
                color: var(--white);
                display: flex;
                align-items: center;
                justify-content: center;
                text-align: center;
            }
            .slider {
                position: absolute;
                top: 0;
                left: 0;
                width: 100%;
                height: 100%;
                z-index: 1;
            }
            .slide {
                position: absolute;
                width: 100%;
                height: 100%;
                background-size: cover;
                background-position: center;
                opacity: 0;
                transition: opacity 1.5s ease-in-out;
            }
            .slide.active {
                opacity: 1;
            }
            .hero-overlay {
                position: absolute;
                top: 0;
                left: 0;
                width: 100%;
                height: 100%;
                background-color: rgba(0, 0, 0, 0.5);
                z-index: 2;
            }
            .hero-content {
                position: relative;
                z-index: 3;
            }
            .hero-content h1 {
                font-size: 3em;
                margin-bottom: 20px;
                text-shadow: 0 2px 4px rgba(0,0,0,0.5);
            }
            .search-form-container {
                background-color: rgba(255,255,255,0.9);
                padding: 30px;
                border-radius: 10px;
                box-shadow: 0 10px 30px rgba(0,0,0,0.2);
                backdrop-filter: blur(5px);
            }
            .search-form {
                display: flex;
                gap: 15px;
                align-items: flex-end;
                flex-wrap: wrap;
                justify-content: center;
            }
            .form-group {
                display: flex;
                flex-direction: column;
            }
            .form-group label {
                font-size: 0.9em;
                color: #555;
                margin-bottom: 5px;
                text-align: left;
            }
            .form-group input {
                padding: 12px;
                border: 1px solid #ccc;
                border-radius: 5px;
                font-size: 1em;
            }
            .btn-search {
                padding: 12px 30px;
                font-size: 1.1em;
                background-color: var(--secondary-color);
                color: var(--white);
                border: none;
                border-radius: 5px;
                cursor: pointer;
                transition: background-color 0.3s;
            }
            .btn-search:hover {
                background-color: #e65c50;
            }
            /* Featured Deals Section */
            .featured-deals-section {
                padding: 60px 0;
                background-color: #e3f2fd;
                position: relative;
                overflow: hidden;
            }
            .cloud-border {
                position: absolute;
                top: 0;
                left: 0;
                width: 100%;
                height: 40px;
                background-image: radial-gradient(circle at 50% 100%, transparent 20px, #4a2d6e 21px);
                background-size: 50px 40px;
                background-repeat: repeat-x;
                opacity: 0.8;
            }
            .section-title {
                text-align: center;
                font-size: 2.2em;
                margin-bottom: 40px;
                color: var(--text-color);
                font-weight: 700;
            }
            .deals-container {
                display: flex;
                justify-content: center;
                gap: 40px;
                flex-wrap: wrap;
            }
            .deal-card {
                text-decoration: none;
                color: var(--text-color);
                transition: transform 0.3s ease;
            }
            .deal-card:hover {
                transform: scale(1.05);
            }
            .deal-card-inner {
                width: 200px;
                background-color: #ffeb3b;
                border-radius: 40px 40px 10px 10px;
                padding: 15px;
                text-align: center;
                box-shadow: 0 4px 12px rgba(0,0,0,0.15);
                position: relative;
            }
            .deal-card-inner::after {
                content: '';
                position: absolute;
                bottom: -15px;
                left: 50%;
                transform: translateX(-50%);
                width: 0;
                height: 0;
                border-left: 15px solid transparent;
                border-right: 15px solid transparent;
                border-top: 15px solid #ffeb3b;
            }
            .from-location {
                font-size: 1em;
                font-weight: 500;
                margin-bottom: 5px;
            }
            .from-location span {
                color: #e53935;
                font-weight: 700;
            }
            .price-tag {
                font-size: 0.8em;
                color: #555;
            }
            .price-amount {
                font-size: 2.2em;
                font-weight: 700;
                color: #e53935;
                line-height: 1;
            }
            .price-currency {
                font-size: 0.9em;
                color: #333;
            }
            .to-location {
                margin-top: 25px;
                font-size: 1em;
                font-weight: 500;
                text-align: center;
            }
            .to-location span {
                color: #e53935;
                font-weight: 700;
            }
            .no-flights {
                text-align: center;
                padding: 40px;
                font-size: 1.2em;
                color: #6c757d;
            }
        </style>
    </head>
    <body>
        <jsp:include page="/WEB-INF/user/components/header.jsp" />
        <div class="main-content">
            <section class="hero-section">
                <div class="slider">
                    <div class="slide active" style="background-image: url('https://images.unsplash.com/photo-1530521954074-e64f6810b32d?q=80&w=2070');"></div>
                    <div class="slide" style="background-image: url('https://images.unsplash.com/photo-1436491865332-7a61a109cc05?q=80&w=2074');"></div>
                    <div class="slide" style="background-image: url('https://images.unsplash.com/photo-1569154941061-e231b4725ef1?q=80&w=2070');"></div>
                </div>
                <div class="hero-overlay"></div>
                <div class="hero-content container">
                    <h1>Chuyến đi trong mơ, trong tầm tay bạn</h1>
                    <div class="search-form-container">
                        <form action="<c:url value='/search'/>" method="get" class="search-form">
                            <div class="form-group">
                                <label for="from"><i class="fa-solid fa-plane-departure"></i> Điểm đi</label>
                                <input type="text" id="from" name="from" placeholder="Thành phố, sân bay..." required>
                            </div>
                            <div class="form-group">
                                <label for="to"><i class="fa-solid fa-plane-arrival"></i> Điểm đến</label>
                                <input type="text" id="to" name="to" placeholder="Thành phố, sân bay..." required>
                            </div>
                            <div class="form-group">
                                <label for="departureDate"><i class="fa-solid fa-calendar-days"></i> Ngày đi</label>
                                <%
                                    java.text.SimpleDateFormat todayFormat = new java.text.SimpleDateFormat("yyyy-MM-dd");
                                    String today = todayFormat.format(new java.util.Date());
                                %>
                                <input type="date" id="departureDate" name="departureDate" required min="<%= today%>">

                            </div>
                            <button type="submit" class="btn-search"><i class="fa-solid fa-magnifying-glass"></i> Tìm kiếm</button>
                        </form>
                    </div>
                </div>
            </section>

            <section class="featured-deals-section">
                <div class="cloud-border"></div>
                <div class="container">
                    <h2 class="section-title">Vé máy bay giá tốt, khám phá thế giới</h2>
                    <div class="deals-container">                  
                        <c:choose>
                            <c:when test="${not empty flights}">
                                <c:forEach var="flight" items="${flights}" begin="0" end="4">                               
                                    <a href="<c:url value='/flight-detail?id=${flight.flightId}'/>" class="deal-card">
                                        <div class="deal-card-inner">
                                            <div class="from-location">Từ <span>${flight.routeFrom}</span></div>
                                            <div class="price-tag">Chỉ từ</div>
                                            <div class="price-amount">
                                                <fmt:formatNumber 
                                                    value="${ecoPrices[flight.flightId]}" 
                                                    type="number" 
                                                    maxFractionDigits="0" 
                                                    groupingUsed="true"/>
                                            </div>
                                            <div class="price-currency">VND</div>
                                        </div>
                                        <div class="to-location">Đến <span>${flight.routeTo}</span></div>
                                    </a>
                                </c:forEach>
                            </c:when>
                            <c:otherwise>
                                <p class="no-flights">Hiện không có chuyến bay nổi bật nào.</p>
                            </c:otherwise>
                        </c:choose>
                    </div>              
                </div>
            </section>
        </div>
        <footer>
            <jsp:include page="/WEB-INF/user/components/footer.jsp" /> 
        </footer>

        <script>
            document.addEventListener('DOMContentLoaded', function () {
                const slides = document.querySelectorAll('.slider .slide');
                if (slides.length > 0) {
                    let currentSlide = 0;
                    function showSlide(index) {
                        slides.forEach((slide, i) => {
                            slide.style.opacity = (i === index) ? '1' : '0';
                        });
                    }

                    function nextSlide() {
                        currentSlide = (currentSlide + 1) % slides.length;
                        showSlide(currentSlide);
                    }

                    showSlide(0);
                    setInterval(nextSlide, 5000);
                }
            });
        </script>

        <!------------------------ ----------------------- Nút Chat nổi ----------------------- ----------------------- -->
        <style>
            #livechat-btn {
                position: fixed;
                bottom: 20px;
                right: 20px;
                background: #007bff;
                color: #fff;
                border: none;
                padding: 12px;
                border-radius: 50%;
                cursor: pointer;
                z-index: 10000;
            }
            #livechat-box {
                display: none;
                flex-direction: column;
                position: fixed;
                bottom: 70px;
                right: 20px;
                width: 320px;
                height: 400px;
                background: #fff;
                border: 1px solid #ccc;
                border-radius: 10px;
                box-shadow: 0 4px 12px rgba(0,0,0,0.15);
                z-index: 9999;
            }
            #chat-messages {
                flex: 1;
                overflow-y: auto;
                padding: 8px;
                display: flex;
                flex-direction: column;
                gap: 4px;
            }
            #chat-input {
                display: flex;
                border-top: 1px solid #ccc;
            }
            #chat-input input {
                flex: 1;
                padding: 8px;
                border: none;
            }
            #chat-input button {
                padding: 8px 12px;
                background: #007bff;
                border: none;
                color: #fff;
                cursor: pointer;
            }
            .msg {
                margin: 4px 0;
                padding: 6px 10px;
                border-radius: 8px;
                max-width: 75%;
                display: flex;
                flex-direction: column;
            }
            .msg.staff {
                background: #007bff;
                color: white;
                align-self: flex-end;
            }
            .msg.user {
                background: #d5f7fd;
                color: black;
                align-self: flex-start;
            }
            .msg.guest {
                background: #d1e7dd;
                color: black;
                align-self: flex-start;
            }
            .msg b {
                font-weight: bold;
                margin-bottom: 2px;
            }
        </style>

        <!-- Nút nổi -->
        <button id="livechat-btn" aria-label="Mở chat">💬</button>

        <!-- Form guest -->
        <div id="guest-form" style="position:fixed; bottom:70px; right:20px; width:320px; background:#fff; border:1px solid #ccc; padding:12px; display:none; z-index:9999;">
            <p>Vui lòng nhập tên để bắt đầu:</p>
            <input type="text" id="guest-name" placeholder="Tên của bạn" style="width:100%; padding:8px; margin:8px 0;"/>
            <button onclick="saveGuestName()">Bắt đầu chat</button>
        </div>

        <!-- Hộp chat -->
        <div id="livechat-box" style="display:none;">
            <div id="chat-messages"></div>
            <div id="chat-input">
                <input type="text" id="chat-text" placeholder="Nhập tin nhắn..."/>
                <button id="send-btn">Gửi</button>
            </div>
        </div>

        <script>
            const userId = "<c:out value='${sessionScope.userId != null ? sessionScope.userId : ""}'/>";
            const btn = document.getElementById("livechat-btn");
            const box = document.getElementById("livechat-box");
            const chatMessages = document.getElementById("chat-messages");
            const chatText = document.getElementById("chat-text");
            const sendBtn = document.getElementById("send-btn");
            const guestForm = document.getElementById("guest-form");
            const livechatUrl = '<c:url value="/LivechatController"/>';

            let guestLabel = sessionStorage.getItem("guestLabel");
            if (!guestLabel || guestLabel === "null" || guestLabel.trim() === "") {
                guestLabel = null;
            }

            // Khi bấm nút mở chat
            btn.onclick = () => {
                if (userId && userId.trim() !== "" && userId !== "null") {
                    guestForm.style.display = "none";
                    box.style.display = "flex";
                    loadMessages();
                    return;
                }
                // Guest chưa nhập tên → hiện form
                if (!guestLabel) {
                    guestForm.style.display = "block";
                    box.style.display = "none";
                } else {
                    // Guest đã nhập tên → mở chat luôn
                    guestForm.style.display = "none";
                    box.style.display = "flex";
                    loadMessages();
                }
            };

            // Gửi tin nhắn
            sendBtn.onclick = () => {
                const content = chatText.value.trim();
                if (!content)
                    return;

                const form = new FormData(); // tạo FormData trước
                form.append("content", content);

                if (userId && userId.trim() !== "" && userId !== "null") {
                    form.append("type", "user");
                } else {
                    if (!guestLabel) {
                        alert("Vui lòng nhập tên trước khi gửi tin nhắn!");
                        guestForm.style.display = "block";
                        box.style.display = "none";
                        return;
                    }
                    form.append("type", "guest");
                    form.append("guestLabel", guestLabel);
                }

                console.log("=== Debug: FormData chuẩn bị gửi ===");
                for (let pair of form.entries()) {
                    console.log(pair[0] + ": " + pair[1]);
                }

                fetch(livechatUrl, {method: "POST", body: form})
                        .then(r => r.json())
                        .then(res => {
                            if (res.success) {
                                chatText.value = "";
                                loadMessages();
                            } else {
                                alert("Gửi tin nhắn thất bại!");
                            }
                        });
            };

            // Lưu tên guest
            function saveGuestName() {
                const name = document.getElementById("guest-name").value.trim();
                if (!name)
                    return alert("Vui lòng nhập tên!");
                guestLabel = name;
                sessionStorage.setItem("guestLabel", name);
                guestForm.style.display = "none";
                box.style.display = "flex";
                chatMessages.innerHTML = ""; // 🔑 xoá sạch thông báo cũ
                loadMessages();
            }

            // Load tin nhắn
            function loadMessages() {
                const params = new URLSearchParams();

                if (userId && userId.trim() !== "" && userId !== "null") {
                    params.append("type", "user");
                } else if (guestLabel) {
                    params.append("type", "guest");
                    params.append("guestLabel", guestLabel);
                } else {
                    chatMessages.innerHTML = "<div class='msg guest'>Vui lòng nhập tên để bắt đầu chat!</div>";
                    return;
                }

                fetch(livechatUrl + "?" + params.toString())
                        .then(r => r.json())
                        .then(list => {
                            chatMessages.innerHTML = "";
                            list.forEach(m => {
                                const div = document.createElement("div");
                                div.className = "msg " + m.senderType;
                                div.textContent = m.content;
                                chatMessages.appendChild(div);
                            });
                            chatMessages.scrollTop = chatMessages.scrollHeight;

                            // Lưu tạm lịch sử vào sessionStorage
                            sessionStorage.setItem("chatHistory", JSON.stringify(list));
                        });

                // Load lại lịch sử cũ khi reload (nếu có)
                const history = sessionStorage.getItem("chatHistory");
                if (history) {
                    chatMessages.innerHTML = "";
                    JSON.parse(history).forEach(m => {
                        const div = document.createElement("div");
                        div.className = "msg " + m.senderType;
                        let senderName = m.senderType;
                        if (m.senderType === "staff")
                            senderName = "Staff";
                        if (m.senderType === "user")
                            senderName = m.senderName || "User";
                        if (m.senderType === "guest")
                            senderName = m.senderName || "Guest";
                        div.innerHTML = `<b>${senderName}:</b> ${m.content}`;
                        chatMessages.appendChild(div);
                    });
                    chatMessages.scrollTop = chatMessages.scrollHeight;
                }
            }

            // Xoá dữ liệu guest khi đóng tab/trình duyệt
            window.addEventListener("beforeunload", (e) => {
                if (!userId && guestLabel) { // chỉ guest mới cảnh báo
                    e.preventDefault();
                    e.returnValue = "Tin nhắn của bạn sẽ bị xóa khi thoát. Bạn có chắc muốn rời trang?";
                }
                sessionStorage.removeItem("guestLabel");
                sessionStorage.removeItem("chatHistory");
            });

            window.addEventListener("unload", () => {
                if (!userId && guestLabel) {
                    const form = new FormData();
                    form.append("type", "guest");
                    form.append("guestLabel", guestLabel);
                    form.append("action", "delete"); // thêm action delete
                    navigator.sendBeacon(livechatUrl, form);
                }
            });

            // Tự load lại tin nhắn
            setInterval(loadMessages, 2000);
        </script>
    </body>
</html>