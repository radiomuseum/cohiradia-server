package org.radiomuseum.cohiradia.meta.descriptor;

import java.io.File;
import java.io.IOException;

public interface DescriptorRepository {
    RecordingDescriptor read(File file);

    RecordingDescriptor readDescriptorInPath(File path) throws IOException;

    void write(RecordingDescriptor descriptor, File destination);
}
