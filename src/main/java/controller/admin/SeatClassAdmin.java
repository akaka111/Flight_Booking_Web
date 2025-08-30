package controller.admin;

import DAO.Admin.SeatClassDAO;
import model.SeatClass;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "SeatClassAdmin", urlPatterns = {"/SeatClassAdmin"})
public class SeatClassAdmin extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) {
            action = "listSeatClasses";
        }
        switch (action) {
            case "listSeatClasses":
                List<SeatClass> seatClasses = new SeatClassDAO().getAllSeatClasses();
                request.setAttribute("seatClasses", seatClasses);
                request.getRequestDispatcher("/WEB-INF/admin/seatClassList.jsp").forward(request, response);
                break;
            case "showAddForm":
                request.getRequestDispatcher("/WEB-INF/admin/addEditSeatClass.jsp").forward(request, response);
                break;
            case "edit":
                int id = Integer.parseInt(request.getParameter("id"));
                SeatClassDAO dao = new SeatClassDAO();
                SeatClass seatClass = dao.getSeatClassById(id);
                request.setAttribute("seatClass", seatClass);
                request.getRequestDispatcher("/WEB-INF/admin/addEditSeatClass.jsp").forward(request, response);
                break;
            default:
                response.sendRedirect("SeatClassAdmin?error=Invalid action");
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        SeatClassDAO dao = new SeatClassDAO();
        try {
            if ("add".equals(action)) {
                SeatClass sc = new SeatClass();
                sc.setName(request.getParameter("name"));
                sc.setDescription(request.getParameter("description"));
                sc.setStatus(request.getParameter("status"));
                boolean success = dao.insertSeatClass(sc);
                response.sendRedirect("SeatClassAdmin?successcreate=" + (success ? "1" : "0"));
            } else if ("update".equals(action)) {
                int id = Integer.parseInt(request.getParameter("id"));
                SeatClass sc = new SeatClass();
                sc.setSeatClassID(id);
                sc.setName(request.getParameter("name"));
                sc.setDescription(request.getParameter("description"));
                sc.setStatus(request.getParameter("status"));
                boolean success = dao.updateSeatClass(sc);
                response.sendRedirect("SeatClassAdmin?successedit=" + (success ? "1" : "0"));
            } else if ("delete".equals(action)) {
                int id = Integer.parseInt(request.getParameter("id"));
                boolean success = dao.deleteSeatClass(id);
                response.sendRedirect("SeatClassAdmin?successdelete=" + (success ? "1" : "0"));
            } else {
                response.sendRedirect("SeatClassAdmin?error=Invalid action");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("SeatClassAdmin?error=" + e.getMessage());
        }
    }

    @Override
    public String getServletInfo() {
        return "SeatClassAdmin Servlet for managing seat classes";
    }
}