package com.ada.avanadestore.entitity;

import com.ada.avanadestore.dto.AddressDTO;
import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "adresses")

public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false, length = 80)
    private String street;

    @Column(length = 10)
    private String number;

    @Column(nullable = false, length = 60)
    private String city;

    @Column(nullable = false, length = 60)
    private String state;

    @Column(nullable = false, length = 9)
    private String zip;

    private String complement;

    public Address() {
    }

    public Address(AddressDTO dto) {
        this.street = dto.street();
        this.number = dto.number();
        this.city = dto.city();
        this.state = dto.state();
        this.zip = dto.zip();
        this.complement = dto.complement();
    }

    public UUID getId() {
        return id;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getComplement() {
        return complement;
    }

    public void setComplement(String complement) {
        this.complement = complement;
    }

    public AddressDTO toDTO() {
        return new AddressDTO(id, street, number, city, state, zip, complement);
    }
}
