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
        // Nếu UI của bạn vẫn còn link GET để xoá, mở comment dưới (nhưng lưu ý rủi ro CSRF)
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

                // ⚠️ Tuỳ chọn: cho phép GET xoá (không khuyến nghị nếu không có CSRF token)
                // case "deleteRoute":
                //     deleteRoute(request, response);
                //     break;

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
        if (action == null) action = "listRoutes";

        try {
            switch (action) {
                case "addRoute":
                    addRoute(request, response);
                    break;

                case "updateRoute":
                    updateRoute(request, response);
                    break;

                case "deleteRoute":
                    deleteRoute(request, response);
                    break;

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

    /* ================== Actions ================== */

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
            throws ServletException, IOException {
        String originIata  = trimParam(request.getParameter("originIata"));
        String destIata    = trimParam(request.getParameter("destIata"));
        Integer distanceKm = parseInteger(request.getParameter("distanceKm"));
        Integer duration   = parseInteger(request.getParameter("durationMinutes"));
        boolean active     = request.getParameter("active") != null;

        try {
            if (originIata == null || destIata == null) {
                request.setAttribute("error", "Vui lòng chọn đầy đủ điểm đi/điểm đến.");
            } else if (originIata.equalsIgnoreCase(destIata)) {
                request.setAttribute("error", "Điểm đi và điểm đến phải khác nhau.");
            } else if (dao.existsRoute(originIata, destIata)) {
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
            throws ServletException, IOException {
        int id            = Integer.parseInt(request.getParameter("id"));
        String originIata = trimParam(request.getParameter("originIata"));
        String destIata   = trimParam(request.getParameter("destIata"));
        Integer distanceKm = parseInteger(request.getParameter("distanceKm"));
        Integer duration   = parseInteger(request.getParameter("durationMinutes"));
        boolean active     = request.getParameter("active") != null;

        try {
            if (originIata == null || destIata == null) {
                request.setAttribute("error", "Vui lòng chọn đầy đủ điểm đi/điểm đến.");
            } else if (originIata.equalsIgnoreCase(destIata)) {
                request.setAttribute("error", "Điểm đi và điểm đến phải khác nhau.");
            } else if (dao.existsRouteForUpdate(id, originIata, destIata)) {
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
        boolean ok = dao.deleteRoute(id);
        if (ok) {
            request.setAttribute("msg", "🗑️ Đã xoá tuyến bay.");
        } else {
            request.setAttribute("error",
                "❌ Không thể xoá tuyến bay. Có thể tuyến bay đang được tham chiếu (ví dụ: Flight) hoặc đã bị xoá trước đó.");
        }
        listRoutes(request, response);
    }

    /* ================== Utils ================== */

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
