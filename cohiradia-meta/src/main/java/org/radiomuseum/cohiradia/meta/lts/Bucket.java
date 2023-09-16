package org.radiomuseum.cohiradia.meta.lts;

import org.radiomuseum.cohiradia.meta.descriptor.RecordingDescriptor;

import java.io.InputStream;
import java.util.List;

/**
 * A bucket encloses specific recordings and corresponding metadata.
 */
public interface Bucket {

    /**
     * Returns the identifier of a bucket.
     *
     * @return the identifier.
     */
    String getIdentifier();

    void addDescriptor(RecordingDescriptor descriptor);

    RecordingDescriptor getDescriptor();

    void addRecording(String name, InputStream outputStream);

    InputStream getRecording(String name);

    List<String> getRecordings();
}
