package com.solvd.uber.parsing;

import com.solvd.uber.models.Ride;

import javax.xml.stream.XMLStreamException;
import java.util.List;

public class RideExporter {

    public static void exportRides(List<Ride> rides) {
        if (rides == null || rides.isEmpty()) {
            System.out.println("No rides to export.");
            return;
        }

        try {
            RideStaxWriter.writeBothFiles(rides);
        } catch (XMLStreamException e) {
            System.err.println("Error while exporting rides: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
