package lekarze;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

public class Doctor {
    private static List<Doctor> doctors = new ArrayList<>();
    private int doctorId;
    private String surname;
    private String name;
    private String specialization;
    private LocalDate birthday;
    private String nip;
    private String pesel;
    private List<Visit> visits;
    public Doctor(int doctorId,
                  String surname,
                  String name,
                  String specialization, 
                  LocalDate birthday,
                  String nip, 
                  String pesel) {
        this.doctorId = doctorId;
        this.surname = surname;
        this.name = name;
        this.specialization = specialization;
        this.birthday = birthday;
        this.nip = nip;
        this.pesel = pesel;
        this.visits = new ArrayList<>();
        validateDoctor(pesel);
    }

    private void validateDoctor(String pesel) {
        List<String> pesels = doctors.stream()
                .map(Doctor::getPesel)
                .toList();

        if(!pesels.contains(pesel)){
            doctors.add(this);
        }
    }

    public static Doctor findById(int id) {
        return doctors.stream().filter(l -> l.getDoctorId() == id)
                .findFirst()
                .get();
    }

    public static List<Doctor> get5OldestDoctors() {
        return doctors.stream()
                .limit(5)
                .sorted(Comparator.comparing(Doctor::getBirthday))
                .toList();
    }
    public static String mostPopularSpecialization() {
        return doctors.stream()
                .collect(groupingBy(Doctor::getSpecialization))
                .entrySet()
                .stream()
                .max(Comparator.comparing(e -> e.getValue().size()))
                .map(Map.Entry::getKey)
                .orElseThrow(IllegalArgumentException::new);
    }

    public static Doctor doctorWithMostVisits(){
        return doctors.stream()
                .max(Comparator.comparingInt(a -> a.getVisits().size()))
                        .orElseThrow(IllegalArgumentException::new);
    }

    public static List<Doctor> top5DoctorsWithMostVisits(){
        return doctors.stream()
                .sorted(Comparator.comparing(a -> a.getVisits().size(),
                        Comparator.reverseOrder()))
                .limit(5)
                .toList();
    }

    public static List<Doctor> getDoctorsWithOnePatient(){
        return doctors.stream()
                .filter(d -> getVisitsWithUniquePatient(d).size() == 1)
                .toList();
    }

    private static Set<Patient> getVisitsWithUniquePatient(Doctor d) {
        return d.getVisits()
                .stream()
                .map(Visit::getPatient)
                .collect(Collectors.toSet());
    }

    public void addVisit(Visit visit){
        visits.add(visit);
    }

    @Override
    public String toString() {
        return "Lekarz{" +
                ", idLekarza=" + doctorId +
                ", nazwisko='" + surname + '\'' +
                ", imie='" + name + '\'' +
                ", specjalnosc='" + specialization + '\'' +
                ", dataUrodzenia=" + birthday +
                ", nip='" + nip + '\'' +
                ", pesel='" + pesel + '\'' +
                '}';
    }

    public int getDoctorId() {
        return doctorId;
    }
    public String getSpecialization() {
        return specialization;
    }
    public LocalDate getBirthday() {
        return birthday;
    }

    public String getPesel() {
        return pesel;
    }

    public List<Visit> getVisits() {
        return visits;
    }
}
