package com.project.NovelWeb.exceptions;

public class MaximumMemoryExceededException extends RuntimeException {
    public MaximumMemoryExceededException() {
        super();
    }

    public MaximumMemoryExceededException(String message) {
        super(message);
    }

    public MaximumMemoryExceededException(String message, Throwable cause) {
        super(message, cause);
    }

    public MaximumMemoryExceededException(Throwable cause) {
        super(cause);
    }
}