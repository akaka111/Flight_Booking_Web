package VNPay;

import jakarta.servlet.ServletContext;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigLoader {

    private static Properties props = new Properties();

    public static void load(ServletContext context) {
        try {
            InputStream input = context.getResourceAsStream("/WEB-INF/config.properties");
            if (input == null) {
                throw new RuntimeException("Không tìm thấy file config.properties trong WEB-INF.");
            }
            props.load(input);
            System.out.println(">>> ĐÃ LOAD config.properties thành công.");
        } catch (IOException e) {
            throw new RuntimeException("Lỗi khi load config.properties: " + e.getMessage(), e);
        }
    }

    public static String get(String key) {
        return props.getProperty(key);
    }
}
