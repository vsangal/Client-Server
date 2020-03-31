package client;

import java.io.*;
import java.net.Socket;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Client {

	public static final int PORT = 3000;

    public static String getRequest(String command) {

        if (command.equals("index")) {
            return command;
        } else if (command.startsWith("get")) {
            return command.substring(command.indexOf(" ") + 1);
        } else if (command.equals("quit") || command.equals("q")) {
            return "quit";
        } else {
        	return "unknown";
        }
    }

    public static void start() {

    	try  {

            Console console = System.console();
            
            BufferedReader kb = new BufferedReader(new InputStreamReader(System.in)); 
            
            String request;

            while (true) {
            	
            	Socket connection = new Socket("localhost", PORT);
            	PrintWriter toServer = new PrintWriter( connection.getOutputStream(),true);
            			
            	String command = kb.readLine();

                request = getRequest(command);
                
                if ("quit".equals(request)) {
                    connection.close();
                    System.exit(0);
                }
                
                toServer.println(request);

                InputStream input = connection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(input));


                List<String> responseLines = reader.lines().collect(Collectors.toList());
                for (String line : responseLines) {
                    System.out.println(line);
                }

                toServer.flush();
            }
        } catch (IOException e) {
        	System.out.println("Error in Client.start()");
            e.printStackTrace();
        }
    }
}
