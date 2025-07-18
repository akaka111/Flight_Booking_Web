<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%-- 
    Kiểm tra xem có thông tin đặt vé trong session không.
    Nếu không có, người dùng có thể đã vào thẳng trang này,
    chúng ta sẽ chuyển họ về trang chủ.
--%>


<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <title>Thanh toán</title>
        <%-- (Copy các link CSS và thẻ <style> từ các trang khác của bạn vào đây) --%>
        <style>
            /* (Copy CSS từ passenger.jsp và dán vào đây) */
            .payment-summary {
                border: 1px solid #ccc;
                padding: 20px;
                border-radius: 8px;
                margin-bottom: 20px;
            }
            .payment-methods {
                list-style: none;
                padding: 0;
            }
            .payment-methods li {
                margin-bottom: 10px;
            }
            .btn {
                display: block;
                width: 100%;
                padding: 15px;
                font-size: 1.1em;
                font-weight: 700;
                cursor: pointer;
                text-align: center;
                text-decoration: none;
                border: none;
                border-radius: 5px;
                margin-top: 20px;
                color: #fff;
                background-color: #4CAF50;
            }
            .container {
                max-width: 800px;
                margin: 30px auto;
                padding: 20px 40px;
                background-color: #fff;
                border-radius: 8px;
            }
            body {
                font-family: 'Montserrat', sans-serif;
                background-color: #f8f9fa;
            }
        </style>
    </head>
    <body>
        <div class="container">
            <h1>Xác nhận và Thanh toán</h1>

            <div class="payment-summary">
                <h3>Tóm tắt đơn hàng</h3>
                <p><strong>Hành khách:</strong> ${sessionScope.tempPassenger.fullName}</p>
                <p><strong>Hạng vé:</strong> ${sessionScope.tempBooking.seatClass}</p>
                <p><strong>Tổng tiền cần thanh toán:</strong> 
                    <span style="color: #e53935; font-weight: bold;">
                        <fmt:formatNumber value="${sessionScope.tempBooking.totalPrice}" type="number" maxFractionDigits="0"/> VND
                    </span>
                </p>
            </div>

            <h3>Chọn phương thức thanh toán</h3>
            <form action="process-payment" method="post">
                <%-- 
                    Không cần gửi bookingId ở đây vì servlet ProcessPaymentController
                    sẽ tự lấy thông tin từ session.
                --%>

                <ul class="payment-methods">
                    <li>
                        <input type="radio" id="cod" name="paymentMethod" value="COD" checked>
                        <label for="cod">Thanh toán khi nhận vé (COD)</label>
                    </li>
                    <li>
                        <input type="radio" id="online" name="paymentMethod" value="ONLINE">
                        <label for="online">Thanh toán Online (Thẻ tín dụng/ATM)</label>
                    </li>
                </ul>

               

                <button type="submit" class="btn">Xác nhận Thanh toán</button>
            </form>
        </div>
    </body>
</html>