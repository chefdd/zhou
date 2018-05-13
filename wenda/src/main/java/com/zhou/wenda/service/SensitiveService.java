package com.zhou.wenda.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.CharUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class SensitiveService implements InitializingBean {


    /**
     * 默认敏感词替换符
     */
    private static final String DEFAULT_REPLACEMENT = "***";

    /**
     * 前缀树结点定义
     */
    private class TrieNode{

        //判断当前结点是否为敏感词结尾
        private boolean end = false;

        //当前结点下所有的子结点
        private Map<Character, TrieNode> subNodes = new HashMap<>();

        /**
         * 向指定位置添加节点树
         */
        public void addSubNode(Character key, TrieNode node){
            subNodes.put(key, node);
        }

        /**
         * 获取下个节点
         */
        public TrieNode getSubNode(Character key){
            return subNodes.get(key);
        }

        public boolean isKeywordEnd(){
            return end;
        }

        public void setKeywordEnd(boolean end){
            this.end = end;
        }


    }

    /**
     * 定义根节点
     */
    private TrieNode rootNode = new TrieNode();

    /**
     * 将敏感词添加到前缀树中
     * @param lineTxt
     */
    private void addWord(String lineTxt){
        TrieNode tempNode = rootNode;

        for (int i = 0; i < lineTxt.length(); i++){
            Character c = lineTxt.charAt(i);

            if (isSymbol(c)){
                continue;
            }

            TrieNode node = tempNode.getSubNode(c);

            if (node == null){
                node = new TrieNode();
                tempNode.addSubNode(c, node);
            }

            tempNode = node;

            if (i == lineTxt.length()-1){
                tempNode.setKeywordEnd(true);
            }
        }
    }

    /**
     * 将敏感词文件中的所有敏感词构建成一棵前缀树（字典树）
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        try {
            InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("SensitiveWords.txt");
            InputStreamReader reader = new InputStreamReader(is);
            BufferedReader bufferedReader = new BufferedReader(reader);
            String lineTxt;
            while ((lineTxt = bufferedReader.readLine()) != null){
                addWord(lineTxt.trim());
            }
            reader.close();
        }catch (Exception e){
            log.error("读取敏感词文件失败："+e.getMessage());
        }
    }

    /**
     * 非法字符判断：不是东亚文字，也不是英文单词 such as ☺
     * @return
     */
    private boolean isSymbol(char c){
        int ic = (int) c;
        return !CharUtils.isAsciiAlphanumeric(c) && (ic < 0x2E80 || ic > 0x9FFF);
    }

    /**
     * 过滤敏感词
     * @param text
     * @return
     */
    public String filter(String text){
        if (StringUtils.isEmpty(text)){
            return text;
        }

        StringBuilder result = new StringBuilder();
        String replacement = DEFAULT_REPLACEMENT;

        TrieNode tempNode = rootNode;   //指针1
        int begin = 0;                  //指针2
        int position = 0;               //指针3

        while (position < text.length()){
            char c = text.charAt(position);    //获取position指针所指向的字符

            /**
             * 非法字符过滤
             */
            if (isSymbol(c)){
                if (tempNode == rootNode){
                    result.append(c);
                    ++begin;
                }
                ++position;
                continue;
            }

            tempNode = tempNode.getSubNode(c);  //判断当前节点是否包含该字符对应的子节点（初始为根节点）

            if (tempNode==null){        //情况1：不包含，即当前字符不属于敏感词
                result.append(text.charAt(begin));  //直接将该字符添加到result
                position = begin + 1;
                begin = position;
                tempNode = rootNode;
            }else if (tempNode.isKeywordEnd()){     //情况2：包含，且是敏感词的最后一个字
                result.append(replacement);
                position = position + 1;
                begin = position;
                tempNode = rootNode;
            }else {         //情况3：包含
                ++position;
            }
        }

        result.append(text.substring(begin));
        return result.toString();
    }

}