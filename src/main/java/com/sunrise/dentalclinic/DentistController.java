package com.sunrise.dentalclinic;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/dentists")
public class DentistController {

    private final DentistRepository dentistRepository;

    public DentistController(DentistRepository dentistRepository) {
        this.dentistRepository = dentistRepository;
    }

    // Get all dentists
    @GetMapping
    public List<Dentist> getAllDentists() {
        return dentistRepository.findAll();
    }

    // Get dentist by ID
    @GetMapping("/{id}")
    public Dentist getDentist(@PathVariable Long id) {
        return dentistRepository.findById(id).orElse(null);
    }

    // Add dentist
    @PostMapping
    public Dentist addDentist(@RequestBody Dentist dentist) {
        return dentistRepository.save(dentist);
    }

    // Update dentist
    @PutMapping("/{id}")
    public Dentist updateDentist(
            @PathVariable Long id,
            @RequestBody Dentist dentist) {

        Dentist existingDentist =
                dentistRepository.findById(id).orElse(null);

        if (existingDentist == null) {
            return null;
        }

        existingDentist.setName(dentist.getName());
        existingDentist.setSpecialization(dentist.getSpecialization());
        existingDentist.setPhone(dentist.getPhone());
        existingDentist.setEmail(dentist.getEmail());

        return dentistRepository.save(existingDentist);
    }

    // Delete dentist
    @DeleteMapping("/{id}")
    public String deleteDentist(@PathVariable Long id) {

        if (!dentistRepository.existsById(id)) {
            return "Dentist not found";
        }

        dentistRepository.deleteById(id);

        return "Dentist deleted successfully";
    }
}