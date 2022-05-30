package executorSer;

import java.util.*;
import java.util.concurrent.*;

/**
 * 1242. Web Crawler Multithreaded
 * BlockingQueue
 * Deque<Future>
 * */
public class WebCrawler {

    public static void main(String[] args) {
        HtmlParser htmlParser = new HtmlParserImp();
        String startUrl = "http://news.yahoo.com";
        List<String> result = crawl(startUrl, htmlParser);

        for (String s : result) {
            System.out.println(s);
        }
    }

    public static List<String> crawl(String startUrl, HtmlParser htmlParser) {
        String hostName = getHostName(startUrl);

        List<String> res = new ArrayList<>();
        Set<String> visited = new HashSet<>();
        BlockingQueue<String> queue = new LinkedBlockingQueue<>();
        Deque<Future> tasks = new ArrayDeque<>();

        queue.offer(startUrl);

        // 	newFixedThreadPool(int nThreads, ThreadFactory threadFactory)
        //  Creates a thread pool that reuses a fixed number of threads operating off a shared unbounded queue,
        //  using the provided ThreadFactory to create new threads when needed.
        ExecutorService es = Executors.newFixedThreadPool(4, r -> {
            Thread t = new Thread(r);
            t.setDaemon(true);
            return t;
        });

        while (true) {
            String url = queue.poll();
            if (url != null) {
                if (getHostName(url).equals(hostName) && visited.add(url)) {
                    res.add(url);
                    tasks.add(es.submit(() -> {
                        List<String> nextUrls = htmlParser.getUrls(url);
                        for (String nextUrl : nextUrls) {
                            queue.offer(nextUrl);
                        }
                    }));
                }
            } else {
                if (!tasks.isEmpty()) {
                    Future task = tasks.poll();
                    try {
                        task.get();
                    } catch (InterruptedException | ExecutionException e) {}
                } else {
                    break;
                }
            }
        }

        return res;
    }

    private static String getHostName(String url) {
        int idx = url.indexOf('/', 7);
        return (idx != -1) ? url.substring(0, idx) : url;
    }

    static interface HtmlParser {
        public List<String> getUrls(String url);
    }

    static class HtmlParserImp implements HtmlParser {

        @Override
        public List<String> getUrls(String url) {
            if (url.equals("http://news.yahoo.com")) {
                return Arrays.asList("http://news.yahoo.com/news", "http://news.yahoo.com/us");
            } else if (url.equals("http://news.yahoo.com/news")) {
                return Arrays.asList("http://news.yahoo.com/news/topics/");
            } else {
                return new ArrayList<>();
            }
        }
    }
}
