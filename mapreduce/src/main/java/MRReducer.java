import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public class MRReducer extends Reducer<Text, DoubleWritable, Text, DoubleWritable> {

    private DoubleWritable doubleRes = new DoubleWritable();

    @Override
    protected void reduce(Text key, Iterable<DoubleWritable> values,
                          Reducer<Text, DoubleWritable, Text,
                                  DoubleWritable>.Context context) throws IOException, InterruptedException {
        double sum = 0;
        int count = 0;

        for(DoubleWritable dw : values){
            sum += dw.get();
            count++;
        }
        doubleRes.set(sum / count);

        context.write(key, doubleRes);
    }

}
