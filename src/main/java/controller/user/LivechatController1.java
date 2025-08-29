package controller.user;

import DAO.user.SupportContact;
import com.google.gson.Gson;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Message;

@MultipartConfig
@WebServlet(name = "LivechatController1", urlPatterns = {"/LivechatController1"})
public class LivechatController1 extends HttpServlet {

    private SupportContact dao = new SupportContact();
    private Gson gson = new Gson();

    // Lấy lịch sử chat
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        long afterId = 0;
        String afterIdParam = request.getParameter("afterId");
        if (afterIdParam != null) {
            try {
                afterId = Long.parseLong(afterIdParam);
            } catch (NumberFormatException e) {
                afterId = 0;
            }
        }
        response.setContentType("application/json;charset=UTF-8");
        HttpSession session = request.getSession(false);

        String type = request.getParameter("type"); // "user" | "guest"
        String guestLabel = request.getParameter("guestLabel");

        Integer userId = null;
        if ("user".equalsIgnoreCase(type) && session != null) {
            userId = (Integer) session.getAttribute("userId");
        }

        List<Message> messages;
        try {
            messages = dao.getMessages(guestLabel, userId, afterId);
            System.out.println("DEBUG getMessages: guestLabel=" + guestLabel + ", userId=" + userId + ", afterId=" + afterId);
            for (Message m : messages) {
                if ("guest".equalsIgnoreCase(m.getSenderType())) {
                    m.setSenderName("Guest-" + m.getGuest_label());
                } else if ("user".equalsIgnoreCase(m.getSenderType())) {
                    if (m.getSenderName() == null) {
                        m.setSenderName(dao.getUsernameById(m.getSenderId()));
                    }
                } else if ("staff".equalsIgnoreCase(m.getSenderType())) {
                    m.setSenderName("Staff");
                }
                System.out.println("id=" + m.getId() + ", senderId=" + m.getSenderId() + ", guestLabel=" + m.getGuest_label() + ", content=" + m.getContent());
            }
            System.out.println("=== DEBUG MESSAGES ===");
            for (Message m : messages) {
                System.out.println("id=" + m.getId() + ", senderType=" + m.getSenderType() + ", senderName=" + m.getSenderName() + ", content=" + m.getContent());
            }
            response.getWriter().write(gson.toJson(messages));
        } catch (SQLException e) {
            e.printStackTrace();
            response.getWriter().write("[]");
        }
    }

    // Gửi tin nhắn
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        getServletContext().log("Content-Type: " + request.getContentType());
        request.getParameterMap().forEach((k, v) -> System.out.println(k + " => " + Arrays.toString(v)));

        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();

        String type = request.getParameter("type"); // "user" | "guest" | "staffJoin"
        String guestLabel = request.getParameter("guestLabel");
        String content = request.getParameter("content");
        HttpSession session = request.getSession(false);
        Integer userId = (Integer) session.getAttribute("userId");

        getServletContext().log("=== Debug Livechat POST ===");
        getServletContext().log("type=" + type);
        getServletContext().log("guestLabel=" + guestLabel);
        getServletContext().log("content=" + content);

        String action = request.getParameter("action");
        if ("delete".equalsIgnoreCase(action) && guestLabel != null) {
            try {
                dao.deleteGuestMessages(guestLabel);
            } catch (SQLException ex) {
                Logger.getLogger(LivechatController1.class.getName()).log(Level.SEVERE, null, ex);
            }
            return;
        }

        try {
            if ("staffJoin".equalsIgnoreCase(type)) {
                String staffName = request.getParameter("staffName");
                Message msg = new Message();
                msg.setContent(staffName + " đã tham gia chat.");
                msg.setSenderType("system");
                dao.insertMessage(msg);
                out.write("{\"success\":true}");
                return;
            }

            if (content == null || content.trim().isEmpty()) {
                out.write("{\"success\":false}");
                return;
            }
            Message msg = new Message();
            msg.setContent(content);
            msg.setSentTime(new java.sql.Timestamp(System.currentTimeMillis()));
            msg.setIsRead(false);
            msg.setRecipientId(null);
            getServletContext().log(" for userId=" + userId + " guestLabel=" + guestLabel);
            if ("user".equalsIgnoreCase(type)) {
                if (userId != null) {
                    msg.setSenderId(userId);
                    msg.setSenderType("user");
                }
            } else if ("guest".equalsIgnoreCase(type) && guestLabel != null) {
                msg.setGuest_label(guestLabel);
                msg.setSenderType("guest");
            }

            int newId = dao.insertMessage(msg);
            out.write("{\"success\":" + (newId != -1) + ", \"id\":" + newId + "}");
            if ("guest".equalsIgnoreCase(type) && guestLabel != null) {
                getServletContext().log("Tạo message guest với guestLabel=" + guestLabel);
            }
            if ("guest".equalsIgnoreCase(type) && guestLabel != null) {
                System.out.println("Tạo message guest với guestLabel=" + guestLabel);
            }
            getServletContext().log("=== Debug Livechat POST ===");
            getServletContext().log("type=" + type);
            getServletContext().log("guestLabel=" + guestLabel);
            getServletContext().log("content=" + content);
        } catch (Exception e) {
            e.printStackTrace();
            out.write("{\"success\":false}");
        }
    }

    @Override
    public String getServletInfo() {
        return "Livechat servlet cho user/guest chat với staff";
    }
}
