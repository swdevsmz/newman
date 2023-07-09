import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RunParallel {

    public static void main(String... args) {

        ExecutorService execService = null;

        try {
            // 固定数のスレッドで処理を行うオブジェクトを取得
            execService = Executors.newFixedThreadPool(200);

            // 実行
            for (int i = 0; i < 100; i++) {
                // 戻り値なし（ execute ）でタスクを実行
                execService.execute(new Newman());
            }

        } finally {
            // ExecutorServiceは明示的に終了する必要がある
            execService.shutdown();
            System.out.println("ExecutorService をシャットダウン");
        }
    }
}

class Newman implements Runnable {

    public void run() {

        String[] command = { "newman.cmd", "run", "collection.json" }; // 起動コマンドを指定する
        Runtime runtime = Runtime.getRuntime(); // ランタイムオブジェクトを取得する
        Process process;
        try {
            process = runtime.exec(command); // 指定したコマンドを実行する
            InputStream is = process.getInputStream();
            InputStreamReader isr = new InputStreamReader(is, "UTF-8");
            BufferedReader reader = new BufferedReader(isr);
            StringBuilder builder = new StringBuilder();
            int c;
            while ((c = reader.read()) != -1) {
                builder.append((char) c);
            }
            // コンソール出力される文字列の格納
            String text = builder.toString();
            // 終了コードの格納(0:正常終了 1:異常終了)
            int ret = process.waitFor();
            System.out.println(text);
            System.out.println(ret);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}