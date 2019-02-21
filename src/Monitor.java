/**
 *
 *  @author Shkred Artur
 *
 */

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class Monitor {
    private static final String CONTEXT = "/app";
    private static List<Agent> agents;

    public static void main(String[] args) throws InterruptedException, IOException {
        Agent agent1 = new Agent("localhost", 9991, new Counter(new AtomicLong(0)), null);
        Agent agent2 = new Agent("localhost", 9992, new Counter(new AtomicLong(agent1.getCounter().value.get())), new AgentAddress(agent1.IP, agent1.port));
        Agent agent3 = new Agent("localhost", 9993, new Counter(new AtomicLong(agent2.getCounter().value.get())), new AgentAddress(agent2.IP, agent2.port));
        Agent agent4 = new Agent("localhost", 9994, new Counter(new AtomicLong(agent3.getCounter().value.get())), new AgentAddress(agent3.IP, agent3.port));
        Agent agent5 = new Agent("localhost", 9995, new Counter(new AtomicLong(agent4.getCounter().value.get())), new AgentAddress(agent4.IP, agent4.port));
        agents = Arrays.asList(agent1, agent2, agent3, agent4, agent5);

        MonitorHttpServer httpServer = new MonitorHttpServer(10000, CONTEXT, new HttpRequestHandler(agents));
        httpServer.start();
    }
}
