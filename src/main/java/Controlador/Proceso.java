package Controlador;

public class Proceso {
    private int priority;
    private int burstTime;

    public Proceso(int priority, int burstTime) {
        this.priority = priority;
        this.burstTime = burstTime;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getBurstTime() {
        return burstTime;
    }

    public void setBurstTime(int burstTime) {
        this.burstTime = burstTime;
    }
    
    
    
}
