package com.homework.demo.model;


import javax.persistence.*;

@Entity
@Table(name="customer")
public class Customer {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name="id")
    private Long id;

    @Column(name="first_name")
    private String firstName;

    @Column(name="middle_name")
    private String middleName;

    @Column(name="last_name")
    private String lastName;

    @Column(name="initials")
    private String initials;

    @Column(name="title")
    private String title;

    @ManyToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "address_id")
   private ResidentialAddress residentialAddress;

    @Column(name="sex")
    private String sex;

    @Column(name="credit_rating")
    private int creditRating;

    @Column(name="is_nab_customer")
    private String isNabCustomer;

    public Customer(){}

    public Customer(Long id, String firstName, String middleName, String lastName, String initials, String title, ResidentialAddress residentialAddress, String sex, int creditRating, String isNabCustomer) {
        this.id = id;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.initials = initials;
        this.title = title;
        this.residentialAddress = residentialAddress;
        this.sex = sex;
        this.creditRating = creditRating;
        this.isNabCustomer = isNabCustomer;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public String getMiddleName() {
        return middleName;
    }
    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }
    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public String getInitials() {
        return initials;
    }
    public void setInitials(String initials) {
        this.initials = initials;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getSex() {
        return sex;
    }
    public void setSex(String sex) {
        this.sex = sex;
    }
    public int getCreditRating() {
        return creditRating;
    }
    public void setCreditRating(int creditRating) {
        this.creditRating = creditRating;
    }
    public String getIsNabCustomer() {
        return isNabCustomer;
    }
    public void setIsNabCustomer(String isNabCustomer) {
        this.isNabCustomer = isNabCustomer;
    }

    public ResidentialAddress getResidentialAddress() {
        return residentialAddress;
    }

    public void setResidentialAddress(ResidentialAddress residentialAddress) {
        this.residentialAddress = residentialAddress;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", middleName='" + middleName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", initials='" + initials + '\'' +
                ", title='" + title + '\'' +
                ", residentialAddress=" + residentialAddress +
                ", sex='" + sex + '\'' +
                ", creditRating=" + creditRating +
                ", isNabCustomer='" + isNabCustomer + '\'' +
                '}';
    }
}
