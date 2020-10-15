package code;

import java.util.HashMap;
import java.util.Map;

public class Test {
    public static void main(String[] args) {
        System.out.println("start");
        Map<Long, Long> map = new HashMap<>();
        final long max = (1L << 22);
        long time = System.currentTimeMillis();
        for (long i = 0; i < max; i++) {
            if (i % 0x400000 == 0) {
                System.out.println(i + " / " + max + " -> " + ((i * 100) / max) + "%");
            }
            long val = (long) (Math.random() * max) * 128L;
            map.put(val, val);
        }
        System.out.println("use " + (System.currentTimeMillis() - time));
    }
}
