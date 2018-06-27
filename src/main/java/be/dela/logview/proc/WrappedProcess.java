package be.dela.logview.proc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

public class WrappedProcess {

    private static final Logger log = LoggerFactory.getLogger(WrappedProcess.class);
    private Process process;
    private InputStream inputStream;

    private ConcurrentLinkedQueue<String> bufferedLines = new ConcurrentLinkedQueue<>();

    public WrappedProcess(String... args) {
        try {
            final ProcessBuilder processBuilder = new ProcessBuilder(args);
            processBuilder.redirectErrorStream(true);
            this.process = processBuilder.start();
            inputStream = this.process.getInputStream();
        } catch (IOException e) {
            this.process = null;
        }
        startUpdateLoop();
    }

    private void startUpdateLoop() {
        new Thread(this::updateLines).run();
    }

    private void updateLines() {
        try (final Reader r = new InputStreamReader(inputStream);
             final BufferedReader br = new BufferedReader(r)) {
            String line;
            while ((line = br.readLine()) != null) {
                bufferedLines.add(line);
            }
        } catch (IOException e) {
            log.error("Problem reading process", e);
        }
    }

    public Collection<String> flush() {
        final List<String> result = new ArrayList<>();
        String line;
        while ((line = bufferedLines.poll()) != null) {
            result.add(line);
        }
        return result;
    }

    public Collection<String> flushToEnd() {
        final List<String> result = new ArrayList<>(this.flush());
        while (process.isAlive()) {
            result.addAll(this.flush());
        }
        return result;
    }
}
