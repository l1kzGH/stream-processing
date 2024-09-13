package likz;


import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.streaming.StreamingQuery;
import org.apache.spark.sql.streaming.StreamingQueryException;
import org.apache.spark.sql.streaming.Trigger;
import org.apache.spark.sql.types.StructType;

import java.util.concurrent.TimeoutException;

import static org.apache.spark.sql.functions.*;

public class Main {
    public static void main(String[] args) throws TimeoutException, StreamingQueryException {

        SparkSession spark = SparkSession.builder()
                .appName("SparkStreaming")
                .getOrCreate();

        StructType jsonSchema = new StructType()
                .add("id", "integer")
                .add("chocolate_id", "integer")
                .add("maker_id", "integer")
                .add("rating", "double")
                .add("review_text", "string")
                .add("review_date", "string")
                .add("purchase_place", "string")
                .add("time_stamp", "timestamp")
                .add("user", new StructType()
                        .add("name", "string")
                        .add("age", "integer")
                        .add("gender", "string")
                        .add("city", "string")
                );

//        Dataset<Row> data = spark
//                .readStream()
//                .format("socket")
//                .option("host", "0.0.0.0")
//                .option("port", 9999)
//                .load()
//                .selectExpr("CAST(value AS STRING) as jsonString")
//                .select(from_json(col("jsonString"), jsonSchema).as("jsonData"))
//                .select("jsonData.*")
//                .withWatermark("time_stamp", "10 seconds");
//        data.printSchema();

        Dataset<Row> data = spark
                .readStream()
                .schema(jsonSchema)
                .json("/jsons")
                .withWatermark("time_stamp", "10 seconds");
        data.printSchema();

        Dataset<Row> result = data
                .groupBy(window(col("time_stamp"), "1 minute", "30 seconds"), col("chocolate_id"))
                .agg(avg("rating").as("avg_rating"));

//        StreamingQuery queryCons = data
//                .writeStream()
//                .format("console")
//                .trigger(Trigger.ProcessingTime("30 seconds"))
//                .start();
//
//        queryCons.awaitTermination();
//        queryCons.stop();

        StreamingQuery query = result
                .coalesce(1)
                .writeStream()
                .outputMode("append")
                .format("parquet")
                .option("checkpointLocation", "checkpoint")
                .option("path", "out")
                .trigger(Trigger.ProcessingTime("10 seconds"))
                .start();

        query.awaitTermination();
        query.stop();

    }
}