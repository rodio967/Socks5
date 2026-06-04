package socks5.error;

import lombok.extern.slf4j.Slf4j;
import socks5.connection.Conn;
import socks5.util.State;

import java.io.IOException;
import java.net.SocketException;
import java.nio.channels.SelectionKey;

@Slf4j
public class ErrorHandler {


    public boolean isExpectedIOError(IOException e) {
        String msg = e.getMessage();
        if (msg == null) return false;

        return msg.contains("Connection reset") ||
                msg.contains("Broken pipe") ||
                msg.contains("Connection reset by peer") ||
                msg.contains("Software caused connection abort");
    }

    public void logConnectionError(SelectionKey key, SocketException e) {
        String context = buildContext(key);

        String msg = e.getMessage();
        if (msg != null && msg.contains("Connection reset")) {
            return;
        }
        log.error("Connection error {}: {}", context, e.getMessage());
    }

    public void logIOError(SelectionKey key, IOException e) {
        String context = buildContext(key);
        String errorType = e.getClass().getSimpleName();

        log.error("I/O error {} [{}]: {}", context, errorType, e.getMessage());
    }

    public void logCriticalError(SelectionKey key, Throwable t) {
        String context = buildContext(key);
        log.error("CRITICAL ERROR {} [{}]: {}",
                context,
                t.getClass().getSimpleName(),
                t.getMessage());

        t.printStackTrace();
    }

    public String buildContext(SelectionKey key) {
        Object att = key.attachment();

        if (att instanceof Conn c) {
            State state = c.getConnectionContext().getState();
            String host = c.getConnectionContext().getPendingHost();

            if (host != null) {
                return String.format(" [%s, %s]", state, host);
            }
            return String.format(" [%s]", state);
        }

        return "";
    }


}