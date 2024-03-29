package lekarze;

import java.time.LocalDate;
import java.util.*;

import static java.util.stream.Collectors.groupingBy;

public class Visit {
    private static List<Visit> wizyty =  new ArrayList<>();
    private Doctor doctor;
    private Patient patient;
    private LocalDate visitDate;

    public Visit(String doctorId, String patientId, LocalDate visitDate) {
        Doctor doctor = Doctor.findById(Integer.parseInt(doctorId));
        Patient patient = Patient.findById(Integer.parseInt(patientId));

        if (doctor != null && patient != null) {

            this.doctor = doctor;
            this.patient = patient;
            this.visitDate = visitDate;
            wizyty.add(this);
            doctor.addVisit(this);
            patient.addVisit(this);
        }
    }

    public static int yearWithMostVisits(){
        return wizyty.stream()
                .collect(groupingBy(w -> w.getVisitDate().getYear()))
                .entrySet()
                .stream()
                .max(Comparator.comparing(e -> e.getValue().size()))
                .map(Map.Entry::getKey)
                .get();
    }

    @Override
    public String toString() {
        return "Wizyta{" +
                "lekarza=" + doctor +
                ", pacjenta=" + patient +
                ", dataWizyty=" + visitDate +
                '}';
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public Patient getPatient() {
        return patient;
    }

    public LocalDate getVisitDate() {
        return visitDate;
    }
}
