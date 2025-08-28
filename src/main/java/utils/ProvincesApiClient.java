package utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;


public class ProvincesApiClient {

    private static final String API_URL_V2 = "https://provinces.open-api.vn/api/v2/p/";
    private static final Duration TIMEOUT = Duration.ofSeconds(5);
    private static final Duration CACHE_TTL = Duration.ofHours(6);

    private static final HttpClient HTTP = HttpClient.newBuilder()
            .connectTimeout(TIMEOUT)
            .build();
    private static final Gson GSON = new Gson();

    private static volatile List<String> cacheCities = null;
    private static volatile long cacheAt = 0L;

   
    private static final Map<String, List<String>> MERGE_DECORATION = new LinkedHashMap<>();
    static {
        // --- Miền núi phía Bắc & Trung du ---
        MERGE_DECORATION.put("Tuyên Quang", Arrays.asList("Hà Giang"));
        MERGE_DECORATION.put("Lào Cai", Arrays.asList("Yên Bái"));
        MERGE_DECORATION.put("Thái Nguyên", Arrays.asList("Bắc Kạn"));
        MERGE_DECORATION.put("Phú Thọ", Arrays.asList("Vĩnh Phúc", "Hòa Bình"));
        MERGE_DECORATION.put("Bắc Ninh", Arrays.asList("Bắc Giang"));
        MERGE_DECORATION.put("Hưng Yên", Arrays.asList("Thái Bình"));
        MERGE_DECORATION.put("Hải Phòng", Arrays.asList("Hải Dương"));
        MERGE_DECORATION.put("Ninh Bình", Arrays.asList("Hà Nam", "Nam Định"));
        MERGE_DECORATION.put("Quảng Trị", Arrays.asList("Quảng Bình"));

        // --- Trung Bộ & Tây Nguyên ---
        MERGE_DECORATION.put("Đà Nẵng", Arrays.asList("Quảng Nam"));
        MERGE_DECORATION.put("Quảng Ngãi", Arrays.asList("Kon Tum"));
        MERGE_DECORATION.put("Gia Lai", Arrays.asList("Bình Định"));
        MERGE_DECORATION.put("Khánh Hòa", Arrays.asList("Ninh Thuận"));
        MERGE_DECORATION.put("Lâm Đồng", Arrays.asList("Đắk Nông", "Bình Thuận"));
        MERGE_DECORATION.put("Đắk Lắk", Arrays.asList("Phú Yên"));

        // --- Đông Nam Bộ ---
        MERGE_DECORATION.put("Hồ Chí Minh", Arrays.asList("Bà Rịa - Vũng Tàu", "Bình Dương"));
        MERGE_DECORATION.put("Đồng Nai", Arrays.asList("Bình Phước"));
        MERGE_DECORATION.put("Tây Ninh", Arrays.asList("Long An"));

        // --- Đồng bằng sông Cửu Long ---
        MERGE_DECORATION.put("Cần Thơ", Arrays.asList("Sóc Trăng", "Hậu Giang"));
        MERGE_DECORATION.put("Vĩnh Long", Arrays.asList("Bến Tre", "Trà Vinh"));
        MERGE_DECORATION.put("Đồng Tháp", Arrays.asList("Tiền Giang"));
        MERGE_DECORATION.put("Cà Mau", Arrays.asList("Bạc Liêu"));
        MERGE_DECORATION.put("An Giang", Arrays.asList("Kiên Giang"));

        // 11 đơn vị giữ nguyên: Hà Nội, Huế, Lai Châu, Điện Biên, Sơn La, Lạng Sơn,
        // Quảng Ninh, Thanh Hóa, Nghệ An, Hà Tĩnh, Cao Bằng -> không cần khai báo.
    }

