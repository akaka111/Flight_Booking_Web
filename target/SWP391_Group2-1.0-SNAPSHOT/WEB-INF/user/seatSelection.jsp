<%-- 
    Document   : seatSelection
    Created on : 31 Jul 2025, 22:43:38
    Author     : Admin (Refactored)
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Chọn ghế - Chuyến bay #${flightId}</title>
        <link rel="preconnect" href="https://fonts.googleapis.com">
        <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
        <link href="https://fonts.googleapis.com/css2?family=Montserrat:wght@400;500;700&display=swap" rel="stylesheet">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css"/>
        <style>
            /* === CSS VARIABLES VÀ STYLES CƠ BẢN (Lấy từ passenger.jsp) === */
            :root {
                --primary-color: #007bff;
                --secondary-color: #ff6f61;
                --text-color: #343a40;
                --border-color: #dee2e6;
                --light-gray: #f8f9fa;
                --white: #ffffff;
                --shadow: 0 5px 15px rgba(0,0,0,0.1);
                --error-color: #d32f2f;
            }
            body {
                font-family: 'Montserrat', sans-serif;
                background-color: var(--light-gray);
                margin: 0;
                color: var(--text-color);
            }
            main {
                min-height: calc(100vh - 150px);
            }
            .container {
                max-width: 1200px;
                margin: 30px auto;
                padding: 0 20px;
            }

            /* === BỐ CỤC 2 CỘT (Lấy từ passenger.jsp) === */
            .page-wrapper {
                display: flex;
                gap: 30px;
                flex-wrap: wrap;
                align-items: flex-start;
            }
            .main-content {
                flex: 3;
                min-width: 300px;
            }
            .sidebar {
                flex: 1;
                min-width: 280px;
                position: sticky; /* Giữ sidebar cố định khi cuộn */
                top: 20px;
            }

            /* === KIỂU "CARD" (Lấy từ passenger.jsp) === */
            .content-card, .summary-card {
                background-color: var(--white);
                border: 1px solid var(--border-color);
                border-radius: 8px;
                box-shadow: var(--shadow);
            }
            .content-card {
                padding: 30px 40px;
            }

            h1 {
                color: var(--primary-color);
                margin-top: 0;
                margin-bottom: 20px;
                font-size: 1.8em;
            }

            /* === CSS CHO SƠ ĐỒ GHẾ (Tùy chỉnh từ code gốc) === */
            .seat-map {
                display: grid;
                grid-template-columns: repeat(6, 1fr); /* 6 ghế mỗi hàng */
                gap: 12px;
                margin: 30px 0;
            }
            .seat {
                padding: 10px 5px;
                border: 1px solid var(--border-color);
                text-align: center;
                border-radius: 5px;
                cursor: pointer;
                font-size: 0.9em;
                font-weight: 500;
                transition: all 0.2s ease-in-out;
            }
            .seat input {
                display: none;
            }
            .seat.available {
                background-color: var(--white);
                border: 1px solid #a5d6a7;
            }
            .seat.available:hover {
                background-color: #e8f5e9;
            }
            .seat.booked {
                background-color: #ef9a9a;
                border-color: #e57373;
                cursor: not-allowed;
                text-decoration: line-through;
                opacity: 0.7;
            }
            .seat.selected {
                background-color: var(--primary-color);
                border-color: var(--primary-color);
                color: var(--white);
                transform: scale(1.05);
            }

            /* === CHÚ THÍCH (Legend) === */
            .legend {
                margin-top: 30px;
                padding-top: 20px;
                border-top: 1px solid var(--border-color);
                display: flex;
                gap: 20px;
                flex-wrap: wrap;
            }
            .legend-item {
                display: flex;
                align-items: center;
                font-size: 0.9em;
            }
            .legend-item span {
                width: 18px;
                height: 18px;
                display: inline-block;
                margin-right: 8px;
                border-radius: 3px;
                border: 1px solid #ccc;
            }

            /* === CSS CHO SIDEBAR (Lấy từ passenger.jsp) === */
            .summary-card-header {
                background-color: var(--primary-color);
                color: var(--white);
                padding: 15px 20px;
                border-top-left-radius: 8px;
                border-top-right-radius: 8px;
            }
            .summary-card-header h3 {
                margin: 0;
            }
            .summary-card-body {
                padding: 20px;
            }
            .summary-row {
                display: flex;
                justify-content: space-between;
                margin-bottom: 15px;
            }
            .summary-total {
                border-top: 1px solid var(--border-color);
                margin-top: 15px;
                padding-top: 15px;
                font-weight: 700;
                font-size: 1.2em;
            }
            .summary-total .total-amount {
                color: var(--secondary-color);
            }
            .btn-continue {
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
                color: var(--white);
                background-color: var(--secondary-color);
                transition: all 0.3s ease;
            }
            .btn-continue:hover {
                background-color: #e65c50;
            }
            .btn-continue:disabled {
                background-color: #cccccc;
                cursor: not-allowed;
            }
            .seat input[type="radio"] {
                display: none;
            }

        </style>
    </head>
    <body>
        <header>      
            <jsp:include page="/WEB-INF/user/components/header.jsp" /> 
        </header>

        <main>
            <div class="container">
                <c:set var="pricePerSeat" value="0" />

                <form action="manageSeatController" method="post">
                    <!-- Input ẩn để gửi lại flightId khi submit form -->
                    <input type="hidden" name="flightId" value="${flightId}" />

                    <div class="page-wrapper">
                        <div class="main-content">
                            <div class="content-card">
                                <h1>Chọn ghế của bạn</h1>
                                <p>Nhấn vào ghế trống để chọn. Nhấn lần nữa để bỏ chọn.</p>

                                <c:if test="${param.error == 'NoSeatSelected'}">
                                    <p style="color: red; font-weight: 500;">Vui lòng chọn ít nhất một ghế để tiếp tục.</p>
                                </c:if>

                                <div class="seat-map">
                                    <c:forEach var="seat" items="${seatList}">
                                        <c:choose>
                                            <c:when test="${seat.booked}">
                                                <div class="seat booked">${seat.seatNumber}</div>
                                            </c:when>
                                            <c:otherwise>
                                                <label class="seat available" id="label_for_seat_${seat.seatId}">
                                                    <input type="radio" class="seat-radio" name="selectedSeats" value="${seat.seatId}"
                                                           data-seat-number="${seat.seatNumber}" onchange="updateSummary()">

                                                    ${seat.seatNumber}
                                                </label>
                                            </c:otherwise>
                                        </c:choose>
                                    </c:forEach>
                                </div>


                                <div class="legend">
                                    <div class="legend-item"><span style="background-color: #e8f5e9; border-color: #a5d6a7;"></span> Còn trống</div>
                                    <div class="legend-item"><span style="background-color: var(--primary-color); border-color: var(--primary-color);"></span> Đang chọn</div>
                                    <div class="legend-item"><span style="background-color: #ef9a9a; border-color: #e57373;"></span> Đã đặt</div>
                                </div>
                            </div>
                        </div>

                        <div class="sidebar">
                            <div class="summary-card">
                                <div class="summary-card-header"><h3>TÓM TẮT ĐẶT VÉ</h3></div>
                                <div class="summary-card-body">
                                    <div class="summary-row">
                                        <span>Số ghế đã chọn</span>
                                        <span id="selected-count" style="font-weight: 700;">0</span>
                                    </div>
                                    <div class="summary-row">
                                        <span>Ghế</span>
                                        <span id="selected-seats-list" style="font-weight: 700; text-align: right;">Chưa chọn</span>
                                    </div>
                                    <div class="summary-row summary-total">
                                        <span>Tổng tiền</span>
                                        <span class="total-amount" id="total-amount">0 VND</span>
                                    </div>

                                    <button type="submit" class="btn-continue" id="submit-button" disabled>Chọn ghế và tiếp tục</button>
                                    <button type="button" onclick="history.back()" class="btn-continue" style="background-color: var(--primary-color);">Quay lại</button>
                                </div>
                            </div>
                        </div>
                    </div>
                </form>
            </div>
        </main>

        <footer>
            <jsp:include page="/WEB-INF/user/components/footer.jsp" /> 
        </footer> 

        <script>
            document.addEventListener('DOMContentLoaded', function () {
                const PRICE_PER_SEAT = ${pricePerSeat}; // Lấy giá từ JSP

                // Định dạng số tiền
                const currencyFormatter = new Intl.NumberFormat('vi-VN', {
                    style: 'currency',
                    currency: 'VND'
                });

                window.updateSummary = function () {
                    const selectedRadio = document.querySelector('.seat-radio:checked');
                    const selectedCountEl = document.getElementById('selected-count');
                    const selectedListEl = document.getElementById('selected-seats-list');
                    const totalAmountEl = document.getElementById('total-amount');
                    const submitButton = document.getElementById('submit-button');

                    // Reset tất cả label
                    document.querySelectorAll('.seat.available').forEach(label => {
                        label.classList.remove('selected');
                    });

                    if (selectedRadio) {
                        const selectedLabel = selectedRadio.closest('label');
                        selectedLabel.classList.add('selected');

                        selectedCountEl.textContent = '1';
                        selectedListEl.textContent = selectedRadio.dataset.seatNumber;
                        totalAmountEl.textContent = currencyFormatter.format(PRICE_PER_SEAT);
                        submitButton.disabled = false;
                    } else {
                        selectedCountEl.textContent = '0';
                        selectedListEl.textContent = 'Chưa chọn';
                        totalAmountEl.textContent = currencyFormatter.format(0);
                        submitButton.disabled = true;
                    }
                };


                // Gọi hàm một lần khi tải trang để đảm bảo trạng thái đúng
                updateSummary();
            });
        </script>
    </body>
</html>