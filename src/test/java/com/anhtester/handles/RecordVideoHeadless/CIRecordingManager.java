// CIRecordingManager.java
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class CIRecordingManager {
    
    private Process ffmpegProcess;
    private WebDriver driver;
    private static final String CI_ENV = System.getenv("CI");
    private static final String GITHUB_ACTIONS = System.getenv("GITHUB_ACTIONS");
    private static final String OS_NAME = System.getProperty("os.name").toLowerCase();
    
    public boolean isRunningInCI() {
        return "true".equals(CI_ENV) || "true".equals(GITHUB_ACTIONS);
    }
    
    public void startRecording(String testName) throws IOException {
        if (!isRunningInCI()) {
            System.out.println("Not running in CI, skipping FFmpeg recording");
            return;
        }
        
        String outputFile = String.format("%s-recording-%d.mp4", 
                testName, System.currentTimeMillis());
        
        String[] command = getFFmpegCommand(outputFile);
        
        ProcessBuilder pb = new ProcessBuilder(command);
        pb.redirectErrorStream(true);
        
        ffmpegProcess = pb.start();
        System.out.println("Started FFmpeg recording: " + outputFile);
        
        // Wait for FFmpeg to initialize
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    private String[] getFFmpegCommand(String outputFile) {
        if (OS_NAME.contains("linux")) {
            // Linux with Xvfb
            String display = System.getenv("DISPLAY");
            if (display == null) display = ":99.0";
            
            return new String[]{
                "ffmpeg", "-f", "x11grab",
                "-video_size", "1920x1080",
                "-framerate", "15",
                "-i", display,
                "-c:v", "libx264",
                "-preset", "ultrafast",
                "-crf", "23",
                "-t", "300", // Max 5 minutes
                "-y", outputFile
            };
        } else if (OS_NAME.contains("windows")) {
            // Windows
            return new String[]{
                "ffmpeg", "-f", "gdigrab",
                "-framerate", "15",
                "-i", "desktop",
                "-c:v", "libx264",
                "-preset", "ultrafast",
                "-crf", "23",
                "-t", "300",
                "-y", outputFile
            };
        } else if (OS_NAME.contains("mac")) {
            // macOS
            return new String[]{
                "ffmpeg", "-f", "avfoundation",
                "-framerate", "15",
                "-i", "1:0",
                "-c:v", "libx264",
                "-preset", "ultrafast",
                "-crf", "23",
                "-t", "300",
                "-y", outputFile
            };
        }
        
        throw new UnsupportedOperationException("Unsupported OS: " + OS_NAME);
    }
    
    public void stopRecording() {
        if (ffmpegProcess != null && ffmpegProcess.isAlive()) {
            try {
                // Try graceful shutdown first
                ffmpegProcess.getOutputStream().write("q\n".getBytes());
                ffmpegProcess.getOutputStream().flush();
                
                boolean finished = ffmpegProcess.waitFor(10, TimeUnit.SECONDS);
                if (!finished) {
                    ffmpegProcess.destroyForcibly();
                }
                
                System.out.println("FFmpeg recording stopped");
            } catch (Exception e) {
                System.err.println("Error stopping FFmpeg: " + e.getMessage());
                ffmpegProcess.destroyForcibly();
            }
        }
    }
    
    public WebDriver initializeBrowserForCI() {
        ChromeOptions options = new ChromeOptions();
        
        if (isRunningInCI()) {
            // CI-specific options
            if (OS_NAME.contains("linux")) {
                // Linux - không dùng headless khi có Xvfb
                options.addArguments("--no-sandbox");
                options.addArguments("--disable-dev-shm-usage");
                options.addArguments("--disable-gpu");
            } else {
                // Windows/macOS - có thể dùng headless
                options.addArguments("--headless=new");
            }
            
            options.addArguments("--window-size=1920,1080");
            options.addArguments("--disable-extensions");
            options.addArguments("--disable-plugins");
            options.addArguments("--disable-images");
            options.addArguments("--remote-debugging-port=9222");
        }
        
        driver = new ChromeDriver(options);
        
        if (isRunningInCI() && !OS_NAME.contains("linux")) {
            // Set window size cho non-Linux CI environments
            driver.manage().window().setSize(new org.openqa.selenium.Dimension(1920, 1080));
        }
        
        return driver;
    }
    
    public void cleanup() {
        stopRecording();
        if (driver != null) {
            driver.quit();
        }
    }
    
    // Test method example
    public void runCITest(String testName) {
        try {
            startRecording(testName);
            
            WebDriver driver = initializeBrowserForCI();
            
            // Test steps
            driver.get("https://www.google.com");
            Thread.sleep(1000);
            
            driver.get("https://www.github.com");
            Thread.sleep(1000);
            
            System.out.println("Test completed: " + testName);
            
        } catch (Exception e) {
            System.err.println("Test error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            cleanup();
        }
    }
}