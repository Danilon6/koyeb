package it.danilo.blog.presentationlayer.api.exceptions;

public class FileSizeExceededException extends RuntimeException {
    public FileSizeExceededException() {
        super("File size exceeds the maximum allowed size");
    }
}
