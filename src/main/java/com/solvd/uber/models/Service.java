package com.solvd.uber.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.solvd.uber.enums.ServiceType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "service")
public class Service {
    private long id;
    private ServiceType serviceType;
    private String optionName;

    public Service() {}

    @XmlElement
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    @JsonIgnore
    public ServiceType getServiceType() { return serviceType; }
    public void setServiceType(ServiceType serviceType) { this.serviceType = serviceType; }

    @XmlElement
    public String getOptionName() { return optionName; }
    public void setOptionName(String optionName) { this.optionName = optionName; }
}
