<%-- 
    Document   : error
    Created on : 31 Jul 2025, 06:46:38
    Author     : Admin
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <title>Đã xảy ra lỗi</title>
        <%-- Sao chép <style> từ các trang khác để đồng bộ --%>
        <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">
        <style>
            :root {
                --primary-color: #007bff;
                --secondary-color: #ff6f61;
                --text-color: #343a40;
                --border-color: #dee2e6;
                --light-gray: #f8f9fa;
                --white: #ffffff;
                --shadow: 0 5px 15px rgba(0,0,0,0.1);
                --error-color: #d32f2f;
            }
            body {
                font-family: 'Montserrat', sans-serif;
                background-color: var(--light-gray);
                margin: 0;
            }
            .container {
                max-width: 800px;
                margin: 50px auto;
                padding: 20px;
            }
            .error-card {
                background: var(--white);
                border: 1px solid var(--border-color);
                border-radius: 8px;
                box-shadow: var(--shadow);
                text-align: center;
                padding: 40px;
            }
            .error-card h1 {
                color: var(--error-color);
                margin-top: 0;
            }
            .error-card p {
                font-size: 1.1em;
                color: var(--text-color);
            }
            .error-details {
                background-color: var(--light-gray);
                border: 1px dashed var(--border-color);
                padding: 15px;
                margin-top: 20px;
                border-radius: 5px;
                color: #6c757d;
            }
            .btn-home {
                display: inline-block;
                margin-top: 30px;
                padding: 12px 30px;
                background-color: var(--primary-color);
                color: var(--white);
                text-decoration: none;
                font-weight: 700;
                border-radius: 5px;
                transition: background-color 0.3s ease;
            }
            .btn-home:hover {
                background-color: #0056b3;
            }
        </style>
    </head>
    <body>
        <header>
            <jsp:include page="/WEB-INF/user/components/header.jsp" /> 
        </header>

        <main>
            <div class="container">
                <div class="error-card">
                    <h1>Rất tiếc, đã có lỗi xảy ra!</h1>
                    <p>Chúng tôi không thể xử lý yêu cầu của bạn vào lúc này.</p>

                    <%-- Lấy thông báo lỗi từ request attribute --%>
                    <c:choose>
                        <c:when test="${not empty param.errorMessage}">
                            <div class="error-details">${param.errorMessage}</div>
                        </c:when>
                        <c:when test="${param.message == 'BookingClosedForThisFlight'}">
                            <div class="error-details">Chuyến bay sẽ khởi hành trong vòng 2 tiếng. Hệ thống đã đóng chức năng đặt vé.</div>
                        </c:when>
                        <c:when test="${param.message == 'BookingNotFound'}">
                            <div class="error-details">Không tìm thấy thông tin đặt chỗ. Vui lòng thử lại từ đầu.</div>
                        </c:when>
                        <c:when test="${param.message == 'PassengerInsertError'}">
                            <div class="error-details">Không thể lưu thông tin hành khách. Vui lòng kiểm tra và thử lại.</div>
                        </c:when>
                        <c:when test="${param.message == 'BookingCreationError'}">
                            <div class="error-details">Lỗi khởi tạo đơn đặt vé. Vui lòng thử lại sau.</div>
                        </c:when>
                        <c:otherwise>
                            <div class="error-details">Đã có lỗi xảy ra. Vui lòng thử lại.</div>
                        </c:otherwise>
                    </c:choose>


                    <a href="home" class="btn-home">Quay về trang chủ</a>
                </div>
            </div>
        </main>

        <footer>
            <jsp:include page="/WEB-INF/user/components/footer.jsp" /> 
        </footer> 
    </body>
</html>