package GymBro.Service;

import GymBro.Model.Person;
import GymBro.Model.Weight;
import GymBro.Model.enums.Gender;
import GymBro.Repository.WeightRepo;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class WeightServiceTest {

    @Mock
    private WeightRepo weightRepo;

    @Mock
    private WorkoutService workoutService;

    @InjectMocks
    private WeightService weightService;

    static Weight createWeight(double weight) {
        Weight w = new Weight();
        w.setPersonWeight(weight);
        return w;
    }

    @ParameterizedTest
    @MethodSource("provideWeightList")
    public void testWeightChange(List<Weight> weights, List<Double> expectedWeightChanges) {

    }

    static Stream<Arguments> provideWeightList() {
        return Stream.of(
                Arguments.of(
                        List.of(createWeight(100.0),
                                createWeight(101.0),
                                createWeight(100.5)),
                        List.of(1.0, -0.5)),
                Arguments.of(
                        List.of(createWeight(100.0),
                                createWeight(99.0)),
                        List.of(-1.0)),
                Arguments.of(
                        List.of(createWeight(100.0),
                                createWeight(100.0),
                                createWeight(100.0)),
                        List.of(0.0, 0.0, 0.0)),
                Arguments.of(
                        List.of(createWeight(100.0)),
                        List.of(0.0)),
                Arguments.of(
                        List.of(),
                        List.of())
        );
    }

    @ParameterizedTest
    @MethodSource("provideCaloriesData")
    void countCalories(int height, int year,
                       Gender gender, double weight,
                       int workouts, List<Integer> expectedCalories) {
        Person person = new Person();
        person.setHeight(height);
        person.setYear(year);
        person.setGender(gender);

        when(weightRepo.findAllByPersonOrderByWeightDateDesc(person))
                .thenReturn(List.of(createWeight(weight)));
        when(workoutService.getCountOfWorkouts(person))
                .thenReturn(workouts);

        List<Integer> result = weightService.getCalories(person);

        assertEquals(expectedCalories, result);
    }


    static Stream<Arguments> provideCaloriesData() {
        return Stream.of(
                Arguments.of(180, 1994, Gender.MALE, 80.0, 5, List.of(2063, 2338, 2751, 3026, 3301)),
                Arguments.of(165, 1999, Gender.FEMALE, 60.0, 2, List.of(1381, 1565, 1842, 2026, 2210)),
                Arguments.of(175, 1989, Gender.MALE, 75.0, 0, List.of(1501, 1701, 2002, 2202, 2402)),
                Arguments.of(160, 1995, Gender.FEMALE, 55.0, 7, List.of(1602, 1816, 2137, 2350, 2564))
        );
    }



}
