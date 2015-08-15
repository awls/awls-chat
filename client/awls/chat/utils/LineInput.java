package awls.chat.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public abstract class LineInput {

    private final BufferedReader in;
    private final Thread readingThread;
    private volatile boolean shouldStop = false;

    public LineInput(InputStream inputStream) {
        this.in = new BufferedReader(new InputStreamReader(inputStream));
        this.readingThread = new Thread(this::readInput);
    }

    public void start() {
        readingThread.start();
    }

    public void stop() {
        shouldStop = true;
    }

    private void readInput() {
        try {
            String line;
            while (!shouldStop && (line = in.readLine()) != null) {
                if (!shouldStop) {
                    handleLine(line);
                }
            }
        } catch (IOException e) {
            if (!shouldStop) {
                handleError(e);
            }
        }
    }

    protected abstract void handleLine(String line);

    protected abstract void handleError(IOException e);

}
