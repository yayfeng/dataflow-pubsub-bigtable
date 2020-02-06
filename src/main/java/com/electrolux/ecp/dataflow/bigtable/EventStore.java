package com.electrolux.ecp.dataflow.bigtable;

import com.google.cloud.bigtable.beam.CloudBigtableIO;
import com.google.cloud.bigtable.beam.CloudBigtableTableConfiguration;
import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.io.gcp.pubsub.PubsubIO;
import org.apache.beam.sdk.transforms.ParDo;

import java.text.MessageFormat;

public class EventStore {

    private static final String PROJECT_ID = "gcp-poc-265809";
    private static final String PUBSUB_TOPIC = "data_stream";
    private static final String INSTANCE_ID = "test-emea";
    private static final String TABLE_ID = "EVENTS";

    public static void main(String[] args) throws Exception {

        CloudBigtableTableConfiguration bigtableConfig =
                new CloudBigtableTableConfiguration.Builder()
                        .withProjectId(PROJECT_ID)
                        .withInstanceId(INSTANCE_ID)
                        .withTableId(TABLE_ID)
                        .build();

        Pipeline p = Pipeline.create();
        p.apply(
                PubsubIO.readStrings().fromTopic(
                        MessageFormat.format("projects/{0}/topics/{1}", PROJECT_ID, PUBSUB_TOPIC)))
            .apply(ParDo.of(new RowGenerator()))
            .apply(CloudBigtableIO.writeToTable(bigtableConfig));

        p.run();
    }

}
