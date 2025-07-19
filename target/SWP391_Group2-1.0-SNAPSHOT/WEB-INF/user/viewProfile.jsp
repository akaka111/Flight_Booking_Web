<%-- 
    Document   : viewProfile
    Created on : Jul 19, 2025, 1:34:10 PM
    Author     : Admin
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>View Profile</title>
        <style>
            body {
                font-family: Arial, sans-serif;
                background: linear-gradient(to bottom, #87CEEB, #E0F6FF);
                display: flex;
                justify-content: center;
                align-items: center;
                height: 100vh;
                margin: 0;
            }
            .profile-container {
                background: white;
                padding: 50px;
                border-radius: 10px;
                box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
                width: 800px; /* Tăng từ 400px lên 500px để rộng hơn */
                text-align: left;
            }
            .profile-container h2 {
                font-size: 45px;
                color: #1E90FF;
                text-align: center;
                margin-bottom: 25px;
                position: relative;
            }
            .profile-container h2::before {
                font-size: 40px;
                content: "✈";
                position: absolute;
                left: 10px;
                color: #1E90FF;
            }
            .profile-container label {
                font-size: 18px;
                display: block;
                margin-bottom: 5px;
                font-weight: bold;
            }
            .profile-container input[type="text"],
            .profile-container select {
                width: 100%;
                padding: 10px; /* Tăng padding để ô nhập liệu bự hơn */
                margin-bottom: 15px;
                border: 1px solid #ccc;
                border-radius: 4px;
                box-sizing: border-box;
                font-size: 30px; /* Tăng kích thước chữ để bự hơn */
            }
            .profile-container .buttons {
                text-align: center;
                margin-top: 20px;
            }
            .profile-container .buttons button,
            .profile-container .buttons a {
                padding: 15px 30px; /* Tăng padding cho nút bự hơn */
                margin: 5px;
                text-decoration: none;
                color: white;
                border: none;
                border-radius: 4px;
                cursor: pointer;
                font-size: 24px; /* Tăng kích thước chữ trên nút */
            }
            .profile-container .buttons .cancel {
                background-color: #6c757d;
            }
            .profile-container .buttons .save {
                background-color: #28a745;
            }
            .profile-container .buttons .edit {
                background-color: #28a745;
            }
            .profile-container .buttons .change-password {
                background-color: #dc3545;
            }
        </style>
    </head>
    <body>
        <div class="profile-container">
            <h2>View Profile</h2>
            <form>
                <label for="fullname">Full Name:</label>
                <input type="text" id="fullname" value="${account.fullname}" readonly>

                <label for="username">Username:</label>
                <input type="text" id="username" value="${account.username}" readonly>

                <label for="email">Email:</label>
                <input type="text" id="email" value="${account.email}" readonly>

                <label for="phone">Phone:</label>
                <input type="text" id="phone" value="${account.phone}" readonly>

                <label for="dob">Date of Birth:</label>
                <input type="text" id="dob" value="${account.dob}" readonly>

                <label for="role">Role:</label>
                <input type="text" id="role" value="${account.role}" readonly>

                <label for="status">Status:</label>
                <input type="text" id="status" value="${account.status ? 'Active' : 'Inactive'}" readonly>
            </form>
            <div class="buttons">
                <a href="updateProfile.jsp" class="edit">Edit Profile</a>
                <a href="changePassword.jsp" class="change-password">Change Password</a>
            </div>
        </div>
    </body>
</html>