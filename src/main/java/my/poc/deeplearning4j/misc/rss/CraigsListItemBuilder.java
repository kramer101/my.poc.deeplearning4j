package my.poc.deeplearning4j.misc.rss;

public class CraigsListItemBuilder {
    private String title;
    private String body;
    private String url;

    public static CraigsListItemBuilder getBuilder() {
        return  new CraigsListItemBuilder();
    }
    public CraigsListItemBuilder title(String title) {
        this.title = title;
        return this;
    }

    public CraigsListItemBuilder body(String body) {
        this.body = body;
        return this;
    }

    public CraigsListItemBuilder url(String url) {

        this.url = url;
        return this;
    }

    public CraigsListItem createCraigsListItem() {
        return new CraigsListItem(title, body, url);
    }
}