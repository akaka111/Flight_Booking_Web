package DAO.Admin;

import model.Seat;
import utils.DBContext;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author $ LienXuanThinh - CE182117
 */
public class SeatDAO extends DBContext {

    /**
     * Lấy danh sách tất cả các ghế của một chuyến bay cụ thể.
     *
     * @param flightId ID của chuyến bay.
     * @return Danh sách các đối tượng Ghế.
     */
    public List<Seat> getSeatsByFlightId(int flightId) {
        List<Seat> list = new ArrayList<>();
        String sql = "SELECT seat_id, flight_id, class_id, seat_number, is_booked FROM Seat WHERE flight_id = ?";

        // Sử dụng try-with-resources để tự động đóng Connection và PreparedStatement
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            // **LỖI ĐÃ SỬA**: Thiết lập tham số flight_id cho câu truy vấn
            ps.setInt(1, flightId);

            // Thực thi truy vấn và nhận kết quả
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Seat seat = new Seat(
                            rs.getInt("seat_id"),
                            rs.getInt("flight_id"),
                            rs.getInt("class_id"),
                            rs.getString("seat_number"),
                            rs.getBoolean("is_booked")
                    );
                    list.add(seat);
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy danh sách ghế theo flightId: " + e.getMessage());
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Cập nhật trạng thái đã đặt của một ghế.
     *
     * @param seatId ID của ghế cần cập nhật.
     * @param isBooked Trạng thái mới (true = đã đặt, false = chưa đặt).
     */
    public void updateSeatBooking(int seatId, boolean isBooked) {
        String sql = "UPDATE Seat SET is_booked = ? WHERE seat_id = ?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setBoolean(1, isBooked);
            ps.setInt(2, seatId);

            int rows = ps.executeUpdate();

            // DEBUG
            System.out.println("[DEBUG] updateSeatBooking(): seat_id = " + seatId + ", isBooked = " + isBooked + ", rowsAffected = " + rows);

        } catch (SQLException e) {
            System.err.println("Lỗi khi cập nhật trạng thái ghế: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public List<Seat> getSeatsByFlightAndClass(int flightId, int classId) {
        List<Seat> list = new ArrayList<>();
        // Câu SQL mới có thêm điều kiện WHERE cho class_id
        String sql = "SELECT seat_id, flight_id, class_id, seat_number, is_booked FROM Seat WHERE flight_id = ? AND class_id = ?";

        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, flightId);
            ps.setInt(2, classId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Seat seat = new Seat(
                            rs.getInt("seat_id"),
                            rs.getInt("flight_id"),
                            rs.getInt("class_id"),
                            rs.getString("seat_number"),
                            rs.getBoolean("is_booked")
                    );

                    System.out.println("  -> seat_id=" + seat.getSeatId() + ", seat_number=" + seat.getSeatNumber() + ", is_booked=" + seat.isIsBooked());

                    list.add(seat);
                    System.out.println("== DEBUG SeatDAO ==");
                    System.out.println("seat_id: " + rs.getInt("seat_id"));
                    System.out.println("seat_number: " + rs.getString("seat_number"));
                    System.out.println("is_booked (raw): " + rs.getBoolean("is_booked"));
                    System.out.println("Querying seats for flight_id=" + flightId + ", class_id=" + classId);

                }
            }
        } catch (Exception e) {
            System.err.println("Lỗi khi lấy danh sách ghế theo chuyến bay và loại ghế: " + e.getMessage());
            e.printStackTrace();
        }
        return list;
    }

    public int getClassId(String seatClassName) {
        String sql = "SELECT tc.class_id "
                + "FROM TicketClass tc "
                + "JOIN SeatClass sc ON tc.SeatClassID = sc.SeatClassID "
                + "WHERE sc.Name = ?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, seatClassName);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("class_id");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0; // Trả về 0 nếu không tìm thấy
    }

    public Seat getSeatById(int seatId) throws SQLException {
        Seat seat = null;
        String sql = "SELECT seat_id, seat_number, is_booked, class_id, flight_id FROM Seat WHERE seat_id = ?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, seatId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    seat = new Seat();
                    seat.setSeatId(rs.getInt("seat_id"));
                    seat.setSeatNumber(rs.getString("seat_number"));
                    seat.setIsBooked(rs.getBoolean("is_booked"));
                    seat.setClassId(rs.getInt("class_id"));
                    seat.setFlightId(rs.getInt("flight_id"));
                }
            }
        }
        return seat;
    }

}
