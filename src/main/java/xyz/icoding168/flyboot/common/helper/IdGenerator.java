package xyz.icoding168.flyboot.common.helper;

import org.apache.marmotta.kiwi.generator.SnowflakeIDGenerator;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class IdGenerator {

    private static SnowflakeIDGenerator snowflakeIDGenerator = new SnowflakeIDGenerator(1);

    private static DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyyMMdd");

    public static String generateId() {
        String date = DateTime.now().toString(formatter);
        String id = String.valueOf(snowflakeIDGenerator.getId());
        String finalId = date + id;
        return finalId;
    }


    public static void main(String[] args){
        System.out.println(IdGenerator.generateId());
    }


}
