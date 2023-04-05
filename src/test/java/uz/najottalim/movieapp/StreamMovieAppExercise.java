package uz.najottalim.movieapp;


import org.junit.jupiter.api.*;

import lombok.extern.slf4j.Slf4j;
import uz.najottalim.movieapp.models.Director;
import uz.najottalim.movieapp.models.Genre;
import uz.najottalim.movieapp.models.Movie;
import uz.najottalim.movieapp.repos.DirectorRepo;
import uz.najottalim.movieapp.repos.GenreRepo;
import uz.najottalim.movieapp.repos.MovieRepo;

import java.awt.event.MouseAdapter;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Slf4j
public class StreamMovieAppExercise {

    private DirectorRepo directorRepo;
    private GenreRepo genreRepo;
    private MovieRepo movieRepo;

    @BeforeEach
    void setUp() {
        directorRepo = new DirectorRepo();
        genreRepo = new GenreRepo();
        movieRepo = new MovieRepo();
    }

    @Test
    @DisplayName("Hammasini ko'rish")
    public void printAll() {
        // System.out.println o'rniga shuni ishlatingla
        log.info("Movies:");
        movieRepo.findAll()
                .forEach(p -> log.info(p.toString() + '\n'));
        log.info("Directors:");
        directorRepo.findAll()
                .forEach(p -> log.info(p.toString() + '\n'));
        log.info("Genres:");
        genreRepo.findAll()
                .forEach(p -> log.info(p.toString() + '\n'));
    }

    @Test
    @DisplayName("Janri 'Drama' yoki 'Komediya' bo'lgan kinolarni toping")
    public void exercise1() {
        List<Movie> movies = movieRepo.findAll()
                .stream()
                .filter(movie ->
                        movie.getGenres()
                                .stream()
                                .anyMatch(genre ->
                                        genre.getName().equalsIgnoreCase("Drama")
                                                || genre.getName().equalsIgnoreCase("Komediya")))
                .collect(Collectors.toList());
        movies.forEach(movie -> {
            System.out.println(movie.getTitle() +
                    " -> janrlari: " +
                    movie.getGenres()
                            .stream()
                            .map(Genre::getName)
                            .collect(Collectors.toList()));
        });
    }

    @Test
    @DisplayName("Har bitta rejissorning olgan kinolar sonini chiqaring")
    public void exercise2() {
         directorRepo.findAll()
                .stream()
                .collect(Collectors.toMap(Function.identity(),direcrtor -> direcrtor.getMovies().size(),(oldValue,newValue) -> oldValue+newValue));
    }

    @Test
    @DisplayName("Eng oldin olingan kinoni chiqaring")
    public void exercise8() {
        Optional<Movie> movie = movieRepo.findAll()
                .stream()
                .min((p1,p2)-> Integer.compare(p1.getYear(),p2.getYear()));
        movie.ifPresentOrElse(movie1 -> System.out.println(movie), () -> System.out.println("Movie bo'sh"));


    }

    @Test
    @DisplayName("2004 chi yilda kinolaga sarflangan umumiy summani chiqaring")
    public void exercise9() {
        Double spend = movieRepo.findAll()
                .stream()
                .filter(movie -> movie.getYear()==2004)
                .map(Movie::getSpending)
                  .reduce((double) 0,Double::sum);
        System.out.println(spend);
    }

    @Test
    @DisplayName("har bir yilda olingan kinolarni o'rtacha reytingini chiqaring")
    public void exercise10() {

         movieRepo.findAll()
                .stream()
                .collect(Collectors.groupingBy(
                        Movie::getYear,
                        Collectors.averagingDouble( Movie::getRating)));
    }

    @Test
    @DisplayName("Janri 'Drama' bo'lgan eng tarixda suratga olingan kinoni chiqaring")
    public void exercise11() {
        Optional<Movie> drama = movieRepo.findAll()
                .stream()
                .filter(movie -> movie.getGenres().equals("Drama"))
                .min(Comparator.comparing(Movie::getYear));
    }

    @Test
    @DisplayName("Qaysi janrdagi kinolar eng ko'p oscar olganligini chiqaring")
    public void exercise12() {
     var movies=   movieRepo.findAll()
                .stream()
                .filter(movie -> movie.getTitle().equalsIgnoreCase("oscar"))
                .map(movie -> movie.getGenres())
                .collect(Collectors.toList());

    }

    @Test
    @DisplayName("Har bir rejissorni olgan kinolarining janrini soni chiqaring"
//            "Misol uchun: " +
//            "Aziz Aliev suratga olgan" +
//            "Komediya    : 2\n" +
//            "Drama       : 5\n" +
//            "Romantika   : 2"
            )

    public void exercise3() {
//      Map <Director,Long> movi= directorRepo.findAll()
//               .stream()
//               .map(director -> director.getMovies().stream()
//                       .collect(Collectors.toMap()));

    }

    @Test
    @DisplayName("2004 chi yilda chiqqan kinolar orasida eng ko'p pul sarflanganini chiqaring")
    public void exercise4() {
            movieRepo.findAll()
                    .stream()
                    .filter(movie -> movie.getYear()==2004)
                    .max(Comparator.comparing(Movie::getSpending)).stream().collect(Collectors.toList());
    }

    @Test
    @DisplayName("har bitta rejissor olgan kinolarining o'rtacha ratingini chiqaring" +
            "Misol uchun:" +
            "Sardor Muhammadaliev: 2.23" +
            "Akrom Aliev: 2.33")
    public void exercise5() {
//        Map <Director,Double> map= directorRepo.findAll()
//                .stream()
//                .map(Director::getMovies)
//                .collect(Collectors.toMap(Director::getName,m -> m.getMovies();
    }

