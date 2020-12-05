package it.unibo.oop.lab.reactivegui03;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class AnotherConcurrentGUI extends JFrame{
    
    private static final long serialVersionUID = 1L;
    private static final double WIDTH_PERC = 0.2;
    private static final double HEIGHT_PERC = 0.1;
    
    private final JButton up = new JButton("up");
    private final JButton down = new JButton("down");
    private final JButton stop = new JButton("stop");
    private final JLabel display = new JLabel();
    
    
    
    public AnotherConcurrentGUI() {
        super();
 
        //set size
        final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setSize((int) (screenSize.getWidth() * WIDTH_PERC), (int) (screenSize.getHeight() * HEIGHT_PERC));
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        //make panel 
        final JPanel panel = new JPanel();
        this.setContentPane(panel);
        panel.add(display);
        panel.add(down);
        panel.add(stop);
        panel.add(up);
        this.setVisible(true);
        
        //create  Thread 
        final Agent agent = new Agent();
        final Thread anotherThread = new Thread(agent);
        anotherThread.start();
                        
        stop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                agent.stopCounting();
            }
        });
        
        up.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                agent.upCount();
            }
            
        });
        
        down.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
               agent.downCount();   
            }
            
        });
        
    }
    
  public class Agent implements Runnable{
        private volatile boolean stop;
        private volatile int counter;
        private volatile boolean down;
        private volatile boolean up;     
        private volatile int time;
        private final static int LIMIT = 100; //val expressed in millisec
        
        public void run() {
            while (!this.stop) {
                try {
                   
                    SwingUtilities.invokeAndWait(() -> AnotherConcurrentGUI.this.display.setText(Integer.toString(Agent.this.counter)));
                    time++;
                    if(this.time == LIMIT) {
                        System.out.println("time limit: " + time/10 + "sec");
                        this.stopCounting();
                    }
                    if(this.up == true && this.down == false) {
                      this.counter++;
                    }
                    if(this.up == false && this.down == true ) {
                      this.counter--;
                      
                    }
                    Thread.sleep(100);
                } catch (InvocationTargetException | InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        } 
             
        /**
         * External command to stop counting.
         */
        public void stopCounting() {
            this.stop = true;
        }      
       //external method to up/down counting.
        public void upCount() {
            this.up = true;
            this.down = false;
        }
        
        public void downCount() {
            this.up = false;
            this.down = true;
        }
        
    }  
    
}
