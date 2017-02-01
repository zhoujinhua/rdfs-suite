package com.rdfs.core.editor;

import java.beans.PropertyEditorSupport;

import com.rdfs.core.utils.StringUtils;

public class StringEditor extends PropertyEditorSupport {


    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        setValue(StringUtils.KillEmpty(text));
    }

    @Override
    public String getAsText() {
        return StringUtils.KillEmpty(getValue().toString());
    }
}
