
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Observable;
import java.util.Observer;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author wijay
 */
class ResponseHandler1 implements Observer {

    private String resp;

    @Override
    public void update(Observable o, Object arg) {
        if (arg instanceof String) {
            resp = (String) arg;
            System.out.println("\nReceived Notification :" + resp);
        }
    }
}
class ResponseHandler2 implements Observer {

    private String resp;

    @Override
    public void update(Observable o, Object arg) {
        if (arg instanceof String) {
            resp = (String) arg;
            System.out.println("\nReceived Notification :" + resp);
        }
    }
}

class EventSource extends Observable implements Runnable {

    @Override
    public void run() {
        try {
            final InputStreamReader isr = new InputStreamReader(System.in);
            final BufferedReader br = new BufferedReader(isr);
            while (true) {
                String response = br.readLine();
                setChanged();
                notifyObservers(response);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

}

public class Observer_Demo {
    public static void main(String[] args) {
        System.out.println("Add Text >");
       
        final EventSource es=new EventSource();
        
        
        final ResponseHandler1 responseHandler1=new ResponseHandler1();
       es.addObserver(responseHandler1);
       final ResponseHandler2 responseHandler2 =new ResponseHandler2();
       es.addObserver(responseHandler2);

       Thread thread=new Thread(es);
       thread.start();
    }
}
