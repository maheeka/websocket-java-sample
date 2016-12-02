package maheeka.github.com.websocket.javawebsocket;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;

/**
 * Simple WebSocket Publisher based on Socket Java Web Socket
 */
public class JavaWebSocketClient extends WebSocketClient {
    public JavaWebSocketClient(URI serverURI) {
        super(serverURI);
    }

    @Override public void onOpen(ServerHandshake serverHandshake) {

    }

    @Override public void onMessage(String message) {
        send(message);
    }

    @Override public void onClose(int i, String s, boolean b) {

    }

    @Override public void onError(Exception e) {
        System.out.println( "Error: " );
        e.printStackTrace();
    }
}
