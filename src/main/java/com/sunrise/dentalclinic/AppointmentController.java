package com.sunrise.dentalclinic;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/appointments")
public class AppointmentController {

    private final AppointmentRepository appointmentRepository;

    public AppointmentController(
            AppointmentRepository appointmentRepository) {

        this.appointmentRepository = appointmentRepository;
    }


    // ==========================================
    // GET ALL APPOINTMENTS
    // ==========================================

    @GetMapping
    public List<Appointment> getAllAppointments() {

        return appointmentRepository.findAll();
    }


    // ==========================================
    // GET APPOINTMENT BY ID
    // ==========================================

    @GetMapping("/{id}")
    public ResponseEntity<Appointment> getAppointment(
            @PathVariable Long id) {

        return appointmentRepository
                .findById(id)
                .map(ResponseEntity::ok)
                .orElse(
                        ResponseEntity.notFound().build()
                );
    }


    // ==========================================
    // SEARCH BY APPOINTMENT NUMBER
    // ==========================================

    @GetMapping("/search")
    public ResponseEntity<?> searchAppointment(
            @RequestParam String appointmentNumber) {

        String number = appointmentNumber.trim();

        if (number.isEmpty()) {

            return ResponseEntity
                    .badRequest()
                    .body("Appointment number is required.");
        }

        Appointment appointment =
                appointmentRepository
                        .findByAppointmentNumber(number)
                        .orElse(null);

        if (appointment == null) {

            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Appointment not found.");
        }

        return ResponseEntity.ok(appointment);
    }


    // ==========================================
    // ADD NEW APPOINTMENT
    // ==========================================

    @PostMapping
    public ResponseEntity<?> addAppointment(
            @RequestBody Appointment appointment) {

        // --------------------------------------
        // BASIC VALIDATION
        // --------------------------------------

        if (isEmpty(appointment.getPatientName())) {

            return ResponseEntity
                    .badRequest()
                    .body("Patient name is required.");
        }


        if (isEmpty(appointment.getAddress())) {

            return ResponseEntity
                    .badRequest()
                    .body("Address is required.");
        }


        if (isEmpty(appointment.getContactNumber())) {

            return ResponseEntity
                    .badRequest()
                    .body("Contact number is required.");
        }


        if (!appointment.getContactNumber()
                .matches("\\d{10}")) {

            return ResponseEntity
                    .badRequest()
                    .body(
                            "Contact number must contain exactly 10 digits."
                    );
        }


        if (isEmpty(appointment.getDentistName())) {

            return ResponseEntity
                    .badRequest()
                    .body("Dentist name is required.");
        }


        if (isEmpty(appointment.getTreatmentType())) {

            return ResponseEntity
                    .badRequest()
                    .body("Treatment type is required.");
        }


        if (isEmpty(appointment.getAppointmentDate())) {

            return ResponseEntity
                    .badRequest()
                    .body("Appointment date is required.");
        }


        if (isEmpty(appointment.getAppointmentTime())) {

            return ResponseEntity
                    .badRequest()
                    .body("Appointment time is required.");
        }


        if (isEmpty(appointment.getStatus())) {

            return ResponseEntity
                    .badRequest()
                    .body("Appointment status is required.");
        }


        // --------------------------------------
        // DATE VALIDATION
        // --------------------------------------

        try {

            LocalDate appointmentDate =
                    LocalDate.parse(
                            appointment.getAppointmentDate()
                    );

            if (appointmentDate.isBefore(
                    LocalDate.now()
            )) {

                return ResponseEntity
                        .badRequest()
                        .body(
                                "Appointment date cannot be in the past."
                        );
            }

        } catch (Exception e) {

            return ResponseEntity
                    .badRequest()
                    .body(
                            "Invalid appointment date."
                    );
        }


        // --------------------------------------
        // DOUBLE BOOKING PREVENTION
        // --------------------------------------

        List<Appointment> appointments =
                appointmentRepository.findAll();


        for (Appointment existing : appointments) {

            boolean sameDentist =
                    existing.getDentistName()
                            .equalsIgnoreCase(
                                    appointment.getDentistName()
                            );

            boolean sameDate =
                    existing.getAppointmentDate()
                            .equals(
                                    appointment.getAppointmentDate()
                            );

            boolean sameTime =
                    existing.getAppointmentTime()
                            .equals(
                                    appointment.getAppointmentTime()
                            );

            boolean activeAppointment =
                    !isCancelled(
                            existing.getStatus()
                    );


            if (
                    sameDentist &&
                            sameDate &&
                            sameTime &&
                            activeAppointment
            ) {

                return ResponseEntity
                        .status(HttpStatus.CONFLICT)
                        .body(
                                "This dentist already has an appointment at the selected date and time."
                        );
            }
        }


        // --------------------------------------
        // GENERATE APPOINTMENT NUMBER
        // --------------------------------------

        long nextNumber =
                appointmentRepository.count() + 1;


        String appointmentNumber =
                String.format(
                        "A%03d",
                        nextNumber
                );


        // Make sure appointment number is unique

        while (
                appointmentRepository
                        .findByAppointmentNumber(
                                appointmentNumber
                        )
                        .isPresent()
        ) {

            nextNumber++;

            appointmentNumber =
                    String.format(
                            "A%03d",
                            nextNumber
                    );
        }


        appointment.setAppointmentNumber(
                appointmentNumber
        );


        // --------------------------------------
        // SAVE APPOINTMENT
        // --------------------------------------

        Appointment savedAppointment =
                appointmentRepository.save(
                        appointment
                );


        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(savedAppointment);
    }


