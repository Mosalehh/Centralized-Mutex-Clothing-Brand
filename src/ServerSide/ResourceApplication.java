package ServerSide;

import ServerSide.Clothes;
import java.net.*;
import java.io.*;
import java.util.ArrayList;

public class ResourceApplication implements Runnable{
    
    Socket cSocket;
    static ArrayList<Clothes> clothes=new ArrayList<Clothes>();
    private static boolean R1Available = true; //R1 lock
    private static boolean R2Available = true; //R2 lock
    static ResourceApplicationGUI RA_GUI=new ResourceApplicationGUI();
    
    public ResourceApplication(Socket cSocket)
    {
        this.cSocket=cSocket;
        clothes.add(new Clothes("Blue T-shirt","T101",50,'M',"Blue",100)); //Resource 1
        clothes.add(new Clothes("Pants","T102",100,'L',"White",10));    //Resource 2
       
    }

    @Override
    public void run() {
        try {
            ObjectOutputStream out = new ObjectOutputStream(cSocket.getOutputStream());
            DataInputStream input = new DataInputStream(cSocket.getInputStream());
            
            String code=input.readUTF();
            RA_GUI.appendTextArea("Request Recieved for resource: "+ code);
            Clothes c1 = null;
            for (Clothes c:clothes)
            {
                if (c.getCode().equals(code))
                {
                    //Check if requested resource is available
                    if((c.getCode().equals("T101") && R1Available) || ((c.getCode().equals("T102") && R2Available)))
                    {
                     c1=c;
                     if(c1.getCode().equals("T101")) //if the requested code is R1 then lock it
                     {
                         R1Available=false;
                     }
                     else if(c1.getCode().equals("T102")) //if the requested code is R2 then lock it
                     {
                         R2Available=false;
                     }
                     RA_GUI.appendTextArea("Resource " + code + " allocated.");
                     break;
                }
                    else
                    {
                        RA_GUI.appendTextArea("Resource " + code + " is not available."); //Resource not found
                    }
                
              }
            }
           
         
            out.writeObject(c1); //Send resource object to the Coordinator
            
           Thread.sleep(3000); //delay for 3 seconds to simulate waiting in the queue for other processes requesting same resource
            
            if(c1!=null)
            {
                if(c1.getCode().equals("T101"))
                {
                    R1Available=true; //unlock
                }
                else if(c1.getCode().equals("T102"))
                {
                    R2Available=true; //unlock
                }
                RA_GUI.appendTextArea("Resource " + code + " is now available.");
            }
            
            input.close();
            out.close();
            cSocket.close();             
      
        }
        catch (Exception e)
        {
            RA_GUI.appendTextArea("Error: " + e.getMessage());       
         }
    }
    
      public static void main (String[] args) throws IOException
    {
        RA_GUI.setVisible(true);
       ServerSocket sSocket = new ServerSocket (3000);
       RA_GUI.appendTextArea("Listening...");
       while (true)
       {
           Socket S = sSocket.accept();
           RA_GUI.appendTextArea("Connected to a branch.");
           Thread Process  =  new Thread(new ResourceApplication(S));      
           Process.start();
               }
    }    
}
