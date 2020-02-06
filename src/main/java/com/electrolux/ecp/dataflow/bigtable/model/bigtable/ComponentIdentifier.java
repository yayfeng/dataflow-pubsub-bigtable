package com.electrolux.ecp.dataflow.bigtable.model.bigtable;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ComponentIdentifier {
    String propertyName;
    String modulePath;
    String componentId;
}
