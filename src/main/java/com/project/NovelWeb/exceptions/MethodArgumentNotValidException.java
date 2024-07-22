package com.project.NovelWeb.exceptions;

import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;

public class MethodArgumentNotValidException extends BindException
{
    public MethodArgumentNotValidException(BindingResult bindingResult) {
        super(bindingResult);
    }
}