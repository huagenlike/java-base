package com.mzl.redis.chap1;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.ZParams;

import java.util.*;

/**
 * @author lihuagen
 * @version 1.0
 * @className: ArticleVoting
 * @description: 文章投票
 * @date 2021/8/2 15:25
 */
public class ArticleVoting {

    private static final int ONE_WEEK_IN_SECONDS = 7 * 86400;
    private static final int VOTE_SCORE = 432;
    private static final int ARTICLES_PER_PAGE = 25;

    public static final void main(String[] args) {
        new ArticleVoting().run();
    }

    public void run() {
        Jedis conn = new Jedis("localhost");
        conn.select(15);

        String articleId = postArticle(conn, "username", "A title", "http://www.google.com");
        System.out.println("我们发布了一篇带有 id 的新文章: " + articleId);
        System.out.println("Its HASH looks like:");
        // 获取散列包含的所有键值对
        Map<String,String> articleData = conn.hgetAll("article:" + articleId);
        for (Map.Entry<String,String> entry : articleData.entrySet()){
            System.out.println("  " + entry.getKey() + ": " + entry.getValue());
        }

        System.out.println();

        // 进行投票
        articleVote(conn, "other_user", "article:" + articleId);
        // 获取制定散列的键的值
        String votes = conn.hget("article:" + articleId, "votes");
        System.out.println("我们为这篇文章投了票，现在有票了: " + votes);
        assert Integer.parseInt(votes) > 1;

        System.out.println("目前评分最高的文章是:");
        List<Map<String,String>> articles = getArticles(conn, 1);
        printArticles(articles);
        assert articles.size() >= 1;

        // 进行文章分组
        addGroups(conn, articleId, new String[]{"new-group"});
        System.out.println("我们将该文章添加到一个新组，其他文章包括:");
        articles = getGroupArticles(conn, "new-group", 1);
        printArticles(articles);
        assert articles.size() >= 1;
    }

    /**
     * @Author lhg
     * @Description 发布并获取文章
     * @Date 16:25 2021/8/2
     * @Param [conn, user, title, link]
     * @return java.lang.String
     **/
    public String postArticle(Jedis conn, String user, String title, String link) {
        // 生成一个新的文章id
        String articleId = String.valueOf(conn.incr("article:"));

        String voted = "voted:" + articleId;
        // sadd 添加到集合里
        // 把发布文章的用户添加到文章的已投票用户名单，然后这个名单设置为一周
        conn.sadd(voted, user);
        conn.expire(voted, ONE_WEEK_IN_SECONDS);

        long now = System.currentTimeMillis() / 1000;
        String article = "article:" + articleId;
        HashMap<String,String> articleData = new HashMap<String,String>();
        articleData.put("title", title);
        articleData.put("link", link);
        articleData.put("user", user);
        articleData.put("now", String.valueOf(now));
        articleData.put("votes", "1");
        // 将文章信息存储到一个散列里面
        conn.hmset(article, articleData);
        // 放入根据发布时间排序的有序集合里面
        conn.zadd("score:", now + VOTE_SCORE, article);
        // 放入根据评分排序的有序集合里面
        conn.zadd("time:", now, article);

        return articleId;
    }

    /**
     * @Author lhg
     * @Description 投票功能
     * @Date 16:12 2021/8/2
     * @Param [conn, user, article]
     * @return void
     **/
    public void articleVote(Jedis conn, String user, String article) {
        // 计算文章投票截止时间
        long cutoff = (System.currentTimeMillis() / 1000) - ONE_WEEK_IN_SECONDS;
        // 检查是否可以对我恩张进行投票（虽然使用散列也可以获取文章的发布时间，但有序集合返回的文章发布时间为浮点数，可以不进行转换直接使用）
        if (conn.zscore("time:", article) < cutoff){
            return;
        }
        // 从article:id标识符（idcntifier）里面获取文章的id
        String articleId = article.substring(article.indexOf(':') + 1);
        if (conn.sadd("voted:" + articleId, user) == 1) {
            conn.zincrby("score:", VOTE_SCORE, article);
            conn.hincrBy(article, "votes", 1);
        }
    }

    /**
     * @Author lhg
     * @Description 获取评分最高的文章
     * @Date 16:18 2021/8/2
     * @Param [conn, page]
     * @return java.util.List<java.util.Map<java.lang.String,java.lang.String>>
     **/
    public List<Map<String,String>> getArticles(Jedis conn, int page) {
        return getArticles(conn, page, "score:");
    }

    /**
     * @Author lhg
     * @Description 获取文章
     * @Date 16:23 2021/8/2
     * @Param [conn, page, order 排序字段：score，time]
     * @return java.util.List<java.util.Map<java.lang.String,java.lang.String>>
     **/
    public List<Map<String,String>> getArticles(Jedis conn, int page, String order) {
        // 设置获取文章的起始索引和结束索引
        int start = (page - 1) * ARTICLES_PER_PAGE;
        int end = start + ARTICLES_PER_PAGE - 1;

        // 获取多个文章id
        Set<String> ids = conn.zrevrange(order, start, end);
        List<Map<String,String>> articles = new ArrayList<Map<String,String>>();
        for (String id : ids){
            // 根据文章id，获取文章的详细信息
            Map<String,String> articleData = conn.hgetAll(id);
            articleData.put("id", id);
            articles.add(articleData);
        }

        return articles;
    }

    /**
     * @Author lhg
     * @Description 对文章进行分组
     * @Date 16:34 2021/8/2
     * @Param [conn, articleId, toAdd]
     * @return void
     **/
    public void addGroups(Jedis conn, String articleId, String[] toAdd) {
        // 构建储存文章信息的键名
        String article = "article:" + articleId;
        for (String group : toAdd) {
            conn.sadd("group:" + group, article);
        }
    }

    public List<Map<String,String>> getGroupArticles(Jedis conn, String group, int page) {
        return getGroupArticles(conn, group, page, "score:");
    }

    public List<Map<String,String>> getGroupArticles(Jedis conn, String group, int page, String order) {
        String key = order + group;
        if (!conn.exists(key)) {
            ZParams params = new ZParams().aggregate(ZParams.Aggregate.MAX);
            conn.zinterstore(key, params, "group:" + group, order);
            conn.expire(key, 60);
        }
        return getArticles(conn, page, key);
    }

    private void printArticles(List<Map<String,String>> articles){
        for (Map<String,String> article : articles){
            System.out.println("  id: " + article.get("id"));
            for (Map.Entry<String,String> entry : article.entrySet()){
                if (entry.getKey().equals("id")){
                    continue;
                }
                System.out.println("    " + entry.getKey() + ": " + entry.getValue());
            }
        }
    }
}
