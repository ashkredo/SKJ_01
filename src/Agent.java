/**
 *
 *  @author Shkred Artur
 *
 */

import java.util.LinkedList;
import java.util.List;

public class Agent extends AgentCommunication {
    String IP;
    int port;
    List<AgentAddress> network;
    private Counter counter;

    public Agent(String IP, int port, Counter counter, AgentAddress parentAddress) {
        network = new LinkedList<>();
        this.IP = IP;
        this.counter = counter;
        this.port = port;
        startServer(port);
        if (parentAddress != null) {
            network.add(parentAddress);
            getNetworkInfoFrom(parentAddress.IP, parentAddress.port);
            registerSelfInNetwork();
            synchronize();
            sendSYNtoNetwork();
        }
    }

    /**
     *  response example :
     *  192.168.0.1 9981
     *  143.128.4.44 9121
     */
    private void getNetworkInfoFrom(String ip, int port){
        String network = Tools.sendRequest(ip, port, "NET");
        if (network != null && network.length() > 1){
            String[] agents = network.split("\n");
            for (String agent : agents){
                String [] ip_port = agent.split(" ");
                AgentAddress agentAddress = new AgentAddress(ip_port[0], Integer.parseInt(ip_port[1]));
                if(!this.network.contains(agentAddress))
                    this.network.add(agentAddress);
            }
        }
    }

    /**
     *  agentInfo example :
     *  192.168.0.1 9981
     */
    @Override
    public void registerNewAgent(String agentInfo){
        String [] ip_port = agentInfo.split(" ");
        AgentAddress newAgent = new AgentAddress(ip_port[0], Integer.parseInt(ip_port[1]));
        if (!network.contains(newAgent))
            network.add(newAgent);
    }

    private void registerSelfInNetwork(){
        network.forEach(agentAddress ->
                Tools.sendRequest(agentAddress.IP, agentAddress.port, String.format("%s %d", IP, port)));
    }

    @Override
    public void synchronize(){
        long newCounterValue = 0;
        int count = 0;
        for (AgentAddress agentAddress : network) {
            String received = Tools.sendRequest(agentAddress.IP, agentAddress.port, "CLK");
            newCounterValue += Long.parseLong(received.replace("\n", ""));
            count++;
        }
        newCounterValue += counter.value.longValue();
        newCounterValue /= count + 1;
        this.counter.value.set(newCounterValue);
    }

    public Counter getCounter(){
        return this.counter;
    }

    public void setCounter(Counter counter){
        this.counter = counter;
        sendSYNtoNetwork();
    }

    private void sendSYNtoNetwork(){
        network.forEach(agentAddress -> Tools.sendRequest(agentAddress.IP, agentAddress.port, "SYN"));
    }

    @Override
    long clk() {
        return counter.value.get();
    }

    @Override
    String net() {
        StringBuilder sb = new StringBuilder();
        network.forEach(agentAddress -> sb.append(agentAddress.IP).append(" ").append(agentAddress.port).append("\n"));
        return sb.toString();
    }
}
