package socks5.monitor;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MemoryMonitor {
    private static final Runtime runtime = Runtime.getRuntime();

    public static void logMemoryUsage() {
        long maxMemory = runtime.maxMemory();
        long totalMemory = runtime.totalMemory();
        long freeMemory = runtime.freeMemory();
        long usedMemory = totalMemory - freeMemory;
        log.info("Memory: used={}MB / total={}MB / max={}MB ({} used)",
                usedMemory / 1024 / 1024,
                totalMemory / 1024 / 1024,
                maxMemory / 1024 / 1024,
                (usedMemory * 100.0) / maxMemory);
    }
}
