<%-- 
    Document   : passenger
    Created on : 15 Jul 2025, 09:07:29
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
        <title>Thông tin hành khách - Flight Booking Web</title>
        <link rel="preconnect" href="https://fonts.googleapis.com">
        <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
        <link href="https://fonts.googleapis.com/css2?family=Montserrat:wght@400;500;700&display=swap" rel="stylesheet">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css"/>
        <style>
            /* === ĐỒNG BỘ CSS VARIABLES VÀ STYLES CƠ BẢN VỚI FLIGHT-DETAIL.JSP === */
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
                min-height: calc(100vh - 250px);
            }
            .container {
                max-width: 1200px;
                margin: 30px auto;
                padding: 0 20px;
            }

            /* === BỐ CỤC 2 CỘT TƯƠNG TỰ FLIGHT-DETAIL === */
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

            /* === ÁP DỤNG KIỂU "CARD" CHO FORM VÀ SIDEBAR === */
            .form-card, .price-summary {
                background-color: var(--white);
                border: 1px solid var(--border-color);
                border-radius: 8px;
                box-shadow: var(--shadow);
            }
            .form-card {
                padding: 30px 40px;
            }

            h1 {
                color: var(--primary-color);
                margin-top: 0;
                margin-bottom: 30px;
                font-size: 1.8em;
            }

            .form-group {
                margin-bottom: 20px;
            }
            .form-group label {
                display: block;
                margin-bottom: 8px;
                font-weight: 500;
            }
            .form-control {
                width: 100%;
                padding: 12px;
                border: 1px solid var(--border-color);
                border-radius: 5px;
                font-size: 1rem;
                box-sizing: border-box;
                font-family: 'Montserrat', sans-serif;
            }
            .form-control:focus {
                outline: none;
                border-color: var(--primary-color);
                box-shadow: 0 0 5px rgba(0, 123, 255, 0.25);
            }
            .form-row {
                display: flex;
                gap: 20px;
                flex-wrap: wrap; /* Cho phép xuống hàng trên màn hình nhỏ */
            }
            .form-row .form-group {
                flex: 1;
                min-width: 200px; /* Đảm bảo các trường không quá hẹp */
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
                transition: background-color 0.3s ease;
            }
            .btn-continue:hover {
                background-color: #e65c50;
            }
            .error-message {
                color: var(--error-color);
                font-size: 0.85em;
                margin-top: 5px;
                display: none;
            }
            .form-control.error {
                border-color: var(--error-color);
            }

            /* === CSS CHO SIDEBAR TÓM TẮT GIÁ (GIỮ NGUYÊN TỪ FLIGHT-DETAIL) === */
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
        </style>
    </head>
    <body>
        <header>      
            <jsp:include page="/WEB-INF/user/components/header.jsp" /> 
        </header>

        <main>
            <%-- Tính toán giá trị thuế và giá gốc để hiển thị --%>
            <c:set var="price" value="${param.finalPrice}" />
            <c:set var="basePrice" value="${price / 1.08}" />
            <c:set var="taxes" value="${price - basePrice}" />

            <div class="container">
                <div class="page-wrapper">
                    <div class="main-content">
                        <div class="form-card">
                            <h1>Thông tin hành khách</h1>
                            <p style="margin-bottom: 30px;">Vui lòng điền chính xác thông tin bên dưới. Thông tin này sẽ được sử dụng để xuất vé.</p>

                            <form id="passengerForm" action="passenger" method="post" novalidate>
                                <input type="hidden" name="step" value="addPassenger">
                                <%-- Các trường ẩn để gửi thông tin chuyến bay đã chọn --%>
                                <input type="hidden" name="flightId" value="${param.flightId}">
                                <input type="hidden" name="selectedClass" value="${param.selectedClass}">
                                <input type="hidden" name="finalPrice" value="${param.finalPrice}">

                                <div class="form-group">
                                    <label for="fullName">Họ và tên (như trên giấy tờ tùy thân)</label>
                                    <input type="text" id="fullName" name="fullName" class="form-control" required>
                                    <div class="error-message" id="fullNameError"></div>
                                </div>

                                <div class="form-row">
                                    <div class="form-group">
                                        <label for="passportNumber">Số hộ chiếu / CMND/CCCD</label>
                                        <input type="text" id="passportNumber" name="passportNumber" class="form-control" required>
                                        <div class="error-message" id="passportNumberError"></div>
                                    </div>
                                    <div class="form-group">
                                        <label for="dob">Ngày sinh</label>
                                        <input type="date" id="dob" name="dob" class="form-control" required max="2025-07-30">
                                        <div class="error-message" id="dobError"></div>
                                    </div>
                                </div>

                                <div class="form-row">
                                    <div class="form-group">
                                        <label for="gender">Giới tính</label>
                                        <select id="gender" name="gender" class="form-control" required>
                                            <option value="" disabled selected>-- Chọn giới tính --</option>
                                            <option value="Nam">Nam</option>
                                            <option value="Nữ">Nữ</option>
                                            <option value="Khác">Khác</option>
                                        </select>
                                        <div class="error-message" id="genderError"></div>
                                    </div>
                                    <div class="form-group">
                                        <label for="country">Quốc tịch</label>
                                        <select id="country" name="country" class="form-control" required>
                                            <option value="Việt Nam">Việt Nam</option>
                                            <option value="Hoa Kỳ">Hoa Kỳ</option>
                                            <option value="Hàn Quốc">Hàn Quốc</option>
                                            <option value="Nhật Bản">Nhật Bản</option>
                                            <!-- Thêm các quốc gia khác nếu cần -->
                                        </select>
                                    </div>
                                </div>

                                <hr style="border: none; border-top: 1px solid var(--border-color); margin: 30px 0;">
                                <h3 style="color: var(--primary-color);">Thông tin liên hệ</h3>
                                <div class="form-row">
                                    <div class="form-group">
                                        <label for="phoneNumber">Số điện thoại</label>
                                        <input type="tel" id="phoneNumber" name="phoneNumber" class="form-control" required>
                                        <div class="error-message" id="phoneNumberError"></div>
                                    </div>
                                    <div class="form-group">
                                        <label for="email">Email</label>
                                        <input type="email" id="email" name="email" class="form-control" required>
                                        <div class="error-message" id="emailError"></div>
                                    </div>
                                </div>

                                <div class="form-group">
                                    <label for="address">Địa chỉ</label>
                                    <input type="text" id="address" name="address" class="form-control" required>
                                    <div class="error-message" id="addressError"></div>
                                </div>

                                <button type="submit" class="btn-continue">Tiếp tục đến thanh toán</button>
                            </form>
                        </div>
                    </div>

                    <div class="sidebar">
                        <div class="price-summary">
                            <div class="price-summary-header"><h3>THÔNG TIN ĐẶT CHỖ</h3></div>
                            <div class="price-summary-body">
                                <h4>Chuyến đi: <span style="color: var(--secondary-color); font-weight: 700;">${param.selectedClass}</span></h4>
                                    <c:if test="${not empty flight}">
                                    <p style="font-weight: 500; margin-bottom: 5px;">${flight.routeFrom} - ${flight.routeTo}</p>
                                    <p style="color: #6c757d; margin-top: 0;"><fmt:formatDate value="${flight.departureTime}" pattern="HH:mm, E, dd/MM/yyyy" /></p>
                                </c:if>
                                <hr>
                                <div class="price-row">
                                    <span>Giá vé</span>
                                    <span><fmt:formatNumber value="${basePrice}" type="number" maxFractionDigits="0"/> VND</span>
                                </div>
                                <div class="price-row">
                                    <span>Thuế, phí</span>
                                    <span><fmt:formatNumber value="${taxes}" type="number" maxFractionDigits="0"/> VND</span>
                                </div>
                                <div class="price-row">
                                    <span>Dịch vụ</span>
                                    <span>0 VND</span>
                                </div>
                                <div class="price-row price-total">
                                    <span>Tổng tiền</span>
                                    <span class="total-amount"><fmt:formatNumber value="${price}" type="number" maxFractionDigits="0"/> VND</span>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </main>

        <footer>
            <jsp:include page="/WEB-INF/user/components/footer.jsp" /> 
        </footer> 

        <script>
            document.addEventListener('DOMContentLoaded', function () {
                const form = document.getElementById('passengerForm');
                if (!form)
                    return;

                // Set max date for date of birth input to today
                const dobInput = document.getElementById('dob');
                if (dobInput) {
                    dobInput.max = new Date().toISOString().split("T")[0];
                }

                form.addEventListener('submit', function (event) {
                    if (!validateForm()) {
                        event.preventDefault();
                    }
                });

                function validateForm() {
                    let isValid = true;

                    document.querySelectorAll('.error-message').forEach(el => el.style.display = 'none');
                    document.querySelectorAll('.form-control').forEach(el => el.classList.remove('error'));

                    const showError = (inputId, message) => {
                        const input = document.getElementById(inputId);
                        const error = document.getElementById(inputId + 'Error');
                        input.classList.add('error');
                        if (error) {
                            error.textContent = message;
                            error.style.display = 'block';
                        }
                        isValid = false;
                    };

                    // Validation logic
                    if (document.getElementById('fullName').value.trim() === '')
                        showError('fullName', 'Họ và tên không được để trống.');
                    if (document.getElementById('passportNumber').value.trim() === '')
                        showError('passportNumber', 'Số hộ chiếu/CCCD không được để trống.');
                    if (document.getElementById('dob').value.trim() === '')
                        showError('dob', 'Vui lòng chọn ngày sinh.');
                    if (document.getElementById('gender').value.trim() === '')
                        showError('genderError', 'Vui lòng chọn giới tính.'); // Custom id for select error

                    const email = document.getElementById('email').value.trim();
                    if (email === '') {
                        showError('email', 'Email không được để trống.');
                    } else if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email)) {
                        showError('email', 'Địa chỉ email không hợp lệ.');
                    }

                    const phoneNumber = document.getElementById('phoneNumber').value.trim();
                    if (phoneNumber === '') {
                        showError('phoneNumber', 'Số điện thoại không được để trống.');
                    } else if (!/^\d{10,11}$/.test(phoneNumber)) {
                        showError('phoneNumber', 'Số điện thoại phải có 10-11 chữ số.');
                    }

                    if (document.getElementById('address').value.trim() === '')
                        showError('address', 'Địa chỉ không được để trống.');

                    return isValid;
                }
            });
        </script>
    </body>
</html>