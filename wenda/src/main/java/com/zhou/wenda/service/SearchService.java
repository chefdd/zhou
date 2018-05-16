package com.zhou.wenda.service;

import com.zhou.wenda.domain.Question;
import lombok.extern.slf4j.Slf4j;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class SearchService {



    private static final String SOLR_URL = "http://127.0.0.1:8983/solr/";
    //private HttpSolrClient client = new HttpSolrClient.Builder(SOLR_URL).build();
    private static final String QUESTION_TITLE_FIELD = "question_title";
    private static final String QUESTION_CONTENT_FIELD = "question_content";

    //private HttpSolrClient client = getSolrClient();

    /* public static HttpSolrClient getSolrClient(){
     *//*
     * 设置超时时间
     * .withConnectionTimeout(10000)
     * .withSocketTimeout(60000)
     *//*
        return new HttpSolrClient.Builder(SOLR_URL)
                .withConnectionTimeout(10000)
                .withSocketTimeout(60000)
                .build();
    }*/


    public HttpSolrClient createSolrServer(){
        HttpSolrClient.Builder builder = null;
        builder =new HttpSolrClient.Builder(SOLR_URL);
        HttpSolrClient  solr=builder.build();
        return solr;
    }
    /**
     * 根据关键词keyword搜索问题
     * @param keyword
     * @param offset
     * @param count
     * @param hlPre 高亮前缀
     * @param hlPos 高亮后缀
     * @return
     */
    public List<Question> searchQuestion(String keyword, int offset, int count, String hlPre, String hlPos) throws Exception{
        log.info("查询关键词为: {}",keyword);
//        HttpSolrClient client = getSolrClient();
        HttpSolrClient.Builder builder = null;
        builder =new HttpSolrClient.Builder(SOLR_URL + "zhouwd");
        HttpSolrClient  client = builder.build();
        log.info("http clien : {}", client.getBaseURL());
        log.info("http clien  client : {}", client.getHttpClient());
        List<Question> questionList = new ArrayList<>();
        SolrQuery query = new SolrQuery(keyword);

        query.setParam("df", "question_title");
        query.setRows(count);
        query.setStart(offset);
        query.setHighlight(true);
        query.setHighlightSimplePre(hlPre);
        query.setHighlightSimplePost(hlPos);
        query.set("hl.fl", QUESTION_TITLE_FIELD + "," + QUESTION_CONTENT_FIELD);
        log.info("查询请求为： {}", query);
        log.info("查询base url query:{}", client.getBaseURL());



        QueryResponse response = client.query(query);
        if (response.getResults().size() == 0) {
            query.setParam("df", "question_content");
            query.setRows(count);
            query.setStart(offset);
            query.setHighlight(true);
            query.setHighlightSimplePre(hlPre);
            query.setHighlightSimplePost(hlPos);
            query.set("hl.fl", QUESTION_TITLE_FIELD + "," + QUESTION_CONTENT_FIELD);
            response = client.query(query);
        }

        log.info("查询返回结果为：{}", response.getResults().size());



        /**
         * 解析response
         */
        for (Map.Entry<String, Map<String, List<String>>> entry : response.getHighlighting().entrySet()) {
            Question q = new Question();
            q.setId(Integer.parseInt(entry.getKey()));
            if (entry.getValue().containsKey(QUESTION_CONTENT_FIELD)) {
                List<String> contentList = entry.getValue().get(QUESTION_CONTENT_FIELD);
                if (contentList.size() > 0) {
                    q.setContent(contentList.get(0));
                }
            }
            if (entry.getValue().containsKey(QUESTION_TITLE_FIELD)) {
                List<String> titleList = entry.getValue().get(QUESTION_TITLE_FIELD);
                if (titleList.size() > 0) {
                    //q.setTitle(titleList.get(0).replace("<font>", "").replace("</font>", ""));
                    q.setTitle(titleList.get(0));
                }
            }
            questionList.add(q);
        }
        log.info("返回的questinosList: {}", questionList.stream().map(Question::getId).collect(Collectors.toList()));
        return questionList;
    }

    /**
     * 当发布一个新问题时，对这个问题建立索引
     * @param qid
     * @param title
     * @param content
     * @return
     */
    public boolean indexQuestion(int qid, String title, String content) throws Exception{
        HttpSolrClient.Builder builder = null;
        builder =new HttpSolrClient.Builder(SOLR_URL + "zhouwd");
        HttpSolrClient  client = builder.build();
        SolrInputDocument doc =  new SolrInputDocument();
        doc.setField("id", qid);
        doc.setField(QUESTION_TITLE_FIELD, title);
        doc.setField(QUESTION_CONTENT_FIELD, content);
        UpdateResponse response = client.add(doc, 1000);
        return response != null && response.getStatus() == 0;
    }

}

