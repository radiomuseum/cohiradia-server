package org.radiomuseum.cohiradia.server.api;

import io.quarkus.logging.Log;
import org.apache.commons.io.FileUtils;
import org.eclipse.microprofile.context.ManagedExecutor;
import org.radiomuseum.cohiradia.meta.yaml.MetaData;
import org.radiomuseum.cohiradia.meta.yaml.YamlRepository;
import org.radiomuseum.cohiradia.server.ApplicationConfiguration;
import org.radiomuseum.cohiradia.server.transformer.TransformerFacade;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

@Path("/api/metadata")
@Produces(MediaType.APPLICATION_JSON)
public class MetaDataResource {

    @Inject
    MetadataCache cache;

    @Inject
    GitRepository gitRepository;

    @Inject
    ApplicationConfiguration config;

    @Inject
    ManagedExecutor executor;

    @Inject
    TransformerFacade transformer;

    @GET
    public Response getMetaDataList() {
        var result = cache.getAll().map(this::convert).toList();
        return Response.ok(result).build();
    }

    @POST
    @Path("/pull")
    public Response pullGitRepository() {
        if (gitRepository.pull()) {
            return Response.ok().build();
        } else {
            return Response.serverError().build();
        }
    }

    @POST
    @Path("/reload")
    public Response reloadCache() {
        cache.init();
        return Response.ok().build();
    }

    @POST
    @Path("/import")
    public Response importRecordings() {

        executor.submit(() -> {
            var repository = new YamlRepository();
            Arrays.stream(Objects.requireNonNull(config.basePathMetadata().listFiles()))
                    .filter(YamlRepository::isYaml)
                    .map(repository::read)
                    .forEach(transformer::transform);
            Log.infof("Import finished.");
            cache.init();
            Log.infof("Cache reload finished.");
        });
        return Response.ok().build();
    }

    @POST
    @Path("/import/{id}")
    public Response reImportRecording(@PathParam("id") int id) {

        var metaData = cache.get(id);
        if (metaData.isEmpty()) {
            return Response.serverError().build();
        } else {
            java.io.File originalPath = new File(config.basePathStorage(), metaData.get().getUri());
            if (originalPath.exists()) {
                Log.infof("Original directory=[%s] exists.", originalPath.getAbsolutePath());

                var target = new File(config.basePathDescriptor(), Integer.toString(id));
                try {
                    Log.infof("Delete directory=[%s]", target.getAbsolutePath());
                    FileUtils.deleteDirectory(target);
                    executor.submit(() -> {
                        transformer.transform(metaData.get());
                        cache.init();
                    });
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return Response.ok().build();
    }

    @GET
    @Path("/{id}")
    public Response getData(@PathParam("id") int id) {
        return cache.get(id)
                .map(Response::ok)
                .orElse(Response.serverError())
                .build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/info-descriptors")
    public Response getInfoDescriptors() {
        return Response.ok(cache.getAllDescriptors().count()).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/info-metadata")
    public Response getInfoMetadata() {
        return Response.ok(cache.getAll().count()).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}/attachments")
    public Response getAttachment(@PathParam("id") int id) {
        var descriptor = cache.getDescriptor(id);
        if (descriptor == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        var attachmentDir = new File(new File(descriptor.basePath()), "attachments");
        var result = new ArrayList<>();
        if (attachmentDir.exists()) {
            for (File file : Objects.requireNonNull(attachmentDir.listFiles())) {
                var parent = file.getParentFile().getParentFile().getName();
                result.add(AttachmentDto.builder()
                        .name(file.getName())
                        .url(String.format("https://cohiradia.radiomuseum.org/download/data/%s/attachments/%s", parent, file.getName()))
                        .build());
            }
        }

        return Response.ok(result).build();
    }

    private MetaDataSummaryDto convert(MetaData metadata) {
        return MetaDataSummaryDto.builder()
                .startTimestamp(metadata.getRecordingDate().toString())
                .content(metadata.getContent())
                .band(metadata.getBand())
                .country(metadata.getLocationCountry())
                .id(metadata.getId())
                .city(metadata.getLocationCity()).build();
    }

}
