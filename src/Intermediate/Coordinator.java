package Intermediate;

import java.net.*;
import java.io.*;
import java.util.LinkedList;
import java.util.Queue;

public class Coordinator implements Runnable {
    
    private Socket cSocket;
    static CoordinatorGUI c_GUI = new CoordinatorGUI();
    private static Queue<Socket> requestQueue = new LinkedList<>();
    private static boolean resourceAvailable = true;

    public Coordinator(Socket cSocket) {
        this.cSocket = cSocket;
        requestQueue.add(cSocket);
        c_GUI.appendTextArea("Branch process stored in queue waiting for resource access.");
    }

    @Override
    public void run() {
        while (true) {
            if (!requestQueue.isEmpty() && resourceAvailable) {
                resourceAvailable = false; // Lock the resource
                Socket currentS = requestQueue.poll();  //Front of the Queue
                c_GUI.appendTextArea("Branch process is now accessing the resource.");
                
                try {
                    Thread.sleep(3000); // Simulate processing delay so that we can see if another branch is actually waiting to access the reserved resource
                    handleRequest(currentS); //Implement the request when ready
                } catch (Exception e) {
                    c_GUI.appendTextArea(e.getMessage());
                }
            }
        }
    }

    private void handleRequest(Socket socket) {
        try {
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            DataInputStream in = new DataInputStream(socket.getInputStream());
            String name = in.readUTF();
            String code = in.readUTF(); 
            
            // Connect as client to ResourceApplication (Main Server)
            Socket s1 = new Socket("localhost", 3000);
            DataOutputStream output = new DataOutputStream(s1.getOutputStream());
            ObjectInputStream input = new ObjectInputStream(s1.getInputStream());
            output.writeUTF(code); // Request resource by code
            
            Object obj = input.readObject();
            
            out.writeObject(obj);
            
            input.close();
            in.close();
            output.close();
            out.close();
            s1.close();
            socket.close();
            
            c_GUI.appendTextArea(name + " process has completed resource access.");
            
            // Unlock the resource
            resourceAvailable = true;
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    
    public static void main(String[] args) throws IOException {
        c_GUI.setVisible(true);
        ServerSocket sSocket = new ServerSocket(6000);
        c_GUI.appendTextArea("Waiting for Requests...");
        
        while (true) {
            Socket s = sSocket.accept();
            c_GUI.appendTextArea("A branch has requested");
            Thread process = new Thread(new Coordinator(s));
            process.start();
        }
    }
}