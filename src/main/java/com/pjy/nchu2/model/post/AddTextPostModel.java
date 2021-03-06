package com.pjy.nchu2.model.post;

import lombok.Data;

import java.util.List;

@Data
public class AddTextPostModel {

    private int postId;
//    @ApiModelProperty("标题")
    private String title;
//    @ApiModelProperty("内容")
    private String content;
//    @ApiModelProperty("标签")
    private String tag;
//    @ApiModelProperty("图片列表")
    private List imageList;


}