    // ==========================================
    // UPDATE APPOINTMENT
    // ==========================================

    @PutMapping("/{id}")
    public ResponseEntity<?> updateAppointment(
            @PathVariable Long id,
            @RequestBody Appointment appointment) {

        Appointment existingAppointment =
                appointmentRepository
                        .findById(id)
                        .orElse(null);


        if (existingAppointment == null) {

            return ResponseEntity
                    .notFound()
                    .build();
        }


        // --------------------------------------
        // BASIC VALIDATION
        // --------------------------------------

        if (isEmpty(appointment.getPatientName())) {

            return ResponseEntity
                    .badRequest()
                    .body("Patient name is required.");
        }


        if (isEmpty(appointment.getAddress())) {

            return ResponseEntity
                    .badRequest()
                    .body("Address is required.");
        }


        if (isEmpty(appointment.getContactNumber())) {

            return ResponseEntity
                    .badRequest()
                    .body("Contact number is required.");
        }


        if (!appointment.getContactNumber()
                .matches("\\d{10}")) {

            return ResponseEntity
                    .badRequest()
                    .body(
                            "Contact number must contain exactly 10 digits."
                    );
        }


        if (isEmpty(appointment.getDentistName())) {

            return ResponseEntity
                    .badRequest()
                    .body("Dentist name is required.");
        }


        if (isEmpty(appointment.getTreatmentType())) {

            return ResponseEntity
                    .badRequest()
                    .body("Treatment type is required.");
        }


        if (isEmpty(appointment.getAppointmentDate())) {

            return ResponseEntity
                    .badRequest()
                    .body("Appointment date is required.");
        }


        if (isEmpty(appointment.getAppointmentTime())) {

            return ResponseEntity
                    .badRequest()
                    .body("Appointment time is required.");
        }


        if (isEmpty(appointment.getStatus())) {

            return ResponseEntity
                    .badRequest()
                    .body("Appointment status is required.");
        }


        // --------------------------------------
        // DATE VALIDATION
        // --------------------------------------

        try {

            LocalDate appointmentDate =
                    LocalDate.parse(
                            appointment.getAppointmentDate()
                    );

            if (appointmentDate.isBefore(
                    LocalDate.now()
            )) {

                return ResponseEntity
                        .badRequest()
                        .body(
                                "Appointment date cannot be in the past."
                        );
            }

        } catch (Exception e) {

            return ResponseEntity
                    .badRequest()
                    .body(
                            "Invalid appointment date."
                    );
        }


        // --------------------------------------
        // DOUBLE BOOKING CHECK
        // --------------------------------------

        List<Appointment> appointments =
                appointmentRepository.findAll();


        for (Appointment other : appointments) {

            // Ignore current appointment

            if (
                    other.getId() != null &&
                            other.getId().equals(id)
            ) {

                continue;
            }


            boolean sameDentist =
                    other.getDentistName()
                            .equalsIgnoreCase(
                                    appointment.getDentistName()
                            );


            boolean sameDate =
                    other.getAppointmentDate()
                            .equals(
                                    appointment.getAppointmentDate()
                            );


            boolean sameTime =
                    other.getAppointmentTime()
                            .equals(
                                    appointment.getAppointmentTime()
                            );


            boolean activeAppointment =
                    !isCancelled(
                            other.getStatus()
                    );


            if (
                    sameDentist &&
                            sameDate &&
                            sameTime &&
                            activeAppointment
            ) {

                return ResponseEntity
                        .status(HttpStatus.CONFLICT)
                        .body(
                                "This dentist already has an appointment at the selected date and time."
                        );
            }
        }


        // --------------------------------------
        // UPDATE APPOINTMENT
        // --------------------------------------

        existingAppointment.setPatientName(
                appointment.getPatientName()
        );


        existingAppointment.setAddress(
                appointment.getAddress()
        );


        existingAppointment.setContactNumber(
                appointment.getContactNumber()
        );


        existingAppointment.setDentistName(
                appointment.getDentistName()
        );


        existingAppointment.setTreatmentType(
                appointment.getTreatmentType()
        );


        existingAppointment.setAppointmentDate(
                appointment.getAppointmentDate()
        );


        existingAppointment.setAppointmentTime(
                appointment.getAppointmentTime()
        );


        existingAppointment.setStatus(
                appointment.getStatus()
        );


        Appointment updatedAppointment =
                appointmentRepository.save(
                        existingAppointment
                );


        return ResponseEntity.ok(
                updatedAppointment
        );
    }


    // ==========================================
    // DELETE APPOINTMENT
    // ==========================================

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAppointment(
            @PathVariable Long id) {

        if (
                !appointmentRepository.existsById(id)
        ) {

            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(
                            "Appointment not found."
                    );
        }


        appointmentRepository.deleteById(id);


        return ResponseEntity.ok(
                "Appointment deleted successfully."
        );
    }


    // ==========================================
    // HELPER METHOD - EMPTY CHECK
    // ==========================================

    private boolean isEmpty(String value) {

        return value == null ||
                value.trim().isEmpty();
    }


    // ==========================================
    // HELPER METHOD - CANCELLED CHECK
    // ==========================================

    private boolean isCancelled(String status) {

        return status != null &&
                status.equalsIgnoreCase(
                        "Cancelled"
                );
    }

}