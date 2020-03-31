package server;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;


public class Server {
    public static final int PORT = 3000;
    static ServerSocket listener;
    
    public static void start(File directory) throws IOException {
        listener = new ServerSocket(PORT);
        System.out.println("Listening on port " + PORT);

        while (true) {
            Thread asyncConnectionHandler = new Thread(new ConnectionHandler(listener, directory));
            asyncConnectionHandler.start(); 
        }
    }
   /* 
    public static void stop() throws Exception {
		listener.close();
	}
    
    public static void main(String... args) throws Exception {
    	stop();
    }
    */
}
