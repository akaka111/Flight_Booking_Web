package controller.staff;

import DAO.Admin.VoucherDAO;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Voucher;

@WebServlet(name = "voucherSERV", urlPatterns = {"/manageVouchers"})
public class voucherSERV extends HttpServlet {

    private final VoucherDAO dao = new VoucherDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        if ("edit".equals(action)) {
            int id = Integer.parseInt(request.getParameter("id"));
            Voucher v = dao.getVoucherById(id);
            request.setAttribute("voucher", v);
            request.getRequestDispatcher("/WEB-INF/staff/VoucherEDIT.jsp").forward(request, response);
            return;
        } else if ("create".equals(action)) {
            request.getRequestDispatcher("/WEB-INF/staff/VoucherADD.jsp").forward(request, response);
            return;
        }

        List<Voucher> list = dao.getAllVouchers();
        for (Voucher v : list) {
            dao.updateVoucherStatusIfExpired(v);
        }
        list = dao.getAllVouchers();
        request.setAttribute("list", list);
        request.getRequestDispatcher("/WEB-INF/staff/VoucherMange.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        switch (action) {
            case "create":
                createVoucher(request, response);
                break;
            case "update":
                updateVoucher(request, response);
                break;
            case "delete":
                deleteVoucher(request, response);
                break;
            default:
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Unknown action: " + action);
        }
    }

    private void createVoucher(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Voucher v = mapRequestToVoucher(request);
        dao.insertVoucher(v);
        response.sendRedirect("manageVouchers");
    }

    private void updateVoucher(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Voucher v = mapRequestToVoucher(request);
        v.setVoucher_id(Integer.parseInt(request.getParameter("id")));
        dao.updateVoucher(v);
        response.sendRedirect("manageVouchers");
    }

    private void deleteVoucher(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        dao.deleteVoucher(id);
        response.sendRedirect("manageVouchers");
    }

    // Map dữ liệu từ request sang object Voucher
    private Voucher mapRequestToVoucher(HttpServletRequest request) {
        Voucher v = new Voucher();
        v.setCode(request.getParameter("code"));
        v.setDiscount_percent(Integer.parseInt(request.getParameter("discount_percent")));

        String discountMax = request.getParameter("discount_max_amount");
        v.setDiscountMaxAmount(discountMax == null || discountMax.isEmpty() ? null : new BigDecimal(discountMax));

        String minOrder = request.getParameter("min_order_value");
        v.setMinOrderValue(minOrder == null || minOrder.isEmpty() ? null : new BigDecimal(minOrder));

        String minPeople = request.getParameter("min_people");
        v.setMinPeople(minPeople == null || minPeople.isEmpty() ? null : Integer.parseInt(minPeople));

        String fromDate = request.getParameter("valid_from");
        v.setValid_from(fromDate == null || fromDate.isEmpty() ? null : Date.valueOf(fromDate));

        String toDate = request.getParameter("valid_to");
        v.setValid_to(toDate == null || toDate.isEmpty() ? null : Date.valueOf(toDate));

        String usage = request.getParameter("usage_limit");
        v.setUsage_limit((usage == null || usage.isEmpty()) ? 0 : Integer.parseInt(usage));

        String activeParam = request.getParameter("is_active");
        v.setIsActive(activeParam != null && activeParam.equals("on"));

        return v;
    }
}
