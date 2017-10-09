/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Multithreaded.client.server;

/**
 *
 * @author huzaifafaruqui
 */
import java.net.*;
import java.io.*;

public class Client implements Runnable {

    /**
     * @param args the command line arguments
     */
    private static Socket socket = null;
    private static BufferedReader inp = null;
    private static DataInputStream sinp = null;
    private static DataOutputStream sout = null;
    public static boolean closed = false;


    @Override
    public void run() {
        String response;
        try {
            while (true) {
                response = sinp.readUTF();
                System.out.println(response);
                if (response.contains("Bye") == true) {
                    break;
                }
            }
            // System.out.println("lol");
            closed = true;
           
        } catch (IOException i) {
            System.out.println(i);
        }

    }

    public static void main(String[] args) {
         try {
            socket = new Socket("127.0.0.1", 5000);
            inp = new BufferedReader(new InputStreamReader(System.in));
            sinp = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            sout = new DataOutputStream(socket.getOutputStream());

            Thread t = new Thread(new Client());
            t.start();

            String ln;

            while (!closed) {

                ln = inp.readLine();
                sout.writeUTF(ln);
            }
            socket.close();
            inp.close();
            sinp.close();
        } catch (UnknownHostException e) {
            System.out.println(e);
        } catch (IOException i) {
            System.out.println(i);
        }

    }

}
