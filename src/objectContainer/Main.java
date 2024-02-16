package objectContainer;

import java.util.LinkedList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        ObjectContainer<Person> peopleFromWarsaw = new ObjectContainer<>(p -> p.getCity().equals("Warsaw"));//tu w konstruktorze podajemy warunek do dodawania osoby do kontenera.
        peopleFromWarsaw.add(new Person("Jan", "Warsaw", 50)); // powinno byc ok, Jan jest z warszawy
        peopleFromWarsaw.add(new Person("Weronika","Warsaw", 50)); // powinno byc tez okej, Weronika jest z warszawy
        peopleFromWarsaw.add(new Person("Weronika","Warsaw", 20)); // powinno byc tez okej, Weronika jest z warszawy
        peopleFromWarsaw.add(new Person("Weronika","Warsaw", 20)); // powinno byc tez okej, Weronika jest z warszawy
        peopleFromWarsaw.add(new Person("Weronika","Warsaw", 30)); // powinno byc tez okej, Weronika jest z warszawy
//        peopleFromWarsaw.add(new Person("Waldek", "Monaco", 34));// waldka niedoda, jest z Monaco, wiec tu powinno albo rzucic wyjatkiem, albo metoda add powinna zwrocic po prostu false - co wybierzesz to twoja decyzja.

//do tego ponizej mozesz juz stosowac liste, no bo masz zwrocic liste:)
        List<Person> females = peopleFromWarsaw.getWithFilter(p -> p.getName().endsWith("a")); // zwraca nam wszystkie osoby spelniajace dany warunek.
        System.out.println(females);

        peopleFromWarsaw.removeIf(p -> p.getAge() > 40); // powinno nam usuwac ludzi spelniajacy dany warunek w nawiasie.

        //tu naturalnie mozesz stosowac zapis do pliku bo wymaga tego polecenie
        peopleFromWarsaw.storeToFile("youngPeopleFromWarsaw.txt", p -> p.getAge() < 30, p -> p.getName()+";"+p.getAge()+";"+p.getCity());
//metoda powinna zapisac obiekty do pliku z pierwszego argumenty metody, tylko te co spelniaja drugi warunek metody, w formacie z trzeciego warunku metody

//        Dobrze by było mieć warstwe persystencji, zrobmy np:
        peopleFromWarsaw.storeToFile("warsawPeople.txt"); // powinno nam zapisac dane trwale do pliku.

//i np:
        ObjectContainer<Person> peopleFromWarsawFromFile = ObjectContainer.fromFile("warsawPeople.txt");
//powinno wczytac caly obiekt tak jak zostal zapisay w metodzie storeToFile, caly stan obiektu powinien byc odwzrowowany
    }
}
