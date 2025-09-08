package com.solvd.uber.parsing;

import com.solvd.uber.dao.mysqlimpl.RideDAO;
import com.solvd.uber.models.Ride;

import javax.xml.stream.XMLStreamException;
import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.List;

public class RideCRUDManager {

    private final String fileName;

    public RideCRUDManager(String fileName) {
        this.fileName = fileName;
    }

    public List<Ride> readAll() throws FileNotFoundException, XMLStreamException {
        return RideStaxReader.readRides(fileName);
    }

    public void addRide(Ride newRide) throws Exception {
        List<Ride> rides = readAll();
        rides.add(newRide);
        RideStaxWriter.writeRides(rides, fileName);
    }

    public void deleteRide(long rideId) throws Exception {
        List<Ride> rides = readAll();
        Iterator<Ride> iterator = rides.iterator();
        while (iterator.hasNext()) {
            Ride ride = iterator.next();
            if (ride.getId() == rideId) {
                iterator.remove();
                break;
            }
        }
        RideStaxWriter.writeRides(rides, fileName);
    }

    public void syncXmlToDatabase(RideDAO rideDAO) throws Exception {
        List<Ride> rides = readAll();
        for (Ride ride : rides) {
            rideDAO.insert(ride);
        }
    }
}
