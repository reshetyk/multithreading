package phaser.downloadmanager;

import org.junit.Test;

import java.net.URL;
import java.util.concurrent.Phaser;

public class DownloadManagerTest {

    @Test
    public void testDownloadFile() throws Exception {
        final DownloadManager downloadManager = new DownloadManager(new Phaser() {
            @Override
            protected boolean onAdvance(int phase, int registeredParties) {
                System.out.println("phase="+phase + " registered parties=" + registeredParties);
                return registeredParties == 0;
            }
        }, new URL("http://filesserver.com/big_movie.avi"));
        downloadManager.downloadFile();
    }
}