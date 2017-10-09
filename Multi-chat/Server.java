/*
 * Copyright (C) 2017 huzaifafaruqui
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package Multithreaded.client.server;

/**
 *
 * @author huzaifafaruqui
 */
import java.net.*;
import java.io.*;

public class Server {

    /**
     * @param args the command line arguments
     */
    private ServerSocket servsocket = null;
    private Socket clntsocket = null;
    // private int maxClients = 8;
    static ClientHandler[] threads = new ClientHandler[10];
    private DataOutputStream sout = null;

    Server(int port) {
        try {
            servsocket = new ServerSocket(port);
            System.out.println("Server started..");
            System.out.println("Waiting for connection..");
            while (true) {
                clntsocket = servsocket.accept();
                //sout = new DataOutputStream(clntsocket.getOutputStream());
                //outputStreams.put(clntsocket, sout);
                System.out.println("Connected");
                for (int i = 0; i < threads.length; ++i) {
                    if (threads[i] == null) {
                        (threads[i] = new ClientHandler(clntsocket, threads)).start();
                        break;
                    }
                }

            }

        } catch (IOException i) {
            System.out.println(i);
        }
    }

    public static void main(String[] args) {
        // TODO code application logic here
        Server serv = new Server(5000);
    }

}

class ClientHandler extends Thread {

    private DataInputStream sinp = null;
    private DataOutputStream sout = null;
    private Socket socket = null;
    private ClientHandler[] threads;

    ClientHandler(Socket socket, ClientHandler[] threads) {
        //t his.server = server;
        this.socket = socket;
        this.threads = threads;
    }

    public void run() {

        try {

            sinp = new DataInputStream(socket.getInputStream());
            sout = new DataOutputStream(socket.getOutputStream());
            String name, line;
            sout.writeUTF("Enter name");
            name = sinp.readUTF();
            int i;
            for (i = 0; i < threads.length; ++i) {
                if (threads[i] != null && threads[i] != this) {
                    threads[i].sout.writeUTF(name + " connected");
                }
            }
            while (true) {
                line = sinp.readUTF();
                if (line.startsWith("exit")) {
                    break;
                }
                for (i = 0; i < threads.length; ++i) {
                    if (threads[i] != null && threads[i] != this) {
                        threads[i].sout.writeUTF(name + " : " + line);
                    }
                }

            }
            for (i = 0; i < threads.length; ++i) {
                if (threads[i] != null && threads[i] != this) {
                    threads[i].sout.writeUTF(name + " leaving");
                } else if (threads[i] == this) {
                    threads[i] = null;
                }
            }
            
            sout.writeUTF("Bye");

            sout.close();
            sinp.close();
            socket.close();

        } catch (IOException e) {
        }
    }

}
