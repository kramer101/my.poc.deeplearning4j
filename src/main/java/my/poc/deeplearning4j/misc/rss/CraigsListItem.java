package my.poc.deeplearning4j.misc.rss;

/**
 * class that represents an article.
 */
public class CraigsListItem {

    private String title;
    private String body;
    private String url;


    public CraigsListItem(String title, String body, String url) {
        this.title = title;
        this.body = body;
        this.url = url;
    }


    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

    public String getUrl() {
        return url;
    }
}
