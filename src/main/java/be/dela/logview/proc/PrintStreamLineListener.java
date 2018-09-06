package be.dela.logview.proc;

import java.io.PrintStream;

public class PrintStreamLineListener implements LineListener {

    final PrintStream printStream;

    public PrintStreamLineListener(PrintStream printStream) {
        this.printStream = printStream;
    }

    @Override
    public void onLine(String processName, String line) {
        synchronized (printStream) {
            printStream.println("[" + processName + "] - " + line);
        }
    }
}
