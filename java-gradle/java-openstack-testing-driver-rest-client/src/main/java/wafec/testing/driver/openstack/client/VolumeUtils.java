package wafec.testing.driver.openstack.client;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class VolumeUtils {
    private VolumeUtils() {

    }

    public static boolean hasChangedStatus(Volume a, Volume b) {
        return !Optional.ofNullable(a)
                .map(Volume::getStatus)
                .equals(Optional.ofNullable(b).map(Volume::getStatus))
                ||
                !Optional.ofNullable(a)
                .map(VolumeUtils::getAttachmentsSignature)
                .equals(Optional.ofNullable(b).map(VolumeUtils::getAttachmentsSignature));
    }

    private static String getAttachmentsSignature(Volume volume) {
        if (volume != null && volume.getAttachments() != null && volume.getAttachments().size() > 0)
            return volume.getAttachments().stream().map(Attachment::toString).collect(Collectors.joining());
        return "";
    }
}
