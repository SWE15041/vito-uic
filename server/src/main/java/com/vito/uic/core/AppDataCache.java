package com.vito.uic.core;

import com.vito.common.model.enums.YesNoEnum;
import com.vito.uic.domain.RegApp;
import com.vito.uic.service.RegAppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 作者: zhaixm
 * 日期: 2017/11/28 17:10
 * 描述:
 */
@Component
public class AppDataCache {

    private List<RegApp> enableAppsCache = new ArrayList<>();

    @Autowired
    private RegAppService regAppService;

    @PostConstruct
    public void init() {
        List<RegApp> regApps = regAppService.findAll();
        enableAppsCache = regApps.stream()
                                 .filter(regApp -> regApp.getEnable() == YesNoEnum.YES)
                                 .collect(Collectors.toList());
    }

}
