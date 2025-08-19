package controller.admin;

import DAO.Admin.RouteDAO;
import DAO.Admin.RouteDAO.AirportOption;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import model.Route;

/**
 * RouteAdmin: Controller quản lý tuyến bay
 * URL: /RouteAdmin
 */
@WebServlet(name = "RouteAdmin", urlPatterns = {"/RouteAdmin"})
public class RouteAdmin extends HttpServlet {

    private RouteDAO dao;

    @Override
    public void init() throws ServletException {
        dao = new RouteDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) action = "listRoutes";

        try {
            switch (action) {
                case "showAddForm":
                    showAddForm(request, response);
                    break;

                case "editRoute":
                    editRouteForm(request, response);
                    break;

                case "listRoutes":
                default:
                    listRoutes(request, response);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Đã xảy ra lỗi: " + e.getMessage());
            listRoutes(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");

        try {
            if ("addRoute".equals(action)) {
                addRoute(request, response);
            } else if ("updateRoute".equals(action)) {
                updateRoute(request, response);
            } else if ("deleteRoute".equals(action)) {
                deleteRoute(request, response);
            } else {
                request.setAttribute("error", "Action không hợp lệ.");
                listRoutes(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Đã xảy ra lỗi: " + e.getMessage());
            listRoutes(request, response);
        }
    }

    /* ===== Actions ===== */

    private void listRoutes(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Route> routes = dao.getAllRoutes();
        request.setAttribute("routes", routes);
        request.getRequestDispatcher("/WEB-INF/admin/manageRoutes.jsp").forward(request, response);
    }

    private void showAddForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<AirportOption> airports = dao.getAirportOptions();
        request.setAttribute("airports", airports);
        request.getRequestDispatcher("/WEB-INF/admin/addRoute.jsp").forward(request, response);
    }

    private void editRouteForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        Route r = dao.getRouteById(id);
        if (r == null) {
            request.setAttribute("error", "Không tìm thấy tuyến bay.");
            listRoutes(request, response);
            return;
        }
        List<AirportOption> airports = dao.getAirportOptions();
        request.setAttribute("route", r);
        request.setAttribute("airports", airports);
        request.getRequestDispatcher("/WEB-INF/admin/editRoute.jsp").forward(request, response);
    }

    private void addRoute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        String originIata = trimParam(request.getParameter("originIata"));
        String destIata   = trimParam(request.getParameter("destIata"));
        Integer distanceKm = parseInteger(request.getParameter("distanceKm"));
        Integer duration   = parseInteger(request.getParameter("durationMinutes"));
        boolean active     = request.getParameter("active") != null;

        try {
            if (dao.existsRoute(originIata, destIata)) {
                request.setAttribute("error", "❌ Tuyến bay từ " + originIata + " đến " + destIata + " đã tồn tại!");
            } else {
                dao.insertRoute(originIata, destIata, distanceKm, duration, active);
                request.setAttribute("msg", "✅ Thêm tuyến bay thành công!");
            }
        } catch (SQLException ex) {
            request.setAttribute("error", "❌ Không thể thêm tuyến bay: " + ex.getMessage());
        }
        listRoutes(request, response);
    }

    private void updateRoute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        int id            = Integer.parseInt(request.getParameter("id"));
        String originIata = trimParam(request.getParameter("originIata"));
        String destIata   = trimParam(request.getParameter("destIata"));
        Integer distanceKm = parseInteger(request.getParameter("distanceKm"));
        Integer duration   = parseInteger(request.getParameter("durationMinutes"));
        boolean active     = request.getParameter("active") != null;

        try {
            // Nếu đã tồn tại tuyến cùng Origin/Dest nhưng khác id -> báo lỗi
            if (dao.existsRouteForUpdate(id, originIata, destIata)) {
                request.setAttribute("error", "❌ Tuyến bay từ " + originIata + " đến " + destIata + " đã tồn tại!");
            } else {
                dao.updateRoute(id, originIata, destIata, distanceKm, duration, active);
                request.setAttribute("msg", "✅ Cập nhật tuyến bay thành công!");
            }
        } catch (SQLException ex) {
            request.setAttribute("error", "❌ Không thể cập nhật: " + ex.getMessage());
        }
        listRoutes(request, response);
    }

    private void deleteRoute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        try {
            dao.deleteRoute(id);
            request.setAttribute("msg", "🗑️ Đã xoá tuyến bay.");
        } catch (SQLException ex) {
            String msg = ex.getMessage();
            request.setAttribute("error", "❌ Không thể xoá tuyến (có thể đang được dùng bởi Flight). Chi tiết: " + msg);
        }
        listRoutes(request, response);
    }

    /* ===== Utils ===== */

    private Integer parseInteger(String raw) {
        try {
            if (raw == null || raw.trim().isEmpty()) return null;
            return Integer.parseInt(raw.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private String trimParam(String s) {
        return s == null ? null : s.trim();
    }
}
