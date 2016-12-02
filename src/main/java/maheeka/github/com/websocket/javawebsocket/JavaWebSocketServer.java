package maheeka.github.com.websocket.javawebsocket;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;

/**
 * Simple WebSocket Publisher based on Java-WebSocket library (https://github.com/TooTallNate/Java-WebSocket)
 */
public class JavaWebSocketServer extends WebSocketServer {

    public static void main(String[] args) {
        JavaWebSocketServer webSocketServer = new JavaWebSocketServer(8890);
        webSocketServer.start();
        System.out.println("Server is started ....");
    }

    public JavaWebSocketServer(int port){
        super(new InetSocketAddress(port));
    }

    @Override public void onOpen(WebSocket webSocket, ClientHandshake clientHandshake) {

    }

    @Override public void onClose(WebSocket webSocket, int i, String s, boolean b) {

    }

    @Override public void onMessage(WebSocket webSocket, String message) {
        System.out.println("Received message : " + message);
        webSocket.send("Sending a message");
    }

    @Override public void onError(WebSocket webSocket, Exception e) {

    }
}
