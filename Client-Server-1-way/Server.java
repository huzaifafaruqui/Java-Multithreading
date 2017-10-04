
import java.io.*;
import java.net.*;

/**
 *
 * @author huzaifafaruqui
 */
public class Server {

    /**
     * @param args the command line arguments
     */
    private Socket socket = null;
    private ServerSocket server = null;
    private DataInputStream inp = null;

    // private DataOutputStream out = null;
    Server(int port) {
        try {
            server = new ServerSocket(port);
            System.out.println("Server started..");
            System.out.println("Waiting for connection..");
            socket = server.accept();
            System.out.println("Connected");
            inp = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            // out = new DataOutputStream(socket.getOutputStream());
        } catch (UnknownHostException e) { // Thrown to indicate that the IP address of a host could not be determined.
            System.out.println(e);
        } catch (IOException ioe) {
            System.out.println(ioe);
        }

        String ln = "";
        while (!ln.equals(".")) {
            try {
                ln = inp.readUTF();
                System.out.println(ln);
            } catch (IOException e) {
                System.out.println(e);
            }
        }
        try {
            inp.close();
            socket.close();
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public static void main(String[] args) {
        // TODO code application logic here
        Server server = new Server(5000);
    }

}
