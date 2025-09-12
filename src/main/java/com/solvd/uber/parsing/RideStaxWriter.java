package com.solvd.uber.parsing;

import com.solvd.uber.models.Person;
import com.solvd.uber.models.Ride;
import com.solvd.uber.models.Service;
import com.solvd.uber.models.Promo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.util.List;

public class RideStaxWriter {

    private static final Logger LOGGER = LogManager.getLogger("RidesLogger");

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
                writeElement(writer, "status", ride.getStatus() != null ? ride.getStatus().name() : "NULL", 2);
                writeElement(writer, "startTime", ride.getStartTime() != null ? ride.getStartTime().toString() : "NULL", 2);
                writeElement(writer, "endTime", ride.getEndTime() != null ? ride.getEndTime().toString() : "NULL", 2);

                Person passenger = ride.getPassenger();
                writer.writeCharacters("\t\t");
                writer.writeStartElement("passenger");
                writer.writeCharacters("\n");
                writeElement(writer, "id", passenger != null ? String.valueOf(passenger.getId()) : "NULL", 3);
                writeElement(writer, "firstName", passenger != null ? passenger.getFirstName() : "NULL", 3);
                writeElement(writer, "lastName", passenger != null ? passenger.getLastName() : "NULL", 3);
                writer.writeCharacters("\t\t");
                writer.writeEndElement();
                writer.writeCharacters("\n");

                Person driver = ride.getDriver();
                writer.writeCharacters("\t\t");
                writer.writeStartElement("driver");
                writer.writeCharacters("\n");
                writeElement(writer, "id", driver != null ? String.valueOf(driver.getId()) : "NULL", 3);
                writeElement(writer, "firstName", driver != null ? driver.getFirstName() : "NULL", 3);
                writeElement(writer, "lastName", driver != null ? driver.getLastName() : "NULL", 3);
                writer.writeCharacters("\t\t");
                writer.writeEndElement();
                writer.writeCharacters("\n");

                Service service = ride.getService();
                writer.writeCharacters("\t\t");
                writer.writeStartElement("service");
                writer.writeCharacters("\n");
                writeElement(writer, "id", service != null ? String.valueOf(service.getId()) : "NULL", 3);
                writeElement(writer, "optionName", service != null ? service.getOptionName() : "NULL", 3);
                writer.writeCharacters("\t\t");
                writer.writeEndElement();
                writer.writeCharacters("\n");

                Promo promo = ride.getPromo();
                writer.writeCharacters("\t\t");
                writer.writeStartElement("promo");
                writer.writeCharacters("\n");
                writeElement(writer, "id", promo != null ? String.valueOf(promo.getId()) : "NULL", 3);
                writeElement(writer, "voucher", promo != null ? String.valueOf(promo.isVoucher()) : "NULL", 3);
                writeElement(writer, "code", promo != null ? promo.getCode() : "NULL", 3);
                writeElement(writer, "discountPercent", promo != null && promo.getDiscountPercent() != null ? String.valueOf(promo.getDiscountPercent()) : "NULL", 3);
                writeElement(writer, "discountAmount", promo != null && promo.getDiscountAmount() != null ? String.valueOf(promo.getDiscountAmount()) : "NULL", 3);
                writeElement(writer, "validUntil", promo != null && promo.getValidUntil() != null ? promo.getValidUntil().toString() : "NULL", 3);
                writeElement(writer, "createdAt", promo != null && promo.getCreatedAt() != null ? promo.getCreatedAt().toString() : "NULL", 3);
                writer.writeCharacters("\t\t");
                writer.writeEndElement();
                writer.writeCharacters("\n");

                writer.writeCharacters("\t");
                writer.writeEndElement();
                writer.writeCharacters("\n");
            }

            writer.writeEndElement();
            writer.writeCharacters("\n");
            writer.writeEndDocument();

            writer.flush();
            writer.close();

            LOGGER.info("{} is saved", fileName);

        } catch (IOException e) {
            LOGGER.error("Error writing XML file {}", fileName, e);
            throw new RuntimeException(e);
        }
    }

    private static void writeElement(XMLStreamWriter writer, String name, String value, int indentLevel) throws XMLStreamException {
        writer.writeCharacters("\t".repeat(indentLevel));
        writer.writeStartElement(name);
        writer.writeCharacters(value != null ? value : "NULL");
        writer.writeEndElement();
        writer.writeCharacters("\n");
    }

    public static void writeBothFiles(List<Ride> rides) throws XMLStreamException {
        writeRides(rides, "rides.xml");
        writeRides(rides, "rides_jaxb.xml");
    }
}
