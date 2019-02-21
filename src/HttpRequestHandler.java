/**
 *
 *  @author Shkred Artur
 *
 */

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.net.Socket;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

public class HttpRequestHandler implements HttpHandler {
    private static final String REQUEST = "request";
    private static final String AGENT_IP = "agentIP";
    private static final String AGENT_PORT = "agentPort";

    private static final int HTTP_OK_STATUS = 200;

    private List<Agent> agents;

    public HttpRequestHandler(List<Agent> agents) {
        super();
        this.agents = new ArrayList<>(agents);
    }

    public void handle(HttpExchange t) throws IOException {

        //Create a response form the request query parameters
        URI uri = t.getRequestURI();
        String response = createResponseFromQueryParams(uri);
        //Set the response header status and length
        t.getResponseHeaders().set("Content-Type","text/html;charset=utf-8");
        t.sendResponseHeaders(HTTP_OK_STATUS, response.getBytes().length);
        //Write the response string
        OutputStream os = t.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    private String createResponseFromQueryParams(URI uri) {
        String request = null;
        String agentIP = null;
        String agentPort = null;
        String query = uri.getQuery();
        System.out.println("QUERY = " + query);
        if (query != null && query.length() > 1) {
            String[] queryParams = query.split("&");
            if (queryParams.length > 0) {
                for (String qParam : queryParams) {
                    String[] param = qParam.split("=");
                    if (param.length == 2) {
                        if (REQUEST.equalsIgnoreCase(param[0]))
                            request = param[1];
                        else if (AGENT_IP.equalsIgnoreCase(param[0]))
                            agentIP = param[1];
                        else if (AGENT_PORT.equalsIgnoreCase(param[0]))
                            agentPort = param[1];
                    }
                }
            }
            if (request != null && agentIP != null && agentPort != null)
                parseAndDoRequest(request, agentIP, Integer.parseInt(agentPort));
        }
        return getNetworkStatus(agents);
    }

    public static String getNetworkStatus(List<Agent> agents){
        StringBuilder res = new StringBuilder();
        agents.forEach(agent -> {
            res.append(String.format("IP = \"%s\", Port = \"%d\", Counter = \"%d milliseconds\"",
                    agent.IP, agent.port, agent.getCounter().value.get()));
            //SYN Button
            res.append(String.format("<a href=\"/app?request=SYN&agentIP=%s&agentPort=%d\">" +
                    "<button>SYN</button></a>", agent.IP, agent.port));
            //Delete button
            res.append(String.format("<a href=\"/app?request=DEL&agentIP=%s&agentPort=%d\">" +
                    "<button>DELETE</button></a>", agent.IP, agent.port));
            res.append("</br>");
        });
        //Refresh button
        res.append("<form action=\"/app\">" +
                "    <input type=\"submit\" value=\"Refresh\" />" +
                "</form>");
        return res.toString();
    }

    private void parseAndDoRequest(String request, String agentIP, int agentPort){
        System.out.println("parseAndDoRequest = " + request + " " + agentIP + " " + agentPort);
        for (Agent agent : agents) {
            if (Objects.equals(agent.IP, agentIP) && Objects.equals(agentPort, agent.port)) {
                if (request.equals("SYN")) {
                    Tools.sendRequest(agentIP, agentPort, request);
                } else if (request.equals("DEL")) {
                    agents.remove(agent);
                    break;
                } else
                    System.out.println("UNKNOWN REQUEST : " + request);
                break;
            }
        }
    }
}
