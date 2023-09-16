package org.radiomuseum.cohiradia.server.view;

import org.radiomuseum.cohiradia.meta.yaml.MetaData;
import org.radiomuseum.cohiradia.server.api.MetadataCache;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

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
