package com.xxl.job.core.controller;

import com.xxl.job.core.biz.ExecutorBiz;
import com.xxl.job.core.biz.impl.ExecutorBizImpl;
import com.xxl.job.core.biz.model.IdleBeatParam;
import com.xxl.job.core.biz.model.KillParam;
import com.xxl.job.core.biz.model.LogParam;
import com.xxl.job.core.biz.model.LogResult;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.biz.model.TriggerParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * XXL控制器
 *
 * @author cn-dumaszhong
 */
@RestController
@RequestMapping("/xxl")
public class XxlController {

    private final ExecutorBiz executorBiz = new ExecutorBizImpl();

    @PostMapping("/beat")
    @ResponseBody
    public ReturnT<String> beat() {
        return executorBiz.beat();
    }

    @PostMapping("/idleBeat")
    @ResponseBody
    public ReturnT<String> idleBeat(@RequestBody IdleBeatParam idleBeatParam) {
        return executorBiz.idleBeat(idleBeatParam);
    }

    @PostMapping("/run")
    @ResponseBody
    public ReturnT<String> run(@RequestBody TriggerParam triggerParam) {
        return executorBiz.run(triggerParam);
    }

    @PostMapping("/kill")
    @ResponseBody
    public ReturnT<String> kill(@RequestBody KillParam killParam) {
        return executorBiz.kill(killParam);
    }

    @PostMapping("/log")
    @ResponseBody
    public ReturnT<LogResult> log(@RequestBody LogParam logParam) {
        return executorBiz.log(logParam);
    }
}