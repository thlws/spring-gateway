package com.thlws.springcloud.gateway.web;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.thlws.commons.ApiResult;
import com.thlws.springcloud.gateway.model.dto.ArticleDto;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author HanleyTang 2020/7/27
 */
@CrossOrigin("*")
@RestController
@RequestMapping("/article")
public class ArticleController {


    @GetMapping("/list")
    public ApiResult<Page<ArticleDto>> list(){
        //published draft deleted
        List<ArticleDto> list = new ArrayList<>();

        Page<ArticleDto> page = new Page<>();
        List<String> os =Arrays.asList("OS", "Mac");
        List<String> statusArr = Arrays.asList("published","draft","deleted");
        for (int i = 1; i < 21; i++) {
            ArticleDto tableDto = ArticleDto.builder()
                    .id(i)
                    .timestamp(System.currentTimeMillis())
                    .pageviews(188068L)
                    .title("My标题" + i)
                    .author("Hanley_" + i)
                    .reviewer("Hanley")
                    .importance("2")
                    .status(statusArr.get(RandomUtil.randomInt(1, 3)))
                    .content("Content-" + i)
                    .content_short("Short_"+i)
                    .display_time(DateUtil.now())
                    .forecast("1")
                    .type("EU")
                    .platforms(os).build();
            list.add(tableDto);
        }
        page.setRecords(list);
        page.setTotal(list.size()*10);

        return ApiResult.ok(page);
    }

}
