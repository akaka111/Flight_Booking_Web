<%-- 
    Document   : Booking
    Created on : Jul 6, 2025, 9:24:30 AM
    Author     : Khoanda-CE180683
--%>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Booking Flight</title>
    <style>
        body { font-family: Arial, sans-serif; }
        .container { max-width: 800px; margin: 0 auto; padding: 20px; }
        .form-group { margin-bottom: 15px; }
        label { display: block; margin-bottom: 5px; }
        input, select { width: 100%; padding: 8px; }
        .services { display: flex; flex-wrap: wrap; gap: 10px; }
        .service-item { border: 1px solid #ccc; padding: 10px; width: 200px; }
        .error { color: red; font-size: 0.9em; display: none; }
        .message { padding: 10px; margin-bottom: 15px; border-radius: 5px; display: none; }
        .success { background-color: #d4edda; color: #155724; border: 1px solid #c3e6cb; display: none; }
        .error-message { background-color: #f8d7da; color: #721c24; border: 1px solid #f5c6cb; display: none; }
    </style>
    <script>
        function validateForm() {
            let isValid = true;
            const fullName = document.forms["bookingForm"]["fullName"].value;
            const dob = document.forms["bookingForm"]["dob"].value;
            const passportNumber = document.forms["bookingForm"]["passportNumber"].value;
            const phone = document.forms["bookingForm"]["phone"].value;
            const email = document.forms["bookingForm"]["email"].value;
            const userId = document.forms["bookingForm"]["userId"].value;
            const flightId = document.forms["bookingForm"]["flightId"].value;
            const totalAmount = document.forms["bookingForm"]["totalAmount"].value;

            // Reset errors
            document.querySelectorAll('.error').forEach(error => error.style.display = 'none');

            // Validate Full Name
            if (!/^[a-zA-Z\s]+$/.test(fullName)) {
                document.getElementById('fullNameError').style.display = 'block';
                isValid = false;
            }

            // Validate DOB
            const today = new Date();
            const dobDate = new Date(dob);
            if (dobDate > today || dobDate.getFullYear() < 1900) {
                document.getElementById('dobError').style.display = 'block';
                isValid = false;
            }

            // Validate Passport Number
            if (!/^\d{9,12}$/.test(passportNumber)) {
                document.getElementById('passportError').style.display = 'block';
                isValid = false;
            }

            // Validate Phone
            if (!/^(\+84|0)\d{9,10}$/.test(phone)) {
                document.getElementById('phoneError').style.display = 'block';
                isValid = false;
            }

            // Validate Email
            if (!/^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/.test(email)) {
                document.getElementById('emailError').style.display = 'block';
                isValid = false;
            }

            // Validate User ID, Flight ID, Total Amount
            if (isNaN(userId) || userId <= 0) {
                document.getElementById('userIdError').style.display = 'block';
                isValid = false;
            }
            if (isNaN(flightId) || flightId <= 0) {
                document.getElementById('flightIdError').style.display = 'block';
                isValid = false;
            }
            if (isNaN(totalAmount) || totalAmount <= 0) {
                document.getElementById('totalAmountError').style.display = 'block';
                isValid = false;
            }

            return isValid;
        }

        // Hiển thị thông báo khi trang load
        window.onload = function() {
            const successMsg = "${successMessage}";
            const errorMsg = "${errorMessage}";
            if (successMsg) {
                document.getElementById('successMessage').textContent = successMsg;
                document.getElementById('successMessage').style.display = 'block';
            }
            if (errorMsg) {
                document.getElementById('errorMessage').textContent = errorMsg;
                document.getElementById('errorMessage').style.display = 'block';
            }
        };
    </script>
</head>
<body>
    <div class="container">
        <h2>Booking Flight</h2>

        <!-- Thông báo thành công -->
        <div id="successMessage" class="message success"></div>

        <!-- Thông báo lỗi -->
        <div id="errorMessage" class="message error-message"></div>

        <form name="bookingForm" action="booking" method="post" onsubmit="return validateForm()">
            <div class="form-group">
                <label>User ID:</label>
                <input type="number" name="userId" required min="1">
                <span id="userIdError" class="error">User ID must be a positive number.</span>
            </div>
            <div class="form-group">
                <label>Flight ID:</label>
                <input type="number" name="flightId" required min="1">
                <span id="flightIdError" class="error">Flight ID must be a positive number.</span>
            </div>
            <div class="form-group">
                <label>Total Amount:</label>
                <input type="number" name="totalAmount" step="0.01" value="10433200.00" required min="0.01">
                <span id="totalAmountError" class="error">Total Amount must be a positive number.</span>
            </div>
            <div class="form-group">
                <label>Full Name:</label>
                <input type="text" name="fullName" value="DO ANH KHOA" required>
                <span id="fullNameError" class="error">Full Name must contain only letters and spaces.</span>
            </div>
            <div class="form-group">
                <label>Gender:</label>
                <select name="gender" required>
                    <option value="Nam">Nam</option>
                    <option value="Nữ">Nữ</option>
                    <option value="Khác">Khác</option>
                </select>
            </div>
            <div class="form-group">
                <label>Date of Birth (YYYY-MM-DD):</label>
                <input type="date" name="dob" value="2004-08-27" required max="<%= java.time.LocalDate.now() %>">
                <span id="dobError" class="error">Date of Birth must be before today and after 1900.</span>
            </div>
            <div class="form-group">
                <label>Passport/CCCD:</label>
                <input type="text" name="passportNumber" value="09620404376" required>
                <span id="passportError" class="error">Passport/CCCD must be 9-12 digits.</span>
            </div>
            <div class="form-group">
                <label>Nationality:</label>
                <input type="text" name="nationality" value="Việt Nam" required>
            </div>
            <div class="form-group">
                <label>Phone:</label>
                <input type="tel" name="phone" value="+84038212354" required>
                <span id="phoneError" class="error">Phone must start with +84 or 0 and be 9-10 digits.</span>
            </div>
            <div class="form-group">
                <label>Email:</label>
                <input type="email" name="email" value="khoa077@gmail.com" required>
                <span id="emailError" class="error">Invalid email format.</span>
            </div>
            <div class="form-group">
                <label>Services:</label>
                <div class="services">
                    <c:forEach var="service" items="${services}">
                        <div class="service-item">
                            <input type="checkbox" name="services" value="${service.serviceId}">
                            ${service.serviceName} - ${service.price} VND
                        </div>
                    </c:forEach>
                </div>
            </div>
            <button type="submit">Submit Booking</button>
        </form>
    </div>
</body>
</html>