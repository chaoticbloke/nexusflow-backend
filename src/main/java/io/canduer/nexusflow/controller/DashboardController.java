package io.canduer.nexusflow.controller;

import io.canduer.nexusflow.dto.DashboardResponse;
import io.canduer.nexusflow.service.Impl.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {
    private final DashboardService dashboardService;

    @GetMapping("/stats")
    public DashboardResponse getDashBoardStats() throws Exception {
        return dashboardService.buildDashboard();
    }

}
