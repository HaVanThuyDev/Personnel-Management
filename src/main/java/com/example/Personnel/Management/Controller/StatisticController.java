package com.example.Personnel.Management.Controller;

import com.example.Personnel.Management.Service.StatisticService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/statistics")
@RequiredArgsConstructor
public class StatisticController {

    private final StatisticService statisticService;

    @GetMapping("/summary")
    public ResponseEntity<Map<String, String>> getStatisticsSummary() {
        try {
            Map<String, String> stats = Map.of(
                    "totalEmployees", String.valueOf(statisticService.getTotalEmployees()),
                    "totalDepartments", String.valueOf(statisticService.getTotalDepartments()),
                    "totalUsers", String.valueOf(statisticService.getTotalUsers()),
                    "totalContracts", String.valueOf(statisticService.getTotalContracts()),
                    "totalPayrollAmount", String.valueOf(statisticService.getTotalPayrollAmount())
            );
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of(
                    "message", "Đã xảy ra lỗi khi lấy thống kê!"
            ));
        }
    }
}
