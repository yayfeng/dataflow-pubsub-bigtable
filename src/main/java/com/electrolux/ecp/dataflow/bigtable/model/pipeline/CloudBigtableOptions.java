package com.electrolux.ecp.dataflow.bigtable.model.pipeline;

import org.apache.beam.runners.dataflow.options.DataflowPipelineOptions;
import org.apache.beam.sdk.options.Description;

public interface CloudBigtableOptions extends DataflowPipelineOptions {
    @Description("The Google Cloud project ID for the Cloud Bigtable instance.")
    String getBigtableProjectId();

    void setBigtableProjectId(String bigtableProjectId);

    @Description("The Google Cloud Bigtable instance ID .")
    String getBigtableInstanceId();

    void setBigtableInstanceId(String bigtableInstanceId);

    @Description("The Cloud Bigtable table ID in the instance.")
    String getBigtableTableId();

    void setBigtableTableId(String bigtableTableId);

}
