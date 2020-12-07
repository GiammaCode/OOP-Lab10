package it.unibo.oop.lab.workers02;

import java.util.ArrayList;
import java.util.List;

public class MultiThreadedSumMatrix implements SumMatrix {
    
    final private int nWorker;
    
    public MultiThreadedSumMatrix (final int nWorker) {
        super();
        if(nWorker < 1) {
            throw new IllegalArgumentException();
        }
        this.nWorker = nWorker; 
    }
    
    private class Worker extends Thread{
        private final double[][] matrix;
        private final int startpos;
        private final int nelem;
        private long res;
        
        public Worker(final double[][] matrix,final int startpos,final int nelem) {
            super();
            this.matrix = matrix;
            this.startpos = startpos;
            this.nelem = nelem;
        }
        
        public void run() {
            for(int i=0; i < matrix.length && i < startpos + nelem; i++) {
                for (double d : this.matrix[i]) {
                    this.res += d;
                }
            }
        }
        
        
        public double getResult() {
           return this.res;
        }
        
    }
    
    
    
    @Override
    public double sum(double[][] matrix) {
        
        final int size = matrix.length % nWorker + matrix.length / nWorker ;
        final List<Worker> workers = new ArrayList<>(nWorker);
        for (int start = 0; start < matrix.length; start += size) {
            workers.add(new Worker(matrix, start, size));
        }
        for (final Thread worker: workers) {
            worker.start();
        }
        double sum = 0;
        for (final Worker worker: workers) {
            try {
                worker.join();
                sum += worker.getResult();
            } catch (InterruptedException e) {
                throw new IllegalStateException(e);
            }
        }
        return sum;
    }
 
}
