<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${action == 'add' ? 'Thêm Loại Máy Bay' : 'Chỉnh Sửa Loại Máy Bay'} - Hệ Thống Đặt Vé Máy Bay</title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Montserrat:wght@400;500;700&display=swap" rel="stylesheet">
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
        <h2><i class="fa fa-plane"></i> ${action == 'add' ? 'Thêm Loại Máy Bay' : 'Chỉnh Sửa Loại Máy Bay'}</h2>
        <!-- Thông báo -->
        <c:if test="${not empty msg}">
            <div class="alert alert-success">${msg}</div>
        </c:if>
        <c:if test="${not empty error}">
            <div class="alert alert-danger">${error}</div>
        </c:if>
        <form method="post" action="AircraftTypeAdmin">
            <input type="hidden" name="action" value="${action}">
            <c:if test="${action == 'update'}">
                <input type="hidden" name="id" value="${aircraftType.aircraftTypeId}">
            </c:if>
            <div class="form-group">
                <label for="aircraftTypeCode">Mã Loại Máy Bay</label>
                <input type="text" class="form-control" id="aircraftTypeCode" name="aircraftTypeCode" 
                       value="${aircraftType.aircraftTypeCode}" required>
            </div>
            <div class="form-group">
                <label for="aircraftTypeName">Tên Loại Máy Bay</label>
                <input type="text" class="form-control" id="aircraftTypeName" name="aircraftTypeName" 
                       value="${aircraftType.aircraftTypeName}" required>
            </div>
            <div class="form-group">
                <label for="airlineId">Hãng Bay</label>
                <select class="form-control" id="airlineId" name="airlineId" required>
                    <option value="">Chọn hãng bay</option>
                    <c:forEach var="airline" items="${airlines}">
                        <option value="${airline.airlineId}" 
                                ${aircraftType.airlineId.airlineId == airline.airlineId ? 'selected' : ''}>
                            ${airline.name} (${airline.code})
                        </option>
                    </c:forEach>
                </select>
            </div>
            <div class="form-group">
                <label for="status">Trạng Thái</label>
                <select class="form-control" id="status" name="status">
                    <option value="Active" ${aircraftType.status == 'Active' ? 'selected' : ''}>Hoạt động</option>
                    <option value="Inactive" ${aircraftType.status == 'Inactive' ? 'selected' : ''}>Không hoạt động</option>
                </select>
            </div>

            <!-- Phần thêm: Cấu hình ghế -->
            <h3 class="mt-4"><i class="fa fa-chair"></i> Cấu Hình Ghế</h3>
            <c:forEach var="seatClass" items="${seatClasses}">
                <div class="form-group">
                    <label for="seatCount_${seatClass.seatClassID}">${seatClass.name} (${seatClass.description}) - Số lượng ghế</label>
                    <input type="number" min="0" class="form-control" id="seatCount_${seatClass.seatClassID}" name="seatCount_${seatClass.seatClassID}"
                           value="${seatCountMap[seatClass.seatClassID] != null ? seatCountMap[seatClass.seatClassID] : 0}">
                </div>
            </c:forEach>

            <div class="form-actions mt-4">
                <a href="AircraftTypeAdmin" class="btn btn-secondary">
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
</body>
</html>