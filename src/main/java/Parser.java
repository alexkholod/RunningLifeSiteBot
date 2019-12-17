import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Parser {
    protected static String postType;

    public Parser(String postType) {
        this.postType = postType;
    }

    public List<Post> parsePosts() throws IOException {
        List<Post> list = new ArrayList<>();

        Document doc = Jsoup.connect("https://www.running-life.ru/" + postType).get();

        Elements h2Elements = doc.getElementsByAttributeValue("class", "entry-title");

        for (Element h2Element : h2Elements) {
            Element aElement = h2Element.child(0);
            String title = aElement.getElementsByAttributeValue("rel", "bookmark").get(0).text();
            String url = aElement.attr("href");
            if (list.size() > 4) break;
            list.add(new Post(title, url));
        }

        return list;
    }

    class Post {
        private String name;
        private String url;


        public Post(String name, String url) {
            this.name = name;
            this.url = url;
        }

        public String getUrl() {
            return url;
        }

        public String getName() {
            return name;
        }

        @Override
        public String toString() {
            return name + " : " + url;
        }
    }
}
