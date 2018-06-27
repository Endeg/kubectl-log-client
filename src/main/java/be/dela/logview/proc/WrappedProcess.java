package be.dela.logview.proc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

public class WrappedProcess {

    private static final Logger log = LoggerFactory.getLogger(WrappedProcess.class);
    Process process;
    private InputStream inputStream;

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
        try (final Reader r = new InputStreamReader(inputStream);
             final BufferedReader br = new BufferedReader(r)) {
            String line;
            while ((line = br.readLine()) != null) {
                log.info("line: " + line);
            }
        } catch (IOException e) {
            log.error("Problem reading process", e);
        }
    }
}
