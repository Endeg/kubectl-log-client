package be.dela.logview.endpoints;

import be.dela.logview.proc.WrappedProcess;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "proc")
public class ProcessEndpoint {

    @GetMapping
    public void readProcess() {
        new WrappedProcess("kubectl", "--kubeconfig=\"c:\\Work\\Dela\\kubectl\\kubeconfig-dev\"", "-n", "dev", "get", "pods");
    }

}
