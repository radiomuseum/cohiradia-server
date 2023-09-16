package org.radiomuseum.cohiradia.meta.lts;

public interface Storage {

    /**
     * Returns an existing {@link Bucket}.
     *
     * @param identifier
     * @return
     */
    Bucket getBucket(String identifier);

    /**
     * Creates a new {@link Bucket}.
     *
     * @param identifier
     * @return
     */
    Bucket createBucket(String identifier);

    /**
     * Creates a new Identifier
     */
    String createIdentifier();
}
