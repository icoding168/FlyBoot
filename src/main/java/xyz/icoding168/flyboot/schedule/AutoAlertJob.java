package xyz.icoding168.flyboot.schedule;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerContext;
import org.springframework.context.ApplicationContext;

public class AutoAlertJob implements Job {

    private ApplicationContext applicationContext;


    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        SchedulerContext schedulerContext = null;
        try {
            schedulerContext = jobExecutionContext.getScheduler().getContext();

            applicationContext = (ApplicationContext) schedulerContext.get("applicationContext");

             System.out.println("alert");


        }catch (Exception e){
            e.printStackTrace();
        }


    }
}
