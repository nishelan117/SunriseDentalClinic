package com.sunrise.dentalclinic;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bills")
public class BillController {

    private final BillRepository billRepository;

    public BillController(BillRepository billRepository) {
        this.billRepository = billRepository;
    }


    // ==========================================
    // GET ALL BILLS
    // ==========================================

    @GetMapping
    public List<Bill> getAllBills() {

        return billRepository.findAll();
    }


    // ==========================================
    // GET BILL BY ID
    // ==========================================

    @GetMapping("/{id}")
    public ResponseEntity<Bill> getBill(
            @PathVariable Long id) {

        return billRepository
                .findById(id)
                .map(ResponseEntity::ok)
                .orElse(
                        ResponseEntity
                                .notFound()
                                .build()
                );
    }


    // ==========================================
    // ADD BILL
    // ==========================================

    @PostMapping
    public ResponseEntity<Bill> addBill(
            @RequestBody Bill bill) {

        /*
         * Calculate total amount from:
         *
         * Treatment Fee
         * +
         * Consultation Fee
         */

        double total =
                bill.getTreatmentFee()
                        + bill.getConsultationFee();

        bill.setAmount(total);


        Bill savedBill =
                billRepository.save(bill);


        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(savedBill);
    }


    // ==========================================
    // UPDATE BILL
    // ==========================================

    @PutMapping("/{id}")
    public ResponseEntity<Bill> updateBill(
            @PathVariable Long id,
            @RequestBody Bill bill) {


        Bill existingBill =
                billRepository
                        .findById(id)
                        .orElse(null);


        if (existingBill == null) {

            return ResponseEntity
                    .notFound()
                    .build();
        }


        existingBill.setAppointmentNumber(
                bill.getAppointmentNumber()
        );


        existingBill.setPatientName(
                bill.getPatientName()
        );


        existingBill.setTreatment(
                bill.getTreatment()
        );


        existingBill.setTreatmentFee(
                bill.getTreatmentFee()
        );


        existingBill.setConsultationFee(
                bill.getConsultationFee()
        );


        /*
         * Recalculate total amount
         */

        double total =
                bill.getTreatmentFee()
                        + bill.getConsultationFee();


        existingBill.setAmount(total);


        existingBill.setPaymentStatus(
                bill.getPaymentStatus()
        );


        existingBill.setBillDate(
                bill.getBillDate()
        );


        Bill updatedBill =
                billRepository.save(existingBill);


        return ResponseEntity.ok(
                updatedBill
        );
    }


    // ==========================================
    // DELETE BILL
    // ==========================================

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBill(
            @PathVariable Long id) {


        if (!billRepository.existsById(id)) {

            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Bill not found");
        }


        billRepository.deleteById(id);


        return ResponseEntity.ok(
                "Bill deleted successfully"
        );
    }

}