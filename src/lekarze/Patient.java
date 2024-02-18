package lekarze;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class Patient {
    private static List<Patient> patients = new ArrayList<>();
    private int patientId;
    private String surname;
    private String name;
    private String pesel;
    private LocalDate birthday;
    private List<Visit> visits;

    public Patient(int patientId,
                   String surname,
                   String name,
                   String pesel,
                   LocalDate birthday) {
        this.patientId = patientId;
        this.surname = surname;
        this.name = name;
        this.pesel = pesel;
        this.birthday = birthday;
        this.visits = new ArrayList<>();
        validatePatient(pesel);
    }

    private void validatePatient(String pesel) {
        List<String> pesels = patients.stream()
                .map(Patient::getPesel)
                .toList();

        if(!pesels.contains(pesel)){
            patients.add(this);
        }
    }

    public static Patient findById(int id){
        return patients.stream().filter(p -> p.getPatientId() == id)
                .findFirst()
                .get();
    }

    public static Patient patientWithMostVisits(){
        return patients.stream()
                .max(Comparator.comparingInt(a -> a.getVisits().size()))
                .orElseThrow(IllegalArgumentException::new);

    }

    public static List<Patient> getPatientsWithAtleast5DifferentDoctors(){
        return patients.stream()
                .filter(a -> getVisitsWithUniqueDoctors(a).size() >= 5)
                .toList();
    }

    private static Set<Doctor> getVisitsWithUniqueDoctors(Patient a) {
        return a.getVisits()
                .stream()
                .map(Visit::getDoctor)
                .collect(Collectors.toSet());
    }

    public void addVisit(Visit visit){
        visits.add(visit);
    }

    public List<Visit> getVisits() {
        return visits;
    }

    public int getPatientId() {
        return patientId;
    }

    public String getPesel() {
        return pesel;
    }


    @Override
    public String toString() {
        return "Pacjent{" +
                "idPacjenta=" + patientId +
                ", nazwisko='" + surname + '\'' +
                ", imie='" + name + '\'' +
                ", pesel='" + pesel + '\'' +
                ", dataUrodzenia=" + birthday +
                '}';
    }
}
