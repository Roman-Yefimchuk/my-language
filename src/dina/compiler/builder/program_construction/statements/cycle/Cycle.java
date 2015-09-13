package dina.compiler.builder.program_construction.statements.cycle;

import java.util.*;
import dina.compiler.builder.program_construction.statements.*;

public class Cycle extends StatementContainer {

    private ArrayList<CycleManagement> managements;

    public Cycle() {
        managements = new ArrayList<CycleManagement>();
    }

    public void addManagement(CycleManagement cm) {
        managements.add(cm);
    }

    public void setNewCycleContainer(StatementContainer container) {
        int managementsAmount = managements.size();
        for (int managementIndex = 0; managementIndex < managementsAmount; managementIndex++) {
            managements.get(managementIndex).setCycleContainer(container);
        }
    }
}
