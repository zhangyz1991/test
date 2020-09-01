package com.vick.test.util.jdbc.framework.io;

import com.vick.test.util.jdbc.framework.Nullable;
import com.vick.test.util.jdbc.framework.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;

/**
 * @author Vick Zhang
 * @create 2020/8/28
 */
public class FileUrlResource extends UrlResource implements WritableResource {

    @Nullable
    private volatile File file;


    /**
     * Create a new {@code FileUrlResource} based on the given URL object.
     * <p>Note that this does not enforce "file" as URL protocol. If a protocol
     * is known to be resolvable to a file,
     * @param url a URL
     * see com.vick.test.util.jdbc.framework.ResourceUtils#isFileURL(URL)
     * @see #getFile()
     */
    public FileUrlResource(URL url) {
        super(url);
    }

    /**
     * Create a new {@code FileUrlResource} based on the given file location,
     * using the URL protocol "file".
     * <p>The given parts will automatically get encoded if necessary.
     * @param location the location (i.e. the file path within that protocol)
     * @throws MalformedURLException if the given URL specification is not valid
     * @see UrlResource#UrlResource(String, String)
     * see com.vick.test.util.jdbc.framework.ResourceUtils#URL_PROTOCOL_FILE
     */
    public FileUrlResource(String location) throws MalformedURLException {
        super(ResourceUtils.URL_PROTOCOL_FILE, location);
    }


    @Override
    public File getFile() throws IOException {
        File file = this.file;
        if (file != null) {
            return file;
        }
        file = super.getFile();
        this.file = file;
        return file;
    }

    @Override
    public boolean isWritable() {
        try {
            URL url = getURL();
            if (ResourceUtils.isFileURL(url)) {
                // Proceed with file system resolution
                File file = getFile();
                return (file.canWrite() && !file.isDirectory());
            }
            else {
                return true;
            }
        }
        catch (IOException ex) {
            return false;
        }
    }

    @Override
    public OutputStream getOutputStream() throws IOException {
        return Files.newOutputStream(getFile().toPath());
    }

    @Override
    public WritableByteChannel writableChannel() throws IOException {
        return FileChannel.open(getFile().toPath(), StandardOpenOption.WRITE);
    }

    @Override
    public Resource createRelative(String relativePath) throws MalformedURLException {
        return new FileUrlResource(createRelativeURL(relativePath));
    }

}
