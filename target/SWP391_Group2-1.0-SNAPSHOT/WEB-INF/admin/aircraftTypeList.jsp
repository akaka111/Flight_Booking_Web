<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Quản Lý Loại Máy Bay - Hệ Thống Đặt Vé Máy Bay</title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Montserrat:wght@400;500;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css"/>
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
    <style>
        body {
            font-family: 'Montserrat', sans-serif;
            background-color: #f8f9fa;
            color: #343a40;
        }
        .table-container {
            background-color: #ffffff;
            padding: 20px;
            margin: 30px auto;
            border-radius: 8px;
            box-shadow: 0 5px 15px rgba(0,0,0,0.1);
            max-width: 100%;
        }
        .btn-add {
            padding: 10px 20px;
            font-size: 16px;
            background-color: #28a745;
            color: white;
            border: none;
            border-radius: 6px;
            text-decoration: none;
            font-weight: 500;
        }
        .action-buttons {
            display: flex;
            gap: 8px;
        }
        .action-buttons .btn {
            font-size: 14px;
            padding: 4px 10px;
        }
        table th {
            background-color: #007bff;
            color: white;
        }
    </style>
</head>
<body>
<div class="container">
    <div class="table-container">
        <h2 class="mb-4">Quản Lý Loại Máy Bay</h2>
        <div class="text-right mb-3">
            <a href="AircraftTypeAdmin?action=showAddForm" class="btn-add">
                <i class="fa fa-plus"></i> Thêm Loại Máy Bay
            </a>
        </div>
        <table class="table table-bordered">
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Mã Loại</th>
                    <th>Tên Loại</th>
                    <th>Hãng Bay</th>
                    <th>Trạng Thái</th>
                    <th>Thao Tác</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="at" items="${aircraftTypes}">
                    <tr>
                        <td>${at.aircraftTypeId}</td>
                        <td>${at.aircraftTypeCode}</td>
                        <td>${at.aircraftTypeName}</td>
                        <td>${at.airlineId.name} (${at.airlineId.code})</td>
                        <td>${at.status}</td>
                        <td class="action-buttons">
                            <a href="AircraftTypeAdmin?action=edit&id=${at.aircraftTypeId}" class="btn btn-sm btn-warning">
                                <i class="fa fa-edit"></i> Sửa
                            </a>
                            <button class="btn btn-sm btn-danger" onclick="confirmDeleteAircraftType(${at.aircraftTypeId})">
                                <i class="fa fa-trash"></i> Xóa
                            </button>
                        </td>
                    </tr>
                </c:forEach>
                <c:if test="${empty aircraftTypes}">
                    <tr>
                        <td colspan="6" class="text-center">Không tìm thấy loại máy bay nào!</td>
                    </tr>
                </c:if>
            </tbody>
        </table>
    </div>
</div>

<% String successcreate = request.getParameter("successcreate"); %>
<% String successedit = request.getParameter("successedit"); %>
<% String successdelete = request.getParameter("successdelete"); %>
<% String error = request.getParameter("error"); %>
<script>
    function confirmDeleteAircraftType(aircraftTypeId) {
        Swal.fire({
            title: 'Xác nhận xóa?',
            text: "Loại máy bay này sẽ bị xóa.",
            icon: 'warning',
            showCancelButton: true,
            confirmButtonColor: '#d33',
            cancelButtonColor: '#3085d6',
            confirmButtonText: 'Xóa',
            cancelButtonText: 'Hủy'
        }).then((result) => {
            if (result.isConfirmed) {
                window.location.href = 'AircraftTypeAdmin?action=delete&id=' + aircraftTypeId;
            }
        });
    }
    window.onload = function () {
        <% if ("1".equals(successcreate)) { %>
            Swal.fire({
                icon: 'success',
                title: 'Thành công!',
                text: 'Loại máy bay đã được tạo.',
                timer: 2000
            });
        <% } else if ("0".equals(successcreate)) { %>
            Swal.fire({
                icon: 'error',
                title: 'Thất bại!',
                text: 'Không thể tạo loại máy bay.',
                timer: 2000
            });
        <% } %>
        <% if ("1".equals(successedit)) { %>
            Swal.fire({
                icon: 'success',
                title: 'Thành công!',
                text: 'Loại máy bay đã được cập nhật.',
                timer: 2000
            });
        <% } else if ("0".equals(successedit)) { %>
            Swal.fire({
                icon: 'error',
                title: 'Thất bại!',
                text: 'Không thể cập nhật loại máy bay.',
                timer: 2000
            });
        <% } %>
        <% if ("1".equals(successdelete)) { %>
            Swal.fire({
                icon: 'success',
                title: 'Thành công!',
                text: 'Loại máy bay đã được xóa.',
                timer: 2000
            });
        <% } else if ("0".equals(successdelete)) { %>
            Swal.fire({
                icon: 'error',
                title: 'Thất bại!',
                text: 'Không thể xóa loại máy bay do đang được sử dụng.',
                timer: 2000
            });
        <% } %>
        <% if (error != null) { %>
            Swal.fire({
                icon: 'error',
                title: 'Lỗi!',
                text: '<%= error %>',
                timer: 3000
            });
        <% } %>
    };
</script>
<script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.1/dist/umd/popper.min.js"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
</body>
</html>