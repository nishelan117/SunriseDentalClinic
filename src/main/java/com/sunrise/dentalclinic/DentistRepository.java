package com.sunrise.dentalclinic;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DentistRepository extends JpaRepository<Dentist, Long> {
}