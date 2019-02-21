/**
 *
 *  @author Shkred Artur
 *
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public abstract class AgentCommunication {

    public void startServer(int port){
        Thread serverThread = new Thread(() -> {
            ServerSocket serverSocket;
            Socket clientSocket;
            try {
                serverSocket = new ServerSocket(port);
                for (;;){
                    System.out.println("port " + port + " waiting for someone connects");
                    clientSocket = serverSocket.accept();
                    new Thread(new ClientHandler(clientSocket)).start();
                    System.out.println("port " + port + " someone connected = " + clientSocket.getPort());
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        });
        serverThread.setName("Server " + port);
        serverThread.start();
    }

    private class ClientHandler implements Runnable {

        Socket clientSocket = null;
        ClientHandler(Socket socket) {
            clientSocket = socket;
        }

        @Override
        public void run() {
            try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                 PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {
                String line;
                line = in.readLine();
                out.println(parseIncomingCommand(line));
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private String parseIncomingCommand(String command){
            switch (command){
                case "CLK":
                    return String.valueOf(clk());
                case "NET":
                    return net();
                case "SYN":
                    synchronize();
                    return "SYNCHRONIZED";
                default: //register new agent
                    registerNewAgent(command);
                    return "AGENT " + command + " REGISTERED";
            }
        }

    }
    /**
     * @return the current value of the clock
     */
    abstract long clk();

    /**
     * @return the record of IP numbers and communication ports of all the agents.
     */
    abstract String net();

    abstract void registerNewAgent(String agentInfo);

    /**
     * Synchronise counter
     */
    abstract void synchronize();
}
