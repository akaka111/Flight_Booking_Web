<%-- 
    Document   : flight-detail
    Created on : 22 Jun 2025, 22:44:44
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
        <title>Chi tiết chuyến bay - Flight Booking Web</title>
        <link rel="preconnect" href="https://fonts.googleapis.com">
        <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
        <link href="https://fonts.googleapis.com/css2?family=Montserrat:wght@400;500;700&display=swap" rel="stylesheet">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css"/>
        <style>
            :root {
                --primary-color: #00529b;
                --secondary-color: #e53935;
                --text-color: #333;
                --border-color: #dee2e6;
                --background-light: #f8f9fa;
                --white: #fff;
            }
            body {
                font-family: 'Montserrat', sans-serif;
                background-color: var(--background-light);
                margin: 0;
            }
            .container {
                max-width: 1200px;
                margin: 20px auto;
                padding: 0 20px;
            }
            .page-wrapper {
                display: flex;
                gap: 30px;
            }
            .main-content {
                flex: 3;
            }
            .sidebar {
                flex: 1;
            }

            .itinerary-card, .price-summary {
                background: var(--white);
                border: 1px solid var(--border-color);
                border-radius: 8px;
                margin-bottom: 20px;
            }
            .itinerary-header {
                padding: 15px 20px;
                border-bottom: 1px solid var(--border-color);
            }
            .itinerary-header h2 {
                margin: 0;
                font-size: 1.4em;
                color: var(--primary-color);
            }
            .itinerary-body {
                padding: 20px;
            }
            .flight-path {
                display: flex;
                justify-content: space-between;
                align-items: center;
                margin-bottom: 20px;
            }
            .location {
                text-align: center;
            }
            .location .code {
                font-size: 2em;
                font-weight: 700;
            }
            .location .name {
                color: #6c757d;
            }
            .flight-icon {
                font-size: 1.5em;
                color: #6c757d;
            }

            .fare-options {
                display: flex;
                gap: 10px;
                margin-bottom: 25px;
                border-bottom: 1px solid var(--border-color);
                padding-bottom: 25px;
            }
            .fare-option {
                flex: 1;
                border: 2px solid var(--border-color);
                border-radius: 8px;
                text-align: center;
                padding: 15px;
                cursor: pointer;
                transition: all 0.3s ease;
            }
            .fare-option.active, .fare-option:hover {
                border-color: var(--secondary-color);
                background-color: #fff8f7;
            }
            .fare-option .class-name {
                font-weight: 700;
            }
            .fare-option .price {
                font-size: 1.2em;
                font-weight: 700;
                color: var(--text-color);
            }
            .fare-option .currency {
                font-size: 0.9em;
            }

            .flight-timeline {
                padding-left: 20px;
                border-left: 3px solid var(--primary-color);
            }
            .time-point {
                position: relative;
                margin-bottom: 20px;
            }
            .time-point::before {
                content: '';
                position: absolute;
                left: -29.5px;
                top: 5px;
                width: 15px;
                height: 15px;
                background: var(--white);
                border: 3px solid var(--primary-color);
                border-radius: 50%;
            }
            .time-info {
                font-weight: 700;
                font-size: 1.1em;
            }
            .location-info {
                color: #6c757d;
            }

            .price-summary-header {
                background-color: var(--secondary-color);
                color: var(--white);
                padding: 15px 20px;
                border-top-left-radius: 8px;
                border-top-right-radius: 8px;
            }
            .price-summary-header h3 {
                margin: 0;
            }
            .price-summary-body {
                padding: 20px;
            }
            .price-row {
                display: flex;
                justify-content: space-between;
                margin-bottom: 10px;
            }
            .price-total {
                border-top: 1px solid var(--border-color);
                margin-top: 15px;
                padding-top: 15px;
                font-weight: 700;
                font-size: 1.2em;
            }
            .price-total .total-amount {
                color: var(--secondary-color);
            }
            .btn-continue {
                display: block;
                width: 100%;
                padding: 15px;
                background: #f9c152;
                border: none;
                border-radius: 5px;
                font-size: 1.1em;
                font-weight: 700;
                cursor: pointer;
                text-align: center;
                text-decoration: none;
                color: #333;
                margin-top: 20px;
            }

        </style>
    </head>
    <body>

        <header>
            <jsp:include page="/WEB-INF/user/components/header.jsp" /> 
        </header>
        <c:if test="${not empty flight}">
            <%-- Tính toán các giá trị để hiển thị --%>
            <c:set var="basePrice" value="${flight.price / 1.1}" />
            <c:set var="taxes" value="${flight.price - basePrice}" />

            <div class="container">
                <div class="page-wrapper">
                    <div class="main-content">
                        <div class="itinerary-card">
                            <div class="itinerary-header">
                                <div class="flight-path">
                                    <div class="location">
                                        <div class="code">${flight.routeFrom.substring(0,3).toUpperCase()}</div>
                                        <div class="name">${flight.routeFrom}</div>
                                    </div>
                                    <i class="fa-solid fa-plane-departure flight-icon"></i>
                                    <div class="location">
                                        <div class="code">${flight.routeTo.substring(0,3).toUpperCase()}</div>
                                        <div class="name">${flight.routeTo}</div>
                                    </div>
                                </div>
                            </div>
                            <div class="itinerary-body">
                                <%-- Sửa lại đoạn này trong flight-detail.jsp --%>
                                <div class="fare-options">
                                    <div class="fare-option" data-class="BUSINESS" data-price="${flight.priceBusiness}">
                                        <span class="class-name">BUSINESS</span>
                                        <span class="price"><fmt:formatNumber value="${flight.priceBusiness}" type="number" maxFractionDigits="0"/> <span class="currency">VND</span></span>
                                    </div>
                                    <div class="fare-option" data-class="SKYBOSS" data-price="${flight.priceSkyboss}">
                                        <span class="class-name">SKYBOSS</span>
                                        <span class="price"><fmt:formatNumber value="${flight.priceSkyboss}" type="number" maxFractionDigits="0"/> <span class="currency">VND</span></span>
                                    </div>
                                    <div class="fare-option" data-class="DELUXE" data-price="${flight.priceDeluxe}">
                                        <span class="class-name">DELUXE</span>
                                        <span class="price"><fmt:formatNumber value="${flight.priceDeluxe}" type="number" maxFractionDigits="0"/> <span class="currency">VND</span></span>
                                    </div>
                                    <div class="fare-option active" data-class="ECO" data-price="${flight.price}">
                                        <span class="class-name">ECO</span>
                                        <span class="price"><fmt:formatNumber value="${flight.price}" type="number" maxFractionDigits="0"/> <span class="currency">VND</span></span>
                                    </div>
                                </div>
                                <div class="flight-timeline">
                                    <div class="time-point">
                                        <div class="time-info"><fmt:formatDate value="${flight.departureTime}" pattern="HH:mm" /></div>
                                        <div class="location-info">${flight.routeFrom}</div>
                                    </div>
                                    <div class="time-point" style="border: none; padding-left: 20px;">
                                        <i class="fa-regular fa-clock"></i> Bay thẳng
                                    </div>
                                    <div class="time-point">
                                        <div class="time-info"><fmt:formatDate value="${flight.arrivalTime}" pattern="HH:mm" /></div>
                                        <div class="location-info">${flight.routeTo}</div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <%-- Sửa lại đoạn này trong flight-detail.jsp --%>
                    <div class="sidebar">
                        <div class="price-summary">
                            <div class="price-summary-header"><h3>THÔNG TIN ĐẶT CHỖ</h3></div>
                            <form action="booking-confirmation" method="post">
                                <div class="price-summary-body">
                                    <h4>Chuyến đi: <span id="flight-class-summary" style="color: var(--secondary-color)"></span></h4>
                                    <p>${flight.routeFrom} - ${flight.routeTo}</p>
                                    <hr>
                                    <div class="price-row">
                                        <span>Giá vé</span>
                                        <span id="summary-base-price">...</span>
                                    </div>
                                    <div class="price-row">
                                        <span>Thuế, phí</span>
                                        <span id="summary-taxes">...</span>
                                    </div>
                                    <div class="price-row">
                                        <span>Dịch vụ</span>
                                        <span>0 VND</span>
                                    </div>
                                    <div class="price-row price-total">
                                        <span>Tổng tiền</span>
                                        <span class="total-amount" id="summary-total-price">...</span>
                                    </div>

                                    <!-- Input ẩn để gửi dữ liệu -->
                                    <input type="hidden" name="flightId" value="${flight.flightId}">
                                    <input type="hidden" name="selectedClass" id="selectedClass" value="">
                                    <input type="hidden" name="finalPrice" id="finalPrice" value="">

                                    <button type="submit" class="btn-continue">Đi tiếp</button>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </c:if>
        <c:if test="${empty flight}">
            <div class="container">
                <h2>Không tìm thấy chuyến bay</h2>
                <p>Rất tiếc, chuyến bay với ID bạn cung cấp không tồn tại hoặc đã bị xóa.</p>
                <a href="home">Quay về trang chủ</a>
            </div>
        </c:if>
        <%-- Dán đoạn này vào ngay trước thẻ </body> --%>
        <script>
            document.addEventListener('DOMContentLoaded', function () {
                const fareOptions = document.querySelectorAll('.fare-option');

                // Lấy các phần tử trong sidebar để cập nhật
                const summaryClass = document.getElementById('flight-class-summary');
                const summaryBasePrice = document.getElementById('summary-base-price');
                const summaryTaxes = document.getElementById('summary-taxes');
                const summaryTotalPrice = document.getElementById('summary-total-price');

                // Lấy các input ẩn trong form
                const selectedClassInput = document.getElementById('selectedClass');
                const finalPriceInput = document.getElementById('finalPrice');

                // Hàm định dạng tiền tệ Việt Nam
                function formatCurrency(value) {
                    return new Intl.NumberFormat('vi-VN', {style: 'decimal'}).format(value) + ' VND';
                }

                // Hàm cập nhật bảng tóm tắt
                function updateSummary(selectedElement) {
                    const price = parseFloat(selectedElement.dataset.price);
                    const className = selectedElement.dataset.class;

                    // Giả sử thuế VAT là 10%, bạn có thể thay đổi công thức này
                    const basePrice = price / 1.1;
                    const taxes = price - basePrice;

                    // Cập nhật phần hiển thị trên sidebar
                    if (summaryClass)
                        summaryClass.textContent = className;
                    if (summaryBasePrice)
                        summaryBasePrice.textContent = formatCurrency(basePrice.toFixed(0));
                    if (summaryTaxes)
                        summaryTaxes.textContent = formatCurrency(taxes.toFixed(0));
                    if (summaryTotalPrice)
                        summaryTotalPrice.textContent = formatCurrency(price);

                    // Cập nhật giá trị vào các input ẩn để gửi đi khi submit form
                    if (selectedClassInput)
                        selectedClassInput.value = className;
                    if (finalPriceInput)
                        finalPriceInput.value = price;
                }

                // Gắn sự kiện click cho từng thẻ hạng vé
                fareOptions.forEach(option => {
                    option.addEventListener('click', function () {
                        // Xóa class 'active' khỏi tất cả các thẻ
                        fareOptions.forEach(opt => opt.classList.remove('active'));
                        // Thêm class 'active' vào thẻ vừa được click
                        this.classList.add('active');
                        // Gọi hàm cập nhật bảng giá
                        updateSummary(this);
                    });
                });

                // Tự động cập nhật giá cho hạng vé đang active khi tải trang
                const initialActiveOption = document.querySelector('.fare-option.active');
                if (initialActiveOption) {
                    updateSummary(initialActiveOption);
                }
            });
        </script>

    </body>
</html>