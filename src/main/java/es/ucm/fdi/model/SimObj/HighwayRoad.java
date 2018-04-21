package es.ucm.fdi.model.SimObj;

import java.util.ArrayList;

import es.ucm.fdi.ini.IniSection;

public class HighwayRoad extends Road {
    
    /**
     * Número de carriles de la autopista.
     */
    private int numLanes;

    /**
     * Tipo de carretera.
     */
    private String type = "lanes";

    public HighwayRoad(String identifier, int len, int spLimit, Junction fromJ, Junction toJ, int lanes) {
        super(identifier, len, spLimit, fromJ, toJ);
        numLanes = lanes;
    }
    
    /**
     * Calcula la velocidad base de la Highway: el mínimo entre el límite
     * de velocidad y la velocidad que permite la congestión del tráfico en
     * la Road.
     */
    @Override
    protected int getBaseSpeed() {
        // Cálculo de velocidadBase según la fórmula
        int congestionSpeed = (speedLimit / Math.max(vehiclesOnRoad.size(), numLanes)) + 1;

        return Math.min(speedLimit, congestionSpeed);
    }

    /**
     * Modifica la velocidad que llevarán los vehículos en la
     * carretera (autopista) previo avance.
     */
    @Override
    protected void vehicleSpeedModifier(ArrayList<Vehicle> onRoad) {
        // Velocidad máxima a la que pueden avanzar los vehículos.
        int baseSpeed = getBaseSpeed();

        // Factor de reducción de velocidad en caso de obstáculos delante.
        int reductionFactor = 1;

        // Número de vehículos averiados.
        int brokenVehicles = 0;

        // Se modifica la velocidad a la que avanzarán los vehículos,
        // teniendo en cuenta el factor de reducción.
        for (Vehicle v : onRoad) {
            v.setSpeed(baseSpeed / reductionFactor);

            if (v.getBreakdownTime() > 0) {
                brokenVehicles += 1;
            }

            if (brokenVehicles >= numLanes) {
                reductionFactor = 2;
            }
        }
    }

    /**
     * Informe de la HighwayRoad en cuestión, mostrando: id,
     * tiempo de simulación, tipo y estado.
     * @param simTime tiempo de simulación
     * @returns well-formatted String representing a Road report
     */
    @Override
    public String getReport(int simTime) {
        StringBuilder report = new StringBuilder();
        // TITLE
        report.append(REPORT_TITLE + '\n');
        // ID
        report.append("id = " + id + '\n');
        // SimTime
        report.append("time = " + simTime + '\n');
        // Type
        report.append("type = " + type + '\n');
        // Road State
        report.append("state = ");
        report.append(getRoadState());

        return report.toString();
    }

    /**
     * A partir de los datos de la carretera (autopista) genera una IniSection
     * @param simTime tiempo del simulador
     * @return IniSection report de la carretera
     */
    @Override
    public IniSection generateIniSection(int simTime) {
        String tag = REPORT_TITLE;
        //Creación de etiqueta (sin corchetes)
        tag = (String) tag.subSequence(1, tag.length() - 1);
        IniSection section = new IniSection(tag);
        section.setValue("id", id);
        section.setValue("time", simTime);
        section.setValue("type", type);
        section.setValue("state", getRoadState().toString());
        return section;
    }
}