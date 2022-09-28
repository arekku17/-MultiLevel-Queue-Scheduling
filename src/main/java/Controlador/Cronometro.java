/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import static java.lang.Thread.sleep;
import javax.swing.JLabel;

public class Cronometro extends Thread {

    public int seconds = 0;
    public String secondsStr = "00";
    public int minutes = 0;
    public String minutesStr = "00";
    public int hours = 0;
    public String hoursStr = "00";
    public boolean state = true;
    private JLabel lblHours = new JLabel();
    private JLabel lblMinutes = new JLabel();
    private JLabel lblSeconds = new JLabel();
    
    private static volatile Cronometro instance = null;

    private Cronometro() {}

    public static Cronometro getInstance() {
        if (instance == null) {
            synchronized(Cronometro.class) {
                if (instance == null) {
                    instance = new Cronometro();
                }
            }
        }

        return instance;
    }

    public void setLblHours(JLabel lblHours) {
        this.lblHours = lblHours;
    }

    public void setLblMinutes(JLabel lblMinutes) {
        this.lblMinutes = lblMinutes;
    }

    public void setLblSeconds(JLabel lblSeconds) {
        this.lblSeconds = lblSeconds;
    }

    @Override
    public void run() {
        try {//si ocurre un error al dormir el proceso(sleep(999))
            for (;;) { //inicio del for infinito 

                //DANDO FORMATO
                if (hours < 10) {
                    hoursStr = "0" + hours;
                } else {
                    hoursStr = "" + hours;
                }
                if (minutes < 10) {
                    minutesStr = "0" + minutes;
                } else {
                    minutesStr = "" + minutes;
                }
                if (seconds < 10) {
                    secondsStr = "0" + seconds;
                } else {
                    secondsStr = "" + seconds;
                }

                //LÓGICA DEL CRONOMETRO
                if (seconds != 59) {//si no es el ultimo segundo
                    seconds++; //incremento el numero de segundos                                  
                } else {
                    if (minutes != 59) {//si no es el ultimo minuto
                        seconds = 0;//pongo en cero los segundos 
                        minutes++;//incremento el numero de minutos
                    } else {//incremento el numero de horas
                        hours++;
                        minutes = 0;//pongo en cero los minutos
                        seconds = 0;//pongo en cero los segundos           
                    }
                }
                actualizarLabels();
                sleep(999);//Duermo el hilo durante 999 milisegundos(casi un segundo, quintandole el tiempo de proceso)
            }//Fin del for infinito             
        } catch (Exception ex) {
            System.out.println(ex.getMessage());//Imprima el error
        }
    }

    public void actualizarLabels() {
        lblHours.setText(hoursStr);
        lblMinutes.setText(":" + minutesStr);
        lblSeconds.setText(":" + secondsStr);
    }
}