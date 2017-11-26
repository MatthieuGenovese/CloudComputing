package stockage.videos;

import com.google.appengine.tools.cloudstorage.GcsFilename;

import java.io.IOException;
import java.util.UUID;

public class VideoRemover extends Video {

    public boolean deleteVideo(String bucketName, UUID uuid) throws IOException {
        GcsFilename filename = new GcsFilename(bucketName, uuid.toString());

        return GCS_SERVICE.delete(filename);
    }
}
