<%-- 
    Document   : register
    Created on : 23 Jun 2025, 22:13:28
    Author     : Admin
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Register</title>
        <style>
            body {
                font-family: sans-serif;
                background: #f5f5f5;
                display: flex;
                justify-content: center;
                align-items: center;
                height: 100vh;
            }
            form {
                background: white;
                padding: 30px;
                border-radius: 10px;
                box-shadow: 0 0 10px gray;
                width: 400px;
            }
            input, select {
                width: 100%;
                padding: 10px;
                margin: 10px 0;
            }
            .error {
                color: red;
                margin-bottom: 10px;
            }
            .message {
                color: green;
                margin-bottom: 10px;
            }
            button {
                width: 100%;
                padding: 10px;
                background: #007bff;
                color: white;
                border: none;
                cursor: pointer;
            }
        </style>
    </head>
    <body>
        <form method="post" action="${pageContext.request.contextPath}/register">
            <h2>Register</h2>

            <c:if test="${not empty error}">
                <div class="error">${error}</div>
            </c:if>
            <c:if test="${not empty message}">
                <div class="message">${message}</div>
            </c:if>

            <input type="text" name="username" placeholder="Username" required />
            <input type="password" name="password" placeholder="Password" required />
            <input type="password" name="confirmPassword" placeholder="Confirm Password" required />
            <input type="text" name="fullName" placeholder="Full Name" required />
            <input type="text" name="email" placeholder="Email (e.g. example@gmail.com)" required />
            <input type="date" name="dob" placeholder="Date of Birth" required />
            <input type="text" name="numberPhone" placeholder="Phone Number" required />


            <button type="submit">Register</button>
        </form>
    </body>
</html>

