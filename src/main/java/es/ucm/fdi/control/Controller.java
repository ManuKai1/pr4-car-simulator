package es.ucm.fdi.control;

import java.io.OutputStream;

import es.ucm.fdi.control.evbuild.EventParser;
import es.ucm.fdi.ini.Ini;
import es.ucm.fdi.ini.IniSection;
import es.ucm.fdi.model.simulation.SimulationException;
import es.ucm.fdi.model.simulation.TrafficSimulation;
import es.ucm.fdi.model.events.Event;

public class Controller {
    Ini iniInput;
    OutputStream outStream;
    int timeLimit;

    public Controller(Ini in, OutputStream out, int time) {
        iniInput = in;
        outStream = out;
        timeLimit = time;
    }

    public void execute() {
        TrafficSimulation simulator = new TrafficSimulation();
        EventParser parser = new EventParser();

        // 1 //
        // Recorre las secciones del archivo .ini de entrada
        // y construye y guarda los eventos en el simulador.
        for ( IniSection sec : iniInput.getSections() ) {
        	try{
        		Event ev = parser.parse(sec);
                simulator.pushEvent(ev);   
        	}
            catch(IllegalArgumentException e){
            	System.err.println( e.getMessage() );
            }   
        	catch (SimulationException e) {
                System.err.println( e.getMessage() );
            } 
        }

        // 2 // 
        // Se ejecuta el simulador el número de pasos timeLimit
        // y se actualiza el OutputStream.
        simulator.execute(timeLimit, outStream);
    }
}
