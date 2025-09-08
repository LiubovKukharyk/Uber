package com.solvd.uber.models;

import java.time.LocalDateTime;

public class Account {
    private long id;
    private Person person = new Person();
    private boolean isBusiness;
    private String companyName;
    private LocalDateTime createdAt;

    public Account() {}

    public Account(long id, Person person, boolean isBusiness, String companyName, LocalDateTime createdAt) {
        this.id = id;
        this.person = person;
        this.isBusiness = isBusiness;
        this.companyName = companyName;
        this.createdAt = createdAt;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public Person getPerson() { return person; }
    public void setPerson(Person person) { this.person = person; }

    public boolean isBusiness() { return isBusiness; }

    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

	public String getType() {
		
		if (isBusiness)
		return "Business";
		else
		return "Personal";
	
	}

	public void setType(String string) {
		if (string == "Business") {
			isBusiness = true;
		}
		if (string == "Personal") {
			isBusiness = false;
		}
		else throw new IllegalArgumentException ("Invalid account type");
		
	}
}
