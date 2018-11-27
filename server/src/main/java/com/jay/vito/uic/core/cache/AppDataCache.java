package com.jay.vito.uic.core.cache;

import com.jay.vito.common.model.enums.YesNoEnum;
import com.jay.vito.uic.server.domain.RegApp;
import com.jay.vito.uic.server.service.RegAppService;
import com.jay.vito.website.core.Application;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 作者: zhaixm
 * 日期: 2017/11/28 17:10
 * 描述:
 */
public class AppDataCache {

    private static List<RegApp> enableApps = new ArrayList<>();

    public static void init() {
        RegAppService regAppService = Application.getBeanContext().getBean(RegAppService.class);
        List<RegApp> regApps = regAppService.findAll();
        enableApps = regApps.stream()
                            .filter(regApp -> regApp.getEnable() == YesNoEnum.YES)
                            .collect(Collectors.toList());
    }

    public static List<RegApp> getEnableRegApps() {
        return enableApps;
    }

}
