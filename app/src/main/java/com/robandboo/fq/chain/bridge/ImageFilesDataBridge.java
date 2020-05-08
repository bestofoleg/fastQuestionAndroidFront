package com.robandboo.fq.chain.bridge;

import java.io.File;
import java.util.List;

public class ImageFilesDataBridge implements IDataBridgeStart<List<File>>,
        IDataBridgeFinish<List<File>> {
    private List<File> files;

    @Override
    public List<File> getData() {
        return files;
    }

    @Override
    public void setData(List<File> data) {
        this.files = data;
    }
}
