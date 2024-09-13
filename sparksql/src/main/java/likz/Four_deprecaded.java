package likz;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructType;

import java.util.ArrayList;
import java.util.List;

public class Four_deprecaded {

    void unknown(){
        SparkSession spark = SparkSession.builder()
                .appName("ChocolateMarker")
                .master("yarn")
                .getOrCreate();

        JavaSparkContext sc = new JavaSparkContext(spark.sparkContext());

        StructType schemaCollection = new StructType()
                .add("id", DataTypes.IntegerType)
                .add("maker_id", DataTypes.IntegerType)
                .add("name", DataTypes.StringType)
                .add("structure", DataTypes.StringType)
                .add("price", DataTypes.IntegerType)
                .add("prodDate", DataTypes.StringType)
                .add("storageLifeDays", DataTypes.IntegerType);

        List<Row> chocolates = new ArrayList<>();
        chocolates.add(RowFactory.create(1, 1, "Nestle's Milk Chocolate", "White chocolate with vanilla and almond pieces", 240, "2023-11-02", 90));
        chocolates.add(RowFactory.create(2, 2, "Hershey's Milk Chocolate", "Milk chocolate with peanuts and caramel", 150, "2023-11-01", 60));
        chocolates.add(RowFactory.create(3, 3, "Cadbury Dairy Milk", "Milk chocolate with hazelnuts and raisins", 200, "2023-10-31", 90));
        chocolates.add(RowFactory.create(4, 4, "Lindt Swiss Chocolate", "Dark chocolate with orange and almond slivers", 300, "2023-10-30", 120));
        chocolates.add(RowFactory.create(5, 5, "Godiva Dark Chocolate", "Dark chocolate with raspberry and pistachio pieces", 400, "2023-10-29", 180));
        chocolates.add(RowFactory.create(6, 6, "Ferrero Rocher", "Milk chocolate with hazelnut filling and whole hazelnut", 500, "2023-10-28", 150));
        chocolates.add(RowFactory.create(7, 7, "Toblerone Milk Chocolate", "Milk chocolate with honey and almond nougat", 250, "2023-10-27", 90));
        chocolates.add(RowFactory.create(8, 1, "Nestle Crunch", "Milk chocolate with crisped rice", 120, "2023-10-26", 60));
        chocolates.add(RowFactory.create(9, 2, "Reese's Peanut Butter Cups", "Milk chocolate with peanut butter filling", 180, "2023-10-25", 90));
        chocolates.add(RowFactory.create(10, 3, "Cadbury Flake", "Milk chocolate with flaky texture", 150, "2023-10-24", 120));
        chocolates.add(RowFactory.create(11, 4, "Lindt Excellence 85%", "Dark chocolate with high cocoa content", 250, "2023-10-23", 180));
        chocolates.add(RowFactory.create(12, 5, "Godiva Milk Chocolate Hazelnut Oyster", "Milk chocolate with hazelnut praline filling", 300, "2023-10-22", 150));
        chocolates.add(RowFactory.create(13, 6, "Ferrero Raffaello", "White chocolate with coconut and almond filling", 400, "2023-10-21", 90));
        chocolates.add(RowFactory.create(14, 7, "Toblerone Dark Chocolate", "Dark chocolate with honey and almond nougat", 250, "2023-10-20", 120));
        chocolates.add(RowFactory.create(15, 8, "Ghirardelli Intense Dark 72%", "Dark chocolate with high cocoa content", 350, "2023-10-19", 180));
        chocolates.add(RowFactory.create(16, 9, "Milka Oreo", "Milk chocolate with Oreo cookie pieces", 200, "2023-10-18", 150));
        chocolates.add(RowFactory.create(17, 1, "Nestle KitKat", "Milk chocolate with wafer layers", 100, "2023-10-17", 90));

        JavaRDD<Row> chocoRDD = sc.parallelize(chocolates);
        Dataset<Row> chocoDS = spark.createDataFrame(chocoRDD, schemaCollection);

        StructType schemaJson = new StructType()
                .add("id", DataTypes.IntegerType)
                .add("name", DataTypes.StringType)
                .add("country", DataTypes.StringType)
                .add("logoPath", DataTypes.StringType)
                .add("description", DataTypes.StringType)
                .add("rating", DataTypes.DoubleType);

        Dataset<Row> makersDS = spark.read()
                .option("multiline", "true")
                .schema(schemaJson)
                .json("sparkdata/2.json");

        StructType schemaCsv = new StructType()
                .add("id", DataTypes.IntegerType)
                .add("chocolate_id", DataTypes.IntegerType)
                .add("rating", DataTypes.DoubleType)
                .add("numRevs", DataTypes.IntegerType)
                .add("lastUpdate", DataTypes.StringType);

        Dataset<Row> ratingDS = spark.read()
                .option("header", "true")
                .schema(schemaCsv)
                .csv("sparkdata/3.csv");

        chocoDS.createOrReplaceTempView("chocolates");
        ratingDS.createOrReplaceTempView("ratings");
        makersDS.createOrReplaceTempView("makers");

        Dataset<Row> request1 = spark.sql("SELECT * FROM ratings").orderBy("rating");
        request1.write().format("parquet").mode("overwrite").save("sqlres/1");

        Dataset<Row> req = spark.sql("SELECT * FROM chocolates");
        req.write().format("parquet").mode("overwrite").save("sqlres/test1");
        Dataset<Row> req2 = spark.sql("SELECT * FROM makers");
        req2.write().format("parquet").mode("overwrite").save("sqlres/test2");

        Dataset<Row> request2 = spark.sql(
                "SELECT c.id AS chocolateId, c.name AS chocolateName, c.price, " +
                        "m.id AS makerId, m.name AS makerName, m.country, m.rating FROM chocolates c " +
                        "LEFT OUTER JOIN makers m ON c.maker_id = m.id");
        request2.write().format("avro").mode("overwrite").save("sqlres/2");

        Dataset<Row> request3 = spark.sql(
                "SELECT c.name, price FROM chocolates c " +
                        "INNER JOIN makers m ON c.maker_id = m.id " +
                        "WHERE price >= 110 AND m.name LIKE 'Nestle%'");
        request3.write().format("parquet").mode("overwrite").save("sqlres/3");

        Dataset<Row> request4 = spark.sql(
                "SELECT rating, SUM(chocolatesNum) AS chocolatesNum FROM (SELECT CASE " +
                        "WHEN rating >= 3.5 AND rating < 4 THEN '< 4' " +
                        "WHEN rating >= 4 AND rating <= 4.5 THEN '4 - 4.5' " +
                        "WHEN rating > 4.5 AND rating <= 5 THEN '> 4.5' " +
                        "END AS rating, COUNT(*) AS chocolatesNum FROM ratings " +
                        "GROUP BY rating) " +
                        "GROUP BY rating");
        request4.write().format("parquet").mode("overwrite").save("sqlres/4");

        Dataset<Row> request5 = spark.sql(
                "SELECT c.name AS chocolateName, m.name AS makerName, AVG(m.rating + r.rating) AS avgRating, c.price FROM ratings r " +
                        "INNER JOIN chocolates c ON r.chocolate_id = c.id " +
                        "INNER JOIN makers m ON c.maker_id = m.id " +
                        "WHERE m.rating >= 4 AND r.rating >= 4 " +
                        "GROUP BY c.name, m.name, c.price");
        request5.write().format("parquet").mode("overwrite").save("sqlres/5");

        spark.stop();
    }


}
