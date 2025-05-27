package com.anhtester.handles.RecordVideoHeadless;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.net.URL;

/**
 * For FFmpeg:
 * Download FFmpeg from https://ffmpeg.org/download.html
 * Add to Windows PATH environment variable
 * Test with "ffmpeg -version" in command prompt
 **/

public class SeleniumVideoRecording {

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

            System.out.println("Starting headless test with smooth video recording: " + testName);

            // Tạo folder cho screenshots
            String screenshotDir = "screenshots/" + testName + "_" + System.currentTimeMillis();
            new java.io.File(screenshotDir).mkdirs();

            int frameCount = 0;

            System.out.println("Step 1: Loading Google...");
            driver.get("https://www.google.com");

            // Chụp nhiều frame hơn để mượt (10 frames)
            for (int i = 0; i < 10; i++) {
                takeScreenshot(screenshotDir, frameCount++);
                Thread.sleep(200); // 5 FPS
            }

            // Wait for page to load completely
            Thread.sleep(1000);
            for (int i = 0; i < 5; i++) {
                takeScreenshot(screenshotDir, frameCount++);
                Thread.sleep(200);
            }

            System.out.println("Step 2: Navigating to GitHub...");
            driver.get("https://www.github.com");

            // Transition frames
            for (int i = 0; i < 10; i++) {
                takeScreenshot(screenshotDir, frameCount++);
                Thread.sleep(200);
            }

            // Wait for GitHub to load
            Thread.sleep(2000);
            for (int i = 0; i < 5; i++) {
                takeScreenshot(screenshotDir, frameCount++);
                Thread.sleep(200);
            }

            System.out.println("Step 3: Scrolling page smoothly...");
            // Smooth scrolling with multiple frames
            for (int scroll = 0; scroll <= 5; scroll++) {
                ((org.openqa.selenium.JavascriptExecutor) driver)
                        .executeScript("window.scrollTo(0, " + (scroll * 200) + ")");

                // Capture scroll animation
                for (int i = 0; i < 3; i++) {
                    takeScreenshot(screenshotDir, frameCount++);
                    Thread.sleep(150);
                }
            }

            System.out.println("Step 4: Form interaction simulation...");
            try {
                // Try to find search box and simulate typing
                org.openqa.selenium.WebElement searchBox = driver.findElement(
                        org.openqa.selenium.By.name("q")
                );

                String searchText = "selenium";
                for (int i = 0; i <= searchText.length(); i++) {
                    searchBox.clear();
                    searchBox.sendKeys(searchText.substring(0, i));
                    takeScreenshot(screenshotDir, frameCount++);
                    Thread.sleep(300); // Slower for typing effect
                }
            } catch (Exception e) {
                System.out.println("Search box not found, continuing...");
                // Just take some more frames
                for (int i = 0; i < 5; i++) {
                    takeScreenshot(screenshotDir, frameCount++);
                    Thread.sleep(200);
                }
            }

            System.out.println("Total frames captured: " + frameCount);
            System.out.println("Step 5: Converting screenshots to smooth video...");
            convertScreenshotsToSmoothVideo(screenshotDir, testName, frameCount);

            System.out.println("Smooth headless test completed with video: " + testName);

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

    private void convertScreenshotsToSmoothVideo(String screenshotDir, String testName, int totalFrames) {
        try {
            String outputVideo = "recordings/" + testName + "_smooth_" + System.currentTimeMillis() + ".mp4";
            new java.io.File("recordings").mkdirs();

            System.out.println("Converting " + totalFrames + " screenshots to smooth video: " + outputVideo);

            String[] command = {
                    "ffmpeg",
                    "-framerate", "5",  // 5 FPS cho mượt hơn
                    "-i", screenshotDir + "/frame_%04d.png",
                    "-c:v", "libx264",
                    "-pix_fmt", "yuv420p",
                    "-vf", "scale=1920:1080,minterpolate=fps=30:mi_mode=mci", // Tăng lên 30fps bằng interpolation
                    "-crf", "18",  // Chất lượng cao hơn (thấp hơn = tốt hơn)
                    "-preset", "slow",  // Encoding chậm hơn nhưng chất lượng tốt
                    "-movflags", "+faststart",  // Tối ưu cho streaming
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
            System.out.println("FFmpeg processing (this may take a moment)...");
            while ((line = reader.readLine()) != null) {
                if (line.contains("frame=") || line.contains("time=")) {
                    System.out.println("FFmpeg progress: " + line.trim());
                }
            }

            boolean finished = process.waitFor(60, TimeUnit.SECONDS); // Tăng timeout cho interpolation
            if (!finished) {
                process.destroyForcibly();
                System.err.println("FFmpeg process timed out after 60 seconds");

                // Fallback to simple conversion
                convertScreenshotsToVideo(screenshotDir, testName + "_simple");
            } else {
                System.out.println("Smooth video created successfully: " + outputVideo);

                // Show file size
                java.io.File videoFile = new java.io.File(outputVideo);
                if (videoFile.exists()) {
                    System.out.println("Video size: " + (videoFile.length() / 1024 / 1024) + " MB");
                }
            }

        } catch (Exception e) {
            System.err.println("Error creating smooth video: " + e.getMessage());
            e.printStackTrace();

            // Fallback to basic conversion
            System.out.println("Falling back to basic video conversion...");
            convertScreenshotsToVideo(screenshotDir, testName + "_fallback");
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
        SeleniumVideoRecording recorder = new SeleniumVideoRecording();
        recorder.runTestWithScreenshotSequence("HeadlessScreenshotTest");

    }
}