package com.vick.test.util.jdbc.framework.io;

import com.vick.test.util.jdbc.framework.Assert;
import com.vick.test.util.jdbc.framework.NestedIOException;
import com.vick.test.util.jdbc.framework.Nullable;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;

/**
 * @author Vick Zhang
 * @create 2020/8/28
 */
public class VfsResource extends AbstractResource {

    private final Object resource;


    /**
     * Create a new {@code VfsResource} wrapping the given resource handle.
     * @param resource a {@code org.jboss.vfs.VirtualFile} instance
     * (untyped in order to avoid a static dependency on the VFS API)
     */
    public VfsResource(Object resource) {
        Assert.notNull(resource, "VirtualFile must not be null");
        this.resource = resource;
    }


    @Override
    public InputStream getInputStream() throws IOException {
        return VfsUtils.getInputStream(this.resource);
    }

    @Override
    public boolean exists() {
        return VfsUtils.exists(this.resource);
    }

    @Override
    public boolean isReadable() {
        return VfsUtils.isReadable(this.resource);
    }

    @Override
    public URL getURL() throws IOException {
        try {
            return VfsUtils.getURL(this.resource);
        }
        catch (Exception ex) {
            throw new NestedIOException("Failed to obtain URL for file " + this.resource, ex);
        }
    }

    @Override
    public URI getURI() throws IOException {
        try {
            return VfsUtils.getURI(this.resource);
        }
        catch (Exception ex) {
            throw new NestedIOException("Failed to obtain URI for " + this.resource, ex);
        }
    }

    @Override
    public File getFile() throws IOException {
        return VfsUtils.getFile(this.resource);
    }

    @Override
    public long contentLength() throws IOException {
        return VfsUtils.getSize(this.resource);
    }

    @Override
    public long lastModified() throws IOException {
        return VfsUtils.getLastModified(this.resource);
    }

    @Override
    public Resource createRelative(String relativePath) throws IOException {
        if (!relativePath.startsWith(".") && relativePath.contains("/")) {
            try {
                return new VfsResource(VfsUtils.getChild(this.resource, relativePath));
            }
            catch (IOException ex) {
                // fall back to getRelative
            }
        }

        return new VfsResource(VfsUtils.getRelative(new URL(getURL(), relativePath)));
    }

    @Override
    public String getFilename() {
        return VfsUtils.getName(this.resource);
    }

    @Override
    public String getDescription() {
        return "VFS resource [" + this.resource + "]";
    }

    @Override
    public boolean equals(@Nullable Object other) {
        return (this == other || (other instanceof VfsResource &&
                this.resource.equals(((VfsResource) other).resource)));
    }

    @Override
    public int hashCode() {
        return this.resource.hashCode();
    }

}
