<%-- 
    Document   : manageCustomers
    Created on : Jul 26, 2025, 8:44:25 PM
    Author     : ADMIN
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <title>Danh sách khách hàng - STAFF</title>
        <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
        <style>
            body {
                font-family: Arial, sans-serif;
                background-color: #f8f9fa;
                padding: auto;
            }
            .container {
                background-color: #fff;
                border-radius: 8px;
                padding: auto;
                box-shadow: 0 0 10px rgba(0,0,0,0.1);
            }
            
            h2 {
                margin-bottom: auto;
            }
            .btn-sm {
                margin-right: auto;
            }
        </style>
    </head>
    <body>
        <div class="container-fluid pl-3 pr-3">
            <h2>Danh sách khách hàng</h2>

            <c:if test="${param.msg == 'reset'}">
                <div class="alert alert-success">Đặt lại mật khẩu thành công!</div>
            </c:if>

            <table 
                table-layout: auto;
                width: 100%;
                class="table table-bordered table-hover">
                <thead class="thead-dark">
                    <tr>
                        <th>ID</th>
                        <th>Họ tên</th>
                        <th>Tên đăng nhập</th>
                        <th>Email</th>
                        <th>SĐT</th>
                        <th>Ngày sinh</th>
                        <th>Trạng thái</th>
                        <th>Hành động</th>
                        <th>Đặt lại mật khẩu(1235)</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="acc" items="${customers}">
                        <tr>
                            <td>${acc.userId}</td>
                            <td>${acc.fullname}</td>
                            <td>${acc.username}</td>
                            <td>${acc.email}</td>
                            <td>${acc.phone}</td>
                            <td>${acc.dob}</td>
                            <td>
                                <c:choose>
                                    <c:when test="${acc.status}">Hoạt động</c:when>
                                    <c:otherwise>Đã khóa</c:otherwise>
                                </c:choose>
                            </td>
                            <td class="d-flex align-items-center gap-1">
                                <!-- Nếu muốn cho sửa và xóa khách hàng, giữ lại 2 dòng dưới -->
                                <a href="ManageCustomerController?action=edit&userId=${acc.userId}" class="btn btn-sm btn-warning">
                                    <i class="fa fa-edit"></i> Xem/Sửa
                                </a>
                                <a href="ManageCustomerController?action=delete&userId=${acc.userId}" 
                                   class="btn btn-sm btn-danger"
                                   onclick="return confirm('Bạn có chắc chắn muốn xóa khách hàng này?');">
                                    <i class="fa fa-trash"></i> Xóa
                                </a>
                            </td>
                            <td>
                                <form action="ResetCustomerPassword" method="post" style="display:inline;">
                                    <input type="hidden" name="userId" value="${acc.userId}" />
                                    <button class="btn btn-sm btn-primary" type="submit">
                                        <i class="fas fa-redo-alt"></i> Đặt lại mật khẩu
                                    </button>
                                </form>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>

        <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
        <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
    </body>
</html>
