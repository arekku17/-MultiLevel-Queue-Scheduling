package Controlador;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

public class MultiLevelQueue implements Runnable {

    int maxProcess = 500; //Numero de procesos maximos que vamos a almacenar
    private List<Proceso> listProcess = new ArrayList<>(); //Lista de procesos
    private List<Proceso> FirstProcessList = new ArrayList<>();
    private List<Proceso> SecondProcessList = new ArrayList<>();
    private List<Proceso> ThirdProcessList = new ArrayList<>();

    private List<JPanel> listPanels = new ArrayList<>();
    private List<JPanel> SecondPanelList = new ArrayList<>();

    private JPanel panelActual;
    int[] completionTime = new int[maxProcess];
    int[] turnAroundTime = new int[maxProcess];
    int[] waitingTime = new int[maxProcess];
    Timer timer;
    int[] index1 = new int[maxProcess];
    int[] index2 = new int[maxProcess];
    int[] index3 = new int[maxProcess];
    int count = 0;

    static int n, quantum = 2, arrivalTime = 0;

    static float totalwt, totaltat;

    public MultiLevelQueue(List<Proceso> list, List<JPanel> listPanels) {
        this.listProcess = list;
        this.listPanels = listPanels;
        this.n = list.size();
    }

    public int getP(int i) {
        return listProcess.get(i).getPriority();
    }

    public int getBurst(int i) {
        return listProcess.get(i).getBurstTime();
    }

    public void simulateProcess1() {
        Runnable myRunnable = new Runnable() {
            public void run() {
                for (int i = 0; i < FirstProcessList.size(); i++) {
                    EventQueue.invokeLater(new Runnable() {
                        public void run() {
                            listPanels.get(index1[count]).setBackground(new Color(255, 77, 77));
                        }
                    });

                    try {
                        System.out.println("delay starts");
                        Thread.sleep(FirstProcessList.get(i).getBurstTime() * 1000); // 2000 mSec
                        System.out.println("delay is over\n");
                    } catch (InterruptedException ie) {
                        System.out.println(ie);
                    }
                    EventQueue.invokeLater(new Runnable() {
                        public void run() {
                            System.out.println(count);
                            listPanels.get(index1[count]).setBackground(new Color(126, 255, 170));
                            count++;
                            if (count == FirstProcessList.size()) {
                                count = 0;
                            }
                        }
                    });
                }

                simulateProcess2();

                for (int i = 0; i < ThirdProcessList.size(); i++) {
                    EventQueue.invokeLater(new Runnable() {
                        public void run() {
                            listPanels.get(index3[count]).setBackground(new Color(255, 77, 77));
                        }
                    });

                    try {
                        System.out.println("delay starts");
                        Thread.sleep(ThirdProcessList.get(i).getBurstTime() * 1000); // 2000 mSec
                        System.out.println("delay is over\n");
                    } catch (InterruptedException ie) {
                        System.out.println(ie);
                    }
                    EventQueue.invokeLater(new Runnable() {
                        public void run() {
                            System.out.println(count);
                            listPanels.get(index3[count]).setBackground(new Color(126, 255, 170));
                            count++;
                            if (count == ThirdProcessList.size()) {
                                count = 0;
                            }
                        }
                    });
                }

            }
        };
        new Thread(myRunnable).start();
    }

    public void simulateProcess2() {
        int t = 0;
        while (true) {
            boolean done = true;
            // Traverse all processes one by one repeatedly
            for (int i = 0; i < SecondProcessList.size(); i++) {
                // If burst time of a process is greater than 0 then only need to process further
                if (SecondProcessList.get(i).getBurstTime() > 0) {
                    done = false;                                           // There is a pending process

                    if (SecondProcessList.get(i).getBurstTime() > quantum) {
                        // Increase the value of t i.e. shows how much time a process has been processed
                        EventQueue.invokeLater(new Runnable() {
                            public void run() {
                                if (count == SecondPanelList.size()) {
                                    count = 0;
                                }
                                System.out.println(count);
                                SecondPanelList.get(count).setBackground(new Color(255, 77, 77));
                            }
                        });

                        try {
                            System.out.println("delay starts");
                            Thread.sleep(quantum * 1000); // 2000 mSec
                            System.out.println("delay is over\n");
                        } catch (InterruptedException ie) {
                            System.out.println(ie);
                        }
                        EventQueue.invokeLater(new Runnable() {
                            public void run() {
                                System.out.println(count);
                                if (count == SecondPanelList.size()) {
                                    count = 0;
                                }
                                SecondPanelList.get(count).setBackground(new Color(83, 83, 83));
                                count++;
                            }
                        });

                        t += quantum;
                        // Decrease the burst_time of current process by quantum
                        SecondProcessList.get(i).setBurstTime(SecondProcessList.get(i).getBurstTime() - quantum);
                    } // If burst time is smaller than or equal to quantum. Last cycle for this process
                    else {
                        // Increase the value of t i.e. shows how much time a process has been processed
                        EventQueue.invokeLater(new Runnable() {
                            public void run() {
                                if (count == SecondPanelList.size()) {
                                    count = 0;
                                }
                                System.out.println(count);
                                SecondPanelList.get(count).setBackground(new Color(255, 77, 77));
                            }
                        });

                        try {
                            System.out.println("delay starts");
                            Thread.sleep(SecondProcessList.get(i).getBurstTime() * 1000); // 2000 mSec
                            System.out.println("delay is over\n");
                        } catch (InterruptedException ie) {
                            System.out.println(ie);
                        }
                        EventQueue.invokeLater(new Runnable() {
                            public void run() {
                                if (count == SecondPanelList.size()) {
                                    count = 0;
                                }
                                System.out.println(count);
                                SecondPanelList.get(count).setBackground(new Color(126, 255, 170));
                                SecondPanelList.remove(count);
                            }
                        });
                        SecondProcessList.get(i).setBurstTime(0);
                    }
                }
            }

            // If all processes are done
            if (done == true) {
                break;
            }
        }

    }

