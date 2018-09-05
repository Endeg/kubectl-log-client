package be.dela.logview;

import be.dela.logview.endpoints.ProcessEndpoint;

import java.util.List;
import java.util.stream.Collectors;

public class LogviewApplication {

    public static void main(String[] args) throws InterruptedException {
        if (args.length == 0) {
            System.out.println("Pass environment (dev/test).");
        } else if (args.length == 1) {
            final ProcessEndpoint endpoint = new ProcessEndpoint();

            final String env = args[0];
            System.out.println("Available pods for " + env + ":");
            endpoint.pods(env).forEach(pod -> System.out.println("\t" + pod));
        } else if (args.length == 2) {
            final ProcessEndpoint endpoint = new ProcessEndpoint();
            final String env = args[0];
            final String podName = args[1];

            final List<String> viewedPods = endpoint.pods(env).stream()
                    .filter(pod -> pod.startsWith(podName))
                    .collect(Collectors.toList());

            while (true) {
                for (String viewedPod : viewedPods) {
                    endpoint.contentForPods(env, viewedPod).stream()
                            .map(line -> "[" + viewedPod + "] - " + line)
                            .forEach(System.out::println);
                }
                Thread.sleep(400L);
            }
        } else {
            System.out.println("Too many args!");
        }
    }
}
