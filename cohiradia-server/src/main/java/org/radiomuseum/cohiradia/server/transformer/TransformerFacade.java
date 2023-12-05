package org.radiomuseum.cohiradia.server.transformer;

import io.quarkus.logging.Log;
import org.radiomuseum.cohiradia.meta.yaml.MetaData;

import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;

@Dependent
public class TransformerFacade {

    @Inject
    SDRUno2SDRUno transformSdrUno;

    @Inject
    Dat2SDRUno transformDat;

    public void transform(MetaData metaData) {
        Log.infof("Transform uri=[%s], id=[%s].", metaData.getUri(), metaData.getId());
        if (metaData.getUri().endsWith(".wav")) {
            transformSdrUno.transformChecked(metaData);
        } else if (metaData.getUri().endsWith(".dat")) {
            transformDat.transformChecked(metaData);
        } else {
            Log.errorf("Could not transform uri=[%s], id=[%s].", metaData.getUri(), metaData.getId());
            throw new IllegalStateException("Unknown file type. " + metaData.getUri());
        }
    }
}
