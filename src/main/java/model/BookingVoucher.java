
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**

 * @author $ LienXuanThinh - CE182117
 */
public class BookingVoucher {

    private int id;
    private int bookingId;
    private int voucherId;
    int AccountId;

    public BookingVoucher() {
    }

    public BookingVoucher(int id, int bookingId, int voucherId, int AccountId) {
        this.id = id;
        this.bookingId = bookingId;
        this.voucherId = voucherId;
        this.AccountId = AccountId;
    }

    public int getAccountId() {
        return AccountId;
    }

    public void setAccountId(int AccountId) {
        this.AccountId = AccountId;
    }

    // Getter và Setter
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBookingId() {
        return bookingId;
    }

    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }

    public int getVoucherId() {
        return voucherId;
    }

    public void setVoucherId(int voucherId) {
        this.voucherId = voucherId;
    }
}
