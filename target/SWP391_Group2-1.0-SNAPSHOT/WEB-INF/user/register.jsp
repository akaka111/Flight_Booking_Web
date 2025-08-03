
<%-- 
    Document   : register
    Created on : 23 Jun 2025, 22:13:28
    Author     : Khoa
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Đăng ký - Flight Booking Web</title>

        <!-- Google Fonts -->
        <link rel="preconnect" href="https://fonts.googleapis.com">
        <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
        <link href="https://fonts.googleapis.com/css2?family=Montserrat:wght@400;500;700&display=style">
        <!-- Font Awesome Icons -->
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css"/>

        <style>
            :root {
                --primary-color: #007bff;
                --secondary-color: #ff6f61;
                --text-color: #333;
                --light-gray: #f8f9fa;
                --white: #ffffff;
                --shadow: 0 4px 12px rgba(0,0,0,0.1);
                --error-color: #dc3545; /* Màu đỏ cho lỗi và hint */
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
            .hero-section {
                position: relative;
                flex: 1;
                display: flex;
                align-items: center;
                justify-content: center;
                text-align: center;
                padding: 40px 20px;
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
            .register-container {
                position: relative;
                z-index: 3;
                max-width: 700px;
                width: 100%;
                background: rgba(255,255,255,0.9);
                padding: 40px;
                border-radius: 12px;
                box-shadow: var(--shadow);
                text-align: center;
                backdrop-filter: blur(5px);
            }
            h2 {
                font-size: 1.8em;
                font-weight: 600;
                margin-bottom: 10px;
                color: var(--text-color);
            }
            .form-grid {
                display: grid;
                grid-template-columns: 1fr 1fr;
                gap: 20px;
            }
            .form-group {
                margin: 15px 0;
                text-align: left;
            }
            .form-group label {
                font-size: 0.95em;
                font-weight: 500;
                color: var(--text-color);
                margin-bottom: 8px;
                display: block;
            }
            .form-group label i {
                margin-right: 6px;
                color: var(--primary-color);
            }
            input[type="text"], input[type="password"], input[type="date"], input[type="tel"] {
                width: 100%;
                padding: 12px;
                border: 1px solid #ddd;
                border-radius: 6px;
                font-size: 0.95em;
                font-family: 'Montserrat', sans-serif;
                transition: border-color 0.3s ease;
            }
            input:focus {
                outline: none;
                border-color: var(--primary-color);
            }
            .btn {
                width: 100%;
                padding: 14px;
                background-color: var(--primary-color);
                color: var(--white);
                border: none;
                border-radius: 6px;
                font-size: 1em;
                font-weight: 500;
                cursor: pointer;
                transition: background-color 0.3s ease;
                grid-column: 1 / -1;
            }
            .btn:hover {
                background-color: #0056b3;
            }
            .error {
                color: var(--error-color);
                font-size: 0.9em;
                margin: 10px 0;
                text-align: center;
                grid-column: 1 / -1;
                display: block;
            }
            .message {
                color: #28a745;
                font-size: 0.9em;
                margin: 10px 0;
                text-align: center;
                grid-column: 1 / -1;
            }
            .validation-hint {
                color: var(--error-color);
                font-size: 0.9em;
                margin: 10px 0;
                text-align: center;
                grid-column: 1 / -1;
                display: none;
            }
            .field-error {
                color: var(--error-color);
                font-size: 0.85em;
                margin-top: 5px;
                display: none;
            }
            .link {
                margin-top: 20px;
                font-size: 0.9em;
                color: var(--text-color);
                grid-column: 1 / -1;
            }
            .link a {
                color: var(--primary-color);
                text-decoration: none;
                font-weight: 500;
            }
            .link a:hover {
                text-decoration: underline;
            }
            @media (max-width: 600px) {
                .form-grid {
                    grid-template-columns: 1fr;
                }
            }
        </style>
    </head>
    <body>
        <jsp:include page="/WEB-INF/user/components/header.jsp" />
        <section class="hero-section">
            <div class="slider">
                <div class="slide active" style="background-image: url('https://images.unsplash.com/photo-1530521954074-e64f6810b32d?q=80&w=2070');"></div>
                <div class="slide" style="background-image: url('https://images.unsplash.com/photo-1436491865332-7a61a109cc05?q=80&w=2074');"></div>
                <div class="slide" style="background-image: url('https://images.unsplash.com/photo-1569154941061-e231b4725ef1?q=80&w=2070');"></div>
            </div>
            <div class="hero-overlay"></div>
            <div class="register-container">
                <h2><i class="fa-solid fa-user-plus"></i> Đăng ký tài khoản</h2>
                <div class="validation-hint" id="validationHint"></div>
                <c:if test="${not empty error}">
                    <div class="error">${error}</div>
                </c:if>
                <c:if test="${not empty message}">
                    <div class="message">${message}</div>
                </c:if>

                <form method="post" action="<c:url value='/register'/>" id="registerForm">
                    <div class="form-grid">
                        <!-- Cột trái -->
                        <div class="form-group">
                            <label for="username"><i class="fa-solid fa-user"></i> Tên tài khoản</label>
                            <input type="text" id="username" name="username" placeholder="Tên tài khoản" required value="${param.username}">
                            <div class="field-error" id="usernameError">Tên tài khoản không hợp lệ</div>
                        </div>
                        <div class="form-group">
                            <label for="fullName"><i class="fa-solid fa-id-card"></i> Họ và tên</label>
                            <input type="text" id="fullName" name="fullName" placeholder="Họ và tên" required value="${param.fullName}">
                            <div class="field-error" id="fullNameError">Họ và tên không hợp lệ</div>
                        </div>
                        <div class="form-group">
                            <label for="password"><i class="fa-solid fa-lock"></i> Mật khẩu</label>
                            <input type="password" id="password" name="password" placeholder="Mật khẩu" required>
                            <div class="field-error" id="passwordError">Mật khẩu không hợp lệ</div>
                        </div>
                        <div class="form-group">
                            <label for="confirmPassword"><i class="fa-solid fa-lock"></i> Xác nhận mật khẩu</label>
                            <input type="password" id="confirmPassword" name="confirmPassword" placeholder="Xác nhận mật khẩu" required>
                            <div class="field-error" id="confirmPasswordError">Mật khẩu xác nhận không khớp</div>
                        </div>

                        <!-- Cột phải -->
                        <div class="form-group">
                            <label for="email"><i class="fa-solid fa-envelope"></i> Email</label>
                            <input type="text" id="email" name="email" placeholder="example@gmail.com" required value="${param.email}">
                            <div class="field-error" id="emailError">Email không hợp lệ</div>
                        </div>
                        <div class="form-group">
                            <label for="dob"><i class="fa-solid fa-calendar-days"></i> Ngày sinh</label>
                            <input type="date" id="dob" name="dob" required value="${param.dob}">
                            <div class="field-error" id="dobError">Ngày sinh không hợp lệ</div>
                        </div>
                        <div class="form-group">
                            <label for="numberPhone"><i class="fa-solid fa-phone"></i> Số điện thoại</label>
                            <input type="tel" id="numberPhone" name="numberPhone" placeholder="Số điện thoại" required value="${param.numberPhone}">
                            <div class="field-error" id="numberPhoneError">Số điện thoại không hợp lệ</div>
                        </div>
                        <!-- Nút và liên kết -->
                        <button type="submit" class="btn">Đăng ký</button>
                        <div class="link">
                            Đã có tài khoản? <a href="<c:url value='/login'/>">Đăng nhập ngay</a>
                        </div>
                    </div>
                </form>
            </div>
        </section>

        <script>
            document.addEventListener('DOMContentLoaded', function () {
                // Slider
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

                // Validation functions
                function validateUsername(username) {
                    return /^[a-zA-Z0-9_-]{3,50}$/.test(username);
                }
                function validatePassword(password) {
                    return /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@#$!%*?&])[A-Za-z\d@#$!%*?&]{8,}$/.test(password);
                }
                function validateConfirmPassword(password, confirmPassword) {
                    return password === confirmPassword;
                }
                function validateFullName(fullName) {
                    return /^[a-zA-Z\s-]{2,100}$/.test(fullName);
                }
                function validateEmail(email) {
                    return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email);
                }
                function validateDob(dob) {
                    if (!dob)
                        return false;
                    const dobDate = new Date(dob);
                    const today = new Date();
                    const minAgeDate = new Date(today.getFullYear() - 13, today.getMonth(), today.getDate());
                    return dobDate <= minAgeDate && dobDate <= today;
                }
                function validatePhone(numberPhone) {
                    return /^\d{10}$/.test(numberPhone);
                }

                // Hint messages
                const hintMessages = {
                    username: 'Tên tài khoản phải từ 3-50 ký tự, chỉ chứa chữ cái, số, _, -',
                    password: 'Mật khẩu phải từ 8 ký tự, chứa chữ hoa, chữ thường, số, ký tự đặc biệt',
                    confirmPassword: 'Mật khẩu xác nhận phải khớp với mật khẩu',
                    fullName: 'Họ và tên phải từ 2-100 ký tự, chỉ chứa chữ cái, khoảng trắng, -',
                    email: 'Nhập email hợp lệ (VD: user@example.com)',
                    dob: 'Bạn phải từ 13 tuổi trở lên',
                    numberPhone: 'Số điện thoại phải có 10 chữ số'
                };

                // Show hint on focus and validate on input
                const validationHint = document.getElementById('validationHint');
                const inputs = document.querySelectorAll('#registerForm input');
                inputs.forEach(input => {
                    input.addEventListener('focus', function () {
                        validationHint.textContent = hintMessages[this.id];
                        validationHint.style.display = 'block';
                        const error = document.getElementById(this.id + 'Error');
                        error.style.display = 'none';
                    });

                    input.addEventListener('blur', function () {
                        validationHint.style.display = 'none';
                    });

                    input.addEventListener('input', function () {
                        let isValid = false;
                        switch (this.id) {
                            case 'username':
                                isValid = validateUsername(this.value);
                                break;
                            case 'password':
                                isValid = validatePassword(this.value);
                                break;
                            case 'confirmPassword':
                                const password = document.getElementById('password').value;
                                isValid = validateConfirmPassword(password, this.value);
                                break;
                            case 'fullName':
                                isValid = validateFullName(this.value);
                                break;
                            case 'email':
                                isValid = validateEmail(this.value);
                                break;
                            case 'dob':
                                isValid = validateDob(this.value);
                                break;
                            case 'numberPhone':
                                isValid = validatePhone(this.value);
                                break;
                        }
                        validationHint.style.display = isValid ? 'none' : 'block';
                    });
                });

                // Form submission validation
                const form = document.getElementById('registerForm');
                form.addEventListener('submit', function (e) {
                    let isValid = true;

                    const username = document.getElementById('username').value;
                    const usernameError = document.getElementById('usernameError');
                    if (!validateUsername(username)) {
                        usernameError.style.display = 'block';
                        isValid = false;
                    } else {
                        usernameError.style.display = 'none';
                    }

                    const password = document.getElementById('password').value;
                    const passwordError = document.getElementById('passwordError');
                    if (!validatePassword(password)) {
                        passwordError.style.display = 'block';
                        isValid = false;
                    } else {
                        passwordError.style.display = 'none';
                    }

                    const confirmPassword = document.getElementById('confirmPassword').value;
                    const confirmPasswordError = document.getElementById('confirmPasswordError');
                    if (!validateConfirmPassword(password, confirmPassword)) {
                        confirmPasswordError.style.display = 'block';
                        isValid = false;
                    } else {
                        confirmPasswordError.style.display = 'none';
                    }

                    const fullName = document.getElementById('fullName').value;
                    const fullNameError = document.getElementById('fullNameError');
                    if (!validateFullName(fullName)) {
                        fullNameError.style.display = 'block';
                        isValid = false;
                    } else {
                        fullNameError.style.display = 'none';
                    }

                    const email = document.getElementById('email').value;
                    const emailError = document.getElementById('emailError');
                    if (!validateEmail(email)) {
                        emailError.style.display = 'block';
                        isValid = false;
                    } else {
                        emailError.style.display = 'none';
                    }

                    const dob = document.getElementById('dob').value;
                    const dobError = document.getElementById('dobError');
                    if (!validateDob(dob)) {
                        dobError.style.display = 'block';
                        isValid = false;
                    } else {
                        dobError.style.display = 'none';
                    }

                    const numberPhone = document.getElementById('numberPhone').value;
                    const numberPhoneError = document.getElementById('numberPhoneError');
                    if (!validatePhone(numberPhone)) {
                        numberPhoneError.style.display = 'block';
                        isValid = false;
                    } else {
                        numberPhoneError.style.display = 'none';
                    }

                    if (!isValid) {
                        e.preventDefault();
                    }
                });
            });
        </script>
    </body>
</html>