<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Thêm Người Dùng Mới</title>
    <link href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css" rel="stylesheet"/>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css"/>

    <style>
        body {
            font-family: 'Montserrat', sans-serif;
            background-color: #f8f9fa;
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
            margin-bottom: 30px;
            text-align: center;
            color: #007bff;
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
    <div class="form-container">
        <h2><i class="fa fa-user-plus"></i> Thêm Người Dùng Mới</h2>

        <form action="manageAccountController" method="post">
            <input type="hidden" name="action" value="create"/>

            <div class="form-group">
                <label for="username">Tên đăng nhập</label>
                <input type="text" class="form-control" id="username" name="username" required/>
            </div>

            <div class="form-group">
                <label for="password">Mật khẩu</label>
                <input type="password" class="form-control" id="password" name="password" required/>
            </div>

            <div class="form-group">
                <label for="fullname">Họ và Tên</label>
                <input type="text" class="form-control" id="fullname" name="fullname" required/>
            </div>

            <div class="form-group">
                <label for="email">Email</label>
                <input type="email" class="form-control" id="email" name="email" required/>
            </div>

            <div class="form-group">
                <label for="phone">Số điện thoại</label>
                <input type="tel" class="form-control" id="phone" name="phone"/>
            </div>

            <div class="form-group">
                <label for="dob">Ngày sinh</label>
                <input type="date" class="form-control" id="dob" name="dob"/>
            </div>

            <div class="form-group">
                <label for="role">Vai trò</label>
                <select class="form-control" id="role" name="role">
                    <option value="CUSTOMER">Customer</option>
                    <option value="STAFF">Staff</option>
                    <option value="ADMIN">Admin</option>
                </select>
            </div>

            <div class="form-group">
                <label>Trạng thái</label><br/>
                <div class="form-check form-check-inline">
                    <input class="form-check-input" type="radio" id="statusActive" name="status" value="true" checked/>
                    <label class="form-check-label" for="statusActive">Active</label>
                </div>
                <div class="form-check form-check-inline">
                    <input class="form-check-input" type="radio" id="statusInactive" name="status" value="false"/>
                    <label class="form-check-label" for="statusInactive">Inactive</label>
                </div>
            </div>

            <div class="form-actions mt-4">
                <a href="manageAccountController" class="btn btn-secondary">
                    <i class="fa fa-arrow-left"></i> Hủy
                </a>
                <button type="submit" class="btn btn-success">
                    <i class="fa fa-save"></i> Lưu
                </button>
            </div>
        </form>
    </div>

    <!-- Bootstrap JS -->
    <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
</body>
</html>
