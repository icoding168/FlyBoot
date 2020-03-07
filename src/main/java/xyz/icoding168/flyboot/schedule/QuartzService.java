package xyz.icoding168.flyboot.schedule;


import org.apache.commons.collections4.CollectionUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerContext;
import org.quartz.impl.DirectSchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.matchers.GroupMatcher;
import org.quartz.simpl.RAMJobStore;
import org.quartz.simpl.SimpleThreadPool;
import org.quartz.spi.JobStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import xyz.icoding168.flyboot.common.UncheckedException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class QuartzService {

    private static final Logger logger = LogManager.getLogger(QuartzService.class);

    @Autowired
    private ApplicationContext applicationContext;


    public Scheduler getScheduler(String schedulerName){
        try{
            Scheduler scheduler = DirectSchedulerFactory.getInstance().getScheduler(schedulerName);
            if(scheduler != null){
                return scheduler;
            }

            SimpleThreadPool threadPool = new SimpleThreadPool(10, Thread.NORM_PRIORITY);
            JobStore jobStore = new RAMJobStore();

            DirectSchedulerFactory.getInstance().createScheduler(schedulerName,schedulerName,threadPool,jobStore);
            scheduler = DirectSchedulerFactory.getInstance().getScheduler(schedulerName);
            SchedulerContext schedulerContext = scheduler.getContext();
            schedulerContext.put("applicationContext",applicationContext);
            scheduler.start();
            return scheduler;
        }catch(Exception e){
            logger.error("error",e);
            throw new UncheckedException();
        }

    }


    public List getAllJobs(String schedulerName){
        List<Map<String,Object>> list = new ArrayList<>();
        try{
            Scheduler scheduler = getScheduler(schedulerName);
            for (String groupName : scheduler.getJobGroupNames()) {
                for (JobKey jobKey : scheduler.getJobKeys(GroupMatcher.jobGroupEquals(groupName))) {

                    Map<String,Object> map = new HashMap<>();
                    list.add(map);

                    String jobName = jobKey.getName();
                    String jobGroup = jobKey.getGroup();
                    JobDetail jobDetail = scheduler.getJobDetail(jobKey);
                    map.put("jobName",jobName);
                    map.put("jobGroup",jobGroup);

                }

            }

            if(CollectionUtils.isEmpty(list)){
                return list;
            }

            return list;

        }catch(Exception e){
            logger.error("error",e);
            throw new UncheckedException();
        }


    }

    public static void main(String[] args) throws Exception{
        Scheduler s = new StdSchedulerFactory().getScheduler();
    }



}
