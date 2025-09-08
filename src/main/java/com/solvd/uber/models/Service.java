package com.solvd.uber.models;

import com.solvd.uber.enums.ServiceType;

public class Service {
    private long id;
    private Account account = new Account();
    private ServiceType serviceType;
    private String optionName;

    public Service() {}

    public Service(long id, Account account, ServiceType serviceType, String optionName) {
        this.id = id;
        this.account = account;
        this.serviceType = serviceType;
        this.optionName = optionName;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public Account getAccount() { return account; }
    public void setAccount(Account account) { this.account = account; }

    public ServiceType getServiceType() { return serviceType; }
    public void setServiceType(ServiceType serviceType) { this.serviceType = serviceType; }

    public String getOptionName() { return optionName; }
    public void setOptionName(String optionName) { this.optionName = optionName; }
}
