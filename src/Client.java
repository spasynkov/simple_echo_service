import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;

public class Client {
    private static final String DEFAULT_HOST = "localhost";

    public static void main(String[] args) throws IOException {
        runSeveralThreads();
        //askUser();
    }

    private static void askUser() throws IOException {
        String userString;
        BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("Enter IP or domain name to connect: ");
        String host = consoleReader.readLine();

        do {
            System.out.print("Enter your message (type exit to leave): ");
            userString = consoleReader.readLine();

            if (host == null || host.isEmpty()) host = DEFAULT_HOST;
            InetAddress ip = InetAddress.getByName(host);
            Socket socket = new Socket(ip, 6789);
            BufferedReader inFromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            DataOutputStream outToServer = new DataOutputStream(socket.getOutputStream());

            outToServer.write(userString.getBytes("UTF-8"));
            outToServer.flush();
            System.out.println("Sent.");
            System.out.println(inFromServer.readLine());
            socket.close();
        } while (!userString.toLowerCase().equals("exit"));

        consoleReader.close();
    }

    private static void runSeveralThreads() {
        for (int i = 0; i < 10000; i++) {
            new Thread("test_thread_" + (i + 1)){
                @Override
                public void run() {
                    try {
                        InetAddress ip = InetAddress.getByName(DEFAULT_HOST);
                        Socket socket = new Socket(ip, 6789);
                        BufferedReader inFromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                        DataOutputStream outToServer = new DataOutputStream(socket.getOutputStream());

                        String s = Thread.currentThread().getName();
                        outToServer.write(s.getBytes("UTF-8"));
                        outToServer.flush();
                        System.out.println(inFromServer.readLine());
                        socket.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        }
    }
}
