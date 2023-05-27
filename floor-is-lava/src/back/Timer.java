package back;

public class Timer {

    private final int TIMER_INITIAL_VALUE = 5;
    private int timerStartValue;
    private int timer;


    public Timer (){
        this.timer = TIMER_INITIAL_VALUE;
        this.timerStartValue = TIMER_INITIAL_VALUE;
    }



    public int getTimerCurrentValue(){
        return timer;
    }

    public void decrementTimer(){
        --timer;
        validateTimer(timer);
    }

    public void setTimer(int value){
        validateTimer(value);
    }

    private void validateTimer(int value){
        timer = (value < 0) ? timerStartValue : value;
    }

    public void setTimerStartValue(int value){
        timerStartValue = (value <= 0) ? TIMER_INITIAL_VALUE : value;
    }

    public int getTimerStartValue(){
        return timerStartValue;
    }

}
