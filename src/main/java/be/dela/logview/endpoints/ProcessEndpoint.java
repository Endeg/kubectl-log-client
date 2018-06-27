package be.dela.logview.endpoints;

import be.dela.logview.proc.WrappedProcess;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "log")
public class ProcessEndpoint {

    private static Map<String, WrappedProcess> podLogs = new HashMap<>();

    @GetMapping
    @RequestMapping(path = "pods")
    public Collection<String> pods() {
        final WrappedProcess proc = new WrappedProcess("kubectl", "--kubeconfig=\"c:\\Work\\Dela\\kubectl\\kubeconfig-dev\"", "-n", "dev", "get", "pods");
        final Collection<String> pods = proc.flushToEnd();
        return pods.stream()
                .skip(1)
                .map(pod -> pod.split(" ")[0])
                .collect(Collectors.toList());
    }

    @GetMapping
    @RequestMapping(path = "pods/{podName}")
    public Collection<String> contentForPods(@PathVariable("podName") String podName) {
        WrappedProcess pod = podLogs.get(podName);

        if (pod == null) {
            pod = new WrappedProcess("kubectl", "--kubeconfig=\"c:\\Work\\Dela\\kubectl\\kubeconfig-dev\"", "logs", "-f", "-v8", podName);
            podLogs.put(podName, pod);
        }

        return pod.flush();
    }
}
