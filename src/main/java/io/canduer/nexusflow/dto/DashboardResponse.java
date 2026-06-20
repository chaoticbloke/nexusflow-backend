package io.canduer.nexusflow.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class DashboardResponse implements Serializable {
    private static final long serialVersionUID = 1L;
    String customerStats;
    String invoiceStats;
}
