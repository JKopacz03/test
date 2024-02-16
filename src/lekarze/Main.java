package lekarze;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) {
        start();

        System.out.println(Doctor.doctorWithMostVisits());
        System.out.println();
        System.out.println(Patient.patientWithMostVisits());
        System.out.println();
        System.out.println(Doctor.mostPopularSpecialization());
        System.out.println();
        System.out.println(Visit.yearWithMostVisits());
        System.out.println();
        System.out.println(Doctor.get5OldestDoctors());
        System.out.println();
        System.out.println(Doctor.top5DoctorsWithMostVisits());
        System.out.println();
        System.out.println(Patient.getPatientsWithAtleast5DifferentDoctors());
        System.out.println();
        System.out.println(Doctor.getDoctorsWithOnePatient());


    }

    public static void start() {
        Path pathOfLekarze = Path.of("lekarze.txt");
        try (Stream<String> lines = Files.lines(pathOfLekarze)) {
            lines.forEach(
                    s ->
                    {
                        String[] lekarzDane = s.split("\t");
                        new Doctor(
                                Integer.parseInt(lekarzDane[0]),
                                lekarzDane[1],
                                lekarzDane[2],
                                lekarzDane[3],
                                LocalDate.parse(lekarzDane[4]),
                                lekarzDane[5],
                                lekarzDane[6]
                        );
                    }
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Path pathOfPacjenci = Path.of("pacjenci.txt");
        try (Stream<String> lines = Files.lines(pathOfPacjenci)) {
            lines.forEach(
                    s ->
                    {
                        String[] pacjentDane = s.split("\t|\s");
                        new Patient(
                                Integer.parseInt(pacjentDane[0]),
                                pacjentDane[1],
                                pacjentDane[2],
                                pacjentDane[3],
                                getLocalDateFromTxt(pacjentDane[4])
                        );
                    }
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Path pathOfWizyty = Path.of("wizyty.txt");
        try (Stream<String> lines = Files.lines(pathOfWizyty)) {
            lines.forEach(
                    s ->
                    {
                        String[] wizyty = s.split("\t|\s");
                        new Visit(
                                wizyty[0],
                                wizyty[1],
                                getLocalDateFromTxt(wizyty[2])
                        );
                    }
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static LocalDate getLocalDateFromTxt(String date) {
        String[] strings = date.split("-");
        return LocalDate.of(Integer.parseInt(strings[0]),
                Integer.parseInt(strings[1]),
                Integer.parseInt(strings[2]));
    }
}
