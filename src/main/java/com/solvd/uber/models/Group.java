package com.solvd.uber.models;

import java.time.LocalDateTime;

public class Group {
    private long id;
    private Account account = new Account();
    private String name;
    private LocalDateTime createdAt;
    private boolean isBusiness;

    public Group() {}

    public Group(long id, Account account, String name, LocalDateTime createdAt, boolean isBusiness) {
        this.id = id;
        this.account = account;
        this.name = name;
        this.createdAt = createdAt;
        this.isBusiness = isBusiness;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public Account getAccount() { return account; }
    public void setAccount(Account account) { this.account = account; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

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
