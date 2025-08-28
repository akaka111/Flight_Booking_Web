package controller.staff;

import DAO.staff.messageDAO1;
import com.google.gson.Gson;
import jakarta.servlet.RequestDispatcher;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Account;
import model.Message;

/**
 *
 * @author ADMIN
 */
@WebServlet(name = "livechatFunc1", urlPatterns = {"/livechatFunc1"})
public class livechat1 extends HttpServlet {

    private messageDAO1 dao = new messageDAO1();
    Message msg = new Message();

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
            out.println("<title>Servlet livechat</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet livechat at " + request.getContextPath() + "</h1>");
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
        String selectedChat = request.getParameter("selectedChat");
        String selectedGuest = request.getParameter("selectedGuest");
        String selectedUser = request.getParameter("selectedUser");
        String action = request.getParameter("action");
        String guestLabel = request.getParameter("guestLabel");
        List<Message> messages = new ArrayList<>();

        try {
            HttpSession session = request.getSession();
            Integer staffId = (Integer) session.getAttribute("userId");
//            if (userId == null) {
//                response.sendRedirect(request.getContextPath() + "/login");
//                return;
//            }
            // --- Lấy danh sách guest đang chat ---
            List<String> guests = dao.getActiveGuests();
            // --- Lấy danh sách user ---
            List<Account> users = dao.getAllUsernames();

            request.setAttribute("chatGuests", guests);
            request.setAttribute("chatUsers", users);

            // --- Action: checkUnread ---
            if ("checkUnread".equals(action)) {
                // Guest
                Map<String, Integer> guestUnread = new HashMap<>();
                for (String g : guests) {
                    int c = dao.countUnreadGuestMessages(g);
                    if (c > 0) {
                        guestUnread.put(g, c);
                    }
                }
                // User
                Map<Integer, Integer> userUnread = new HashMap<>();
                for (Account u : users) {
                    int c = dao.countUnreadUserMessages(u.getUserId());
                    if (c > 0) {
                        userUnread.put(u.getUserId(), c);
                    }
                }
                Map<String, Object> result = new HashMap<>();
                result.put("guest", guestUnread);
                result.put("user", userUnread);
                response.setContentType("application/json");
                new Gson().toJson(result, response.getWriter());
                return;
            }

            // --- Action: mark read ---    
            if ("markGuestRead".equals(action)) {
                String guest = request.getParameter("guestLabel");
                dao.markGuestMessagesRead(guest);
                response.setStatus(200);
                return;
            }

            if ("markUserRead".equals(action)) {
                int userId = Integer.parseInt(request.getParameter("userId"));
                dao.markUserMessagesRead(userId);
                response.setStatus(200);
                return;
            }

            // --- Xác định selected chat ---
            if (selectedGuest != null && !selectedGuest.isEmpty()) {
                selectedChat = "GUEST:" + selectedGuest;
            } else if (selectedUser != null && !selectedUser.isEmpty()) {
                selectedChat = "USER:" + selectedUser;
            } else if (!guests.isEmpty()) {
                selectedGuest = guests.get(0);
                selectedChat = "GUEST:" + selectedGuest;
            } else if (!users.isEmpty()) {
                selectedUser = String.valueOf(users.get(0).getUserId());
                selectedChat = "USER:" + selectedUser;
            } else {
                selectedChat = ""; // fallback
            }
            request.setAttribute("selectedChat", selectedChat);

            // --- Lấy messages ---
            if ("getMessages".equals(action)) {
                if (selectedChat != null) {
                    if (selectedChat.startsWith("GUEST:")) {
                        String guest = selectedChat.substring(6);
                        messages = dao.LiveChatGuest(guest, (Integer) session.getAttribute("userId"));
                    } else if (selectedChat.startsWith("USER:")) {
                        int userId = Integer.parseInt(selectedChat.substring(5));
                        messages = dao.LiveChatUser(userId, (Integer) session.getAttribute("userId"));
                    }
                    // fix senderName/guest_label null
                    for (Message m : messages) {
                        if ("staff".equals(m.getSenderType()) && (m.getSenderName() == null || m.getSenderName().isEmpty())) {
                            m.setSenderName("Staff");
                        }
                        if ("guest".equals(m.getSenderType()) && (m.getGuest_label() == null || m.getGuest_label().isEmpty())) {
                            m.setGuest_label("Guest");
                        }
                    }
                }
                response.setContentType("application/json");
                new Gson().toJson(messages, response.getWriter());
                return;
            }
            request.setAttribute("messages", messages);
            request.getRequestDispatcher("/WEB-INF/staff/livechat_1.jsp").forward(request, response);

            System.out.println("StaffId: " + staffId + ", Guests: " + guests + ", Users: " + users + ", Selected: " + selectedChat + ", Messages: " + messages.size());

            // forward ra JSP
        } catch (SQLException ex) {
            Logger.getLogger(livechat1.class.getName()).log(Level.SEVERE, null, ex);
            response.getWriter().write("<p>Error loading chat: " + ex.getMessage() + "</p>");
        }
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
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession(false);
        int staffId = (int) session.getAttribute("userId");
        String selectedChat = request.getParameter("selectedChat");
        String content = request.getParameter("content");
        List<Message> messages = new ArrayList<>();

        if (selectedChat == null || content == null || content.trim().isEmpty()) {
            response.sendRedirect("livechatFunc1?selectedChat=" + selectedChat);
            return;
        }

        Message msg = new Message();
        msg.setContent(content);
        msg.setSenderId(staffId);
        msg.setSentTime(new java.sql.Timestamp(System.currentTimeMillis()));
        msg.setIsRead(false);

        try {
            if (selectedChat.toUpperCase().startsWith("GUEST:")) {
                String guestLabel = selectedChat.substring(6);
                msg.setGuest_label(guestLabel);
            } else if (selectedChat.toUpperCase().startsWith("USER:")) {
                int userId = Integer.parseInt(selectedChat.substring(5));
                msg.setRecipientId(userId);
                msg.setGuest_label(null);
            }
            dao.insertLiveChat(msg);
            // đảm bảo JSON trả về có senderName/guest_label để JS append
            if ("staff".equals(msg.getSenderType()) && (msg.getSenderName() == null || msg.getSenderName().isEmpty())) {
                msg.setSenderName("Staff");
            }
            if ("guest".equals(msg.getSenderType()) && (msg.getGuest_label() == null || msg.getGuest_label().isEmpty())) {
                msg.setGuest_label("Guest");
            }

            // Trả về JSON ngay lập tức
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            new Gson().toJson(msg, response.getWriter());

        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to send message");
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fol
}
