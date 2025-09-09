package com.solvd.uber.models;

import com.solvd.uber.models.Ride;
import com.solvd.uber.models.RideListWrapper;
import com.solvd.uber.models.Person;
import com.solvd.uber.enums.RideStatus;

import javax.xml.bind.*;
import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Main {
    private static final String FILE_NAME = "rides_jaxb.xml";

    public static void main(String[] args) throws Exception {
        List<Ride> rides = new ArrayList<>();

        Ride ride1 = new Ride();
        ride1.setId(1);
        ride1.setStatus(RideStatus.REQUESTED);
        ride1.setStartTime(LocalDateTime.now());
        Person passenger = new Person();
        passenger.setId(101);
        passenger.setFirstName("John");
        passenger.setLastName("Smith");
        ride1.setPassenger(passenger);

        rides.add(ride1);

        RideListWrapper wrapper = new RideListWrapper();
        wrapper.setRides(rides);

        marshal(wrapper);
        RideListWrapper unmarshalled = unmarshal();

        for (Ride r : unmarshalled.getRides()) {
            System.out.println("Ride id=" + r.getId() + ", status=" + r.getStatus());
        }
    }

    private static void marshal(RideListWrapper wrapper) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(RideListWrapper.class, Ride.class);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.marshal(wrapper, new File(FILE_NAME));
    }

    private static RideListWrapper unmarshal() throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(RideListWrapper.class, Ride.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        return (RideListWrapper) unmarshaller.unmarshal(new File(FILE_NAME));
    }
}
