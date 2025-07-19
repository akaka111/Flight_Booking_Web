/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.user;

import DAO.Admin.BookingDAO;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import model.Booking;
import model.Passenger;
import model.Account;
import utils.DBContext;

@WebServlet(name = "PassengerController", urlPatterns = {"/passenger"})
public class PassengerController extends HttpServlet {

    /**
     * HIỂN THỊ TRANG NHẬP THÔNG TIN HÀNH KHÁCH Sẽ được gọi khi người dùng nhấn
     * "Đi tiếp" từ trang flight-detail.jsp
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        DBContext dbContext = new DBContext();
        try (Connection conn = dbContext.getConnection()) {
            System.out.println("==> Kết nối DB: " + conn);
            // 1. Lấy thông tin chuyến bay từ URL (do flight-detail.jsp gửi qua)
            int flightId = Integer.parseInt(request.getParameter("flightId"));
            String selectedClass = request.getParameter("selectedClass");
            double finalPrice = Double.parseDouble(request.getParameter("finalPrice"));

            // Giả sử bạn lưu thông tin người dùng trong session
            HttpSession session = request.getSession();
            Account account = (Account) session.getAttribute("user");

            if (account == null) {
                request.getRequestDispatcher("/WEB-INF/common/Login.jsp").forward(request, response);
                return;
            }

            // 2. Tạo một bản ghi Booking mới trong DB để lấy bookingId
            Booking newBooking = new Booking();
            newBooking.setUserId(account.getUserId());
            newBooking.setFlightId(flightId);
            newBooking.setBookingDate(new Timestamp(System.currentTimeMillis()));
            newBooking.setSeatClass(selectedClass);
            newBooking.setTotalPrice(finalPrice);
            newBooking.setStatus("CONFIRMED");

            BookingDAO bookingDAO = new BookingDAO(conn);
            int newBookingId = bookingDAO.createBooking(newBooking); // DAO phải trả về ID vừa tạo

            // Chỉ tiếp tục nếu việc tạo booking thành công (ID > 0)
            if (newBookingId > 0) {
                // 3. Lưu booking tạm thời vào session để sau này xử lý thanh toán
                newBooking.setBookingId(newBookingId); // Set ID vào booking
                session.setAttribute("tempBooking", newBooking);

                // 4. Chuyển đến trang nhập thông tin hành khách
                RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/user/passenger.jsp");
                dispatcher.forward(request, response);
            } else {
                // Nếu tạo booking thất bại, chuyển về trang lỗi
                response.sendRedirect("error.jsp?message=BookingCreationError");
            }

        } catch (SQLException | NumberFormatException e) {
            e.printStackTrace();
            response.sendRedirect("error.jsp?message=BookingCreationError");
        }
    }

    /**
     * LƯU THÔNG TIN HÀNH KHÁCH VÀO DATABASE Sẽ được gọi khi người dùng nhấn
     * "Hoàn tất" từ trang passenger.jsp
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession();

        // Dùng một tham số 'step' để biết đang ở bước nào
        String step = request.getParameter("step");

        if ("addFlight".equals(step)) {
            Account account = (Account) session.getAttribute("user");
            if (account == null) {
                response.sendRedirect("login.jsp");
                return;
            }

            Booking tempBooking = new Booking();
            tempBooking.setUserId(account.getUserId());
            tempBooking.setFlightId(Integer.parseInt(request.getParameter("flightId")));
            tempBooking.setSeatClass(request.getParameter("selectedClass"));
            tempBooking.setTotalPrice(Double.parseDouble(request.getParameter("finalPrice")));
            tempBooking.setBookingDate(new Timestamp(System.currentTimeMillis()));
            tempBooking.setStatus("CONFIRMED");

            session.setAttribute("tempBooking", tempBooking);

            // Sửa redirect thành forward
            request.setAttribute("tempBooking", tempBooking);
            RequestDispatcher dispatcher = request.getRequestDispatcher("passenger.jsp");
            dispatcher.forward(request, response);

        } else if ("addPassenger".equals(step)) {
            // === BƯỚC 2: LƯU THÔNG TIN HÀNH KHÁCH VÀO SESSION ===
            Passenger tempPassenger = new Passenger();
            //tempPassenger.setBookingId(Integer.parseInt(request.getParameter("bookingId")));
            tempPassenger.setFullName(request.getParameter("fullName"));
            tempPassenger.setPassportNumber(request.getParameter("passportNumber"));
            tempPassenger.setGender(request.getParameter("gender"));
            tempPassenger.setPhoneNumber(request.getParameter("phoneNumber"));
            tempPassenger.setEmail(request.getParameter("email"));
            tempPassenger.setCountry(request.getParameter("country"));
            tempPassenger.setAddress(request.getParameter("address"));

            // Lưu đối tượng tạm và chuỗi ngày sinh vào session
            session.setAttribute("tempPassenger", tempPassenger);
            session.setAttribute("dobStr", request.getParameter("dob"));

            // Chuyển đến trang thanh toán
            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/user/payment.jsp");
            dispatcher.forward(request, response);
        }
    }
}
