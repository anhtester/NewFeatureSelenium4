package com.anhtester.handles.RecordVideoHeadless;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.IOException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

/**
 * For FFmpeg:
 * Download FFmpeg from https://ffmpeg.org/download.html
 * Add to Windows PATH environment variable
 * Test with "ffmpeg -version" in command prompt
 **/

public class SeleniumVideoRecordingV2 {

    private Process ffmpegProcess;
    private WebDriver driver;

    // GIẢI PHÁP 2: Sử dụng Virtual Display (Xvfb trên Linux/Docker)
    public void runTestWithVirtualDisplay(String testName) {
        String videoFileName = "recordings/" + testName + "_" + System.currentTimeMillis() + ".mp4";

        try {
            new java.io.File("recordings").mkdirs();

            // Start virtual display (chỉ work trên Linux)
            // Cần cài xvfb: sudo apt-get install xvfb
            startVirtualDisplay();

            // Start FFmpeg với virtual display
            startFFmpegVirtualDisplayRecording(videoFileName);

            driver = initializeHeadlessChrome();

            System.out.println("Starting test: " + testName);
            driver.get("https://www.google.com");
            Thread.sleep(3000);

            driver.get("https://www.github.com");
            Thread.sleep(3000);

            System.out.println("Test completed: " + testName);

        } catch (Exception e) {
            System.err.println("Error during test: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (driver != null) {
                driver.quit();
            }
            stopFFmpegRecording();
            stopVirtualDisplay();
        }
    }

    // GIẢI PHÁP 3: Sử dụng Selenium Grid với VNC
    public void runTestWithSeleniumGrid(String testName) {
        String videoFileName = "recordings/" + testName + "_" + System.currentTimeMillis() + ".mp4";

        try {
            new java.io.File("recordings").mkdirs();

            // Connect to Selenium Grid container có VNC
            DesiredCapabilities caps = new DesiredCapabilities();
            caps.setBrowserName("chrome");
            caps.setCapability("enableVNC", true);
            caps.setCapability("enableVideo", true);

            // Selenium Grid URL (ví dụ với Docker container)
            driver = new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"), caps);

            System.out.println("Starting test: " + testName);
            driver.get("https://www.google.com");
            Thread.sleep(3000);

            driver.get("https://www.github.com");
            Thread.sleep(3000);

            System.out.println("Test completed: " + testName);

        } catch (Exception e) {
            System.err.println("Error during test: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (driver != null) {
                driver.quit();
            }
        }
    }

    // GIẢI PHÁP CHÍNH: Screenshot sequence thành video (cho headless)
    public void runTestWithScreenshotSequence(String testName) {
        try {
            driver = initializeHeadlessChrome();

            System.out.println("Starting headless test with optimized video recording: " + testName);

            // Tạo folder cho screenshots
            String screenshotDir = "screenshots/" + testName + "_" + System.currentTimeMillis();
            new java.io.File(screenshotDir).mkdirs();

            int frameCount = 0;

            System.out.println("Step 1: Loading Google...");
            driver.get("https://www.google.com");

            // Chụp 5 frames cho Google load
            for (int i = 0; i < 5; i++) {
                takeScreenshot(screenshotDir, frameCount++);
                Thread.sleep(500); // 2 FPS
            }

            System.out.println("Step 2: Navigating to GitHub...");
            driver.get("https://www.github.com");

            // Chụp 5 frames cho GitHub load
            for (int i = 0; i < 5; i++) {
                takeScreenshot(screenshotDir, frameCount++);
                Thread.sleep(500);
            }

            System.out.println("Step 3: Scrolling page...");
            // Smooth scrolling với 5 steps
            for (int scroll = 0; scroll < 5; scroll++) {
                ((org.openqa.selenium.JavascriptExecutor) driver)
                        .executeScript("window.scrollTo(0, " + (scroll * 300) + ")");
                takeScreenshot(screenshotDir, frameCount++);
                Thread.sleep(500);
            }

            System.out.println("Step 4: Final frames...");
            // 5 frames cuối
            for (int i = 0; i < 5; i++) {
                takeScreenshot(screenshotDir, frameCount++);
                Thread.sleep(500);
            }

            System.out.println("Total frames captured: " + frameCount); // Should be 20
            System.out.println("Step 5: Converting screenshots to video...");
            convertScreenshotsToOptimizedVideo(screenshotDir, testName, frameCount);

            System.out.println("Optimized headless test completed with video: " + testName);

        } catch (Exception e) {
            System.err.println("Error during test: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (driver != null) {
                System.out.println("Closing browser...");
                driver.quit();
            }
        }
    }

    private void takeScreenshot(String dir, int frameNumber) {
        try {
            org.openqa.selenium.TakesScreenshot takesScreenshot = (org.openqa.selenium.TakesScreenshot) driver;
            byte[] screenshot = takesScreenshot.getScreenshotAs(org.openqa.selenium.OutputType.BYTES);

            java.io.FileOutputStream fos = new java.io.FileOutputStream(
                    dir + "/frame_" + String.format("%04d", frameNumber) + ".png"
            );
            fos.write(screenshot);
            fos.close();
        } catch (Exception e) {
            System.err.println("Error taking screenshot: " + e.getMessage());
        }
    }

