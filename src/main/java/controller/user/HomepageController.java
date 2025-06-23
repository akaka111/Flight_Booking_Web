/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.user;

import DAO.Admin.FlightDAO;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import model.Flight;

/**
 *
 * @author Admin
 */
@WebServlet(name = "HomepageController", urlPatterns = {"/home"})
public class HomepageController extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet HomepageController</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet HomepageController at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println("--- Bắt đầu xử lý HomepageController ---"); // Log bắt đầu
        try {
            FlightDAO dao = new FlightDAO();
            List<Flight> flights = dao.getAllFlights();

            // === DÒNG DEBUG QUAN TRỌNG ===
            if (flights != null) {
                System.out.println("==> FlightDAO đã trả về danh sách. Số lượng: " + flights.size());
                if (!flights.isEmpty()) {
                    // In ra thông tin chuyến bay đầu tiên để kiểm tra
                    Flight firstFlight = flights.get(0);
                    System.out.println("==> Chuyến bay đầu tiên: " + firstFlight.getFlightNumber()
                            + " | Giá: " + firstFlight.getPrice());
                }
            } else {
                System.out.println("==> LỖI: FlightDAO trả về NULL!");
            }
            // =============================

            request.setAttribute("flights", flights);
        } catch (Exception e) {
            System.out.println("==> LỖI NGOẠI LỆ TRONG SERVLET: ");
            e.printStackTrace(); // In ra chi tiết lỗi nếu có
        }

        System.out.println("--- Chuyển hướng đến home.jsp ---"); // Log kết thúc
        request.getRequestDispatcher("/WEB-INF/views/home.jsp").forward(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
