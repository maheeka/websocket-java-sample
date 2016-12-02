package maheeka.github.com.websocket.socketio;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

import java.net.URISyntaxException;

/**
 * Simple WebSocket Client based on Socket IO Java Client
 */
public class SocketIOClient {

    public static void main(String[] args) throws URISyntaxException {
        final Socket socket = IO.socket("http://localhost:3000");
        socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {

            @Override
            public void call(Object... args) {
                socket.emit("foo", "hi");
                socket.disconnect();
            }

        }).on("event", new Emitter.Listener() {

            @Override
            public void call(Object... args) {}

        }).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {

            @Override public void call(Object... args) {
            }

        });
        socket.connect();
    }
}
