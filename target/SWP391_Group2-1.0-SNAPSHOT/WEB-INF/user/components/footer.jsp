<%-- 
    Document   : footer
    Created on : 17-Jun-2025
    Author     : Admin
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<style>
    /* CSS cho Footer */
    .main-footer {
        background-color: #343a40; /* Màu nền tối */
        color: #f8f9fa; /* Màu chữ sáng */
        padding: 40px 0;
        margin-top: auto; /* Đẩy footer xuống cuối nếu nội dung trang ngắn */
    }

    .footer-container {
        max-width: 1200px;
        margin: 0 auto;
        padding: 0 20px;
        display: flex;
        justify-content: space-between;
        align-items: center;
        flex-wrap: wrap; /* Cho phép xuống hàng trên màn hình nhỏ */
        gap: 20px; /* Khoảng cách giữa các mục */
    }

    .footer-about, .footer-links, .footer-social {
        flex: 1;
        min-width: 250px; /* Độ rộng tối thiểu cho các cột */
    }
    
    .footer-about h3 {
        color: var(--primary-color, #007bff); /* Sử dụng biến màu chính nếu có */
        margin-bottom: 15px;
    }

    .footer-links ul {
        list-style: none;
        padding: 0;
        margin: 0;
    }

    .footer-links ul li {
        margin-bottom: 10px;
    }

    .footer-links a {
        color: #adb5bd;
        text-decoration: none;
        transition: color 0.3s ease;
    }

    .footer-links a:hover {
        color: #ffffff;
    }
    
    .footer-social h3 {
        margin-bottom: 15px;
    }

    .social-icons a {
        color: #f8f9fa;
        font-size: 1.5rem;
        margin-right: 15px;
        transition: color 0.3s ease;
    }

    .social-icons a:hover {
        color: var(--primary-color, #007bff);
    }

    .footer-bottom {
        text-align: center;
        margin-top: 30px;
        padding-top: 20px;
        border-top: 1px solid #495057;
        width: 100%;
    }

    .footer-bottom p {
        margin: 0;
        color: #6c757d;
        font-size: 0.9em;
    }

</style>

<div class="main-footer">
    <div class="footer-container">
        <div class="footer-about">
            <h3><i class="fa-solid fa-paper-plane"></i> Flight Booking</h3>
            <p>Nền tảng đặt vé máy bay trực tuyến, mang đến cho bạn những chuyến đi tuyệt vời với giá cả phải chăng.</p>
        </div>

        <div class="footer-links">
            <h3>Liên kết nhanh</h3>
            <ul>
                <li><a href="${pageContext.request.contextPath}/home">Trang chủ</a></li>
                <li><a href="#">Về chúng tôi</a></li>
                <li><a href="#">Khuyến mãi</a></li>
                <li><a href="#">Liên hệ</a></li>
            </ul>
        </div>
        
        <div class="footer-social">
            <h3>Theo dõi chúng tôi</h3>
            <div class="social-icons">
                 <a href="#" aria-label="Facebook"><i class="fab fa-facebook-f"></i></a>
                 <a href="#" aria-label="Instagram"><i class="fab fa-instagram"></i></a>
                 <a href="#" aria-label="Twitter"><i class="fab fa-twitter"></i></a>
            </div>
        </div>
        
        <div class="footer-bottom">
            <p>© 2025 Flight Booking Web. All Rights Reserved.</p>
        </div>
    </div>
</div>