package com.solvd.uber.models;

import jakarta.xml.bind.annotation.adapters.XmlAdapter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateAdapter extends XmlAdapter<String, Date> {

    private static final SimpleDateFormat FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public Date unmarshal(String v) throws Exception {
        return (v == null || v.isEmpty()) ? null : FORMAT.parse(v);
    }

    @Override
    public String marshal(Date v) throws Exception {
        return (v == null) ? null : FORMAT.format(v);
    }
}
