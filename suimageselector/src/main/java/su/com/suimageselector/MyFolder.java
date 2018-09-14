package su.com.suimageselector;

public class MyFolder {

    String name;
    String path;
    int fileNum;
    String firstImagePath;
    boolean isVedio;

    public boolean isVedio() {
        return isVedio;
    }

    public void setVedio(boolean vedio) {
        isVedio = vedio;
    }

    public String getFirstImagePath() {
        return firstImagePath;
    }

    public void setFirstImagePath(String firstImagePath) {
        this.firstImagePath = firstImagePath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getFileNum() {
        return fileNum;
    }

    public void setFileNum(int fileNum) {
        this.fileNum = fileNum;
    }
}
