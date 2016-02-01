import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;

public class Client {
    public int counter = 0;
    public static void main(String[] args) throws IOException {
        String s;
        BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));

        for (int i = 0; i < 10000; i++) {
            new Thread("test_thread_" + (i + 1)){
                @Override
                public void run() {
                    try {
                        InetAddress ip = InetAddress.getByName("192.168.0.2");
                        Socket socket = new Socket(ip, 6789);
                        BufferedReader inFromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                        DataOutputStream outToServer = new DataOutputStream(socket.getOutputStream());

                        String s = Thread.currentThread().getName();
                        outToServer.write((s + "\n").getBytes("UTF-8"));
                        System.out.println(inFromServer.readLine());
                        socket.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        }

/*        System.out.print("Enter IP or domain name to connect: ");
        String sIP = consoleReader.readLine();
        if (sIP == null || sIP.isEmpty()) sIP = "ff.ddns.ukrtel.net";
        InetAddress ip = InetAddress.getByName(sIP);
        Socket socket = new Socket(ip, 6789);
        BufferedReader inFromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        DataOutputStream outToServer = new DataOutputStream(socket.getOutputStream());

        System.out.print("Enter your message: ");
        s = consoleReader.readLine();
        outToServer.write((s + "\n").getBytes("UTF-8"));
        System.out.println("Sent.");
        System.out.println(inFromServer.readLine());
        socket.close();
*/
        consoleReader.close();
    }
}
