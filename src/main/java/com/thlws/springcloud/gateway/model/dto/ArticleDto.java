package com.thlws.springcloud.gateway.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Access-Control-Allow-Origin
 * @author HanleyTang 2020/7/31
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArticleDto {

    private Integer id;
    private Long timestamp;
    private Long pageviews;
    private String title;
    private String author;
    private String reviewer;
    private String importance;
    private String status;//published draft deleted
    private String content;
    private String content_short;
    private String display_time;
    private String forecast;
    private String image_uri;
    private String type;
    private List<String> platforms;


}
