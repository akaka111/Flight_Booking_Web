<%-- 
    Document   : updateTicketPrice
    Created on : Aug 30, 2025, 4:08:12 AM
    Author     : Khoa
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
    <title>Cập Nhật Giá Vé</title>
    <!-- Bootstrap 5 -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet"/>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</head>
<body class="bg-light">

<div class="container mt-4">
    <div class="card shadow-sm">
        <div class="card-body">
            <h3 class="text-center mb-4">Quản Lý Giá Vé </h3>
            <h3 class="text-center mb-4">
                Tuyến ${flight.route.originIata} (${flight.route.originName})
                → ${flight.route.destIata} (${flight.route.destName})
                | Hãng ${flight.airline.name}
            </h3>
            
            <form action="ticketPrice" method="post">
                <input type="hidden" name="flightId" value="${flightId}"/>

                <c:forEach var="seatClass" items="${seatClasses}">
                    <div class="mb-3 row">
                        <label class="col-sm-3 col-form-label">Hạng Ghế: ${seatClass.name}</label>
                        <div class="col-sm-9">
                            <input type="hidden" name="seatClassId" value="${seatClass.seatClassId}"/>
                            <c:set var="existingPrice" value="0.0"/>
                            <c:forEach var="ticketClass" items="${ticketClasses}">
                                <c:if test="${ticketClass.className eq seatClass.name}">
                                    <c:set var="existingPrice" value="${ticketClass.price}"/>
                                </c:if>
                            </c:forEach>
                            <input type="number" class="form-control" name="price" 
                                   value="${existingPrice}" step="0.01" min="0" required/>
                        </div>
                    </div>
                </c:forEach>

                <div class="d-flex justify-content-between mt-4">
                    <a href="ticketPrice" class="btn btn-secondary">
                        <i class="fa fa-arrow-left"></i> Quay Lại
                    </a>
                    <button type="submit" class="btn btn-success">
                        <i class="fa fa-save"></i> Lưu Giá Vé
                    </button>
                </div>
            </form>
        </div>
    </div>
</div>

</body>
</html>
