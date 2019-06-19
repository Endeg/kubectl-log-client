package be.dela.logview.endpoints;

import be.dela.logview.proc.LineListener;
import be.dela.logview.proc.PrintStreamLineListener;
import be.dela.logview.proc.WrappedProcess;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class ProcessEndpoint {

    private static Map<String, WrappedProcess> podProcesses = new HashMap<>();

    private static final LineListener CONSOLE_LINE_LISTENER = new PrintStreamLineListener(System.out);

    public Collection<String> pods(String env) {
        final WrappedProcess proc = new WrappedProcess("list-pods", "kubectl", "-n", env, "get", "pods");

        final Collection<String> pods = proc.flushToEnd();
        return pods.stream()
                .skip(1)
                .map(pod -> pod.split(" ")[0])
                .collect(Collectors.toList());
    }

    public Collection<String> contentForPods(final String env, String podName) {
        WrappedProcess pod = podProcesses.get(podName);

        if (pod == null) {
            pod = new WrappedProcess(podName, "kubectl", "-n", env, "logs", "-f",/*"--tail=20",*/ podName);
            pod.addLineListener(CONSOLE_LINE_LISTENER);
            podProcesses.put(podName, pod);
        }

        return pod.flush();
    }
}
