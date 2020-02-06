package com.electrolux.ecp.dataflow.bigtable.model.bigtable;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ApplianceIdentifier {
    String pnc;
    String elc;
    String sn;
    String mac;
}
