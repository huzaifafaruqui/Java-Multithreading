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
    private Socket socket = null;
    private BufferedReader inp = null;
    private DataInputStream sinp = null;
    private DataOutputStream sout = null;
    private boolean closed = false;

    Client(String addr, int port) {

        try {
            socket = new Socket(addr, port);
            inp = new BufferedReader(new InputStreamReader(System.in));
            sinp = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            sout = new DataOutputStream(socket.getOutputStream());

            Thread t1 = new Thread(this);
            t1.start();

            String ln = "";

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

    @Override
    public void run() {
        String response = "";
        try {
            while (!response.equals("bye")) {
                response = sinp.readUTF();
                System.out.println(response);
            }
            closed = true;
        } catch (IOException i) {
            System.out.println(i);
        }

    }

    public static void main(String[] args) {
        Client c = new Client("127.0.0.1", 5000);
        // TODO code application logic here
    }

}
