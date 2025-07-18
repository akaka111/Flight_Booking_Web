<%-- 
    Document   : passenger
    Created on : 15 Jul 2025, 09:07:29
    Author     : Admin
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Thông tin hành khách - Flight Booking Web</title>
        <link rel="preconnect" href="https://fonts.googleapis.com">
        <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
        <link href="https://fonts.googleapis.com/css2?family=Montserrat:wght@400;500;700&display=swap" rel="stylesheet">
        <style>
            :root {
                --primary-color: #00529b;
                --secondary-color: #e53935;
                --text-color: #333;
                --border-color: #dee2e6;
                --background-light: #f8f9fa;
                --white: #fff;
                --success-color: #4CAF50;
                --error-color: #d32f2f; /* Màu cho thông báo lỗi */
            }
            body {
                font-family: 'Montserrat', sans-serif;
                background-color: var(--background-light);
                margin: 0;
                color: var(--text-color);
            }
            .container {
                max-width: 800px;
                margin: 30px auto;
                padding: 20px 40px;
                background-color: var(--white);
                border: 1px solid var(--border-color);
                border-radius: 8px;
                box-shadow: 0 4px 8px rgba(0, 0, 0, 0.05);
            }
            h1 {
                color: var(--primary-color);
                text-align: center;
                margin-bottom: 30px;
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
            }
            .form-control:focus {
                outline: none;
                border-color: var(--primary-color);
                box-shadow: 0 0 5px rgba(0, 82, 155, 0.2);
            }
            .form-row {
                display: flex;
                gap: 20px;
            }
            .form-row .form-group {
                flex: 1;
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
                color: var(--white);
                background-color: var(--success-color);
                transition: background-color 0.3s ease;
            }
            .btn:hover {
                background-color: #45a049;
            }

            /* === CSS MỚI CHO VALIDATION === */
            .error-message {
                color: var(--error-color);
                font-size: 0.85em;
                margin-top: 5px;
                display: none; /* Ẩn mặc định */
            }
            .form-control.error {
                border-color: var(--error-color); /* Thêm viền đỏ khi có lỗi */
            }
        </style>
    </head>
    <body>
        <header>      
            <jsp:include page="/WEB-INF/user/components/header.jsp" /> 
        </header>

        <div class="container">
            <h1>Thông tin hành khách</h1>
            <p>Vui lòng điền chính xác thông tin bên dưới. Thông tin này sẽ được sử dụng để xuất vé.</p>

            <%-- Chú ý: form bây giờ có id="passengerForm" --%>
            <form id="passengerForm" action="passenger" method="post">
                <input type="hidden" name="step" value="addPassenger">

                <div class="form-group">
                    <label for="fullName">Họ và tên (như trên giấy tờ tùy thân)</label>
                    <input type="text" id="fullName" name="fullName" class="form-control" required>
                    <div class="error-message" id="fullNameError">Họ và tên không được để trống.</div>
                </div>

                <div class="form-row">
                    <div class="form-group">
                        <label for="passportNumber">Số hộ chiếu / CMND/CCCD</label>
                        <input type="text" id="passportNumber" name="passportNumber" class="form-control" required>
                        <div class="error-message" id="passportNumberError">Số hộ chiếu không được để trống.</div>
                    </div>
                    <div class="form-group">
                        <label for="dob">Ngày sinh</label>
                        <input type="text" id="dob" name="dob" class="form-control" placeholder="dd/MM/yyyy" required>
                        <div class="error-message" id="dobError">Ngày sinh phải có định dạng dd/MM/yyyy.</div>
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
                    </div>
                    <div class="form-group">
                        <label for="country">Quốc tịch</label>
                        <select id="country" name="country" class="form-control" required>
                            <option value="Việt Nam">Việt Nam</option>
                            <option value="Hoa Kỳ">Hoa Kỳ</option>
                            <option value="Hàn Quốc">Hàn Quốc</option>
                            <option value="Nhật Bản">Nhật Bản</option>
                        </select>
                    </div>
                </div>

                <hr style="border: none; border-top: 1px solid var(--border-color); margin: 30px 0;">
                <h3>Thông tin liên hệ</h3>
                <div class="form-row">
                    <div class="form-group">
                        <label for="phoneNumber">Số điện thoại</label>
                        <input type="tel" id="phoneNumber" name="phoneNumber" class="form-control" required>
                        <div class="error-message" id="phoneNumberError">Số điện thoại không hợp lệ.</div>
                    </div>
                    <div class="form-group">
                        <label for="email">Email</label>
                        <input type="email" id="email" name="email" class="form-control" required>
                        <div class="error-message" id="emailError">Email không hợp lệ.</div>
                    </div>
                </div>

                <div class="form-group">
                    <label for="address">Địa chỉ</label>
                    <input type="text" id="address" name="address" class="form-control" required>
                    <div class="error-message" id="addressError">Địa chỉ không được để trống.</div>
                </div>

                <button type="submit" class="btn">Tiếp tục đến thanh toán</button>
            </form>
        </div>

        <%-- SCRIPT MỚI CHO VALIDATION --%>
        <script>
            document.getElementById('passengerForm').addEventListener('submit', function(event) {
            // Ngăn form gửi đi ngay lập tức để kiểm tra
            event.preventDefault();
                    let isValid = true;
                    // BƯỚC 1: Xóa tất cả các lỗi cũ.
                    // Đây là cách đúng để "ẩn lỗi", không cần hàm hideError()
                    document.querySelectorAll('.error-message').forEach(el => el.style.display = 'none');
                    document.querySelectorAll('.form-control').forEach(el => el.classList.remove('error'));
                    // Hàm tiện ích để hiển thị lỗi
                            function showError(inputId, errorId, message) {
                            const input = document.getElementById(inputId);
                                    const error = document.getElementById(errorId);
                                    input.classList.add('error');
                                    error.textContent = message;
                                    error.style.display = 'block';
                                    isValid = false;
                            }

                    // BƯỚC 2: Bắt đầu kiểm tra từng trường

                    // Kiểm tra Họ và tên
                    const fullName = document.getElementById('fullName').value.trim();
                            if (fullName === '') {
                    showError('fullName', 'fullNameError', 'Họ và tên không được để trống.');
                    }

                    // Kiểm tra Số hộ chiếu
                    const passportNumber = document.getElementById('passportNumber').value.trim();
                            if (passportNumber === '') {
                    showError('passportNumber', 'passportNumberError', 'Số hộ chiếu không được để trống.');
                    }

                    // Kiểm tra Ngày sinh (định dạng dd-MM-yyyy)
                    const dob = document.getElementById('dob').value.trim();
                            const dateRegex = /^(0[1-9]|[12][0-9]|3[01])-(0[1-9]|1[012])-\d{4}$/;
                            if (!dateRegex.test(dob)) {
                    showError('dob', 'dobError', 'Ngày sinh phải có định dạng dd-MM-yyyy.');
                    }

                    // Kiểm tra Email
                    const email = document.getElementById('email').value.trim();
                            const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
                            if (!emailRegex.test(email)) {
                    showError('email', 'emailError', 'Địa chỉ email không hợp lệ.');
                    }

                    // Kiểm tra Số điện thoại
                    const phoneNumber = document.getElementById('phoneNumber').value.trim();
                            const phoneRegex = /^\d{10,11}$/; // Ví dụ: 10-11 chữ số
                            if (!phoneRegex.test(phoneNumber)) {
                    showError('phoneNumber', 'phoneNumberError', 'Số điện thoại phải có 10-11 chữ số.');
                    }

                    // Kiểm tra Địa chỉ
                    const address = document.getElementById('address').value.trim();
                            if (address === '') {
                    showError('address', 'addressError', 'Địa chỉ không được để trống.');
                    }

                    // BƯỚC 3: Nếu tất cả đều hợp lệ, gửi form đi
                    if (isValid) {
                    this.submit();
                    }
                    });
                    </body>
            </html>