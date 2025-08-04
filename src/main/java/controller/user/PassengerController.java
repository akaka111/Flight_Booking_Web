/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.user;

import DAO.Admin.BookingDAO;
import DAO.Admin.PassengerDAO;
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
import java.text.SimpleDateFormat;
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
        try {
            int flightId = Integer.parseInt(request.getParameter("flightId"));
            String selectedClass = request.getParameter("selectedClass");
            double finalPrice = Double.parseDouble(request.getParameter("finalPrice"));

            HttpSession session = request.getSession();
            Account account = (Account) session.getAttribute("user");

            if (account == null) {
                request.getRequestDispatcher("/WEB-INF/common/Login.jsp").forward(request, response);
                return;
            }

            // === KIỂM TRA GIỜ BAY CÓ QUÁ GIỜ HIỆN TẠI KHÔNG ===
            // === KIỂM TRA GIỜ BAY CÓ CÒN ĐỦ HƠN 2 TIẾNG KHÔNG ===
            BookingDAO bookingDAO = new BookingDAO();
            Timestamp departureTime = bookingDAO.getDepartureTimeByFlightId(flightId);

            if (departureTime != null) {
                long currentTimeMillis = System.currentTimeMillis();
                long twoHoursBeforeFlight = departureTime.getTime() - (2 * 60 * 60 * 1000); // trừ 2 tiếng

                if (currentTimeMillis > twoHoursBeforeFlight) {
                    request.setAttribute("errorMessage", "Chuyến bay này sẽ khởi hành trong vòng 2 tiếng. Hệ thống đã đóng chức năng đặt vé.");
                    RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/user/error.jsp");
                    dispatcher.forward(request, response);
                    return;
                }

            }

            // Booking tạm
            Booking tempBooking = new Booking();
            tempBooking.setUserId(account.getUserId());
            tempBooking.setFlightId(flightId);
            tempBooking.setBookingDate(new Timestamp(System.currentTimeMillis()));
            tempBooking.setSeatClass(selectedClass);
            tempBooking.setTotalPrice(finalPrice);
            tempBooking.setStatus("PENDING");
            tempBooking.setBookingCode("TEMP_" + System.currentTimeMillis());

            session.setAttribute("tempBooking", tempBooking);

            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/user/passenger.jsp");
            dispatcher.forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("error.jsp?message=BookingCreationError");
        }
    }

    // ... (Các phần import và doGet giữ nguyên)
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession();

        String step = request.getParameter("step");

        if ("addPassenger".equals(step)) {
            try {
                // Lấy booking tạm từ session
                Booking booking = (Booking) session.getAttribute("tempBooking");
                if (booking == null) {
                    response.sendRedirect("error.jsp?message=BookingNotFound");
                    return;
                }

                // Tạo và lưu thông tin hành khách tạm
                Passenger tempPassenger = new Passenger();
                tempPassenger.setFullName(request.getParameter("fullName"));
                tempPassenger.setPassportNumber(request.getParameter("passportNumber"));
                tempPassenger.setGender(request.getParameter("gender"));
                tempPassenger.setPhoneNumber(request.getParameter("phoneNumber"));
                tempPassenger.setEmail(request.getParameter("email"));
                tempPassenger.setCountry(request.getParameter("country"));
                tempPassenger.setAddress(request.getParameter("address"));

                String dobStr = request.getParameter("dob");
                // LƯU Ý: Định dạng ngày tháng của input type="date" là yyyy-MM-dd
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                java.util.Date utilDate = sdf.parse(dobStr);
                java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
                tempPassenger.setDob(sqlDate);

                session.setAttribute("tempPassenger", tempPassenger);

                // === THAY ĐỔI QUAN TRỌNG Ở ĐÂY ===
                // SAI: request.getRequestDispatcher("/WEB-INF/user/seatSelection.jsp").forward(request, response);
                // ĐÚNG: Chuyển hướng đến ManageSeatController và gửi kèm flightId
                // Controller sẽ lấy flightId này để truy vấn danh sách ghế
                response.sendRedirect("manageSeatController?flightId=" + booking.getFlightId());

            } catch (Exception e) {
                e.printStackTrace();
                response.sendRedirect("error.jsp?message=PassengerInsertError");
            }
        }
    }
}
