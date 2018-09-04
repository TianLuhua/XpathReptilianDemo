package com.example.demo.bean;

public class APPBean {

    String name;
    double loadCount;
    String loadCount_;
    String detailsPath;
    String version;
    String developer;

    public APPBean() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLoadCount() {
        return loadCount;
    }

    public void setLoadCount(double loadCount) {
        this.loadCount = loadCount;
    }

    public String getLoadCount_() {
        return loadCount_;
    }

    public void setLoadCount_(String loadCount_) {
        this.loadCount_ = loadCount_;
    }

    public String getDetailsPath() {
        return detailsPath;
    }

    public void setDetailsPath(String detailsPath) {
        this.detailsPath = detailsPath;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getDeveloper() {
        return developer;
    }

    public void setDeveloper(String developer) {
        this.developer = developer;
    }


    @Override
    public String toString() {
        return "APPBean{" +
                "name='" + name + '\'' +
                ", loadCount=" + loadCount +
                ", loadCount_='" + loadCount_ + '\'' +
                ", detailsPath='" + detailsPath + '\'' +
                ", version='" + version + '\'' +
                ", developer='" + developer + '\'' +
                '}';
    }
}


