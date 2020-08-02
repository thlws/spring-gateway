package com.thlws.springcloud.gateway.web;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.thlws.commons.ApiResult;
import com.thlws.springcloud.gateway.dto.TableDto;
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
@RequestMapping("/table")
public class TableController {

    @GetMapping("/list")
    public ApiResult<Page<TableDto>> list(){
        //published draft deleted
        List<TableDto> list = new ArrayList<>();

        Page<TableDto> page = new Page();

        List<String> statusArr = Arrays.asList("published","draft","deleted");
        for (int i = 1; i < 57; i++) {
            TableDto tableDto = TableDto.builder().title("My标题" + i).author("Hanley_"+i)
                    .display_time(DateUtil.now())
                    .created_at(System.currentTimeMillis())
                    .pageviews(188068L).status(statusArr.get(RandomUtil.randomInt(1,3))).build();
            list.add(tableDto);
        }
        page.setRecords(list);
        page.setTotal(list.size());

        return ApiResult.ok(page);
    }


}
