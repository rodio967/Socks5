package socks5.connection.context;
import lombok.Getter;
import lombok.Setter;
import socks5.util.State;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

@Getter
@Setter
public class ConnectionContext {
    private final SelectionKey clientKey;
    private final SocketChannel client;

    private SelectionKey remoteKey;
    private SocketChannel remote;

    private State state = State.GREETING;
    private String pendingHost;
    private int pendingPort;

    public ConnectionContext(SelectionKey clientKey, SocketChannel client) {
        this.clientKey = clientKey;
        this.client = client;
    }

    private void closeQuietly(SelectionKey key, SocketChannel channel) {
        if (key != null) {
            try {
                key.cancel();
            } catch (Exception ignored) {}
        }

        if (channel != null) {
            try {
                channel.close();
            } catch (IOException ignored) {}
        }
    }

    public void closeAll() {
        state = State.CLOSED;

        closeQuietly(clientKey, client);
        closeQuietly(remoteKey, remote);
    }
}
