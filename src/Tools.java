/**
 *
 *  @author Shkred Artur
 *
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Tools {
    public static String sendRequest(String receiverIP , int receiverPort, String request){
        Socket echoSocket;
        PrintWriter out;
        BufferedReader in;

        try {
            echoSocket = new Socket(receiverIP, receiverPort);
            out = new PrintWriter(echoSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));

            System.out.println(Thread.currentThread().getName() + " request("+request+")");
            out.println(request);

            StringBuilder response = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null){
                System.out.println(line);
                response.append(line).append("\n");
            }
            out.close();
            in.close();
            echoSocket.close();
            return response.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
