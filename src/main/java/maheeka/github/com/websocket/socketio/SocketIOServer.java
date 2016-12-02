package maheeka.github.com.websocket.socketio;

import io.socket.client.IO;
import io.socket.client.Socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * SocketIO Java Client does not provide SocketIO server capabilities. Hence we create a server from the JavaScript
 * implementation. The following is the same implementation done at https://github.com/socketio/socket
 * .io-client-java/blob/master/src/test/java/io/socket/client/Connection.java
 */

public class SocketIOServer {

    final static int PORT = 3000;

    private Process serverProcess;
    private ExecutorService serverService;
    private Future serverOutput;
    private Future serverError;

    public static void main(String[] args) throws URISyntaxException, IOException, InterruptedException {
        SocketIOServer socketIOServer = new SocketIOServer();
        socketIOServer.startServer();
    }

    public void startServer() throws IOException, InterruptedException {
        System.out.println("Starting server ...");

        final CountDownLatch latch = new CountDownLatch(1);
        serverProcess =
                Runtime.getRuntime().exec(String.format("node src/main/resources/server.js %s", nsp()), createEnv());
        serverService = Executors.newCachedThreadPool();
        serverOutput = serverService.submit(new Runnable() {
            @Override public void run() {
                BufferedReader reader = new BufferedReader(new InputStreamReader(serverProcess.getInputStream()));
                String line;
                try {
                    line = reader.readLine();
                    latch.countDown();
                    do {
                        System.out.println("SERVER OUT: " + line);
                    } while ((line = reader.readLine()) != null);
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }
        });
        serverError = serverService.submit(new Runnable() {
            @Override public void run() {
                BufferedReader reader = new BufferedReader(new InputStreamReader(serverProcess.getErrorStream()));
                String line;
                try {
                    while ((line = reader.readLine()) != null) {
                        System.out.println("SERVER ERR: " + line);
                    }
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }
        });
        latch.await(3000, TimeUnit.MILLISECONDS);
    }

    public void stopServer() throws InterruptedException {
        System.out.println("Stopping server ...");
        serverProcess.destroy();
        serverOutput.cancel(false);
        serverError.cancel(false);
        serverService.shutdown();
        serverService.awaitTermination(3000, TimeUnit.MILLISECONDS);
    }

    Socket client() throws URISyntaxException {
        return client(createOptions());
    }

    Socket client(String path) throws URISyntaxException {
        return IO.socket(path, createOptions());
    }

    Socket client(IO.Options opts) throws URISyntaxException {
        return client(nsp(), opts);
    }

    Socket client(String path, IO.Options opts) throws URISyntaxException {
        return IO.socket(uri() + path, opts);
    }

    String uri() {
        return "http://localhost:" + PORT;
    }

    String nsp() {
        return "/";
    }

    IO.Options createOptions() {
        IO.Options opts = new IO.Options();
        opts.forceNew = true;
        return opts;
    }

    String[] createEnv() {
        Map<String, String> env = new HashMap<String, String>(System.getenv());
        env.put("DEBUG", "socket.io:*");
        env.put("PORT", String.valueOf(PORT));
        String[] _env = new String[env.size()];
        int i = 0;
        for (String key : env.keySet()) {
            _env[i] = key + "=" + env.get(key);
            i++;
        }
        return _env;

    }
}

