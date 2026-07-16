package io.canduer.nexusflow.controller;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.info.BuildProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

@RestController
@RequestMapping("/api/system")
@RequiredArgsConstructor
public class SystemController {

    private final BuildProperties buildProperties;

    @GetMapping("/info")
    public Map<String, Object> info() {

        return Map.of(
                "application", buildProperties.getName(),
                "version", buildProperties.getVersion(),
                "artifact", buildProperties.getArtifact(),
                "group", buildProperties.getGroup(),
                "builtAt", buildProperties.getTime()
        );
    }
}
