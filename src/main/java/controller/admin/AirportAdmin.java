package controller.admin;

import DAO.Admin.AirportDAO;
import DAO.Admin.RouteDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import jakarta.servlet.RequestDispatcher;
import java.io.IOException;
import java.util.List;
import model.Airport;

@WebServlet(name = "AirportAdmin", urlPatterns = {"/AirportAdmin"})
public class AirportAdmin extends HttpServlet {

    private void forward(HttpServletRequest req, HttpServletResponse resp, String path)
            throws ServletException, IOException {
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
                List<Airport> airports = dao.getAllAirports();
                req.setAttribute("airports", airports);
                forward(req, resp, "/WEB-INF/admin/manageAirports.jsp");
                break;
            }
            case "showAddForm": {
                forward(req, resp, "/WEB-INF/admin/addAirport.jsp");
                break;
            }
            case "editAirport": {
                int id = Integer.parseInt(req.getParameter("id"));
                Airport a = dao.getById(id);
                req.setAttribute("airport", a);
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
                // validate IATA
                String iata = a.getIataCode();
                if (iata == null || iata.length() != 3) {
                    req.setAttribute("error", "IATA phải gồm đúng 3 ký tự.");
                    forward(req, resp, "/WEB-INF/admin/addAirport.jsp");
                    return;
                }
                if (dao.existsIata(iata)) {
                    req.setAttribute("error", "IATA đã tồn tại.");
                    forward(req, resp, "/WEB-INF/admin/addAirport.jsp");
                    return;
                }
                dao.insert(a);
                req.setAttribute("msg", "Đã thêm sân bay.");
                req.setAttribute("airports", dao.getAllAirports());
                forward(req, resp, "/WEB-INF/admin/manageAirports.jsp");

            } else if ("updateAirport".equals(action)) {
                Airport a = readAirportFromRequest(req, true);
                if (a.getIataCode() == null || a.getIataCode().length() != 3) {
                    req.setAttribute("error", "IATA phải gồm đúng 3 ký tự.");
                    req.setAttribute("airport", a);
                    forward(req, resp, "/WEB-INF/admin/editAirport.jsp");
                    return;
                }
                dao.update(a);
                req.setAttribute("msg", "Đã cập nhật sân bay.");
                req.setAttribute("airports", dao.getAllAirports());
                forward(req, resp, "/WEB-INF/admin/manageAirports.jsp");

            } else if ("deleteAirport".equals(action)) {
                int id = Integer.parseInt(req.getParameter("id"));

                // Lấy airport để hiển thị thông tin đẹp hơn nếu cần
                Airport target = dao.getById(id);
                if (target == null) {
                    req.setAttribute("error", "Không tìm thấy sân bay cần xoá.");
                    req.setAttribute("airports", dao.getAllAirports());
                    forward(req, resp, "/WEB-INF/admin/manageAirports.jsp");
                    return;
                }

                try {
                    // Kiểm tra sân bay có đang được sử dụng trong Route?
                    RouteDAO routeDAO = new RouteDAO();
                    int using = routeDAO.countUsingAirportId(id);

                    if (using > 0) {
                        String iata = target.getIataCode() != null ? target.getIataCode() : ("ID=" + id);
                        req.setAttribute("error",
                            "❌ Sân bay " + iata + " đang được sử dụng bởi " + using +
                            " tuyến bay. Vui lòng qua mục Tuyến bay để xoá hoặc đổi sân bay khác trước.");
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
        a.setCity(param(req, "city"));
        a.setCountry(param(req, "country"));
        a.setTimezone(param(req, "timezone"));
        a.setActive(req.getParameter("active") != null);
        return a;
    }

    private String param(HttpServletRequest req, String name) {
        String v = req.getParameter(name);
        return (v == null || v.isEmpty()) ? null : v;
    }
}
