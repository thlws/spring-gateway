package com.thlws.springcloud.gateway.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author HanleyTang 2020/7/30
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TableDto {

    private String title;
    private String author;
    private Long pageviews;
    private String display_time;
    private Long created_at;
    private String status;//published draft deleted

}
