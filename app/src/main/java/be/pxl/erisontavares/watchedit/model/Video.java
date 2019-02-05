package be.pxl.erisontavares.watchedit.model;

public class Video {

    private String id;
    private String name;
    private String size;
    private String type;
    private String site;
    private String key;

    public Video() {
    }

    public Video(String id, String name, String size, String type, String site, String key) {
        this.id = id;
        this.name = name;
        this.size = size;
        this.type = type;
        this.site = site;
        this.key = key;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
