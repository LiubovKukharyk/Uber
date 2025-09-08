package com.solvd.uber.parsing;

import com.solvd.uber.models.Person;
import com.solvd.uber.models.Ride;
import com.solvd.uber.models.Service;
import com.solvd.uber.models.Promo;
import com.solvd.uber.enums.RideStatus;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.util.List;

public class RideStaxWriter {

    public static void writeRides(List<Ride> rides, String fileName) throws XMLStreamException {
        try (OutputStream outputStream = new FileOutputStream(fileName)) {
            XMLOutputFactory factory = XMLOutputFactory.newFactory();
            XMLStreamWriter writer = factory.createXMLStreamWriter(outputStream, "UTF-8");

            writer.writeStartDocument("UTF-8", "1.0");
            writer.writeCharacters("\n");
            writer.writeStartElement("rides");
            writer.writeCharacters("\n");

            for (Ride ride : rides) {
                writer.writeCharacters("\t");
                writer.writeStartElement("ride");
                writer.writeCharacters("\n");

                writeElement(writer, "id", String.valueOf(ride.getId()), 2);

                RideStatus status = ride.getStatus();
                if (status != null) {
                    writeElement(writer, "status", status.name(), 2);
                }

                if (ride.getStartTime() != null) {
                    writeElement(writer, "startTime", ride.getStartTime().toString(), 2);
                }
                if (ride.getEndTime() != null) {
                    writeElement(writer, "endTime", ride.getEndTime().toString(), 2);
                }

                // Passenger
                Person passenger = ride.getPassenger();
                if (passenger != null) {
                    writer.writeCharacters("\t\t");
                    writer.writeStartElement("passenger");
                    writer.writeCharacters("\n");
                    writeElement(writer, "passengerId", String.valueOf(passenger.getId()), 3);
                    writeElement(writer, "passengerFirstName", passenger.getFirstName(), 3);
                    writeElement(writer, "passengerLastName", passenger.getLastName(), 3);
                    writer.writeCharacters("\t\t");
                    writer.writeEndElement();
                    writer.writeCharacters("\n");
                }

                // Driver
                Person driver = ride.getDriver();
                if (driver != null) {
                    writer.writeCharacters("\t\t");
                    writer.writeStartElement("driver");
                    writer.writeCharacters("\n");
                    writeElement(writer, "driverId", String.valueOf(driver.getId()), 3);
                    writeElement(writer, "driverFirstName", driver.getFirstName(), 3);
                    writeElement(writer, "driverLastName", driver.getLastName(), 3);
                    writer.writeCharacters("\t\t");
                    writer.writeEndElement();
                    writer.writeCharacters("\n");
                }

                // Service
                Service service = ride.getService();
                if (service != null) {
                    writer.writeCharacters("\t\t");
                    writer.writeStartElement("service");
                    writer.writeCharacters("\n");
                    writeElement(writer, "serviceId", String.valueOf(service.getId()), 3);
                    writeElement(writer, "serviceName", service.getOptionName(), 3);
                    writer.writeCharacters("\t\t");
                    writer.writeEndElement();
                    writer.writeCharacters("\n");
                }

                // Promo
                Promo promo = ride.getPromo();
                if (promo != null) {
                    writer.writeCharacters("\t\t");
                    writer.writeStartElement("promo");
                    writer.writeCharacters("\n");
                    writeElement(writer, "promoId", String.valueOf(promo.getId()), 3);
                    writeElement(writer, "promoCode", promo.getCode(), 3);
                    writeElement(writer, "isVoucher", String.valueOf(promo.isVoucher()), 3);
                    if (promo.getDiscountPercent() != null) {
                        writeElement(writer, "discountPercent", String.valueOf(promo.getDiscountPercent()), 3);
                    }
                    if (promo.getDiscountAmount() != null) {
                        writeElement(writer, "discountAmount", String.valueOf(promo.getDiscountAmount()), 3);
                    }
                    if (promo.getValidUntil() != null) {
                        writeElement(writer, "validUntil", promo.getValidUntil().toString(), 3);
                    }
                    if (promo.getCreatedAt() != null) {
                        writeElement(writer, "createdAt", promo.getCreatedAt().toString(), 3);
                    }
                    writer.writeCharacters("\t\t");
                    writer.writeEndElement();
                    writer.writeCharacters("\n");
                }

                writer.writeCharacters("\t");
                writer.writeEndElement();
                writer.writeCharacters("\n");
            }

            writer.writeEndElement();
            writer.writeCharacters("\n");
            writer.writeEndDocument();

            writer.flush();
            writer.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void writeElement(XMLStreamWriter writer, String name, String value, int indentLevel) throws XMLStreamException {
        if (value != null) {
            String indent = "\t".repeat(indentLevel);
            writer.writeCharacters(indent);
            writer.writeStartElement(name);
            writer.writeCharacters(value);
            writer.writeEndElement();
            writer.writeCharacters("\n");
        }
    }
}
