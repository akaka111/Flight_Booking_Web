/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.admin;

import DAO.Admin.voucherDAO;
import jakarta.servlet.RequestDispatcher;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import model.Voucher;
import utils.DBContext;

/**
 *
 * @author ADMIN
 */
@WebServlet(name = "voucherSERV", urlPatterns = {"/manageVouchers"})
public class voucherSERV extends HttpServlet {

    voucherDAO dao = new voucherDAO();
     DBContext dbContext = new DBContext();
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
            out.println("<title>Servlet voucher</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet voucher at " + request.getContextPath() + "</h1>");
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
        try (Connection conn = dbContext.getConnection()) {
            if (conn != null) {
                System.out.println(" ☑ Kết nối thành công!");
            } else {
                System.out.println(" X Kết nối thất bại!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.getWriter().println("X Lỗi: " + e.getMessage());
        }
        String action = request.getParameter("action");
        if (action != null && action.equals("edit")) {
            int id = Integer.parseInt(request.getParameter("id"));
            Voucher v = dao.getVoucherById(id);
            request.setAttribute("voucher", v);
            request.getRequestDispatcher("/WEB-INF/admin/edit-voucher.jsp").forward(request, response);
            return;
        } else if (action != null && action.equals("create")) {
            request.getRequestDispatcher("/WEB-INF/admin/add-voucher.jsp").forward(request, response);
        }

        List<Voucher> list = dao.getAllVouchers();
        request.setAttribute("list", list);
        request.setAttribute("message", "No voucher");
        request.getRequestDispatcher("/WEB-INF/admin/manageVouchers.jsp").forward(request, response);
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
        try (Connection conn = dbContext.getConnection()) {
            if (conn != null) {
                System.out.println(" Kết nối thành công!");
            } else {
                System.out.println(" Kết nối thất bại!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.getWriter().println(" Lỗi: " + e.getMessage());
        }
        String action = request.getParameter("action");
        switch (action) {
            case "create":
                Create(request, response, dao);
                break;
            case "update":
                Update(request, response, dao);
                break;
            case "delete":
                Delete(request, dao);
                response.sendRedirect("manageVouchers");
                break;
            default:
                System.out.println("❗ Unknown action: " + action);
                break;
        }
    }

    private void Create(HttpServletRequest request, HttpServletResponse response, voucherDAO dao) throws IOException {
        Voucher v = new Voucher();
        v.setCode(request.getParameter("code"));
        v.setDiscount_percent(Integer.parseInt(request.getParameter("discount")));
        v.setValid_from(request.getParameter("valid_from"));
        v.setValid_to(request.getParameter("valid_to"));
        v.setUsage_limit(Integer.parseInt(request.getParameter("usage_limit")));
        dao.insertVoucher(v);
        response.sendRedirect("manageVouchers");
    }

    private void Update(HttpServletRequest request, HttpServletResponse response, voucherDAO dao) throws IOException {
        Voucher v = new Voucher();
        v.setVoucher_id(Integer.parseInt(request.getParameter("id")));
        v.setCode(request.getParameter("code"));
        v.setDiscount_percent(Integer.parseInt(request.getParameter("discount")));
        v.setValid_from(request.getParameter("valid_from"));
        v.setValid_to(request.getParameter("valid_to"));
        v.setUsage_limit(Integer.parseInt(request.getParameter("usage_limit")));
        dao.updateVoucher(v);
        response.sendRedirect("manageVouchers");
    }

    private void Delete(HttpServletRequest request, voucherDAO dao) {
        int id = Integer.parseInt(request.getParameter("id"));
        dao.deleteVoucher(id);
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
