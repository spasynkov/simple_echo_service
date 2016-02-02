import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;

public class Server {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(6789);
        SimpleDateFormat format = new SimpleDateFormat("H:mm:ss:SSS EEE, d MMMM y ('GMT' XXX)");
        long clientNumber = 0;

        while (true) {
            try {
                System.out.println(format.format(System.currentTimeMillis()) + "\tListening...");
                new Replier(serverSocket.accept(), ++clientNumber, format).start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static class Replier extends Thread {
        private Socket socket;
        private long clientNumber;
        private SimpleDateFormat format;

        Replier(Socket socket, long clientNumber, SimpleDateFormat dateFormat) {
            this.socket = socket;
            this.clientNumber = clientNumber;
            format = dateFormat;
        }

        @Override
        public void run() {
            String messageFromClient = null;

            System.out.println(format.format(System.currentTimeMillis()) + "\tClient #" + clientNumber + " connected.");

            try (InputStream inFromClient = socket.getInputStream();
                 PrintWriter outToClient = new PrintWriter(socket.getOutputStream())) {

                messageFromClient = getMessageFromClient(inFromClient);
                System.out.println(format.format(System.currentTimeMillis()) + "\tGot message from client #" + clientNumber + ": \"" + messageFromClient + "\"\tSending back...");

                outToClient.write(messageFromClient.toUpperCase());
                System.out.println(format.format(System.currentTimeMillis()) + "\tSent to client #" + clientNumber + ".\n");
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                close(socket);
                // if (messageFromClient != null && messageFromClient.toLowerCase().equals("exit")) System.exit(1);
            }
        }

        private void close(Closeable... streams) {
            for (Closeable stream : streams) {
                try {
                    stream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        private String getMessageFromClient(InputStream inputStream) throws IOException {
            byte[] buffer = new byte[1024];
            int bufferSize;
            StringBuilder sb = new StringBuilder();

            while (inputStream.available() > 0) {
                bufferSize = inputStream.read(buffer);
                for (int i = 0; i < bufferSize; i++)
                    sb.append((char) buffer[i]);
            }
            return sb.toString();
        }
    }
}
