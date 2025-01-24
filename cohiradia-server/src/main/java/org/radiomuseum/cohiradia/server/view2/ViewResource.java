package org.radiomuseum.cohiradia.server.view2;

import io.quarkus.qute.CheckedTemplate;
import io.quarkus.qute.TemplateInstance;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.radiomuseum.cohiradia.meta.dat.DatFilename;
import org.radiomuseum.cohiradia.meta.descriptor.RecordingDescriptor;
import org.radiomuseum.cohiradia.meta.yaml.MetaData;
import org.radiomuseum.cohiradia.server.api.MetadataCache;
import org.radiomuseum.cohiradia.server.view.PathUtil;

import java.util.List;

@Path("/view2/recordings")
public class ViewResource {

    @Inject
    MetadataCache cache;

    @Inject
    PathUtil pathUtil;

    @CheckedTemplate(requireTypeSafeExpressions = false)
    public static class Templates {
        public static native TemplateInstance recording(RecordingDescriptor descriptor, MetaData metadata, String name, String waveName);
        public static native TemplateInstance recordings(List<RecordingDescriptor> descriptors);
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance getRecording(@PathParam("id") int id) {

        var name = pathUtil.getPath(id);
        var descriptor = cache.getDescriptor(id);
        var metadata = cache.get(id);
        var title = metadata.map(MetaData::getContent).orElse(descriptor.name());
        var waveName = DatFilename.generate(descriptor).replace(".dat", "");
        return Templates.recording(descriptor, metadata.get(), name, waveName);
    }

    @GET
    @Path("/")
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance getRecordings() {
        var descriptors = cache.getAllDescriptors();
        return Templates.recordings(descriptors.toList());
    }
}