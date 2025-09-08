package com.solvd.uber.models;

import java.time.LocalDateTime;

public class DriverProfile {
    private long id;
    private Person person = new Person();
    private LocalDateTime hiredAt;

    public DriverProfile() {}

    public DriverProfile(long id, Person person, LocalDateTime hiredAt) {
        this.id = id;
        this.person = person;
        this.hiredAt = hiredAt;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public Person getPerson() { return person; }
    public void setPerson(Person person) { this.person = person; }

    public LocalDateTime getHiredAt() { return hiredAt; }
    public void setHiredAt(LocalDateTime hiredAt) { this.hiredAt = hiredAt; }

	public void setPersonId(long id) {
		// TODO Auto-generated method stub
		
	}
}
