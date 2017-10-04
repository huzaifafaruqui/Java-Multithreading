
/**
 *
 * @author huzaifafaruqui
 */
import java.net.*;
import java.io.*;

public class Client {

    private Socket socket = null;
    private BufferedReader inp = null;

    private DataOutputStream out = null;

    Client(String addr, int port) {
        try {
            socket = new Socket(addr, port);
            inp = new BufferedReader(new InputStreamReader(System.in));
            out = new DataOutputStream(socket.getOutputStream());
        } catch (UnknownHostException e) { // Thrown to indicate that the IP address of a host could not be determined.
            System.out.println(e);
        } catch (IOException ioe) {
            System.out.println(ioe);
        }

        String ln = "";
        while (!ln.equals(".")) {
            try {
                ln = inp.readLine();
                out.writeUTF(ln);
            } catch (IOException e) {
                System.out.println(e);
            }
        }
        try {
            inp.close();
            out.close();
            socket.close();
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        Client c = new Client("127.0.0.1", 5000);
    }

}
