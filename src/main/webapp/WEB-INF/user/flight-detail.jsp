
<%--
    Document : flight-detail
    Created on : 22 Jun 2025, 22:44:44
    Author : Admin
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
                --primary-color: #007bff;
                --secondary-color: #ff6f61;
                --text-color: #343a40;
                --border-color: #dee2e6;
                --light-gray: #f8f9fa;
                --white: #ffffff;
                --shadow: 0 5px 15px rgba(0,0,0,0.1);
            }
            body {
                font-family: 'Montserrat', sans-serif;
                background-color: var(--light-gray);
                margin: 0;
            }
            .container {
                max-width: 1200px;
                margin: 30px auto;
                padding: 0 20px;
            }
            main {
                min-height: calc(100vh - 250px);
            }
            .page-wrapper {
                display: flex;
                gap: 30px;
                flex-wrap: wrap;
            }
            .main-content {
                flex: 3;
                min-width: 300px;
            }
            .sidebar {
                flex: 1;
                min-width: 280px;
            }
            .itinerary-card, .price-summary {
                background: var(--white);
                border: 1px solid var(--border-color);
                border-radius: 8px;
                margin-bottom: 20px;
                box-shadow: var(--shadow);
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
                color: var(--primary-color);
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
                box-shadow: 0 4px 10px rgba(255, 111, 97, 0.3);
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
                background-color: var(--primary-color);
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
            .btn-continue:hover {
                background-color: #e65c50;
            }
            .btn-continue:disabled {
                background-color: #cccccc;
                cursor: not-allowed;
            }
            .no-tickets-message {
                color: #f44336;
                font-weight: 500;
                text-align: center;
            }
        </style>
    </head>
    <body>
        <header>
            <jsp:include page="/WEB-INF/user/components/header.jsp" />
        </header>
        <main>
            <c:if test="${not empty flights}">
                <c:forEach var="flight" items="${flights}" varStatus="loop">
                    <%
                        java.util.Date now = new java.util.Date();
                        java.sql.Timestamp departure = ((model.Flight) pageContext.getAttribute("flight")).getDepartureTime();
                        boolean bookingClosed = false;
                        if (departure != null) {
                            long millisBeforeFlight = departure.getTime() - now.getTime();
                            long twoHoursMillis = 2 * 60 * 60 * 1000;
                            if (millisBeforeFlight <= twoHoursMillis) {
                                bookingClosed = true;
                            }
                        }
                        pageContext.setAttribute("bookingClosed", bookingClosed);
                    %>
                    <c:if test="${bookingClosed}">
                        <div style="background-color: #ffdddd; border-left: 6px solid #f44336; padding: 20px; margin-bottom: 20px;">
                            <strong>⚠️ Lưu ý:</strong> Chuyến bay này sẽ khởi hành trong vòng 2 tiếng. Hệ thống đã đóng chức năng đặt vé.
                        </div>
                    </c:if>
                    <div class="container">
                        <div class="page-wrapper" data-flight-id="${flight.flightId}">
                            <div class="main-content">
                                <div class="itinerary-card">
                                    <div class="itinerary-header">
                                        <div class="flight-path">
                                            <div class="location">
                                                <div class="code">${flight.route.originIata}</div>
                                                <div class="name">${flight.route.originCity}</div>
                                            </div>
                                            <i class="fa-solid fa-plane-departure flight-icon"></i>
                                            <div class="location">
                                                <div class="code">${flight.route.destIata}</div>
                                                <div class="name">${flight.route.destCity}</div>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="itinerary-body">
                                        <div class="fare-options" data-flight-id="${flight.flightId}">
                                            <c:forEach var="ticket" items="${ticketClassesMap[flight.flightId]}">
                                                <div class="fare-option ${ticket.className == 'ECO' ? 'active' : ''}"
                                                     data-class="${ticket.className}"
                                                     data-price="${ticket.price}"
                                                     data-flight-id="${flight.flightId}">
                                                    <span class="class-name">${ticket.className}</span>
                                                    <span class="price">
                                                        <fmt:formatNumber value="${ticket.price}" type="number" maxFractionDigits="0"/>
                                                        <span class="currency">VND</span>
                                                    </span>
                                                </div>
                                            </c:forEach>
                                        </div>
                                        <div class="flight-timeline">
                                            <div class="time-point">
                                                <div class="time-info"><fmt:formatDate value="${flight.departureTime}" pattern="HH:mm" /></div>
                                                <div class="location-info">${flight.route.originCity}</div>
                                            </div>
                                            <div class="time-point" style="border: none; padding-left: 20px;">
                                                <i class="fa-regular fa-clock"></i> Bay thẳng
                                            </div>
                                            <div class="time-point">
                                                <div class="time-info"><fmt:formatDate value="${flight.arrivalTime}" pattern="HH:mm" /></div>
                                                <div class="location-info">${flight.route.destCity}</div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="sidebar">
                                <div class="price-summary">
                                    <div class="price-summary-header"><h3>THÔNG TIN ĐẶT CHỖ</h3></div>
                                    <form action="passenger" method="get" data-flight-id="${flight.flightId}">
                                        <div class="price-summary-body">
                                            <c:choose>
                                                <c:when test="${empty ticketClassesMap[flight.flightId]}">
                                                    <p class="no-tickets-message">Không có hạng vé nào khả dụng cho chuyến bay này.</p>
                                                </c:when>
                                                <c:otherwise>
                                                    <h4>Chuyến đi: <span class="flight-class-summary" style="color: var(--secondary-color)"></span></h4>
                                                    <p>${flight.route.originCity} - ${flight.route.destCity}</p>
                                                    <hr>
                                                    <div class="price-row">
                                                        <span>Giá vé</span>
                                                        <span class="summary-base-price">...</span>
                                                    </div>
                                                    <div class="price-row">
                                                        <span>Thuế, phí (8%)</span>
                                                        <span class="summary-taxes">...</span>
                                                    </div>
                                                    <div class="price-row">
                                                        <span>Dịch vụ</span>
                                                        <span>0 VND</span>
                                                    </div>
                                                    <div class="price-row price-total">
                                                        <span>Tổng tiền</span>
                                                        <span class="total-amount summary-total-price">...</span>
                                                    </div>
                                                    <input type="hidden" name="flightId" value="${flight.flightId}">
                                                    <input type="hidden" name="selectedClass" class="selectedClass" value="">
                                                    <input type="hidden" name="finalPrice" class="finalPrice" value="">
                                                    <button type="submit" class="btn-continue" ${bookingClosed || empty ticketClassesMap[flight.flightId] ? 'disabled' : ''}>Đi tiếp</button>
                                                </c:otherwise>
                                            </c:choose>
                                        </div>
                                    </form>
                                </div>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </c:if>
            <c:if test="${empty flights}">
                <div class="container">
                    <h4>Không tìm thấy chuyến bay</h4>
                    <p>Rất tiếc, không có chuyến bay nào phù hợp với yêu cầu của bạn.</p>
                    <a href="<c:url value='/home'/>">Quay về trang chủ</a>
                </div>
            </c:if>
        </main>
        <footer>
            <jsp:include page="/WEB-INF/user/components/footer.jsp" />
        </footer>
        <script>
            document.addEventListener('DOMContentLoaded', function () {
                console.log('Page loaded, initializing flight selection...');

                // Đơn giản hóa cho 1 chuyến bay duy nhất
                const fareOptions = document.querySelectorAll('.fare-option');
                const summaryClass = document.querySelector('.flight-class-summary');
                const summaryBasePrice = document.querySelector('.summary-base-price');
                const summaryTaxes = document.querySelector('.summary-taxes');
                const summaryTotalPrice = document.querySelector('.summary-total-price');
                const selectedClassInput = document.querySelector('.selectedClass');
                const finalPriceInput = document.querySelector('.finalPrice');
                const bookingForm = document.querySelector('form[data-flight-id]') || document.querySelector('form');
                const continueButton = document.querySelector('.btn-continue');

                console.log('Found elements:', {
                    fareOptions: fareOptions.length,
                    summaryClass: !!summaryClass,
                    summaryBasePrice: !!summaryBasePrice,
                    bookingForm: !!bookingForm
                });

                // Vô hiệu hóa nút "Đi tiếp" nếu không có hạng vé
                if (!fareOptions.length && continueButton) {
                    continueButton.disabled = true;
                    return;
                }

                // Xử lý form submit
                if (bookingForm) {
                    bookingForm.addEventListener('submit', function (e) {
                        if (!selectedClassInput.value || !finalPriceInput.value) {
                            alert('Vui lòng chọn hạng vé trước khi tiếp tục.');
                            e.preventDefault();
                        }
                    });
                }

                function formatCurrency(value) {
                    return new Intl.NumberFormat('vi-VN', {style: 'decimal'}).format(value) + ' VND';
                }

                function updateSummary(selectedElement) {
                    if (!selectedElement)
                        return;

                    const price = parseFloat(selectedElement.dataset.price);
                    const className = selectedElement.dataset.class;
                    const taxes = price * 0.08; // Thêm 8% thuế
                    const totalPrice = price + taxes;

                    console.log('Updating summary:', {price, className, taxes, totalPrice});

                    if (summaryClass)
                        summaryClass.textContent = className;
                    if (summaryBasePrice)
                        summaryBasePrice.textContent = formatCurrency(Math.round(price));
                    if (summaryTaxes)
                        summaryTaxes.textContent = formatCurrency(Math.round(taxes));
                    if (summaryTotalPrice)
                        summaryTotalPrice.textContent = formatCurrency(Math.round(totalPrice));
                    if (selectedClassInput)
                        selectedClassInput.value = className;
                    if (finalPriceInput)
                        finalPriceInput.value = totalPrice;
                }

                // Thêm event listeners cho fare options
                fareOptions.forEach(option => {
                    option.addEventListener('click', function () {
                        console.log('Fare option clicked:', this.dataset.class);
                        fareOptions.forEach(opt => opt.classList.remove('active'));
                        this.classList.add('active');
                        updateSummary(this);
                    });
                });

                // Khởi tạo với option đầu tiên hoặc option active
                const initialActiveOption = document.querySelector('.fare-option.active') || fareOptions[0];
                if (initialActiveOption) {
                    console.log('Setting initial active option:', initialActiveOption.dataset.class);
                    initialActiveOption.classList.add('active');
                    updateSummary(initialActiveOption);
                }
            });
        </script>
    </body>
</html>