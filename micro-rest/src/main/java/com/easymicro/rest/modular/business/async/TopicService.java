package com.easymicro.rest.modular.business.async;

import com.easymicro.core.util.SpringContextHolder;
import com.google.common.eventbus.AsyncEventBus;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**************************************
 *
 * @author LinYingQiang
 * @date 2018-04-27 14:40
 * @qq 961410800
 *
************************************/
@Component
@DependsOn("springContextHolder")
public class TopicService {

    private ExecutorService executor = null;

    private AsyncEventBus asyncEventBus = null;

    @PostConstruct
    private void init(){
        executor = Executors.newFixedThreadPool(5);
        asyncEventBus = new AsyncEventBus(executor);
        Map<String,BusService> beans = SpringContextHolder.getBeanByType(BusService.class);
        beans.values().forEach(v -> asyncEventBus.register(v));

    }

    public void post(Object topic){
        assert executor != null && asyncEventBus != null;
        asyncEventBus.post(topic);
    }

    @PreDestroy
    private void destory(){
        executor.shutdown();
    }
}
