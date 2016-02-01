import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Server {
    public static void main(String[] args) throws IOException {
        String s;
        ServerSocket serverSocket = new ServerSocket(6789);
        SimpleDateFormat format = new SimpleDateFormat("H:mm:ss:SSS EEE, d MMMM y ('GMT' XXX)");

        while (true) {
            try {
                System.out.println(format.format(new Date()) + "\tListening...");
                Socket socket = serverSocket.accept();
                InputStream inFromClient = socket.getInputStream();
                PrintWriter outToClient = new PrintWriter(socket.getOutputStream());
                s = getMessageFromClient(inFromClient);
                System.out.println();
                outToClient.write(s);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static String getMessageFromClient(InputStream inputStream) {
        return "";
    }
}
