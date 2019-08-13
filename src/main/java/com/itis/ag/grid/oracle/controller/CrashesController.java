package com.itis.ag.grid.oracle.controller;

import com.itis.ag.grid.oracle.dao.CrashesDao;
import com.itis.ag.grid.oracle.request.EnterpriseGetRowsRequest;
import com.itis.ag.grid.oracle.response.EnterpriseGetRowsResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
public class CrashesController {

    private CrashesDao crashesDao;

    @Autowired
    public CrashesController(@Qualifier("crashesDao") CrashesDao tradeDao) {
        this.crashesDao = crashesDao;
    }

    @RequestMapping(method = POST, value = "/getRows")
    @ResponseBody
    public EnterpriseGetRowsResponse getRows(@RequestBody EnterpriseGetRowsRequest request) {
        return crashesDao.getData(request);
    }
}