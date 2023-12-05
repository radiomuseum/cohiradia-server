package org.radiomuseum.cohiradia.server.view;

import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import org.radiomuseum.cohiradia.meta.yaml.MetaData;
import org.radiomuseum.cohiradia.server.api.MetadataCache;

@Dependent
public class PathUtil {

    @Inject
    MetadataCache cache;

    public String getPath(int i) {
        return cache.get(i)
                .map(this::getTargetPath)
                .orElseThrow(() -> new IllegalArgumentException("unknown id."));
    }

    private String getTargetPath(MetaData metaData) {
        return Integer.toString(metaData.getId());
    }

}
