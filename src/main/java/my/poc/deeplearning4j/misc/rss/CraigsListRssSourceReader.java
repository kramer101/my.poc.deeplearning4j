package my.poc.deeplearning4j.misc.rss;

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Assert;
import org.junit.Test;

import java.net.URL;
import java.util.LinkedList;
import java.util.List;

/**
 *
 */
public class CraigsListRssSourceReader {



    public List<CraigsListItem> getArticles(final String feedUrl) throws Exception {
        List<CraigsListItem> articles = new LinkedList<>();

        SyndFeed feed = new SyndFeedInput().build(new XmlReader(new URL(feedUrl)));

        CraigsListItemBuilder builder = CraigsListItemBuilder.getBuilder();

        for (SyndEntry syndEntry : feed.getEntries()) {

            URL item = new URL(syndEntry.getLink());
            Document parsedItem = Jsoup.parse(item, 0);
            Element itemTitle = parsedItem.getElementById("titletextonly");

            StringBuilder articleStringBuilder  = new StringBuilder();

            articleStringBuilder.append(itemTitle.text());

            Element section = parsedItem.getElementById("postingbody");

            section.textNodes().forEach(textNode -> {
                String text = textNode.text();

                if (StringUtils.isNotBlank(text)) {
                    articleStringBuilder.append(text);
                }
            });


            builder.url(syndEntry.getLink()).
                    title(itemTitle.text()).
                    body(articleStringBuilder.toString());
            articles.add(builder.createCraigsListItem());


        }

        return articles;
    }




    @Test
    public void testGetArticles() {
        CraigsListRssSourceReader reader =
                new CraigsListRssSourceReader();
        try {
            List<CraigsListItem> result =
                    reader.getArticles(
                    "https://sfbay.craigslist.org/search/sby/cto?min_auto_year=2000&format=rss");
            Assert.assertTrue(result.size() > 0);

        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail(e.getMessage());
        }

    }

}
