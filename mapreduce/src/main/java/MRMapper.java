import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class MRMapper extends Mapper<LongWritable, Text, Text, DoubleWritable> {

    private Text text = new Text();
    private DoubleWritable doubleRes = new DoubleWritable();

    @Override
    protected void map(LongWritable key, Text value, Mapper<LongWritable, Text, Text, DoubleWritable>.Context context) throws IOException, InterruptedException {
        List<String> list = Arrays.asList(value.toString().split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)"));

        text.set(list.get(0));
        doubleRes.set(Double.parseDouble(list.get(6)));

        context.write(text, doubleRes);
    }
}
