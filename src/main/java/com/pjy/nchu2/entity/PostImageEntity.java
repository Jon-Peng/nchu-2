package com.pjy.nchu2.entity;

import lombok.Data;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

@Data
public class PostImageEntity {
    private int postId = 0;
    private String image = "";
    private Timestamp createTime = null;
    private int orderIndex = 0;

    public Map toMap() {
        Map dataMap = new HashMap();

        dataMap.put("postId", postId);
        dataMap.put("image", image);
        dataMap.put("createTime", createTime);
        dataMap.put("orderIndex", orderIndex);

        return dataMap;
    }
}