    @Test
    @DisplayName("Rejissolarni umumiy kinolari uchun olgan oskarlari soni bo'yicha saralab chiqaring")
    public void exercise6() {
        directorRepo.findAll()
                .stream()
                .map(director -> director.getOscarCount()).collect(Collectors.toList());
    }

    @Test
    @DisplayName("2004 yilda olingan Komediya kinolariga ketgan umumiy summani chiqaring")
    public void exercise7() {
        Double sum =movieRepo.findAll()
                .stream()
                .filter(movie -> movie.getYear()==2004)
                .peek(movie -> {
                    System.out.println(movie.getGenres());
                })
                .filter(mov -> {
                    return mov.getGenres().stream()
                            .anyMatch(genre -> genre.getName().equalsIgnoreCase("Komediya"));


                })
                .mapToDouble(Movie::getSpending).sum();
        System.out.println(sum);
    }


    @Test
    @DisplayName("Kinolarni chiqgan yili bo'yicha guruhlab, ratingi bo'yicha saralab toping")
    public void exercise13() {
        movieRepo.findAll()
                .stream()
                .sorted(Comparator.comparing(Movie::getRating))
                .collect(Collectors.groupingBy(Movie::getYear));
    }

    @Test
    @DisplayName("Har bitta rejiossor qaysi janrda o'rtacha reytingi eng ko'p ekanligini chiqaring")
    public void exercise14() {
           var mes = directorRepo.findAll()
                    .stream()
                    .collect(Collectors.toMap(Function.identity(),director -> {
                        var movie = director.getMovies();
                       var ganreAvaregeReyting= genreRepo.findAll()
                                .stream()
                                .collect(Collectors.toMap(Function.identity(),genre -> movie.stream()
                                        .filter(movie1 -> movie1.getGenres().equals(genre))
                                        .mapToDouble(Movie::getRating)
                                        .average()
                                        .orElse(-1)));
                             return   ganreAvaregeReyting.entrySet()
                                     .stream()
                                     .max(Comparator.comparingDouble(Map.Entry::getValue))
                                     .map(Map.Entry::getKey)
                                     .get();


                    }));
        System.out.println(mes);
    }

    @Test
    @DisplayName("Eng kam kinolarga pul sarflagan rejissorni chiqaring")
    public void exercise15() {
//        var sorted = directorRepo.findAll()
//                .stream()
//                .map(Director::getMovies)
//                .min(Comparator.comparingDouble(Movie));
//                .stream()
//                .map(Movie::getDirectors)
//                .map(directors -> directors.stream().map(Director::getMovies))
//                .map(mo);
    }

    @Test
    @DisplayName("Komediya kinolarini, 2000 chi yildan keyin olinganlarga ketgan narxni DoubleSummaryStatisticasini chiqaring")
    public void exercise16() {
                    movieRepo.findAll()
                            .stream()
                            .filter(movie -> movie.getGenres().equals("Komediya"))
                            .filter(mov -> mov.getYear()>2000 )
                            .mapToDouble(Movie::getSpending).summaryStatistics();
    }

    @Test
    @DisplayName("Qaysi kinoni eng ko'p rejissorlar birgalikda olishgan va ratingi eng baland chiqqan")
    public void exercise17() {
        var maxDir = movieRepo.findAll()
                .stream()
                .max(Comparator.comparingInt(o -> o.getDirectors().size()))
                .get().getDirectors().size();
        var ans = movieRepo.findAll()
                .stream()
                .filter(movie -> movie.getDirectors().size() == maxDir)
                .max(Comparator.comparingDouble(Movie::getRating));
        System.out.println(ans);
    }

    @Test
    @DisplayName("Har bir janrdagi kino nomlari umumiy nechta so'zdan iborat")
    public void exercise18() {
        movieRepo.findAll()
                .stream()
                .collect(Collectors.toMap((ganre -> ganre),
                        ganre -> movieRepo.findAll()
                                .stream()
                                .filter(movie -> movie.getGenres().contains(ganre))
                                .map(movie -> movie.getTitle())
                                .reduce("",(p,p1) -> p+ "" + p1).split(" ").length));
    }

    @Test
    @DisplayName("Har bir asrda olingan kinolarni o'rtacha reytingini chiqaring")
    public void exercise19() {
        Map<Integer, Double> collect = movieRepo.findAll()
                .stream()
                .collect(Collectors.groupingBy(movie -> movie.getYear() / 100)).entrySet()
                .stream()
                .collect(Collectors.toMap(
                        integerListEntry -> integerListEntry.getKey(),
                        integerListEntry -> integerListEntry.getValue().stream()
                                .mapToDouble(value -> value.getRating())
                                .average().getAsDouble()
                ));
        collect.forEach((integer, aDouble) -> System.out.println(integer+":"+aDouble));

    }

    @Test
    @DisplayName("Ismi A harfi bilan boshlanadigan rejissorlarni olgan kinolarini o'rtacha ratingi bo'yicha saralab chiqaring")
    public void exercise20() {
        var a = directorRepo.findAll()
                .stream()
                .collect(Collectors.toMap(Function.identity(),Director::getName)).entrySet()
                        .stream()
                                .filter(directorStringEntry -> directorStringEntry.getValue().startsWith("A"))
                                        .collect(Collectors.groupingBy(directorStringEntry -> directorStringEntry.getKey().getMovies()
                                                .stream()
                                                .collect(Collectors.groupingBy(Movie::getId,Collectors.averagingDouble(Movie::getRating)))));

        a.forEach((integer, Double) -> System.out.println(integer+":"+Double));
    }
}
