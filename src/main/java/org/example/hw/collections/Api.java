package org.example.hw.collections;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.hw.collections.model.User;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;


//На этапе старта программы считайте пользователей  и создайте
//коллекции которые будут использованы для вызовов методов
//выбирайте коллекции так чтобы время на вызов было минимальным.
//каждому методу ставьте комментарий со сложностью O(?)
public class Api {

    private static List<User> userListWithoutSorting;
    private static List<User> userSortedByNames;
    private static Map<LocalDate, List<User>> usersByBirthDate;
    private static TreeMap<LocalDate, List<User>> usersSortedByBirthDate;
    private static Map<Integer,List<User>> usersByTheYear;
    private static Map<Integer,List<User>> usersByTheMonth;
    private static Map<String,List<User>> allUsersCityWise;
    private static Map<String,List<User>>  allUsersCityWiseSorted;
    private static Set<User> usersWithoutDOBDuplicates;
    private static Set<User> usersWithoutEmailDuplicates;
    private static Map<Long, User> usersByUserId;
    private static TreeMap<Long, User> usersSortedByUserId;
    private static Map<String,List<User>> usersByCities;
    private static Map<String,List<User>> usersByNames;
    private static List<String> allAddressesSortedByNaturalReverseOrder;
    private static TreeSet<User> userSortedByNamesSet;
    private static Map<String, List<User>> usersBy3EmailChars;

    public static void main(String[] args) throws IOException,InterruptedException {
        userListWithoutSorting = new ArrayList<>();
        userSortedByNames = new ArrayList<>();
        usersByBirthDate = new HashMap<>();
        usersSortedByBirthDate = new TreeMap<>();
        usersByTheYear = new HashMap<>();
        usersByTheMonth = new HashMap<>();
        allUsersCityWise = new HashMap<>();
        usersWithoutDOBDuplicates = new TreeSet<>(Comparator.comparing(User::getBirthDate)); //Передаём компаратор
        usersWithoutEmailDuplicates = new TreeSet<>(Comparator.comparing(User::getEmail));
        usersByUserId = new HashMap<>();
        usersSortedByUserId = new TreeMap<>();
        usersByCities = new HashMap<>();
        usersByNames = new HashMap<>();
        allAddressesSortedByNaturalReverseOrder = new ArrayList<>();
        userSortedByNamesSet = new TreeSet<>(Comparator.comparing(User::getName).thenComparing(User::getId));
        usersBy3EmailChars = new HashMap<>();



        // данные для загрузки лежат тут
        //https://gist.githubusercontent.com/isicju/f40e58017d64533a2660cf04285f6cb9/raw/5fde264d0758aab1df4afe92f33bd83ad3d7b9ad/gistfile1.txt
        String url = "https://gist.githubusercontent.com/isicju/f40e58017d64533a2660cf04285f6cb9/raw/5fde264d0758aab1df4afe92f33bd83ad3d7b9ad/gistfile1.txt";
        String data;
        try (HttpClient client = HttpClient.newHttpClient()) { //создаем HTTP клиент
            HttpRequest request = HttpRequest.newBuilder(URI.create(url))
                    .timeout(Duration.ofSeconds(30))
                    .GET().build();
            HttpResponse<String> response = client.send(
                    request,
                    HttpResponse.BodyHandlers.ofString()); //Получаем массив строк от сервера
            if (response.statusCode() != 200) {
                throw new IOException(
                        "Не удалось загрузить пользователей. HTTP: "
                                + response.statusCode()
                );
            }

            data = response.body();
        }
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        userListWithoutSorting = mapper.readValue(
                data, new TypeReference<List<User>>() {
                });
        userSortedByNames = userListWithoutSorting.stream().sorted(Comparator.comparing(User::getName)).toList();

        for (User user : userListWithoutSorting) {
            usersByUserId.put(user.getId(), user);
        }

        usersWithoutDOBDuplicates.addAll(userListWithoutSorting);
        usersWithoutEmailDuplicates.addAll(userListWithoutSorting);
        userSortedByNamesSet.addAll(userListWithoutSorting);


        for (User user : userListWithoutSorting) {
            usersByBirthDate
                    .computeIfAbsent(user.getBirthDate(), date -> new ArrayList<>())
                    .add(user);
        }
        usersSortedByBirthDate = new TreeMap<>(usersByBirthDate); //Создаём тримапу на основе хэшмапы
        usersSortedByUserId = new TreeMap<>(usersByUserId);


        for (User user : userListWithoutSorting) {
            usersByTheYear
                    .computeIfAbsent(user.getBirthDate().getYear(), year -> new ArrayList<>())
                    .add(user);
        }

        for (User user : userListWithoutSorting) {
            usersByTheMonth
                    .computeIfAbsent(user.getBirthDate().getMonthValue(), year -> new ArrayList<>())
                    .add(user);
        }

        for (User user : userListWithoutSorting) {
            usersByCities
                    .computeIfAbsent(user.getCity(), city -> new ArrayList<>())
                    .add(user);
        }

        for (User user : userListWithoutSorting) {
            usersByNames
                    .computeIfAbsent(user.getName(), name -> new ArrayList<>())
                    .add(user);
        }



        allUsersCityWiseSorted = new TreeMap<>(usersByCities);

        for (User user : userListWithoutSorting) { //создаём мапу где ключами будут являтся комбинации трех символов в email а значениями список пользователей
            String email = user.getEmail();
            Set<String> seen = new HashSet<>(); //Set нужен для проверки, что такая комбинация уже есть и не повторяется

            for (int i = 0; i <= email.length() - 3; i++) { //Пробегаемся циклом по email
                String part = email.substring(i, i + 3); //Выделяем подстроку из 3 символов
                if (seen.add(part)) { //Если такой подсроки ещё не было для данного пользователя
                    usersBy3EmailChars
                            .computeIfAbsent(part, key -> new ArrayList<>())
                            .add(user); //Добавляем в список пользователя
                }
            }
        }

       allAddressesSortedByNaturalReverseOrder = userListWithoutSorting.stream().map(User::getAddress).sorted(Comparator.reverseOrder()).toList();

    }

