/*
 * Click nb
fs://SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nb
fs://SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.admin;

import DAO.Admin.AirlineDAO;
import model.Airline;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Servlet to handle CRUD operations for Airline management.
 * @author Khoa
 */
@WebServlet(name = "AirlineAdmin", urlPatterns = {"/AirlineAdmin"})
public class AirlineAdmin extends HttpServlet {

    private AirlineDAO airlineDAO;
    private static final Logger LOGGER = Logger.getLogger(AirlineAdmin.class.getName());

    @Override
    public void init() throws ServletException {
        airlineDAO = new AirlineDAO();
    }

    /**
     * Processes requests for both HTTP GET and POST methods.
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");

        String action = request.getParameter("action");
        try {
            if (action == null || action.equals("list")) {
                listAirlines(request, response);
            } else if (action.equals("new")) {
                showNewForm(request, response);
            } else if (action.equals("edit")) {
                showEditForm(request, response);
            } else if (action.equals("delete")) {
                deleteAirline(request, response);
            } else if (action.equals("add")) {
                addAirline(request, response);
            } else if (action.equals("update")) {
                updateAirline(request, response);
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Hành động không hợp lệ!");
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Database error in action: " + action, e);
            request.setAttribute("error", "Lỗi cơ sở dữ liệu: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/admin/manageAirlines.jsp").forward(request, response);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Unexpected error in action: " + action, e);
            request.setAttribute("error", "Lỗi xử lý: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/admin/manageAirlines.jsp").forward(request, response);
        }
    }

    private void listAirlines(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        List<Airline> airlines = airlineDAO.getAllAirlines();
        if (airlines == null) {
            request.setAttribute("error", "Không thể lấy danh sách hãng bay do lỗi cơ sở dữ liệu.");
        } else if (airlines.isEmpty()) {
            request.setAttribute("message", "Danh sách hãng bay rỗng. Vui lòng thêm hãng bay mới.");
        }
        request.setAttribute("airlines", airlines != null ? airlines : new ArrayList<>());
        request.getRequestDispatcher("/WEB-INF/admin/manageAirlines.jsp").forward(request, response);
    }

    private void showNewForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/admin/addAirline.jsp").forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        String idParam = request.getParameter("id");
        LOGGER.info("Edit airline ID param: " + idParam);
        if (idParam == null || idParam.trim().isEmpty()) {
            request.setAttribute("error", "ID hãng bay không được để trống!");
            listAirlines(request, response);
            return;
        }
        try {
            int airlineId = Integer.parseInt(idParam);
            Airline airline = airlineDAO.getById(airlineId);
            if (airline != null) {
                request.setAttribute("airline", airline);
                request.getRequestDispatcher("/WEB-INF/admin/editAirline.jsp").forward(request, response);
            } else {
                request.setAttribute("error", "Không tìm thấy hãng bay với ID: " + airlineId);
                listAirlines(request, response);
            }
        } catch (NumberFormatException e) {
            LOGGER.log(Level.WARNING, "Invalid airline ID: " + idParam, e);
            request.setAttribute("error", "ID hãng bay không hợp lệ!");
            listAirlines(request, response);
        }
    }

    private void deleteAirline(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        String idParam = request.getParameter("id");
        LOGGER.info("Delete airline ID param: " + idParam);
        if (idParam == null || idParam.trim().isEmpty()) {
            request.setAttribute("error", "ID hãng bay không được để trống!");
            listAirlines(request, response);
            return;
        }
        try {
            int airlineId = Integer.parseInt(idParam);
            boolean result = airlineDAO.deleteAirline(airlineId);
            if (result) {
                request.setAttribute("message", "Xóa hãng bay thành công!");
            } else {
                request.setAttribute("error", "❌ Không thể xóa hãng bay. Có thể hãng bay đang được sử dụng hoặc không tồn tại.");
            }
        } catch (NumberFormatException e) {
            LOGGER.log(Level.WARNING, "Invalid airline ID for delete: " + idParam, e);
            request.setAttribute("error", "ID hãng bay không hợp lệ!");
        }
        listAirlines(request, response);
    }

    private void addAirline(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        String name = request.getParameter("name");
        String code = request.getParameter("code");
        String description = request.getParameter("description");
        String services = request.getParameter("services");

        LOGGER.info("Adding airline: Name=" + name + ", Code=" + code);

        // Validate input
        if (name == null || name.trim().isEmpty() || code == null || code.trim().isEmpty()) {
            request.setAttribute("error", "Tên hãng bay và mã hãng bay không được để trống!");
            request.getRequestDispatcher("/WEB-INF/admin/addAirline.jsp").forward(request, response);
            return;
        }

        try {
            boolean result = airlineDAO.insertAirline(name, code, description, services);
            if (result) {
                request.setAttribute("message", "Thêm hãng bay thành công!");
                listAirlines(request, response);
            } else {
                request.setAttribute("error", "Không thể thêm hãng bay! Mã hãng bay có thể đã tồn tại hoặc có lỗi dữ liệu.");
                request.getRequestDispatcher("/WEB-INF/admin/addAirline.jsp").forward(request, response);
            }
        } catch (SQLException e) {
            request.setAttribute("error", "Lỗi khi thêm hãng bay: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/admin/addAirline.jsp").forward(request, response);
        }
    }

    private void updateAirline(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        String idParam = request.getParameter("id");
        LOGGER.info("Update airline ID param: " + idParam);
        if (idParam == null || idParam.trim().isEmpty()) {
            request.setAttribute("error", "ID hãng bay không được để trống!");
            listAirlines(request, response);
            return;
        }
        try {
            int airlineId = Integer.parseInt(idParam);
            Airline airline = airlineDAO.getById(airlineId);
            if (airline == null) {
                request.setAttribute("error", "Không tìm thấy hãng bay với ID: " + airlineId);
                listAirlines(request, response);
                return;
            }

            String name = request.getParameter("name");
            String code = request.getParameter("code");
            String description = request.getParameter("description");
            String services = request.getParameter("services");

            // Validate input
            if (name == null || name.trim().isEmpty() || code == null || code.trim().isEmpty()) {
                request.setAttribute("error", "Tên hãng bay và mã hãng bay không được để trống!");
                request.setAttribute("airline", airline);
                request.getRequestDispatcher("/WEB-INF/admin/editAirline.jsp").forward(request, response);
                return;
            }

            airline.setName(name);
            airline.setCode(code);
            airline.setDescription(description);
            airline.setServices(services);

            boolean result = airlineDAO.updateAirline(airline);
            if (result) {
                request.setAttribute("message", "Cập nhật hãng bay thành công!");
                listAirlines(request, response);
            } else {
                request.setAttribute("error", "Không thể cập nhật hãng bay! Mã hãng bay có thể đã tồn tại hoặc có lỗi dữ liệu.");
                request.setAttribute("airline", airline);
                request.getRequestDispatcher("/WEB-INF/admin/editAirline.jsp").forward(request, response);
            }
        } catch (NumberFormatException e) {
            LOGGER.log(Level.WARNING, "Invalid airline ID for update: " + idParam, e);
            request.setAttribute("error", "ID hãng bay không hợp lệ!");
            listAirlines(request, response);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "AirlineAdmin Servlet for managing airlines";
    }
}