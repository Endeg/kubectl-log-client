package be.dela.logview;

import be.dela.logview.endpoints.ProcessEndpoint;

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
            while (true) {
                endpoint.contentForPods(env, podName).forEach(System.out::println);
                Thread.sleep(400L);
            }
        } else {
            System.out.println("Too many args!");
        }
    }
}
