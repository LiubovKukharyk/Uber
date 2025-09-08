package com.solvd.uber.parsing;

import com.solvd.uber.dao.IRideDAO;
import com.solvd.uber.dao.mysqlimpl.RideDAO;
import com.solvd.uber.models.Ride;
import com.solvd.uber.parsing.RideStaxWriter;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class RideExporter {
    public static void main(String[] args) throws SQLException, IOException {
        IRideDAO<Ride> rideDAO = new RideDAO();

        try {
            List<Ride> rides = rideDAO.getAll();
            RideStaxWriter.writeRides(rides, "rides.xml");
            System.out.println("rides.xml is saved");
        } catch (XMLStreamException e) {
            e.printStackTrace();
        }
    }
}
