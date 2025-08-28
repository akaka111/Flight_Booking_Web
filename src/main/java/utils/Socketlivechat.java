package utils;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.OnClose;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

/**
 * WebSocket cho Live Chat endpoint: ws://localhost:8080/yourApp/chat/{username}
 */
@ServerEndpoint("/chat/{username}")
public class Socketlivechat {

    // Lưu danh sách session theo username (guest/user/staff)
    private static final Map<String, Session> clients = new ConcurrentHashMap<>();

    @OnOpen
    public void onOpen(Session session, @PathParam("username") String username) {
        clients.put(username, session);
        System.out.println("Kết nối mới: " + username + " - " + session.getId());
        broadcast("📢 " + username + " đã tham gia phòng chat!");
    }

    @OnMessage
    public void onMessage(String message, Session session, @PathParam("username") String username) {
        System.out.println("Tin nhắn từ " + username + ": " + message);
        // Broadcast cho tất cả (có thể tuỳ chỉnh chỉ gửi cho staff/guest cụ thể)
        broadcast(username + ": " + message);
    }

    @OnClose
    public void onClose(Session session, @PathParam("username") String username) {
        clients.remove(username);
        System.out.println("Đóng kết nối: " + username + " - " + session.getId());
        broadcast("❌ " + username + " đã rời khỏi phòng chat.");
    }

    // Hàm gửi tin nhắn cho tất cả client đang online
    private void broadcast(String message) {
        Set<Map.Entry<String, Session>> entries = clients.entrySet();
        for (Map.Entry<String, Session> entry : entries) {
            try {
                entry.getValue().getBasicRemote().sendText(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
