package com.fintracker.backend.fintrackermonolith.other.service.cbr.dao;

import jakarta.xml.bind.annotation.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@XmlRootElement(name = "ValCurs")
@XmlAccessorType(XmlAccessType.FIELD)
@Getter
@Setter
public class ValKursCBR {
    @XmlAttribute(name = "Date")
    private String date;

    @XmlAttribute(name = "name")
    private String name;

    @XmlElement(name = "Valute")
    private List<ValuteCBR> valutes;
}
