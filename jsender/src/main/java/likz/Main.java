package likz;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Date;
import java.util.Random;

public class Main {

    final static String path = "/jsons";
    static int counter = 0;

    public static void main(String[] args) throws InterruptedException, IOException {

        System.out.println("--5S remaining--");
        Thread.sleep(5000);
        System.out.println("--Server started to create files--");
        while (true) {
            JSONObject json = createJSON();
            int c_id = json.getInt("chocolate_id");
            double rating = json.getDouble("rating");
            System.out.println("chocolate_id = " + c_id + ", rating = " + rating);

            Configuration conf = new Configuration();
            conf.set("fs.defaultFS", "hdfs://namenode:9000");

            FileSystem fs = FileSystem.get(conf);
            OutputStream os = fs.create(new Path(path + "/json_" + counter++));
            try(OutputStreamWriter writer = new OutputStreamWriter(os)){
                writer.write(json.toString());
            }
            os.close();

            System.out.println("Json created! [" + path + "/json_" + counter + "]");
            Thread.sleep(2500);
        }

    }

    static JSONObject createJSON() {
        JSONObject data = new JSONObject();
        data.put("id", 1);
        data.put("chocolate_id", new Random().nextInt(10) + 1);
        data.put("maker_id", 1);
        data.put("rating", 2.0 + new Random().nextDouble() * (5.0 - 2.0));
        data.put("review_text", "asdasdsadsadasd");
        data.put("review_date", "2023-11-08");
        data.put("purchase_place", "The name");

        data.put("time_stamp", new Date().getTime() / 1000);

        JSONObject user = new JSONObject();
        user.put("name", "Kirill Antonov");
        user.put("age", 20);
        user.put("gender", "M");
        user.put("city", "Saratov");

        data.put("user", user);

        return data;
    }

//    static void unused() throws IOException {
//        ServerSocket server = new ServerSocket( 9999);
//        System.out.println("-----------SERVER ON");
//        System.out.println("-----------" + server.getInetAddress() + ":" + server.getLocalPort());
//        Socket client = server.accept();
//
//        ScheduledExecutorService exec = Executors.newScheduledThreadPool(1);
//        exec.scheduleAtFixedRate(() -> {
//            try {
//                System.out.println("----------------------ACTIVE");
//                JSONObject data = createJSON();
//
//                OutputStream outputStream = client.getOutputStream();
//                System.out.println("----------------------chocolate_id = " + data.getInt("chocolate_id") + " | "
//                        + "rating = " + data.getDouble("rating"));
//                outputStream.write((data.toString() + "\n").getBytes(StandardCharsets.UTF_8));
//
//                outputStream.flush();
//            } catch (IOException e) {
//                System.out.println("something goin wrong");
//                e.printStackTrace();
//            }
//        }, 0, 5, TimeUnit.SECONDS);
//    }
}
