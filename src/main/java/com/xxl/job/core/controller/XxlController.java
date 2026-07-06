package com.xxl.job.core.controller;

import com.xxl.job.core.openapi.ExecutorBiz;
import com.xxl.job.core.openapi.impl.ExecutorBizImpl;
import com.xxl.job.core.openapi.model.IdleBeatRequest;
import com.xxl.job.core.openapi.model.KillRequest;
import com.xxl.job.core.openapi.model.LogRequest;
import com.xxl.job.core.openapi.model.LogResult;
import com.xxl.job.core.openapi.model.TriggerRequest;
import com.xxl.tool.response.Response;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/xxl")
public class XxlController {

    private final ExecutorBiz executorBiz = new ExecutorBizImpl();

    @PostMapping("/beat")
    public Response<String> beat() {
        return executorBiz.beat();
    }

    @PostMapping("/idleBeat")
    public Response<String> idleBeat(@RequestBody IdleBeatRequest request) {
        return executorBiz.idleBeat(request);
    }

    @PostMapping("/run")
    public Response<String> run(@RequestBody TriggerRequest request) {
        return executorBiz.run(request);
    }

    @PostMapping("/kill")
    public Response<String> kill(@RequestBody KillRequest request) {
        return executorBiz.kill(request);
    }

    @PostMapping("/log")
    public Response<LogResult> log(@RequestBody LogRequest request) {
        return executorBiz.log(request);
    }
}
