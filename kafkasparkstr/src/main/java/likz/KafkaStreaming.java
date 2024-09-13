package likz;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.functions;
import org.apache.spark.sql.streaming.StreamingQuery;
import org.apache.spark.sql.streaming.StreamingQueryException;
import org.apache.spark.sql.streaming.Trigger;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructType;

import java.util.concurrent.TimeoutException;

public class KafkaStreaming {

    private static final String KAFKA_SERVER = "kafka:9092";

    public static void main(String[] args) throws TimeoutException, StreamingQueryException {

        SparkSession spark = SparkSession.builder()
                .appName("KafkaSparkS")
                .getOrCreate();

        StructType jsonSchema = new StructType()
                .add("user_id", DataTypes.IntegerType)
                .add("chocolate_id", DataTypes.IntegerType)
                .add("rating", DataTypes.DoubleType)
                .add("review_text", DataTypes.StringType)
                .add("purchase_place", DataTypes.StringType)
                .add("review_date", DataTypes.StringType);

        Dataset<Row> data = spark
                .readStream()
                .format("kafka")
                .option("kafka.bootstrap.servers", KAFKA_SERVER)
                .option("subscribe", "reviews_unsorted")
                .load();
                //.selectExpr("CAST(value AS STRING) as value")
                //.select(functions.from_json(functions.col("value"), jsonSchema).as("data"))
                //.select("data.*");

        data.printSchema();

        StreamingQuery query = data.writeStream()
                .outputMode("append")
                .format("kafka")
                .option("kafka.bootstrap.servers", KAFKA_SERVER)
                .option("checkpointLocation", "checkpoint")
                .option("topic", "reviews")
                .partitionBy("value.chocolate_id")
                .trigger(Trigger.ProcessingTime("10 seconds"))
                .start();

        query.awaitTermination();
        query.stop();

    }
}
