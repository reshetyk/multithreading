package phaser.downloadmanager;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Phaser;

/**
 * @author Alexey
 */
public class DownloadManager {
    private Phaser phaser;
    private URL url;

    public DownloadManager(Phaser phaser, URL url) {
        this.phaser = phaser;
        this.url = url;
    }

    public void downloadFile() {
        List<PartDownloader.Part> parts = divideFileIntoParts(url);
        phaser.bulkRegister(parts.size()); //register participants
        //start downloading each part in separated thread
        for(PartDownloader.Part part : parts) {
            new Thread(new PartDownloader(part, phaser)).start();
        }
        phaser.awaitAdvance(phaser.getPhase()); //wait here other participants
        mergeDownloadedParts(); //merge after all
    }

    private void mergeDownloadedParts() {
        System.out.println("Merging downloaded parts...");
        System.out.println("File '" + url + "' was successfully downloaded!");
    }

    private List<PartDownloader.Part> divideFileIntoParts(URL url) {
        List<PartDownloader.Part> parts = new ArrayList<PartDownloader.Part>();
        parts.add(new PartDownloader.Part(url, 1, 2));
        parts.add(new PartDownloader.Part(url, 3, 4));
        parts.add(new PartDownloader.Part(url, 5, 6));
        parts.add(new PartDownloader.Part(url, 7, 8));
        parts.add(new PartDownloader.Part(url, 9, 10));
        return parts;
    }
}