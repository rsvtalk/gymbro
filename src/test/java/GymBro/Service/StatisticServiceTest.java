package GymBro.Service;

import GymBro.Model.WorkoutExercise;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class StatisticServiceTest {

    StatisticService statisticService = new StatisticService(null);

    static WorkoutExercise createWE(int reps, double weight){
        WorkoutExercise we = new WorkoutExercise();
        we.setReps(reps);
        we.setWorkWeight(weight);
        return we;
    }

    @ParameterizedTest
    @MethodSource("provideListOfWEWithPercentChange")
    void testWeightChangeInPercent(List<WorkoutExercise> we, List<Double> percentsExpected){
        List<Double> result = statisticService.getWeightChangeInPercent(we);

        for (int i = 0; i < result.size(); i++){
            assertEquals(percentsExpected.get(i), result.get(i), 0.001);
        }

    }

    static Stream<Arguments> provideListOfWEWithPercentChange(){
        return Stream.of(
                Arguments.of(
                        List.of(
                                createWE(5,10),
                                createWE(5,20),
                                createWE(4,40)
                        ),
                        List.of(-50.0, -50.0, 0.0)
                ),
                Arguments.of(
                        List.of(
                                createWE(5,30),
                                createWE(6,20),
                                createWE(5,10)
                        ),
                        List.of(50.0, 100.0, 0.0)
                ),
                Arguments.of(
                        List.of(
                                createWE(5,50),
                                createWE(5,100),
                                createWE(5,50)
                        ),
                        List.of(-50.0,100.0, 0.0)
                ),
                Arguments.of(
                        List.of(),
                        List.of()
                )
        );
    }

    @ParameterizedTest
    @MethodSource("provideListOfWEWithRepsChange")
    void testRepsChange(List<WorkoutExercise> we, List<Integer> changeExpected){
        List<Integer> result = statisticService.getRepsChange(we);

        for (int i = 0; i < result.size(); i++){
            assertEquals(changeExpected.get(i), result.get(i));
        }
    }

    static Stream<Arguments> provideListOfWEWithRepsChange(){
        return Stream.of(
                Arguments.of(
                        List.of(
                                createWE(5,10),
                                createWE(4,20),
                                createWE(3,40)
                        ),
                        List.of(1, 1, 0)
                ),
                Arguments.of(
                        List.of(
                                createWE(3,30),
                                createWE(4,20),
                                createWE(5,10)
                        ),
                        List.of(-1, -1, 0)
                ),
                Arguments.of(
                        List.of(
                                createWE(5,50),
                                createWE(5,100),
                                createWE(5,50)
                        ),
                        List.of(0, 0, 0)
                ),
                Arguments.of(
                        List.of(),
                        List.of()
                )
        );
    }

    @ParameterizedTest
    @MethodSource("provideWeightChangeData")
    void testWeightChange(List<WorkoutExercise> workoutExercises,
                          List<WorkoutExercise> previousworkoutExercises,
                          List<Double> changeExpected){

        List<Double> result = statisticService.getListOfWeightChangeInPercent(workoutExercises,
                                                                              previousworkoutExercises);

        for (int i = 0; i < result.size(); i++){
            assertEquals(changeExpected.get(i), result.get(i), 0.01);
        }
    }



    static Stream<Arguments> provideWeightChangeData() {
        return Stream.of(
                Arguments.of(
                        List.of(createWE(5, 100.0),
                                createWE(5, 50.0),
                                createWE(5, 200.0)),
                        List.of(createWE(5, 80.0),
                                createWE(5, 40.0),
                                createWE(5, 150.0)),
                        List.of(25.0, 25.0, 33.3)
                ),
                Arguments.of(
                        List.of(createWE(5, 80.0),
                                createWE(5, 30.0),
                                createWE(5, 120.0)),
                        List.of(createWE(5, 100.0),
                                createWE(5, 50.0),
                                createWE(5, 200.0)),
                        List.of(-20.0, -40.0, -40.0)
                ),
                Arguments.of(
                        List.of(createWE(5, 100.0),
                                createWE(5, 50.0),
                                createWE(5, 100.0)),
                        List.of(createWE(5, 100.0),
                                createWE(5, 60.0),
                                createWE(5, 80.0)),
                        List.of(0.0, -16.7, 25.0)
                )
        );
    }

    @ParameterizedTest
    @MethodSource("provideRepsChangeData")
    void testRepsChange(List<WorkoutExercise> workoutExercises,
                          List<WorkoutExercise> previousworkoutExercises,
                          List<Integer> changeExpected) {
        List<Integer> result = statisticService.getListOfRepsChange(workoutExercises,
                                                                    previousworkoutExercises);
        for (int i = 0; i < result.size(); i++){
            assertEquals(changeExpected.get(i), result.get(i));
        }
    }

    static Stream<Arguments> provideRepsChangeData() {
        return Stream.of(
                Arguments.of(
                        List.of(createWE(5, 100.0),
                                createWE(4, 50.0),
                                createWE(3, 200.0)),
                        List.of(createWE(4, 80.0),
                                createWE(3, 40.0),
                                createWE(2, 150.0)),
                        List.of(1, 1, 1)
                ),
                Arguments.of(
                        List.of(createWE(5, 80.0),
                                createWE(4, 30.0),
                                createWE(3, 120.0)),
                        List.of(createWE(6, 100.0),
                                createWE(5, 50.0),
                                createWE(4, 200.0)),
                        List.of(-1, -1, -1)
                ),
                Arguments.of(
                        List.of(createWE(5, 100.0),
                                createWE(5, 50.0),
                                createWE(5, 100.0)),
                        List.of(createWE(5, 100.0),
                                createWE(5, 60.0),
                                createWE(5, 80.0)),
                        List.of(0, 0, 0)
                )
        );
    }



}
