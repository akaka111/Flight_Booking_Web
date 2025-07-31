<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%-- 
    Kiểm tra xem có thông tin đặt vé trong session không.
    Nếu không có, chuyển người dùng về trang chủ.
--%>
<c:if test="${empty sessionScope.tempBooking or empty sessionScope.tempPassenger}">
    <c:redirect url="/home" />
</c:if>

<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Thanh toán - Flight Booking Web</title>

        <link rel="preconnect" href="https://fonts.googleapis.com">
        <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
        <link href="https://fonts.googleapis.com/css2?family=Montserrat:wght@400;500;700&display=swap" rel="stylesheet">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css"/>

        <style>
            :root {
                --primary-color: #007bff;
                --secondary-color: #ff6f61;
                --text-color: #343a40;
                --border-color: #dee2e6;
                --light-gray: #f8f9fa;
                --white: #ffffff;
                --shadow: 0 5px 15px rgba(0,0,0,0.1);
                --success-color: #28a745;
                --danger-color: #dc3545;
            }

            body {
                font-family: 'Montserrat', sans-serif;
                background-color: var(--light-gray);
                color: var(--text-color);
                margin: 0;
                line-height: 1.6;
            }
            main {
                min-height: calc(100vh - 250px);
            }
            .container {
                max-width: 800px;
                margin: 30px auto;
                padding: 20px 40px;
                background-color: var(--white);
                border-radius: 8px;
                box-shadow: var(--shadow);
            }
            h1, h3 {
                color: var(--primary-color);
            }
            hr {
                border: 0;
                border-top: 1px solid var(--border-color);
                margin: 20px 0;
            }

            /* === KHUNG TÓM TẮT ĐƠN HÀNG === */
            .order-summary {
                border: 1px solid var(--border-color);
                padding: 20px;
                border-radius: 8px;
                margin-bottom: 30px;
                background-color: #fdfdfd;
            }
            .price-row {
                display: flex;
                justify-content: space-between;
                font-size: 1.1em;
                margin-bottom: 10px;
            }
            .price-row.total {
                font-size: 1.3em;
                font-weight: 700;
                color: var(--secondary-color);
                margin-top: 15px;
                padding-top: 15px;
                border-top: 1px solid var(--border-color);
            }
            .price-row.discount {
                color: var(--success-color);
            }

            /* === KHU VỰC VOUCHER === */
            .voucher-section {
                margin-bottom: 30px;
            }
            .voucher-input-group {
                display: flex;
                gap: 10px;
            }
            .voucher-input-group input {
                flex-grow: 1;
                padding: 10px 15px;
                border: 2px solid var(--border-color);
                border-radius: 5px;
                font-size: 1em;
            }
            .voucher-input-group button {
                padding: 10px 20px;
                border: none;
                background-color: var(--primary-color);
                color: var(--white);
                border-radius: 5px;
                cursor: pointer;
                font-weight: 500;
                transition: background-color 0.3s;
            }
            .voucher-input-group button:hover {
                background-color: #0056b3;
            }
            #voucher-message {
                margin-top: 10px;
                font-size: 0.9em;
                font-weight: 500;
            }

            /* === PHƯƠNG THỨC THANH TOÁN === */
            .payment-method {
                border: 2px solid var(--border-color);
                border-radius: 8px;
                padding: 15px;
                display: flex;
                align-items: center;
                background-color: #f8f9fa; /* Màu nền nhẹ để nổi bật */
            }
            .payment-method input[type="radio"] {
                margin-right: 15px;
            }

            /* === NÚT BẤM === */
            .btn-submit {
                display: block;
                width: 100%;
                padding: 15px;
                background: var(--secondary-color);
                border: none;
                border-radius: 5px;
                font-size: 1.1em;
                font-weight: 700;
                cursor: pointer;
                text-align: center;
                text-decoration: none;
                color: var(--white);
                margin-top: 20px;
                transition: background-color 0.3s ease;
            }
            .btn-submit:hover {
                background-color: #e65c50;
            }
        </style>
    </head>
    <body>
        <header>
            <jsp:include page="/WEB-INF/user/components/header.jsp" /> 
        </header>

        <main>
            <div class="container">
                <h1>Xác nhận và Thanh toán</h1>

                <%-- KHUNG TÓM TẮT ĐƠN HÀNG --%>
                <div class="order-summary">
                    <h3><i class="fas fa-receipt"></i> Tóm tắt đơn hàng</h3>
                    <div class="price-row">
                        <p><strong>Hành khách:</strong></p>
                        <p>${sessionScope.tempPassenger.fullName}</p>
                    </div>
                    <div class="price-row">
                        <p><strong>Hạng vé:</strong></p>
                        <p>${sessionScope.tempBooking.seatClass}</p>
                    </div>
                    <div class="price-row">
                        <p><strong>Tạm tính:</strong></p>
                        <p id="summary-subtotal"><fmt:formatNumber value="${sessionScope.tempBooking.totalPrice}" type="number" maxFractionDigits="0"/> VND</p>
                    </div>
                    <div class="price-row discount" id="discount-row" style="display: none;">
                        <p><strong>Giảm giá:</strong></p>
                        <p id="summary-discount-amount">0 VND</p>
                    </div>
                    <hr>
                    <div class="price-row total">
                        <p><strong>Tổng cộng:</strong></p>
                        <p id="summary-final-price"><fmt:formatNumber value="${sessionScope.tempBooking.totalPrice}" type="number" maxFractionDigits="0"/> VND</p>
                    </div>
                </div>

                <%-- KHU VỰC VOUCHER --%>
                <div class="voucher-section">
                    <h3><i class="fas fa-ticket-alt"></i> Mã giảm giá</h3>
                    <div class="voucher-input-group">
                        <input type="text" id="voucher-code-input" placeholder="Nhập mã giảm giá">
                        <button type="button" id="apply-voucher-btn">Áp dụng</button>
                    </div>
                    <p id="voucher-message"></p>
                </div>

                <h3><i class="fas fa-credit-card"></i> Phương thức thanh toán</h3>
                <form action="process-payment" method="post">

                    <div class="payment-method">
                        <input type="radio" id="online" name="paymentMethod" value="ONLINE" checked>
                        <label for="online">Thanh toán Online (Thẻ tín dụng/ATM)</label>
                    </div>

                    <%-- Các trường input ẩn để gửi dữ liệu cuối cùng về server --%>
                    <input type="hidden" name="voucherCode" id="applied-voucher-code" value="">
                    <input type="hidden" name="finalPrice" id="final-price-hidden-input" value="${sessionScope.tempBooking.totalPrice}">

                    <button type="submit" class="btn-submit">Tiến hành Thanh toán</button>
                </form>
            </div>
        </main>

        <footer>
            <jsp:include page="/WEB-INF/user/components/footer.jsp" /> 
        </footer> 

        <script>
            const applyBtn = document.getElementById('apply-voucher-btn');
            const voucherInput = document.getElementById('voucher-code-input');
            const summaryDiscount = document.getElementById('summary-discount-amount');
            const discountRow = document.getElementById('discount-row');
            const summaryFinalPrice = document.getElementById('summary-final-price');
            const voucherMessage = document.getElementById('voucher-message');
            const hiddenVoucherInput = document.getElementById('applied-voucher-code');
            const hiddenFinalPriceInput = document.getElementById('final-price-hidden-input');

            // Lấy giá gốc từ thẻ hiển thị (dạng "123,456 VND") và convert về dạng số
            const originalTotalPrice = parseFloat(
                    summaryFinalPrice.textContent.replace(/[^\d]/g, '')
                    );

            function formatCurrency(number) {
                return number.toLocaleString('vi-VN') + ' VND';
            }

            // Hàm để reset giao diện về trạng thái ban đầu
            function resetVoucherUI(message) {
                discountRow.style.display = 'none';
                summaryFinalPrice.textContent = formatCurrency(originalTotalPrice);
                voucherMessage.textContent = message;
                voucherMessage.style.color = 'var(--danger-color)';
                hiddenVoucherInput.value = '';
                hiddenFinalPriceInput.value = originalTotalPrice;
            }

            applyBtn.addEventListener('click', function () {
                const code = voucherInput.value.trim().toUpperCase();

                if (!code) {
                    resetVoucherUI("Vui lòng nhập mã giảm giá.");
                    return;
                }

                // Dữ liệu gửi đi nên rõ ràng hơn
                const formData = new URLSearchParams();
                formData.append('voucherCode', code);
                // Gửi luôn giá gốc để server có thể tính toán nếu cần
                formData.append('originalPrice', originalTotalPrice);

                fetch('applyVoucher', {// Đảm bảo URL là chính xác
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded'
                    },
                    body: formData
                })
                        .then(response => {
                            if (!response.ok) {
                                throw new Error('Network response was not ok');
                            }
                            return response.json();
                        })
                        .then(data => {
                            if (data.success) {
                                // =================================================================
                                // ====> LOGIC KIỂM TRA NGÀY THÁNG ĐƯỢC THÊM VÀO ĐÂY <====
                                // =================================================================
                                const today = new Date();
                                today.setHours(0, 0, 0, 0); // Xóa thông tin giờ để chỉ so sánh ngày

                                const validFrom = new Date(data.validFrom);
                                const validTo = new Date(data.validTo);

                                if (today < validFrom || today > validTo) {
                                    resetVoucherUI("Mã giảm giá đã hết hạn hoặc chưa đến ngày áp dụng.");
                                    return; // Dừng lại nếu ngày không hợp lệ
                                }
                                // =================================================================

                                // Nếu ngày hợp lệ, tiếp tục xử lý
                                const discountPercent = data.discountPercent;
                                const discountAmount = originalTotalPrice * (discountPercent / 100);
                                const newFinalPrice = originalTotalPrice - discountAmount;

                                summaryDiscount.textContent = "- " + formatCurrency(discountAmount);
                                discountRow.style.display = 'flex';
                                summaryFinalPrice.textContent = formatCurrency(newFinalPrice);

                                voucherMessage.textContent = data.message;
                                voucherMessage.style.color = 'var(--success-color)';

                                hiddenVoucherInput.value = data.code;
                                hiddenFinalPriceInput.value = newFinalPrice;

                            } else {
                                // Xử lý các lỗi khác do server trả về (hết lượt dùng, không tồn tại...)
                                resetVoucherUI(data.message);
                            }
                        })
                        .catch(error => {
                            console.error("Lỗi khi gửi request voucher:", error);
                            resetVoucherUI("Đã có lỗi xảy ra. Vui lòng thử lại.");
                        });
            });
        </script>

    </body>
</html>