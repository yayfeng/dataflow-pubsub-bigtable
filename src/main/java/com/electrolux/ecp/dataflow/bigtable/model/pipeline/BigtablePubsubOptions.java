package com.electrolux.ecp.dataflow.bigtable.model.pipeline;

import org.apache.beam.sdk.options.Default;

public interface BigtablePubsubOptions extends CloudBigtableOptions {

    static final int WINDOW_SIZE = 1;

    @Default.Integer(WINDOW_SIZE)
    Integer getWindowSize();

    void setWindowSize(Integer value);

    String getPubsubTopic();

    void setPubsubTopic(String pubsubTopic);

    String getInputFile();

    void setInputFile(String location);

}
