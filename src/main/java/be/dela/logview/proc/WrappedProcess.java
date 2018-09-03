package be.dela.logview.proc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

public class WrappedProcess {

    private Process process;
    private InputStream inputStream;

    private ConcurrentLinkedQueue<String> bufferedLines = new ConcurrentLinkedQueue<>();

    private AtomicBoolean completed = new AtomicBoolean(false);

    public WrappedProcess(String... args) {
        try {
            System.out.println("Running command: " + Arrays.stream(args).collect(Collectors.joining(" ")));
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
        new Thread(this::updateLines).start();
    }

    private void updateLines() {
        try (final Reader r = new InputStreamReader(inputStream);
             final BufferedReader br = new BufferedReader(r)) {
            String line;
            while ((line = br.readLine()) != null) {
                bufferedLines.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
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

        process.onExit().thenAccept(proc -> this.completed.set(true));

        while (!completed.get()) {
            result.addAll(this.flush());
        }
        return result;
    }
}
