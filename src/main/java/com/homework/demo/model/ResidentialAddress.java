package com.homework.demo.model;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="residential_address")
public class ResidentialAddress{


    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name="address_id")
    private Long id;

    @Column(name="address")
    private String address;

    public  ResidentialAddress(){}
    public ResidentialAddress(Long id, String address) {

        this.id=id;
        this.address = address;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "ResidentialAddress{" +
                "id=" + id +
                ", address='" + address + '\'' +
                '}';
    }
}
