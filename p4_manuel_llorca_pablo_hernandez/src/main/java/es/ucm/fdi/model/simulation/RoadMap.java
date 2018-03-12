package es.ucm.fdi.model.simulation;

import java.util.ArrayList;
import java.util.Iterator;

import es.ucm.fdi.model.SimObj.Junction;
import es.ucm.fdi.model.SimObj.Road;
import es.ucm.fdi.util.MultiTreeMap;

public class RoadMap {
    
    // MAPA
    private MultiTreeMap<Junction, Road> inputRoads;
    private MultiTreeMap<Junction, Road> exitRoads;

    // MÉTODOS
    /**
     * Devuelve el número de carreteras entrantes que tiene
     * un cruce.
     * @param junction cruce a consultar
     * @returns número de carreteras entrantes
     */
    public int getIncomingSizeIn(Junction junction) {
        return inputRoads.get(junction).size();
    }

    /**
     * Devuelve una carretera entrante dada su posición en el ArrayList.
     * @param junction cruce en cuestión
     * @param pos posición en la lista de entrantes
     * @returns carretera entrante en esa posición
     */
    public Road getIncomingRoad(Junction junction, int pos) {
        return inputRoads.get(junction).get(pos);
    }

    /**
     * Devuelve la lista de carreteras entrantes de un Junction.
     * @param junction cruce en cuestión
     * @returns ArrayList con carreteras entrantes
     */
    public ArrayList<Road> getIncomingRoadsOf(Junction junction) {
        return inputRoads.get(junction);
    }
    
    /**
     * Devuelve la lista de carreteras salientes de un Junction
     * @param junction cruce en cuestión
     * @returns ArrayList con carreteras salientes
     */
    public ArrayList<Road> getExitRoadsOf(Junction junction) {
        return exitRoads.get(junction);
    }

    /**
     * Busca y devuelve la carretera (única) que une dos Junction.
     * @param fromJ cruce de salida
     * @param toJ cruce de llegada
     * @returns carretera común
     */
    public Road getRoadBetween(Junction fromJ, Junction toJ) {
        // Carreteras de salida y entrada.
        ArrayList<Road> fromRoads = exitRoads.get(fromJ);
        ArrayList<Road> toRoads = inputRoads.get(toJ);
        // Carretera buscada.
        Road searched = null;

        // Se recorren las carreteras de salida.
        boolean found = false;
        Iterator<Road> fromIt = fromRoads.iterator();
        while ( fromIt.hasNext() && ! found ) {
            Road fromR = fromIt.next();
            
            // Se recorren las carreteras de entrada.
            Iterator<Road> toIt = toRoads.iterator();
            while ( toIt.hasNext() && ! found ) {
                Road toR = toIt.next();
                if ( toR == fromR ) {
                    found = true;
                    searched = fromR;
                }
            }
        }

        if ( found ) {
            return searched;
        }
        else { 
            // EXCEPCION.
            return null;
        }
    }
    
    /*
    * COMENTARIO TOCHO DE COSAS SOBRE FORMA DE ALMACENAR ESTO
    */ 
    
    /*
    * Encontrar forma de mapear, si utilizaramos 2 treeMaps,
    * tal vez, la clase incomingRoads dejaría de tener sentido
    * en Junction.
    * Pero si crearamos IncomingRoad(y quizá exitRoad) como clase,
    * ¿no habría una (exit)Road en el mapa exitRoads y la misma
    * incomingRoad en el mapa inputRoads; es decir, duplicadas.
    */

    /*
    * Tener en cuenta también que con MultiTreeMap, podríamos
    * saber qué carreteras salientes tiene una Junction, pero
    * dada una carretera no podríamos hacer la operación inversa.
    *
    * Quizá por ello tuviese sentido los atributos initialJunction
    * y endJunction en cada Road.
    */

    /*
    * Y al final el problema acaba siendo: ¿de qué forma va a leer el
    * programa los datos?
    */
    
    
    // POSIBLE IMPLEMENTACIÓN.
    

    // Métodos de entrada de datos.

    // public Road searchRoadBetween(Junction fromJunction, Junction toJunction)


}