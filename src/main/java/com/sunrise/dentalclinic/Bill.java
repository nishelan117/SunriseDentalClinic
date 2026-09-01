package com.sunrise.dentalclinic;

import jakarta.persistence.*;

@Entity
@Table(name = "bills")
public class Bill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String appointmentNumber;

    private String patientName;

    private String treatment;

    private double treatmentFee;

    private double consultationFee;

    private double amount;

    private String paymentStatus;

    private String billDate;


    // ==========================================
    // CONSTRUCTOR
    // ==========================================

    public Bill() {
    }


    // ==========================================
    // ID
    // ==========================================

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    // ==========================================
    // APPOINTMENT NUMBER
    // ==========================================

    public String getAppointmentNumber() {
        return appointmentNumber;
    }

    public void setAppointmentNumber(
            String appointmentNumber) {

        this.appointmentNumber =
                appointmentNumber;
    }


    // ==========================================
    // PATIENT NAME
    // ==========================================

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(
            String patientName) {

        this.patientName =
                patientName;
    }


    // ==========================================
    // TREATMENT
    // ==========================================

    public String getTreatment() {
        return treatment;
    }

    public void setTreatment(
            String treatment) {

        this.treatment =
                treatment;
    }


    // ==========================================
    // TREATMENT FEE
    // ==========================================

    public double getTreatmentFee() {
        return treatmentFee;
    }

    public void setTreatmentFee(
            double treatmentFee) {

        this.treatmentFee =
                treatmentFee;
    }


    // ==========================================
    // CONSULTATION FEE
    // ==========================================

    public double getConsultationFee() {
        return consultationFee;
    }

    public void setConsultationFee(
            double consultationFee) {

        this.consultationFee =
                consultationFee;
    }


    // ==========================================
    // TOTAL AMOUNT
    // ==========================================

    public double getAmount() {
        return amount;
    }

    public void setAmount(
            double amount) {

        this.amount =
                amount;
    }


    // ==========================================
    // PAYMENT STATUS
    // ==========================================

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(
            String paymentStatus) {

        this.paymentStatus =
                paymentStatus;
    }


    // ==========================================
    // BILL DATE
    // ==========================================

    public String getBillDate() {
        return billDate;
    }

    public void setBillDate(
            String billDate) {

        this.billDate =
                billDate;
    }

}