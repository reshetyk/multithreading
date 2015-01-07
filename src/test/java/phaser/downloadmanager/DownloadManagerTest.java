package phaser.downloadmanager;

import org.junit.Test;

import java.net.URL;
import java.util.concurrent.Phaser;

public class DownloadManagerTest {

    @Test
    public void testDownloadFile() throws Exception {
        final DownloadManager downloadManager = new DownloadManager(new Phaser(), new URL("http://filesserver.com/big_movie.avi"));
        downloadManager.downloadFile();
    }
}