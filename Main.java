import com.sun.net.httpserver.HttpServer;
import controllers.HomeController;
import java.net.InetSocketAddress;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try {
            // Use environment PORT variable for cloud deployment, fallback to 8080
            int port = Integer.parseInt(System.getenv().getOrDefault("PORT", "8080"));
            HttpServer server = HttpServer.create(new InetSocketAddress("0.0.0.0", port), 0);
            server.createContext("/", new HomeController());
            server.setExecutor(null);
            System.out.println("✅ Server running at http://0.0.0.0:" + port + " (accessible on your network)");
            server.start();
        } catch (IOException e) {
            System.err.println("❌ Failed to start server: " + e.getMessage());
        }
    }
}
