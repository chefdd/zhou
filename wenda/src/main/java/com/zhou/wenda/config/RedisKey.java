package com.zhou.wenda.config;

public class RedisKey {
    private static String SPLIT = ":";
    private static String BIZ_LIKE = "LIKE";              //点赞key
    private static String BIZ_DISLIKE = "DISLIKE";        //点踩key
    private static String BIZ_EVENTQUEUE = "EVENT_QUEUE"; //事件队列key
    private static String BIZ_FOLLOWER = "FOLLOWER";      //粉丝key
    private static String BIZ_FOLLOWEE = "FOLLOWEE";      //关注对象key
    private static String BIZ_TIMELINE = "TIMELINE";      //

    public static String getLikeKey(int entityType, int entityId) {
        return BIZ_LIKE + SPLIT + String.valueOf(entityType) + SPLIT + String.valueOf(entityId);
    }

    public static String getDisLikeKey(int entityType, int entityId) {
        return BIZ_DISLIKE + SPLIT + String.valueOf(entityType) + SPLIT + String.valueOf(entityId);
    }

    public static String getEventQueueKey() {
        return BIZ_EVENTQUEUE;
    }

    public static String getFollowerKey(int entityType, int entityId){
        return BIZ_FOLLOWER + SPLIT + String.valueOf(entityType) + SPLIT + String.valueOf(entityId);
    }

    public static String getFolloweeKey(int userId, int entityType){
        return BIZ_FOLLOWEE + SPLIT + String.valueOf(userId) + SPLIT + String.valueOf(entityType);
    }

    public static String getTimelineKey(int userId){
        return BIZ_TIMELINE + SPLIT + String.valueOf(userId);
    }

}
