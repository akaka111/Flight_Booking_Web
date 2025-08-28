/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.math.BigDecimal;
import java.sql.Date;

/**
 *
 * @author $ bao
 */
public class Voucher {

    private int voucher_id;
    private String code;
    private int discount_percent;
    private BigDecimal discountMaxAmount;
    private BigDecimal minOrderValue;
    private Integer minPeople;
    private Date valid_from;
    private Date valid_to;
    private int usage_limit;
    private boolean isActive;

    public String getStatus() {
        java.util.Date today = new java.util.Date();

        if (!isActive) {
            return "Ngừng hoạt động";
        }

        if (valid_to != null && today.after(valid_to)) {
            return "Hết hạn";
        }

        if (usage_limit <= 0) {
            return "Hết lượt sử dụng";
        }

        return "Đang hoạt động";
    }

    public Voucher() {
    }

    public Voucher(int voucher_id, String code, int discountPercent, BigDecimal discountMaxAmount, BigDecimal minOrderValue, Integer minPeople, Date validFrom, Date validTo, int usageLimit, boolean isActive) {
        this.voucher_id = voucher_id;
        this.code = code;
        this.discount_percent = discountPercent;
        this.discountMaxAmount = discountMaxAmount;
        this.minOrderValue = minOrderValue;
        this.minPeople = minPeople;
        this.valid_from = validFrom;
        this.valid_to = validTo;
        this.usage_limit = usageLimit;
        this.isActive = isActive;
    }

    public int getVoucher_id() {
        return voucher_id;
    }

    public void setVoucher_id(int voucher_id) {
        this.voucher_id = voucher_id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getDiscount_percent() {
        return discount_percent;
    }

    public void setDiscount_percent(int discount_percent) {
        this.discount_percent = discount_percent;
    }

    public BigDecimal getDiscountMaxAmount() {
        return discountMaxAmount;
    }

    public void setDiscountMaxAmount(BigDecimal discountMaxAmount) {
        this.discountMaxAmount = discountMaxAmount;
    }

    public BigDecimal getMinOrderValue() {
        return minOrderValue;
    }

    public void setMinOrderValue(BigDecimal minOrderValue) {
        this.minOrderValue = minOrderValue;
    }

    public Integer getMinPeople() {
        return minPeople;
    }

    public void setMinPeople(Integer minPeople) {
        this.minPeople = minPeople;
    }

    public Date getValid_from() {
        return valid_from;
    }

    public void setValid_from(Date valid_from) {
        this.valid_from = valid_from;
    }

    public Date getValid_to() {
        return valid_to;
    }

    public void setValid_to(Date valid_to) {
        this.valid_to = valid_to;
    }

    public int getUsage_limit() {
        return usage_limit;
    }

    public void setUsage_limit(int usage_limit) {
        this.usage_limit = usage_limit;
    }

    public boolean isIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

}
