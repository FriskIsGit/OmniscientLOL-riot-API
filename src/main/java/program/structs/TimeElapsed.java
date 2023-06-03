package program.structs;

import java.sql.Time;

public class TimeElapsed{
    //-+1 second precision
    public int minutes;
    public int seconds;

    private TimeElapsed(){
    }

    public TimeElapsed(long unixMillis){
        seconds = (int) (unixMillis / 1000);
        minutes = seconds / 60;
        seconds = seconds % 60;
    }
    public static TimeElapsed fromSeconds(int seconds){
        TimeElapsed time = new TimeElapsed();
        time.minutes = seconds / 60;
        time.seconds = seconds % 60;
        return time;
    }

    public int toAllSeconds(){
        return minutes * 60 + seconds;
    }

    @Override
    public String toString(){
        String sec = seconds < 10 ? "0" + seconds : String.valueOf(seconds);
        return "[" + minutes + ':' + sec + ']';
    }
}
