package com.sunrise.dentalclinic;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/patients")
public class PatientController {

    private final PatientRepository patientRepository;

    public PatientController(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    @GetMapping
    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    @GetMapping("/{id}")
    public Patient getPatient(@PathVariable Long id) {
        return patientRepository.findById(id).orElse(null);
    }

    @PostMapping
    public Patient addPatient(@RequestBody Patient patient) {
        return patientRepository.save(patient);
    }

    @PutMapping("/{id}")
    public Patient updatePatient(
            @PathVariable Long id,
            @RequestBody Patient patient) {

        Patient existingPatient =
                patientRepository.findById(id).orElse(null);

        if (existingPatient == null) {
            return null;
        }

        existingPatient.setName(patient.getName());
        existingPatient.setAddress(patient.getAddress());
        existingPatient.setEmail(patient.getEmail());
        existingPatient.setPhone(patient.getPhone());

        return patientRepository.save(existingPatient);
    }

    @DeleteMapping("/{id}")
    public String deletePatient(@PathVariable Long id) {

        if (!patientRepository.existsById(id)) {
            return "Patient not found";
        }

        patientRepository.deleteById(id);
        return "Patient deleted successfully";
    }
}