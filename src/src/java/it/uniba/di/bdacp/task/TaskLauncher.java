package it.uniba.di.bdacp.task;

import it.uniba.di.bdacp.exceptions.InvalidArgumentsException;
import it.uniba.di.bdacp.tools.Utility;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class TaskLauncher {

    public static void main(String[] args) throws Exception {
        /*
        ITWAC_corpus_converter /media/dati/ITWAC /media/win/Users/Gianvito/Desktop/TESI/CODE/BDACP/src/resources/conf_itwac_converter.txt

        crawler /media/win/Users/Gianvito/Desktop/TESI/CODE/BDACP/src/resources/conf_twitter_streaming_crawler.txt

        sudo -s
        java -jar BDACP.jar build_index /media/win/Users/Gianvito/Desktop/TESI/CODE/output/itwac_converted /media/win/Users/Gianvito/Desktop/TESI/CODE/output/itwac_index_prefixed plain_text_tokenizer itwac:
        java -jar BDACP.jar build_index /media/win/Users/Gianvito/Desktop/TESI/CODE/output/tweets/class4 /media/win/Users/Gianvito/Desktop/TESI/CODE/output/class4_index_prefixed tweets_tokenizer class4:

        sort_index /media/win/Users/Gianvito/Desktop/TESI/CODE/output/itwac_index /media/win/Users/Gianvito/Desktop/TESI/CODE/output/itwac_sorted_index.txt tab Long all _SUCCESS
        sort_index /media/win/Users/Gianvito/Desktop/TESI/CODE/output/klresult /media/win/Users/Gianvito/Desktop/TESI/CODE/output/klresult_sorted_index.txt tab Double all _SUCCESS

        external_sort_index /media/win/Users/Gianvito/Desktop/TESI/CODE/output/itwac_index /media/win/Users/Gianvito/Desktop/TESI/CODE/output/itwac_ext_sort tab Long _SUCCESS

        sudo -s
        java -jar BDACP.jar kl_divergence /media/win/Users/Gianvito/Desktop/TESI/CODE/output/indexes /media/win/Users/Gianvito/Desktop/TESI/CODE/output/klresult class4: itwac: tab
        */

        if(args.length == 0) {
            throw new InvalidArgumentsException("No task selected." + Utility.LINE_SEP + "usage: java -jar BDACP.jar task_id" + Utility.LINE_SEP + Utility.getTaskSuggestion());
        }

        long startTime = System.nanoTime();
        PlatformTask userTask = PlatformTaskFactory.build(args);
        userTask.executeTask();
        long nanosecondsElapsed = System.nanoTime() - startTime;
        long hoursElapsed = TimeUnit.NANOSECONDS.toHours(nanosecondsElapsed);
        long minutesElapsed = TimeUnit.NANOSECONDS.toMinutes(nanosecondsElapsed) - TimeUnit.HOURS.toMinutes(hoursElapsed);
        long secondsElapsed = TimeUnit.NANOSECONDS.toSeconds(nanosecondsElapsed) - TimeUnit.MINUTES.toSeconds(minutesElapsed);
        String timeLog = String.format("%d hour(s), %d minute(s), %d second(s)", hoursElapsed, minutesElapsed, secondsElapsed);
        System.out.println("Elapsed time: " + timeLog);
    }

}