    // O(1)
    public User getUserByIndexWithoutSorting(int position) {
        return userListWithoutSorting.get(position);
    }

    // O(1)
    public Collection<User> getUsersSortedByName() {
        return userSortedByNames;
    }

    // O(1)
    public Collection<User> getUsersByBirthDate(LocalDate birthDate) { //Используем Hashmap, так как нужно получить значение по ключу O(1)
        return usersByBirthDate.getOrDefault(birthDate, List.of());
    }

    // O(log n)
    public Collection<User> getAllUsersOlderThan(LocalDate birthDate) {    //Используем Treemap, так как ищем по диапазону значений
        List<User> result = new ArrayList<>();
        for (List<User> users : usersSortedByBirthDate.headMap(birthDate, false).values()){
            result.addAll(users);
        }
        return result;
    }

    // O(log n)
    public Collection<User> getAllUsersBetweenBirthDates(LocalDate birthDateFrom, LocalDate birthDateTo) {
        List<User> result = new ArrayList<>();
        for (List<User> users : usersSortedByBirthDate.subMap(birthDateFrom, true, birthDateTo, true).values()) {
            result.addAll(users);
        }
        return result;
    }

    // O(1)
    public User getUserSortedByIndex(int position) {
        return userSortedByNames.get(position);
    }

    // O(1)
    public Collection<User> getUsersByTheYear(int year) {
        return usersByTheYear.getOrDefault(year, List.of());
    }

    //month can be 0,1,2..,11
    // O(1)
    public Collection<User> getUsersByMonth(int month) {
        return usersByTheMonth.getOrDefault(month + 1, List.of());
    }

    //add to collection but replace users with the same date of birth (no date of birth duplicates)
    //replace existent if existed, don't use maps
    // O(log n)
    public User addUserWithoutMapNoDOBDuplicates(User user) {
        usersWithoutDOBDuplicates.remove(user);
        usersWithoutDOBDuplicates.add(user);
        return  user;
    }

    //add to collection but replace users with the same email (no email duplicates)
    // don't use maps
    // O(log n)
    public User addUserWithoutMapNoEmailDuplicates(User user) {
        usersWithoutEmailDuplicates.remove(user); // O(log n)
        usersWithoutEmailDuplicates.add(user); // O(log n)
        return  user;
    }

    //collection should be as small as possible
    //O(1)
    public User deleteUserById(long userId) {
        return usersByUserId.remove(userId);
    }
    //O(1)
    public User getUserById(long userId) {
        return usersByUserId.get(userId);
    }

    //must be sorted by userId
    // O(log n)
    public Collection<User> getSubCollectionId(long userIdFrom, long userIdTo) {
        return usersSortedByUserId.subMap(userIdFrom, userIdTo).values();
    }

    //O(1)
    public Collection<String> getAllCitiesWithoutDuplicates() {
       return usersByCities.keySet();
    }

    //O(1)
    public Collection<String> getAllNamesWithoutDuplicates() {
        return usersByNames.keySet();
    }

    //O(1)
    public Collection<User> getUsersByName(String name) {
        return usersByNames.getOrDefault(name, List.of());
    }
    //O(1)
    public Collection<String> getAllAddressesSortedByNaturalReverseOrder() {
        return allAddressesSortedByNaturalReverseOrder;
    }

    // поменяйте сигнатуру, нужно вернуть всех пользователей разбитых по городам
    //O(1)
    public Map <String, List<User>> getAllUsersCityWise() {
        return allUsersCityWise;
    }

    // поменяйте сигнатуру, нужно вернуть всех пользователей разбитых с сортировкой
    //O(1)
    public Map <String, List<User>>  getAllUsersByCityWiseSorted() {
        return allUsersCityWiseSorted;
    }

// Создайте трисет который будет возвращать всех пользователей с сортировкой по имени но
// при этом пользователи с одинаковым именем не должны быть искючены. при этом нужно помнить
// что все пользователи имеют уникальный userId и этим можно воспользоваться.
//O(1)
    public TreeSet<User> getSortedUsersByName() {
        return userSortedByNamesSet;
    }

    //Верните всех пользователей по 3 символам (идущим вместе) в емейле например vas должен вернуть vasya@gmail.com, 123vaskek@gmail.com
    //проще говорят должен отработать contains(3chars)
    //O(1)
    public Collection<User> getUsersBy3EmailChars(String threeMatchingChars) {
        return usersBy3EmailChars.getOrDefault(threeMatchingChars, List.of());
    }

//Сигнатура метода не идеальна, хотелось бы чтобы методы ниже (точнее их сигнатуры)
//   были более общны. В зависимости от содержания метода поменяйте сигнатуры чтобы они подходили не только под
// List интерфейс

    private void printAllUserNames(Iterable<User> usersToBePrinted) {     //посколько тут мы просто итерируемся, то достаточно интерфейса Iterable
        //Так как Collection extends Iterable тут может быть любая коллекция
        for (User user : usersToBePrinted) {
            System.out.println(user.getName());
        }
    }

    //Сложность зависит от коллекции, наихудшая для List O(n), лучшая HashSet O(1)
    private boolean existentUser(Collection<User> users, User toBeExistent) {
        return users.contains(toBeExistent);
    }

}

