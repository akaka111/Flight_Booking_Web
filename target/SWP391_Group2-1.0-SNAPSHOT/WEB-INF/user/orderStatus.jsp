<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <title>Trạng thái đơn hàng</title>
        <link rel="preconnect" href="https://fonts.googleapis.com">
        <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
        <link href="https://fonts.googleapis.com/css2?family=Montserrat:wght@400;500;700&display=swap" rel="stylesheet">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css"/>
        <style>
            :root {
                --primary-color: #007bff;
                --secondary-color: #ff6f61;
                --text-color: #343a40;
                --light-gray: #f8f9fa;
                --white: #ffffff;
                --shadow: 0 5px 15px rgba(0,0,0,0.1);
                --success-color: #28a745;
                --fail-color: #dc3545;
            }
            body {
                font-family: 'Montserrat', sans-serif;
                background-color: var(--light-gray);
                color: var(--text-color);
                margin: 0;
                padding: 0;
                display: flex;
                flex-direction: column;
                min-height: 100vh;
            }
            main {
                flex: 1;
                display: flex;
                align-items: center;
                justify-content: center;
            }
            .container {
                max-width: 600px;
                margin: 30px auto;
                background-color: var(--white);
                border-radius: 12px;
                box-shadow: var(--shadow);
                padding: 40px;
                text-align: center;
            }
            h2 {
                font-size: 2em;
                margin-bottom: 20px;
            }
            .success {
                color: var(--success-color);
            }
            .fail {
                color: var(--fail-color);
            }
            .icon {
                font-size: 4em;
                margin-bottom: 20px;
            }
            p {
                font-size: 1.1em;
            }
            .back-link {
                display: inline-block;
                margin-top: 30px;
                padding: 12px 25px;
                border-radius: 5px;
                background-color: var(--primary-color);
                color: var(--white);
                text-decoration: none;
                font-weight: 500;
                transition: background-color 0.3s ease;
            }
            .back-link:hover {
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
                <%
                    String status = (String) request.getAttribute("status");
                    String error = (String) request.getAttribute("error");

                    if ("false".equals(status)) {
                %>
                <div class="icon fail"><i class="fa-solid fa-circle-xmark"></i></div>
                <h2 class="fail">Thanh toán thất bại!</h2>
                <p><strong>Lý do:</strong>
                    <%
                        if ("invalid_signature".equals(error)) {
                            out.print("Sai chữ ký xác thực.");
                        } else if (error != null && !error.isEmpty()) {
                            out.print(error);
                        } else {
                            out.print("Giao dịch đã bị hủy hoặc có lỗi không xác định.");
                        }
                    %>
                </p>
                <%
                } else {
                %>
                <div class="icon success"><i class="fa-solid fa-circle-check"></i></div>
                <h2 class="success">Thanh toán thành công!</h2>
                <p>Cảm ơn bạn đã sử dụng dịch vụ. Thông tin vé sẽ sớm được gửi đến email của bạn.</p>
                <%
                    }
                %>

                <a class="back-link" href="<%= request.getContextPath()%>/home">Quay về trang chủ</a>
            </div>
        </main>

        <footer>
            <jsp:include page="/WEB-INF/user/components/footer.jsp" />
        </footer>
    </body>
</html>
