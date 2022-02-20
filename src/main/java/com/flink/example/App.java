package com.flink.example;

import java.beans.Expression;
import java.util.Collection;
import java.util.List;
import java.util.Properties;
import com.ibm.icu.impl.duration.impl.DataRecord.ENumberSystem;
import org.apache.calcite.model.JsonSchema;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.functions.util.ListCollector;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.connector.kafka.source.enumerator.initializer.OffsetsInitializer;
import org.apache.flink.formats.avro.AvroDeserializationSchema;
import org.apache.flink.formats.avro.registry.confluent.ConfluentRegistryAvroDeserializationSchema;
import org.apache.flink.shaded.curator4.com.google.common.collect.ImmutableMap;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.filesystem.Bucket;
import org.apache.flink.table.api.TableDescriptor;
import org.apache.flink.table.api.TableResult;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.hl7.fhir.r4.model.Patient;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flink.example.com.trireme.Weather;
import com.flink.schema.ObservationTest;
import io.confluent.kafka.serializers.*;
import io.confluent.kafka.schemaregistry.avro.AvroSchema;

import org.apache.flink.table.api.Table;

/**
 * Hello world!
 *
 */
public class App {
  public static void main(String[] args) throws Exception {
    StreamExecutionEnvironment environment = StreamExecutionEnvironment.createLocalEnvironment();

    StreamTableEnvironment streamTableEnvironment = StreamTableEnvironment.create(environment);
    DataStream<Weather> exampleDataStream  = environment.fromElements("{\r\n    \r\n    \"wind_velocity\" : 5,\r\n    \"wind_direction\" : \"NorthEast\",\r\n    \"humidity\" : 25,\r\n    \"temperature\" : 35,\r\n    \"city\" : \"vadodara\"\r\n}")
    .map(new MapFunction<String,Weather>() {

      ObjectMapper objectMapper = new ObjectMapper();

      @Override
      public Weather map(String value) throws Exception {
        // TODO Auto-generated method stub
        return objectMapper.readValue(value, Weather.class);
      }
      
    });
    Table result = streamTableEnvironment.fromDataStream(exampleDataStream);
    
    Table cityResult = streamTableEnvironment.sqlQuery(
  "SELECT city  FROM " + result + " WHERE wind_velocity > 4 ");

    cityResult.distinct().execute().print();

    //environment.execute();

  }
}
