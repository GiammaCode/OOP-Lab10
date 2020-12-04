package it.unibo.oop.lab.reactivegui03;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import it.unibo.oop.lab.reactivegui02.ConcurrentGUI;
import it.unibo.oop.lab.reactivegui02.ConcurrentGUI.Agent;

public class AnotherConcurrentGUI {
    
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
        
        //create a counter 
        final Agent agent = new Agent();
        new Thread(agent).start();
        
        stop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                // Agent should be final
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
    
    private class Agent implements Runnable{
        private volatile boolean stop;
        private volatile int counter;
        private volatile boolean down;
        private volatile boolean up; 
        

        public void run() {
            while (!this.stop) {
                try {
                   
                    SwingUtilities.invokeAndWait(() -> ConcurrentGUI.this.display.setText(Integer.toString(Agent.this.counter)));
                    if(this.up == true && this.down == false) {
                      this.counter++;
                    }
                    if(this.up == false && this.down == true ) {
                      this.counter--;
                      
                    }
                    Thread.sleep(100);
                } catch (InvocationTargetException | InterruptedException ex) {
                    /*
                     * This is just a stack trace print, in a real program there
                     * should be some logging and decent error reporting
                     */
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
        
        /**
         *command to up counting 
         */
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
