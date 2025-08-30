<%-- 
    Document   : editAirline
    Created on : Aug 20, 2025, 1:19:11 AM
    Author     : Khoa
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Chỉnh sửa Hãng Bay - Hệ Thống Đặt Vé Máy Bay</title>
        <link rel="preconnect" href="https://fonts.googleapis.com">
        <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
        <link href="https://fonts.googleapis.com/css2?family=Montserrat:wght@400;500;700&display=stylesheet">
        <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css"/>
        <style>
            body {
                font-family: 'Montserrat', sans-serif;
                background-color: #f8f9fa;
                color: #343a40;
                padding: 40px;
            }
            .form-container {
                max-width: 900px;
                margin: auto;
                background: #fff;
                padding: 30px;
                border-radius: 10px;
                box-shadow: 0 5px 15px rgba(0,0,0,0.1);
            }
            h2 {
                text-align: center;
                margin-bottom: 30px;
                color: #007bff;
            }
            .form-group input[readonly] {
                background-color: #e9ecef;
            }
            .form-actions {
                text-align: right;
            }
            .form-actions .btn {
                min-width: 120px;
            }
        </style>
    </head>
    <body>
        <div class="container">
            <div class="form-container">
                <h2><i class="fa fa-pen"></i> Chỉnh sửa Hãng Bay</h2>

                <!-- Thông báo -->
                <c:if test="${not empty message}">
                    <div class="alert alert-success">${message}</div>
                </c:if>
                <c:if test="${not empty error}">
                    <div class="alert alert-danger">${error}</div>
                </c:if>

                <form action="${pageContext.request.contextPath}/AirlineAdmin" method="post">
                    <input type="hidden" name="action" value="update">
                    <input type="hidden" name="id" value="${airline.airlineId}">
                    <div class="form-group">
                        <label for="name">Tên Hãng Bay</label>
                        <input type="text" class="form-control" id="name" name="name" value="${airline.name}" required>
                        <div class="invalid-feedback"></div>
                    </div>
                    <div class="form-group">
                        <label for="code">Mã Hãng Bay</label>
                        <input type="text" class="form-control" id="code" name="code" value="${airline.code}" required>
                        <div class="invalid-feedback"></div>
                    </div>
                    <div class="form-group">
                        <label for="description">Mô Tả</label>
                        <textarea class="form-control" id="description" name="description" rows="3">${airline.description}</textarea>
                        <div class="invalid-feedback"></div>
                    </div>
                    <div class="form-group">
                        <label for="services">Ghi Chú Dịch Vụ</label>
                        <textarea class="form-control" id="services" name="services" rows="3">${airline.services}</textarea>
                        <div class="invalid-feedback"></div>
                    </div>
                    <div class="form-actions mt-4">
                        <a href="${pageContext.request.contextPath}/AirlineAdmin" class="btn btn-secondary">
                            <i class="fa fa-arrow-left"></i> Hủy
                        </a>
                        <button type="submit" class="btn btn-success">
                            <i class="fa fa-save"></i> Lưu
                        </button>
                    </div>
                </form>
            </div>
        </div>

        <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
        <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
        <script>
            $(document).ready(function () {
                $('form').on('submit', function (e) {
                    let isValid = true;

                    // Xóa lỗi cũ
                    $('.form-control').removeClass('is-invalid');
                    $('.invalid-feedback').text('');

                    // Lấy dữ liệu
                    let name = $('#name').val().trim();
                    let code = $('#code').val().trim();

                    if (name === "") {
                        $('#name').addClass('is-invalid');
                        $('#name').next('.invalid-feedback').text("Tên hãng bay không được để trống.");
                        isValid = false;
                    }

                    if (code === "") {
                        $('#code').addClass('is-invalid');
                        $('#code').next('.invalid-feedback').text("Mã hãng bay không được để trống.");
                        isValid = false;
                    } else if (!/^[A-Z0-9]{2,5}$/.test(code)) {
                        $('#code').addClass('is-invalid');
                        $('#code').next('.invalid-feedback').text("Mã hãng bay phải từ 2-5 ký tự, chỉ chứa chữ cái in hoa hoặc số.");
                        isValid = false;
                    }

                    if (!isValid) {
                        e.preventDefault();
                    }
                });
            });
        </script>
    </body>
</html>