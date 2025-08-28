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
@WebServlet(name = "livechatFunc11", urlPatterns = {"/livechatFunc11"})
public class livechat11 extends HttpServlet {

    private messageDAO1 dao = new messageDAO1();
    private Gson gson = new Gson();

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
        HttpSession session = request.getSession();
        Integer staffId = (Integer) session.getAttribute("userId");
        if (staffId == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        String action = request.getParameter("action");
        try {
            if (action != null && !action.isEmpty()) {
                response.setContentType("application/json;charset=UTF-8");
                handleJsonAction(action, request, response, staffId);
            } else {
                // Load JSP
                List<String> guests = dao.getActiveGuests();
                List<Account> users = dao.getAllUsernames();

                String selectedGuest = request.getParameter("selectedGuest");
                String selectedUserId = request.getParameter("selectedUser");
                String selectedUserName = null;
                String selectedChat = null;

                if (selectedGuest != null && !selectedGuest.isEmpty()) {
                    selectedChat = "GUEST:" + selectedGuest;
                } else if (selectedUserId != null && !selectedUserId.isEmpty()) {
                    selectedChat = "USER:" + selectedUserId;
                    selectedUserName = users.stream()
                            .filter(u -> Integer.toString(u.getUserId()).equals(selectedUserId))
                            .map(Account::getUsername)
                            .findFirst().orElse(null);
                }

                List<Message> messages = (selectedChat != null) ? fetchMessagesByChat(selectedChat, staffId) : List.of();

                request.setAttribute("chatGuests", guests);
                request.setAttribute("chatUsers", users);
                request.setAttribute("selectedGuest", selectedGuest);
                request.setAttribute("selectedUser", selectedUserId);
                request.setAttribute("selectedChat", selectedChat);
                request.setAttribute("messages", messages);

                request.getRequestDispatcher("/WEB-INF/staff/livechat_1_1.jsp")
                        .forward(request, response);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            if (!response.isCommitted()) {
                response.getWriter().write("{\"error\":\"" + e.getMessage() + "\"}");
            }
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
        response.setContentType("application/json;charset=UTF-8");

        HttpSession session = request.getSession();
        Integer staffId = (Integer) session.getAttribute("userId");
        if (staffId == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String content = request.getParameter("content");
        String selectedChat = request.getParameter("selectedChat");

        if (content != null && !content.trim().isEmpty() && selectedChat != null) {
            Message msg = new Message();
            msg.setSenderId(staffId);
            msg.setContent(content);
            msg.setSenderType("staff");      // thêm
            msg.setSenderName("Staff");
            if (selectedChat.startsWith("GUEST:")) {
                msg.setGuest_label(selectedChat.substring(6));
            } else if (selectedChat.startsWith("USER:")) {
                msg.setRecipientId(Integer.parseInt(selectedChat.substring(5)));
            }

            boolean success = dao.insertLiveChat(msg);
            response.getWriter().write(gson.toJson(Map.of("success", success, "message", msg)));
        }
    }

    // =========================
    private void handleJsonAction(String action, HttpServletRequest request,
            HttpServletResponse response, Integer staffId) throws IOException, SQLException {

        switch (action) {
            case "getActiveChats" -> {
                Map<String, Object> chats = Map.of(
                        "guests", dao.getActiveGuests(),
                        "users", dao.getAllUsernames()
                );
                response.getWriter().write(gson.toJson(chats));
            }
            case "getMessages" -> {
                String selectedChat = request.getParameter("selectedChat");
                String afterParam = request.getParameter("after");
                long after = 0;
                if (afterParam != null && !afterParam.isEmpty()) {
                    try {
                        after = Long.parseLong(afterParam);
                    } catch (NumberFormatException e) {
                        after = 0;
                    }
                }

                List<Message> messages = fetchMessagesByChat(selectedChat, staffId);

                // Lọc theo sentTime
                List<Message> newMessages = new ArrayList<>();
                for (Message m : messages) {
                    if (m.getSentTime() != null && m.getSentTime().getTime() > after) {
                        newMessages.add(m);
                    }
                }

                response.getWriter().write(gson.toJson(newMessages));
            }

            case "checkUnread" -> {
                Map<String, Integer> unreadMap = new HashMap<>();
                dao.getActiveGuests().forEach(g -> {
                    int c = dao.countUnreadGuestMessages(g);
                    if (c > 0) {
                        unreadMap.put("GUEST:" + g, c);
                    }
                });
                dao.getAllUsernames().forEach(u -> {
                    int c = dao.countUnreadUserMessages(u.getUserId());
                    if (c > 0) {
                        unreadMap.put("USER:" + u.getUserId(), c);
                    }
                });
                response.getWriter().write(gson.toJson(unreadMap));
            }
            case "markRead" -> {
                String selectedChat = request.getParameter("selectedChat");
                if (selectedChat.startsWith("GUEST:")) {
                    dao.markGuestMessagesRead(selectedChat.substring(6));
                } else if (selectedChat.startsWith("USER:")) {
                    dao.markUserMessagesRead(Integer.parseInt(selectedChat.substring(5)));
                }
                response.getWriter().write("{\"status\":\"ok\"}");
            }
            case "assign" -> {
                String selectedChat = request.getParameter("selectedChat");
                boolean success = false;
                if (selectedChat.startsWith("GUEST:")) {
                    String guestLabel = selectedChat.substring(6);
                    success = dao.assignConversationToStaff(staffId, null, guestLabel);
                } else if (selectedChat.startsWith("USER:")) {
                    int userId = Integer.parseInt(selectedChat.substring(5));
                    success = dao.assignConversationToStaff(staffId, userId, null);
                }
                response.getWriter().write("{\"status\":\"" + (success ? "ok" : "fail") + "\"}");
            }
        }
    }

    private List<Message> fetchMessagesByChat(String selectedChat, Integer staffId) throws SQLException {
        List<Message> messages;
        if (selectedChat.startsWith("GUEST:")) {
            String guest = selectedChat.substring(6);
            messages = dao.LiveChatGuest(guest, staffId);
            for (Message m : messages) {
                if (m.getSenderId() != null && m.getSenderId().equals(staffId)) {
                    m.setSenderType("staff");
                    m.setSenderName("Staff");
                } else {
                    m.setSenderType("guest");
                    if (m.getGuest_label() == null) {
                        m.setGuest_label(guest);
                    }
                }
            }
        } else if (selectedChat.startsWith("USER:")) {
            int userId = Integer.parseInt(selectedChat.substring(5));
            messages = dao.LiveChatUser(userId, staffId);
            for (Message m : messages) {
                if (m.getSenderId() != null && m.getSenderId().equals(staffId)) {
                    m.setSenderType("staff");
                    m.setSenderName("Staff");
                } else {
                    m.setSenderType("user");
                    if (m.getSenderName() == null) {
                        for (Account u : dao.getAllUsernames()) {
                            if (u.getUserId() == m.getSenderId()) {
                                m.setSenderName(u.getUsername());
                                break;
                            }
                        }
                    }
                }
            }
        } else {
            messages = List.of();
        }
        return messages;
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
