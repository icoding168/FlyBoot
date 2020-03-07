package xyz.icoding168.flyboot.schedule;


import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.quartz.CronScheduleBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.ConcurrentHashMap;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;


@Component
public class ScheduleTask {

    private static final Logger logger = LogManager.getLogger(ScheduleTask.class);

    public static final ConcurrentHashMap<String,Object> envConfig = new ConcurrentHashMap<>();

    @Autowired
    private QuartzService quartzService;

    @PostConstruct
    private void init(){
        try{
            Scheduler scheduler = quartzService.getScheduler("scheduleTask");
            String groupName = "scheduleTask";

            {
                String cronString = "0 0/30 * * * ? ";
                String jobName = "AutoAlertJob";
                JobDetail job = newJob(AutoAlertJob.class).withIdentity(jobName, groupName).build();
                Trigger trigger = newTrigger().withIdentity(jobName, groupName).withSchedule(CronScheduleBuilder.cronSchedule(cronString)).build();
                scheduler.scheduleJob(job, trigger);
            }


        }catch(Exception e){
            logger.error("error",e);
            try {

            } catch (Exception e1) {
                logger.error("error",e1);
            }
        }
    }

}
