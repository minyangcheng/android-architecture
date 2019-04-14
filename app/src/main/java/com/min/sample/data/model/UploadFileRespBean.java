package com.min.sample.data.model;

public class UploadFileRespBean {


   // {"handle": "1","message": "success","path":"files/2019-04-13/201904132205549961695.jpg",
    // "fileserver":"http://cdn-file.cheguo.com/","thumbnailserver":"http://cheguo-image.cheguo.com/"}

    public String handle;
    public String message;
    public String fileserver;
    public String thumbnailserver;
    public String path;

    public String getUrl(){
        return fileserver+path;
    }

}