    @Override
    public void run() {
        scheduling();

        Runnable myRunnable = new Runnable() {
            public void run() {
                EventQueue.invokeLater(new Runnable() {
                    public void run() {
                        simulateProcess1();
                    }
                });
            }
        };
        new Thread(myRunnable).start();

    }

    public void scheduling() {
        ArrayList<Integer> listaSegundosProcesos = new ArrayList<Integer>();
        int z = 0;
        int x = 0;

        // High priority queue execution with FCFS scheduling algorithm
        for (int i = 0; i < n; i++) {
            if (getP(i) == 0) {
                index1[x] = i;
                x++;
                FirstProcessList.add(listProcess.get(i));
                completionTime[i] = completionTime[z] + getBurst(i);
                z = i;
                turnAroundTime[i] = completionTime[i] - arrivalTime;
                waitingTime[i] = turnAroundTime[i] - getBurst(i);
            }
        }

        x = 0;

        // Low Priority queue execution with Round Robin scheduling algorithm
        for (int i = 0; i < n; i++) {
            if (getP(i) == 1) {
                listaSegundosProcesos.add(i);                            //  lowPriorityProcessList contains all the process (like p2,p3,p4) which are in low priority
                SecondPanelList.add(listPanels.get(i));
                SecondProcessList.add(listProcess.get(i));
                index2[x] = i;
                x++;
            }
        }

        int[] lowPriorityProcessArray = new int[50];
        for (int i = 0; i < listaSegundosProcesos.size(); i++) {
            lowPriorityProcessArray[i] = listaSegundosProcesos.get(i);     //Copying the process from lowPriorityProcessList to lowPriorityProcessArray
        }

        // Make a copy of burst times burstTime[] to store remaining burst times.
        int rem_bt[] = new int[listaSegundosProcesos.size()];
        for (int i = 0; i < listaSegundosProcesos.size(); i++) {
            rem_bt[i] = getBurst(lowPriorityProcessArray[i]);           // Burst time of all the process with low priority are copied into rem_bt array
        }

        int t = completionTime[z]; // Current time in RR

        // Keep traversing processes in round robin manner until all of them are not done.
        while (true) {
            boolean done = true;

            // Traverse all processes one by one repeatedly
            for (int i = 0; i < listaSegundosProcesos.size(); i++) {
                // If burst time of a process is greater than 0 then only need to process further
                if (rem_bt[i] > 0) {
                    done = false;                                           // There is a pending process

                    if (rem_bt[i] > quantum) {
                        // Increase the value of t i.e. shows how much time a process has been processed
                        t += quantum;
                        // Decrease the burst_time of current process by quantum
                        rem_bt[i] -= quantum;
                    } // If burst time is smaller than or equal to quantum. Last cycle for this process
                    else {
                        // Increase the value of t i.e. shows how much time a process has been processed
                        t = t + rem_bt[i];
                        completionTime[lowPriorityProcessArray[i]] = t;

                        // As the process gets fully executed make its remaining burst time = 0
                        rem_bt[i] = 0;
                    }
                }
            }

            // If all processes are done
            if (done == true) {
                break;
            }
        }

        z = 0;
        x = 0;

        // High priority queue execution with FCFS scheduling algorithm
        for (int i = 0; i < n; i++) {
            if (getP(i) == 2) {
                index3[x] = i;
                x++;
                ThirdProcessList.add(listProcess.get(i));
                completionTime[i] = completionTime[z] + getBurst(i);
                z = i;
                turnAroundTime[i] = completionTime[i] - arrivalTime;
                waitingTime[i] = turnAroundTime[i] - getBurst(i);
            }
        }

        // Calculating waiting time and turn around time for all the process in low priority queue
        for (int j = 0; j < listaSegundosProcesos.size(); j++) {
            turnAroundTime[lowPriorityProcessArray[j]] = completionTime[lowPriorityProcessArray[j]];
            waitingTime[lowPriorityProcessArray[j]] = turnAroundTime[lowPriorityProcessArray[j]] - getBurst(lowPriorityProcessArray[j]);
        }

        for (int i = 0; i < n; i++) {
            totalwt += waitingTime[i];
            totaltat += turnAroundTime[i];
        }

        System.out.println("\n" + "Average Waiting Time is: " + totalwt / n);
        System.out.println("Average Turnaround Time is : " + totaltat / n);

    }

}
