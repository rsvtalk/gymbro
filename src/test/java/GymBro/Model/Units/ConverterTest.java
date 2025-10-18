package GymBro.Model.Units;

import GymBro.Model.Weight;
import GymBro.Model.WorkoutExercise;
import GymBro.Model.utils.Converter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ConverterTest {

    private final Converter converter = new Converter();

    @ParameterizedTest
    @CsvSource({
            "100.0, 45.4",
            "0.0, 0.0",
            "220.0, 99.8"
    })
    void testLbToKg(double lb, double kg) {
        double result = converter.lbToKg(lb);
        assertEquals(kg, result);
    }

    @ParameterizedTest
    @CsvSource({
            "45.4, 100.0",
            "0.0, 0.0",
            "99.8, 220.0"
    })
    void testKgToLb(double kg, double lb) {
        double result = converter.kgToLb(kg);
        assertEquals(lb, result, 0.1);
    }

    @ParameterizedTest
    @MethodSource("provideLbsToKgsLists")
    void testLbsToKgs(List<Double> weights, List<Boolean> isLbs, List<Double> expectedResult) {
        List<Double> result = converter.lbsToKgs(weights, isLbs);
        assertEquals(expectedResult, result);
    }

    static Stream<Arguments> provideLbsToKgsLists() {
        return Stream.of(
                Arguments.of(
                        List.of(100.0, 0.0, 220.0),
                        List.of(true, true, true),
                        List.of(45.4, 0.0, 99.8)
                ),

                Arguments.of(
                        List.of(100.0, 0.0, 220.0),
                        List.of(false, false, false),
                        List.of(100.0, 0.0, 220.0)
                ),

                Arguments.of(
                        List.of(100.0, 0.0, 220.0),
                        List.of(true, true, false),
                        List.of(45.4, 0.0, 220.0)
                )
        );
    }

    @ParameterizedTest
    @CsvSource({
            "100.0, 220.5",
            "0.0, 0.0",
            "50.0, 110.2"
    })
    void testWorkoutExerciseConverter(double kg, double lb) {
        WorkoutExercise we = new WorkoutExercise();
        we.setId(1);
        we.setReps(10);
        we.setWorkWeight(kg);

        WorkoutExercise converted = converter.convertWE(we);

        assertEquals(we.getId(), converted.getId());
        assertEquals(we.getReps(), converted.getReps());
        assertEquals(lb, converted.getWorkWeight(), 0.1);
    }

    @ParameterizedTest
    @MethodSource("provideKgsToLbsWELists")
    void testKgsToLbsWorkoutExerciseListConverter(List<WorkoutExercise> workoutExercises,
                                                  List<WorkoutExercise> expectedResult) {
        List<WorkoutExercise> convertedWorkoutExercises = converter.kgsToLbs(workoutExercises);
        for (int i = 0; i < convertedWorkoutExercises.size(); i++) {
            assertEquals(expectedResult.get(i).getId(), convertedWorkoutExercises.get(i).getId());
            assertEquals(expectedResult.get(i).getReps(), convertedWorkoutExercises.get(i).getReps());
            assertEquals(expectedResult.get(i).getWorkWeight(), convertedWorkoutExercises.get(i).getWorkWeight(), 0.1);
        }
    }

    private static WorkoutExercise createWE(double weight) {
        WorkoutExercise we = new WorkoutExercise();
        we.setWorkWeight(weight);
        return we;
    }

    static Stream<Arguments> provideKgsToLbsWELists() {
        return Stream.of(
                Arguments.of(
                        List.of(createWE(100.0),createWE(0.0),createWE(50.0)),
                        List.of(createWE(220.5),createWE(0.0),createWE(110.2))
                ),
                Arguments.of(
                        List.of(createWE(100.0)),
                        List.of(createWE(220.5))
                )
        );
    }

    @Test
    void testKgsToLbsPageWorkoutExercise() {
        List<WorkoutExercise> content = List.of(createWE(100.0),
                                                createWE(0.0),
                                                createWE(50.0));
        Pageable pageable = PageRequest.of(0, 10);
        Page<WorkoutExercise> page = new PageImpl<>(content, pageable, 100);

        Page<WorkoutExercise> convertedPage = converter.kgsToLbs(page);

        List<WorkoutExercise> convertedContent = convertedPage.getContent();
        assertEquals(220.5, convertedContent.get(0).getWorkWeight(), 0.1);
        assertEquals(0.0, convertedContent.get(1).getWorkWeight(), 0.1);
        assertEquals(110.2, convertedContent.get(2).getWorkWeight(), 0.1);

        assertEquals(page.getTotalElements(), convertedPage.getTotalElements());
        assertEquals(page.getPageable(), convertedPage.getPageable());
    }

    @ParameterizedTest
    @MethodSource("provideWeight")
    void testWeightConverter(Weight weightInKg, Weight expectingWeightInLb) {
        Weight weightInLb = converter.convertWeight(weightInKg);
        assertEquals(expectingWeightInLb.getId(), weightInLb.getId());
        assertEquals(expectingWeightInLb.getWeightDate(), weightInLb.getWeightDate());
        assertEquals(expectingWeightInLb.getPersonWeight(), weightInLb.getPersonWeight(), 0.1);
    }

    private static Weight createWeight(double kg){
        Weight w = new Weight();
        w.setId(1);
        w.setPersonWeight(kg);
        w.setWeightDate(Timestamp.valueOf("2025-01-01 12:00:00"));
        return w;
    }

    static Stream<Arguments> provideWeight() {
        return Stream.of(
                Arguments.of(createWeight(100.0), createWeight(220.5)),
                Arguments.of(createWeight(0.0), createWeight(0.0)),
                Arguments.of(createWeight(50.0), createWeight(110.2))
        );
    }

    @ParameterizedTest
    @MethodSource("provideWeights")
    void testKgsToLbsWeights(List<Weight> weightsInKg, List<Weight> expectingWeightsInLb) {

        List<Weight> weightsInLb = converter.kgsToLbsWeights(weightsInKg);

        for (int i = 0; i < weightsInLb.size(); i++) {
            assertEquals(expectingWeightsInLb.get(i).getId(), weightsInLb.get(i).getId());
            assertEquals(expectingWeightsInLb.get(i).getWeightDate(), weightsInLb.get(i).getWeightDate());
            assertEquals(expectingWeightsInLb.get(i).getPersonWeight(), weightsInLb.get(i).getPersonWeight());
        }

    }

    static Stream<Arguments> provideWeights() {
        return Stream.of(
                Arguments.of(
                        List.of(createWeight(100.0),
                                createWeight(0.0),
                                createWeight(50.0)),
                        List.of(createWeight(220.5),
                                createWeight(0.0),
                                createWeight(110.2))
                ),
                Arguments.of(
                        List.of(createWeight(30.0)),
                        List.of(createWeight(66.1))

                )
        );
    }

    @Test
    void testKgsToLbsPageWeights() {
        List<Weight> content = new ArrayList<>(List.of(createWeight(100.00),
                                                       createWeight(0.0),
                                                       createWeight(50.0)));

        List<Double> expected = new ArrayList<>(List.of(220.5, 0.0, 110.2));

        Pageable pageable = PageRequest.of(0, 10);
        Page<Weight> page = new PageImpl<>(content, pageable, 100);

        Page<Weight> convertedPage = converter.kgsToLbsWeights(page);
        List<Weight> convertedContent = convertedPage.getContent();

        for (int i = 0; i < convertedContent.size(); i++) {
            assertEquals(content.get(i).getId(), convertedContent.get(i).getId());
            assertEquals(content.get(i).getWeightDate(), convertedContent.get(i).getWeightDate());
            assertEquals(expected.get(i), convertedContent.get(i).getPersonWeight());
        }

        assertEquals(page.getTotalElements(), convertedPage.getTotalElements());
        assertEquals(page.getPageable(), convertedPage.getPageable());
    }

    @ParameterizedTest
    @CsvSource({
            "170, 5'7\"",
            "180, 5'11\"",
            "0, 0'0\"",
            "30, 1'0\"",
            "200, 6'7\"",
            "182, 6'0\"",
            "185, 6'1\""
    })
    void cmToFt_ConvertsCorrectly(int cm, String expected) {
        String result = Converter.cmToFt(cm);
        assertEquals(expected, result);
    }

}