    // Backup method - simple video conversion
    private void convertScreenshotsToVideo(String screenshotDir, String testName) {
        try {
            String outputVideo = "recordings/" + testName + "_" + System.currentTimeMillis() + ".mp4";
            new java.io.File("recordings").mkdirs();

            System.out.println("Converting screenshots to basic video: " + outputVideo);

            String[] command = {
                    "ffmpeg",
                    "-framerate", "3",  // 3 FPS - cân bằng giữa mượt và tốc độ
                    "-i", screenshotDir + "/frame_%04d.png",
                    "-c:v", "libx264",
                    "-pix_fmt", "yuv420p",
                    "-vf", "scale=1920:1080",
                    "-crf", "23",  // Chất lượng vừa phải
                    "-y",
                    outputVideo
            };

            ProcessBuilder pb = new ProcessBuilder(command);
            pb.redirectErrorStream(true);
            Process process = pb.start();

            boolean finished = process.waitFor(30, TimeUnit.SECONDS);
            if (!finished) {
                process.destroyForcibly();
                System.err.println("Basic FFmpeg process timed out");
            } else {
                System.out.println("Basic video created: " + outputVideo);
            }

        } catch (Exception e) {
            System.err.println("Error creating basic video: " + e.getMessage());
        }
    }

    private void convertScreenshotsToOptimizedVideo(String screenshotDir, String testName, int totalFrames) {
        try {
            String outputVideo = "recordings/" + testName + "_optimized_" + System.currentTimeMillis() + ".mp4";
            new java.io.File("recordings").mkdirs();

            System.out.println("Converting " + totalFrames + " screenshots to optimized video: " + outputVideo);

            String[] command = {
                    "ffmpeg",
                    "-framerate", "2",  // 2 FPS input (20 frames = 10 giây video)
                    "-i", screenshotDir + "/frame_%04d.png",
                    "-c:v", "libx264",
                    "-pix_fmt", "yuv420p",
                    "-vf", "scale=1920:1080",  // Giữ resolution cố định
                    "-crf", "20",  // Chất lượng tốt nhưng không quá cao
                    "-preset", "fast",  // Encoding nhanh
                    "-movflags", "+faststart",  // Tối ưu cho playback
                    "-y",
                    outputVideo
            };

            ProcessBuilder pb = new ProcessBuilder(command);
            pb.redirectErrorStream(true);
            Process process = pb.start();

            // Log FFmpeg output
            java.io.BufferedReader reader = new java.io.BufferedReader(
                    new java.io.InputStreamReader(process.getInputStream())
            );
            String line;
            System.out.println("FFmpeg processing...");
            while ((line = reader.readLine()) != null) {
                if (line.contains("frame=") || line.contains("time=")) {
                    System.out.println("Progress: " + line.trim());
                }
            }

            boolean finished = process.waitFor(30, TimeUnit.SECONDS);
            if (!finished) {
                process.destroyForcibly();
                System.err.println("FFmpeg process timed out");
            } else {
                System.out.println("Optimized video created successfully: " + outputVideo);

                // Show file info
                java.io.File videoFile = new java.io.File(outputVideo);
                if (videoFile.exists()) {
                    long fileSizeMB = videoFile.length() / 1024 / 1024;
                    //System.out.println("Video size: " + fileSizeMB + " MB");
                    System.out.println("Estimated duration: " + (totalFrames / 2) + " seconds");
                }
            }

        } catch (Exception e) {
            System.err.println("Error creating optimized video: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // FFmpeg methods
    public void startFFmpegDesktopRecording(String outputFileName) throws IOException {
        String[] command = {
                "ffmpeg",
                "-f", "gdigrab",
                "-framerate", "30",
                "-i", "desktop",
                "-video_size", "1920x1080",
                "-c:v", "libx264",
                "-preset", "ultrafast",
                "-crf", "18",
                "-y",
                outputFileName
        };

        ProcessBuilder processBuilder = new ProcessBuilder(command);
        processBuilder.redirectErrorStream(true);
        ffmpegProcess = processBuilder.start();

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void startFFmpegVirtualDisplayRecording(String outputFileName) throws IOException {
        String[] command = {
                "ffmpeg",
                "-f", "x11grab",  // Linux X11
                "-framerate", "30",
                "-s", "1920x1080",
                "-i", ":99.0",    // Virtual display :99
                "-c:v", "libx264",
                "-preset", "ultrafast",
                "-crf", "18",
                "-y",
                outputFileName
        };

        ProcessBuilder processBuilder = new ProcessBuilder(command);
        processBuilder.redirectErrorStream(true);
        ffmpegProcess = processBuilder.start();

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public WebDriver initializeHeadlessChrome() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new");
        options.addArguments("--window-size=1920,1080");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");

        return new ChromeDriver(options);
    }

    private void startVirtualDisplay() {
        try {
            // Start Xvfb virtual display
            String[] command = {"Xvfb", ":99", "-screen", "0", "1920x1080x24"};
            ProcessBuilder pb = new ProcessBuilder(command);
            pb.start();

            // Set DISPLAY environment variable
            System.setProperty("DISPLAY", ":99");

            Thread.sleep(2000);
        } catch (Exception e) {
            System.err.println("Error starting virtual display: " + e.getMessage());
        }
    }

    private void stopVirtualDisplay() {
        try {
            Runtime.getRuntime().exec("pkill Xvfb");
        } catch (IOException e) {
            System.err.println("Error stopping virtual display: " + e.getMessage());
        }
    }

    public void stopFFmpegRecording() {
        if (ffmpegProcess != null && ffmpegProcess.isAlive()) {
            try {
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

    public static void main(String[] args) {
        SeleniumVideoRecordingV2 recorder = new SeleniumVideoRecordingV2();
        recorder.runTestWithScreenshotSequence("HeadlessScreenshotTest");

    }
}