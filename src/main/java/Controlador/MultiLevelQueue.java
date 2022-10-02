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
    Cronometro cronometro = Cronometro.getInstance();

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

    public void simulateProcesses() {
        Runnable myRunnable = new Runnable() {
            public void run() {
                simulateProcess1(); //Proceso 1 (FCFS)
                simulateProcess2(); //Proceso 2 (RR)
                simulateProcess3(); //Proceso 3 (FCFS)
                cronometro.state = false; //Es para que termine el cronometro

            }
        };
        new Thread(myRunnable).start();
    }

    public void simulateProcess1() {
        for (int i = 0; i < FirstProcessList.size(); i++) {
            //PINTAMOS DE ROJO EL PROCESO EN EJECUCIÓN
            EventQueue.invokeLater(new Runnable() {
                public void run() {
                    listPanels.get(index1[count]).setBackground(new Color(255, 77, 77));
                }
            });
            //ESPERAMOS EL TIEMPO DE BURST O DE RAFAGA CON UN SLEEP
            try {
                System.out.println("delay starts");
                Thread.sleep(FirstProcessList.get(i).getBurstTime() * 1000); // 2000 mSec
                System.out.println("delay is over\n");
            } catch (InterruptedException ie) {
                System.out.println(ie);
            }
            //PINTAMOS DE VERDE EL PROCESO QUE ACABA DE TERMINAR DE EJECUTARSE
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
    }

    public void simulateProcess2() {
        while (true) {
            boolean done = true;
            // Atraviesa todos los procesos uno por uno repetidamente
            for (int i = 0; i < SecondProcessList.size(); i++) {
                // Si el tiempo de ráfaga de un proceso es mayor que 0, sigue procesando
                if (SecondProcessList.get(i).getBurstTime() > 0) {
                    done = false;                                           // Hay un proceso pendiente

                    //Si el tiempo de rafaga es mayor que el cuantum realiza lo siguiente:
                    if (SecondProcessList.get(i).getBurstTime() > quantum) {
                        //Pinta de color rojo
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
                            Thread.sleep(quantum * 1000); // Espera el tiempo del cuantum que es 2 segundos
                            System.out.println("delay is over\n");
                        } catch (InterruptedException ie) {
                            System.out.println(ie);
                        }
                        //Pinta de gris oscuro
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
                        // Disminuir el burst_time del proceso actual por el cuantum
                        SecondProcessList.get(i).setBurstTime(SecondProcessList.get(i).getBurstTime() - quantum);
                    } 
                    // Si el tiempo de ráfaga es menor o igual que el cuanto. Último ciclo para este proceso
                    else {
                        //Pinta de color rojo
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
                            Thread.sleep(SecondProcessList.get(i).getBurstTime() * 1000); // Espera lo que quede del tiempo de rafaga
                            System.out.println("delay is over\n");
                        } catch (InterruptedException ie) {
                            System.out.println(ie);
                        }
                        
                        //Pinta de color verde porque ya finalizó el proceso
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
                        
                        //El tiempo de rafaga del proceso lo igualamos a cero porque ya acabó.
                        SecondProcessList.get(i).setBurstTime(0);
                    }
                }
            }

            // Si todos los procesos ya acabaron entonces salimos del bucle.
            if (done == true) {
                break;
            }
        }

    }

    public void simulateProcess3() {
        for (int i = 0; i < ThirdProcessList.size(); i++) {
            //PINTAMOS DE ROJO EL PROCESO EN EJECUCIÓN
            EventQueue.invokeLater(new Runnable() {
                public void run() {
                    listPanels.get(index3[count]).setBackground(new Color(255, 77, 77));
                }
            });
            //ESPERAMOS EL TIEMPO DE BURST O DE RAFAGA CON UN SLEEP
            try {
                System.out.println("delay starts");
                Thread.sleep(ThirdProcessList.get(i).getBurstTime() * 1000); // 2000 mSec
                System.out.println("delay is over\n");
            } catch (InterruptedException ie) {
                System.out.println(ie);
            }
            //PINTAMOS DE VERDE EL PROCESO QUE ACABA DE TERMINAR DE EJECUTARSE
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

    @Override
    public void run() {
        scheduling();

        Runnable myRunnable = new Runnable() {
            public void run() {
                EventQueue.invokeLater(new Runnable() {
                    public void run() {
                        simulateProcesses();
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

        // Ejecución de cola de PRIMERA PRIORIDAD con algoritmo de programación FCFS
        for (int i = 0; i < n; i++) {
            if (getP(i) == 0) {
                index1[x] = i;
                x++;
                FirstProcessList.add(listProcess.get(i));
            }
        }

        x = 0;

        // Ejecución de cola de SEGUNDA PRIORIDAD con algoritmo de programación Round Robin
        for (int i = 0; i < n; i++) {
            if (getP(i) == 1) {
                listaSegundosProcesos.add(i);
                SecondPanelList.add(listPanels.get(i));
                SecondProcessList.add(listProcess.get(i));
                index2[x] = i;
                x++;
            }
        }

        z = 0;
        x = 0;

        //Ejecución de cola de TERCERA PRIORIDAD con algoritmo de programación FCFS
        for (int i = 0; i < n; i++) {
            if (getP(i) == 2) {
                index3[x] = i;
                x++;
                ThirdProcessList.add(listProcess.get(i));
            }
        }

    }

}
