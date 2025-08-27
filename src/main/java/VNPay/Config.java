/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package VNPay;

import jakarta.servlet.http.HttpServletRequest;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 *
 * @author $ LienXuanThinh - CE182117
 */
public class Config {

    public static String vnp_Version = "2.1.0";
    public static String vnp_Command = "pay";
    public static String vnp_TmnCode = "EWQ0TZFM";
    public static String vnp_HashSecret = "1HA8XR4J3IBVO7JJFAF8ZX0TXC5Y5DW6";
    public static String vnp_PayUrl = "https://sandbox.vnpayment.vn/paymentv2/vpcpay.html";
    public static String vnp_ReturnUrl = "http://localhost:8085/SWP391_Group2/vnpay_return";
    public static String vnp_ApiUrl = "https://sandbox.vnpayment.vn/merchant_webapi/api/transaction";

    public static String hashAllFields(Map<String, String> fields) {
        try {
            List<String> fieldNames = new ArrayList<>(fields.keySet());
            Collections.sort(fieldNames);
            StringBuilder sb = new StringBuilder();
            for (String name : fieldNames) {
                String value = fields.get(name);
                if (value != null && !value.isEmpty()) {
                    sb.append(name).append('=').append(URLEncoder.encode(value, "UTF-8"));
                    sb.append('&');
                }
            }
            sb.deleteCharAt(sb.length() - 1); // remove last &
            return hmacSHA512(vnp_HashSecret, sb.toString());
        } catch (Exception e) {
            return "";
        }
    }

    public static String hmacSHA512(String key, String data) {
        try {
            byte[] hmacKeyBytes = key.getBytes("UTF-8");
            javax.crypto.Mac mac = javax.crypto.Mac.getInstance("HmacSHA512");
            mac.init(new javax.crypto.spec.SecretKeySpec(hmacKeyBytes, "HmacSHA512"));
            byte[] result = mac.doFinal(data.getBytes("UTF-8"));
            StringBuilder sb = new StringBuilder(2 * result.length);
            for (byte b : result) {
                sb.append(String.format("%02x", b & 0xff));
            }
            return sb.toString();
        } catch (Exception ex) {
            return "";
        }
    }

    // Thêm hàm này vào trong lớp Config.java của bạn
    public static String getRandomNumber(int len) {
        Random rnd = new Random();
        String chars = "0123456789";
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            sb.append(chars.charAt(rnd.nextInt(chars.length())));
        }
        return sb.toString();
    }

    // Thêm cả hàm này vào trong lớp Config.java của bạn
    public static String getIpAddress(HttpServletRequest request) {
        String ipAddress = request.getHeader("X-FORWARDED-FOR");
        if (ipAddress == null) {
            ipAddress = request.getRemoteAddr();
        }
        return ipAddress;
    }
}
