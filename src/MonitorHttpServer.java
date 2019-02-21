/**
 *
 *  @author Shkred Artur
 *
 */

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class MonitorHttpServer {
    private HttpServer httpServer;

    /**
     * Instantiates a new simple http server.
     *
     * @param port the port
     * @param context the context
     * @param handler the handler
     */
    public MonitorHttpServer(int port, String context, HttpHandler handler) {
        try {
            httpServer = HttpServer.create(new InetSocketAddress(port), 0);
            httpServer.createContext(context, handler);
            httpServer.setExecutor(null);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void start() {
        if (httpServer != null)
            httpServer.start();
    }
}
