package xyz.icoding168.flyboot.common.helper;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.util.CollectionUtils;
import xyz.icoding168.flyboot.common.UncheckedException;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ThreadPool {

    private static final Logger logger = LogManager.getLogger();

    private static ExecutorService executorService = Executors.newCachedThreadPool();

    public static Future submitTask(Runnable runnable){
        Future future = executorService.submit(runnable);
        return future;
    }

    public static Future submitTask(List<Future> futureList,Runnable runnable){
        if(CollectionUtils.isEmpty(futureList)){
            throw new UncheckedException("futureList 不能为空");
        }

        Future future = executorService.submit(runnable);
        futureList.add(future);
        return future;
    }

    public static void waitFuturesDone(List<Future> futureList){
        try{
            for(Future future:futureList){
                future.get();
            }
        }catch(Exception e){
            throw new UncheckedException(e);
        }

    }
}
