package Controlador;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;

public class MultiLevelQueue {
    int maxProcess = 500; //Numero de procesos maximos que vamos a almacenar
    private List<Proceso> listProcess = new ArrayList<>(); //Lista de procesos
    private List<JPanel> listPanels = new ArrayList<>();
    int[] completionTime = new int[maxProcess];
    int[] turnAroundTime = new int[maxProcess];
    int[] waitingTime = new int[maxProcess];
    
    static int n, quantum = 4 ,arrivalTime = 0;

    public MultiLevelQueue(List<Proceso> list) {
        this.listProcess = list;
        this.n = list.size();
    }
    
    public int getP(int i){
        return listProcess.get(i).getPriority();
    }
    
    public int getBurst(int i){
        return listProcess.get(i).getBurstTime();
    }
    
    public void scheduling () {

		ArrayList<Integer> topPriorityList = new ArrayList<Integer>();
		ArrayList<Integer> lowPriorityProcessList = new ArrayList<Integer>();
		int z = 0;

		// High priority queue execution with FCFS scheduling algorithm

		for (int i = 0;i <n; i++) {
			if (getP(i)==1)
			{
                                listPanels.get(i).setBackground(new Color(255, 77, 77));
                                try {
                                    Thread.sleep(getBurst(i) * 1000);
                                } catch (Exception e) {
                                    System.out.println(e);
                                }
                                listPanels.get(i).setBackground(new Color(126, 255, 170));
				topPriorityList.add(getP(i));
				completionTime[i]=completionTime[z]+getBurst(i);
				z=i;
				turnAroundTime[i]=completionTime[i]-arrivalTime;
				waitingTime[i]=turnAroundTime[i]-getBurst(i);
			}
		}

		// Low Priority queue execution with Round Robin scheduling algorithm

		for (int i = 0;i <n; i++) {
			if (getP(i)==0)
			{
				lowPriorityProcessList.add(i);                            //  lowPriorityProcessList contains all the process (like p2,p3,p4) which are in low priority
			}
		}

		int[] lowPriorityProcessArray = new int[50];  
		for (int i = 0;i <lowPriorityProcessList.size(); i++) {
			lowPriorityProcessArray[i]=lowPriorityProcessList.get(i);     //Copying the process from lowPriorityProcessList to lowPriorityProcessArray
		}

		// Make a copy of burst times burstTime[] to store remaining burst times.

		int rem_bt[]= new int[lowPriorityProcessList.size()];
		for (int i = 0 ; i < lowPriorityProcessList.size() ; i++)
		{
			rem_bt[i] =  getBurst(lowPriorityProcessArray[i]);           // Burst time of all the process with low priority are copied into rem_bt array
		}

		int t = completionTime[z]; // Current time in RR

		// Keep traversing processes in round robin manner until all of them are not done.

		while (true)
		{
			boolean done = true;

			// Traverse all processes one by one repeatedly
			for (int i = 0 ; i < lowPriorityProcessList.size(); i++)
			{
				// If burst time of a process is greater than 0 then only need to process further
				if (rem_bt[i] > 0)
				{
					done = false;                                           // There is a pending process

					if (rem_bt[i] > quantum)
					{
						// Increase the value of t i.e. shows how much time a process has been processed
						t += quantum;
                                                listPanels.get(i).setBackground(new Color(255, 77, 77));
                                                try {
                                                    Thread.sleep(quantum * 1000);
                                                } catch (Exception e) {
                                                    System.out.println(e);
                                                }
                                                 listPanels.get(i).setBackground(new Color(158, 158, 158));
						// Decrease the burst_time of current process by quantum
						rem_bt[i] -= quantum;
					}

					// If burst time is smaller than or equal to quantum. Last cycle for this process
					else
					{
						// Increase the value of t i.e. shows how much time a process has been processed
						t = t + rem_bt[i];
                                                listPanels.get(i).setBackground(new Color(255, 77, 77));
                                                try {
                                                    Thread.sleep(rem_bt[i] * 1000);
                                                } catch (Exception e) {
                                                    System.out.println(e);
                                                }
                                                 listPanels.get(i).setBackground(new Color(126, 255, 170));
						completionTime[lowPriorityProcessArray[i]]=t;

						// As the process gets fully executed make its remaining burst time = 0
						rem_bt[i] = 0;
					}
				}
			}

			// If all processes are done
			if (done == true)
				break;
		}

		// Calculating waiting time and turn around time for all the process in low priority queue
		for (int j = 0; j < lowPriorityProcessList.size(); j++) {
			turnAroundTime[lowPriorityProcessArray[j]]=completionTime[lowPriorityProcessArray[j]];
			waitingTime[lowPriorityProcessArray[j]]=turnAroundTime[lowPriorityProcessArray[j]]-getBurst(lowPriorityProcessArray[j]);
		}
    }
    
    
}