    /** Public API: danh sách tỉnh/thành đã sort + decorate (dùng cho dropdown). */
    public static List<String> getVietnamCities() {
        long now = System.currentTimeMillis();
        if (cacheCities != null && (now - cacheAt) < CACHE_TTL.toMillis()) {
            return cacheCities;
        }

        List<String> base;
        try {
            base = fetchFromApiV2();
            if (base == null || base.isEmpty()) {
                base = fallbackBase34();
            }
        } catch (Exception e) {
            base = fallbackBase34();
        }

        List<String> decorated = decorateMergedNames(base);
        cacheCities = decorated;
        cacheAt = now;
        return decorated;
    }

    /** Gọi API v2, parse JSON, chuẩn hoá tên (bỏ "Tỉnh"/"Thành phố"), distinct + sort. */
    private static List<String> fetchFromApiV2() throws IOException, InterruptedException {
        HttpRequest req = HttpRequest.newBuilder(URI.create(API_URL_V2))
                .timeout(TIMEOUT)
                .header("Accept", "application/json; charset=UTF-8")
                .GET()
                .build();

        HttpResponse<byte[]> resp = HTTP.send(req, HttpResponse.BodyHandlers.ofByteArray());
        if (resp.statusCode() != 200 || resp.body() == null) return Collections.emptyList();

        String json = new String(resp.body(), StandardCharsets.UTF_8);
        Type listType = new TypeToken<List<Map<String, Object>>>(){}.getType();
        List<Map<String, Object>> arr = GSON.fromJson(json, listType);
        if (arr == null) return Collections.emptyList();

        List<String> names = new ArrayList<>();
        for (Map<String, Object> it : arr) {
            Object name = it.get("name");
            if (name != null) {
                String s = String.valueOf(name).trim();
                s = s.replaceFirst("^Tỉnh\\s+", "")
                     .replaceFirst("^Thành phố\\s+", "");
                names.add(s);
            }
        }
        return normalizeAndSort(names);
    }

    /** Trang trí: "Tên Mới (Tên cũ 1, Tên cũ 2, ...)" nếu có cấu hình trong MERGE_DECORATION. */
    private static List<String> decorateMergedNames(List<String> input) {
        List<String> out = new ArrayList<>(input.size());
        for (String name : input) {
            List<String> olds = MERGE_DECORATION.get(name);
            if (olds != null && !olds.isEmpty()) {
                out.add(name + " (" + String.join(", ", olds) + ")");
            } else {
                out.add(name);
            }
        }
        return normalizeAndSort(out);
    }

    /** Chuẩn hoá, distinct, sort. */
    private static List<String> normalizeAndSort(List<String> input) {
        return input.stream()
                .filter(Objects::nonNull)
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .distinct()
                .sorted(String.CASE_INSENSITIVE_ORDER)
                .collect(Collectors.toList());
    }

    /**
     * Fallback tĩnh: 34 đơn vị (không ngoặc) — sẽ được decorate ở bước sau.
     * Tên đã chuẩn hoá bỏ "Tỉnh"/"Thành phố".
     */
    private static List<String> fallbackBase34() {
        return normalizeAndSort(Arrays.asList(
            // 23 đơn vị hình thành sau sắp xếp
            "Tuyên Quang", "Lào Cai", "Thái Nguyên", "Phú Thọ", "Bắc Ninh",
            "Hưng Yên", "Hải Phòng", "Ninh Bình", "Quảng Trị", "Đà Nẵng",
            "Quảng Ngãi", "Gia Lai", "Khánh Hòa", "Lâm Đồng", "Đắk Lắk",
            "Hồ Chí Minh", "Đồng Nai", "Tây Ninh", "Cần Thơ", "Vĩnh Long",
            "Đồng Tháp", "Cà Mau", "An Giang",

            // 11 đơn vị giữ nguyên
            "Hà Nội", "Huế", "Lai Châu", "Điện Biên", "Sơn La",
            "Lạng Sơn", "Quảng Ninh", "Thanh Hóa", "Nghệ An",
            "Hà Tĩnh", "Cao Bằng"
        ));
    }
}
