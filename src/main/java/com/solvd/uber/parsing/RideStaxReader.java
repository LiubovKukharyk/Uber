package com.solvd.uber.parsing;

import com.solvd.uber.models.Person;
import com.solvd.uber.models.Ride;
import com.solvd.uber.models.Service;
import com.solvd.uber.models.Promo;
import com.solvd.uber.enums.RideStatus;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.XMLEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RideStaxReader {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

    public static List<Ride> readRides(String fileName) throws FileNotFoundException, XMLStreamException {
        List<Ride> rides = new ArrayList<>();
        Ride currentRide = null;
        Person currentPassenger = null;
        Person currentDriver = null;
        Service currentService = null;
        Promo currentPromo = null;
        String elementContent = null;
        String currentElement = null;

        XMLEventReader reader = XMLInputFactory.newInstance()
                .createXMLEventReader(new FileInputStream(fileName));

        while (reader.hasNext()) {
            XMLEvent event = reader.nextEvent();

            switch (event.getEventType()) {
                case XMLStreamConstants.START_ELEMENT:
                    currentElement = event.asStartElement().getName().getLocalPart();
                    switch (currentElement) {
                        case "ride": currentRide = new Ride(); break;
                        case "passenger": currentPassenger = new Person(); break;
                        case "driver": currentDriver = new Person(); break;
                        case "service": currentService = new Service(); break;
                        case "promo": currentPromo = new Promo(); break;
                    }
                    break;

                case XMLStreamConstants.CHARACTERS:
                    Characters characters = event.asCharacters();
                    if (!characters.isWhiteSpace()) elementContent = characters.getData();
                    break;

                case XMLStreamConstants.END_ELEMENT:
                    String endElementName = event.asEndElement().getName().getLocalPart();

                    if (elementContent != null) {
                        try {
                            switch (endElementName) {
                                case "id":
                                    if (currentRide != null) currentRide.setId(Long.parseLong(elementContent));
                                    break;
                                case "status":
                                    if (currentRide != null) {
                                        String norm = elementContent.trim().replace(' ', '_').toUpperCase();
                                        try {
                                            currentRide.setStatus(RideStatus.valueOf(norm));
                                        } catch (IllegalArgumentException ignored) {}
                                    }
                                    break;
                                case "startTime":
                                    if (currentRide != null) currentRide.setStartTime(DATE_FORMAT.parse(elementContent).toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDateTime());
                                    break;
                                case "endTime":
                                    if (currentRide != null) currentRide.setEndTime(DATE_FORMAT.parse(elementContent).toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDateTime());
                                    break;

                                case "passengerId":
                                    if (currentPassenger != null) currentPassenger.setId(Long.parseLong(elementContent));
                                    break;
                                case "passengerFirstName":
                                    if (currentPassenger != null) currentPassenger.setFirstName(elementContent);
                                    break;
                                case "passengerLastName":
                                    if (currentPassenger != null) currentPassenger.setLastName(elementContent);
                                    break;

                                case "driverId":
                                    if (currentDriver != null) currentDriver.setId(Long.parseLong(elementContent));
                                    break;
                                case "driverFirstName":
                                    if (currentDriver != null) currentDriver.setFirstName(elementContent);
                                    break;
                                case "driverLastName":
                                    if (currentDriver != null) currentDriver.setLastName(elementContent);
                                    break;

                                case "serviceId":
                                    if (currentService != null) currentService.setId(Long.parseLong(elementContent));
                                    break;
                                case "serviceName":
                                    if (currentService != null) currentService.setOptionName(elementContent);
                                    break;

                                case "promoId":
                                    if (currentPromo != null) currentPromo.setId(Long.parseLong(elementContent));
                                    break;
                                case "promoCode":
                                    if (currentPromo != null) currentPromo.setCode(elementContent);
                                    break;
                                case "isVoucher":
                                    if (currentPromo != null) currentPromo.setVoucher(Boolean.parseBoolean(elementContent));
                                    break;
                                case "discountPercent":
                                    if (currentPromo != null) currentPromo.setDiscountPercent(Double.parseDouble(elementContent));
                                    break;
                                case "discountAmount":
                                    if (currentPromo != null) currentPromo.setDiscountAmount(Double.parseDouble(elementContent));
                                    break;
                                case "validUntil":
                                    if (currentPromo != null) currentPromo.setValidUntil(DATE_FORMAT.parse(elementContent));
                                    break;
                                case "createdAt":
                                    if (currentPromo != null) currentPromo.setCreatedAt(DATE_FORMAT.parse(elementContent));
                                    break;
                            }
                        } catch (Exception e) {
                            System.err.println("Error parsing element " + endElementName + ": " + e.getMessage());
                        }
                    }

                    switch (endElementName) {
                        case "passenger":
                            if (currentRide != null) currentRide.setPassenger(currentPassenger);
                            break;
                        case "driver":
                            if (currentRide != null) currentRide.setDriver(currentDriver);
                            break;
                        case "service":
                            if (currentRide != null) currentRide.setService(currentService);
                            break;
                        case "promo":
                            if (currentRide != null) currentRide.setPromo(currentPromo);
                            break;
                        case "ride":
                            rides.add(currentRide);
                            break;
                    }
                    elementContent = null;
                    break;
            }
        }
        reader.close();
        return rides;
    }
}
