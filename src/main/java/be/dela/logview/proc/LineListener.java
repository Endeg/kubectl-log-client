package be.dela.logview.proc;

public interface LineListener {
    void onLine(String processName, String line);
}
