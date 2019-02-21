/**
 *
 *  @author Shkred Artur
 *
 */

public class AgentAddress {
    public String IP;
    public int port;

    public AgentAddress(String IP, int port) {
        this.IP = IP;
        this.port = port;
    }

    @Override
    public String toString() {
        return String.format("IP = %s, port = %d", IP, port);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AgentAddress that = (AgentAddress) o;

        return port == that.port && (IP != null ? IP.equals(that.IP) : that.IP == null);
    }
}
