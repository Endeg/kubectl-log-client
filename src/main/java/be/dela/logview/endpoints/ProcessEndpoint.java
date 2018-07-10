package be.dela.logview.endpoints;

import be.dela.logview.proc.WrappedProcess;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class ProcessEndpoint {

    private static Map<String, WrappedProcess> podLogs = new HashMap<>();

    public Collection<String> pods(String env) {
        final WrappedProcess proc = new WrappedProcess("kubectl", "--kubeconfig=\"c:\\Work\\Dela\\kubectl\\kubeconfig-" + env + "\"", "-n", env, "get", "pods");
        final Collection<String> pods = proc.flushToEnd();
        return pods.stream()
                .skip(1)
                .map(pod -> pod.split(" ")[0])
                .collect(Collectors.toList());
    }

    public Collection<String> contentForPods(final String env, String podName) {
        WrappedProcess pod = podLogs.get(podName);

        if (pod == null) {
            pod = new WrappedProcess("kubectl", "--kubeconfig=\"c:\\Work\\Dela\\kubectl\\kubeconfig-" + env + "\"", "logs", "-f", "-v8", podName);
            podLogs.put(podName, pod);
        }

        return pod.flush();
    }
}
