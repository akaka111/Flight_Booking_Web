package controller.admin;

import DAO.Admin.AirportDAO;
import DAO.Admin.RouteDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import jakarta.servlet.RequestDispatcher;
import java.io.IOException;
import model.Airport;
import utils.ProvincesApiClient;

@WebServlet(name = "AirportAdmin", urlPatterns = {"/AirportAdmin"})
public class AirportAdmin extends HttpServlet {

    private void forward(HttpServletRequest req, HttpServletResponse resp, String path)
            throws ServletException, IOException {
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html; charset=UTF-8");
        RequestDispatcher rd = req.getRequestDispatcher(path);
        rd.forward(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String action = req.getParameter("action");
        if (action == null) action = "listAirports";

        AirportDAO dao = new AirportDAO();

        switch (action) {
            case "listAirports": {
                req.setAttribute("airports", dao.getAllAirports());
                forward(req, resp, "/WEB-INF/admin/manageAirports.jsp");
                break;
            }
            case "showAddForm": {
                req.setAttribute("cities", ProvincesApiClient.getVietnamCities());
                forward(req, resp, "/WEB-INF/admin/addAirport.jsp");
                break;
            }
            case "editAirport": {
                int id = Integer.parseInt(req.getParameter("id"));
                Airport a = dao.getById(id);
                req.setAttribute("airport", a);
                req.setAttribute("cities", ProvincesApiClient.getVietnamCities());
                forward(req, resp, "/WEB-INF/admin/editAirport.jsp");
                break;
            }
            default:
                resp.getWriter().println("Action không hợp lệ");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String action = req.getParameter("action");
        AirportDAO dao = new AirportDAO();

        try {
            if ("addAirport".equals(action)) {
                Airport a = readAirportFromRequest(req, false);
                String iata = a.getIataCode();

                if (iata == null || iata.length() != 3) {
                    req.setAttribute("error", "IATA phải gồm đúng 3 ký tự.");
                    req.setAttribute("cities", ProvincesApiClient.getVietnamCities());
                    forward(req, resp, "/WEB-INF/admin/addAirport.jsp");
                    return;
                }
                if (dao.existsIata(iata)) {
                    req.setAttribute("error", "IATA đã tồn tại.");
                    req.setAttribute("cities", ProvincesApiClient.getVietnamCities());
                    forward(req, resp, "/WEB-INF/admin/addAirport.jsp");
                    return;
                }

                dao.insert(a);
                req.setAttribute("msg", "✅ Đã thêm sân bay.");
                req.setAttribute("airports", dao.getAllAirports());
                forward(req, resp, "/WEB-INF/admin/manageAirports.jsp");

            } else if ("updateAirport".equals(action)) {
                Airport a = readAirportFromRequest(req, true);

                if (a.getIataCode() == null || a.getIataCode().length() != 3) {
                    req.setAttribute("error", "IATA phải gồm đúng 3 ký tự.");
                    req.setAttribute("airport", a);
                    req.setAttribute("cities", ProvincesApiClient.getVietnamCities());
                    forward(req, resp, "/WEB-INF/admin/editAirport.jsp");
                    return;
                }

                dao.update(a);
                req.setAttribute("msg", "✅ Đã cập nhật sân bay.");
                req.setAttribute("airports", dao.getAllAirports());
                forward(req, resp, "/WEB-INF/admin/manageAirports.jsp");

            } else if ("deleteAirport".equals(action)) {
                int id = Integer.parseInt(req.getParameter("id"));
                Airport target = dao.getById(id);
                if (target == null) {
                    req.setAttribute("error", "Không tìm thấy sân bay cần xoá.");
                    req.setAttribute("airports", dao.getAllAirports());
                    forward(req, resp, "/WEB-INF/admin/manageAirports.jsp");
                    return;
                }
                try {
                    int using = new RouteDAO().countUsingAirportId(id);
                    if (using > 0) {
                        // Gợi ý 2 lựa chọn: sang Quản lý tuyến bay, hoặc chuyển Inactive sân bay
                        String iata = (target.getIataCode() != null) ? target.getIataCode() : ("ID=" + id);
                        String base = req.getContextPath();
                        String linkRoutes   = base + "/RouteAdmin";
                        String linkInactive = base + "/AirportAdmin?action=editAirport&id=" + id;

                        String html =
                                "❌ Sân bay <b>" + iata + "</b> đang được sử dụng bởi <b>" + using + "</b> tuyến bay."
                              + "<br/>Bạn có thể xoá/điều chỉnh các tuyến bay trước, "
                              + "hoặc <i>chuyển trạng thái sân bay thành Inactive</i>."
                              + "<br/><div style='margin-top:8px'>"
                              + "<a href='" + linkRoutes   + "' class='btn btn-sm btn-danger' style='margin-right:8px'>Quản lý tuyến bay</a>"
                              + "<a href='" + linkInactive + "' class='btn btn-sm btn-secondary'>Chuyển thành Inactive</a>"
                              + "</div>";

                        // Chú ý: trong JSP, hiển thị error bằng <c:out escapeXml="false"> để render HTML
                        req.setAttribute("error", html);
                    } else {
                        dao.delete(id);
                        req.setAttribute("msg", "🗑️ Đã xoá sân bay.");
                    }
                } catch (Exception e) {
                    req.setAttribute("error", "Không thể xoá sân bay: " + e.getMessage());
                }
                req.setAttribute("airports", dao.getAllAirports());
                forward(req, resp, "/WEB-INF/admin/manageAirports.jsp");

            } else {
                resp.getWriter().println("Action không hợp lệ");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            req.setAttribute("error", "Lỗi: " + ex.getMessage());
            req.setAttribute("airports", dao.getAllAirports());
            forward(req, resp, "/WEB-INF/admin/manageAirports.jsp");
        }
    }

    private Airport readAirportFromRequest(HttpServletRequest req, boolean hasId) {
        Airport a = new Airport();
        if (hasId) {
            a.setAirportId(Integer.parseInt(req.getParameter("id")));
        }

        String iata = param(req, "iata");
        if (iata != null) iata = iata.trim().toUpperCase();
        a.setIataCode(iata);

        String icao = param(req, "icao");
        if (icao != null) icao = icao.trim().toUpperCase();
        a.setIcaoCode(icao);

        a.setName(param(req, "name"));
        a.setCity(param(req, "city"));     // từ dropdown API
        a.setCountry(param(req, "country"));
        a.setTimezone(param(req, "timezone"));
        a.setActive(req.getParameter("active") != null);
        return a;
    }

    private String param(HttpServletRequest req, String name) {
        String v = req.getParameter(name);
        return (v == null || v.isEmpty()) ? null : v;
        // có thể trim() nếu bạn muốn loại bỏ khoảng trắng đầu/cuối
    }
}
