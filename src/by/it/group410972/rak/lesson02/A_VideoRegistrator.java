package by.it.group410972.rak.lesson02;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
/*
Даны события events
реализуйте метод calcStartTimes, так, чтобы число включений регистратора на
заданный период времени (1) было минимальным, а все события events
были зарегистрированы.
Алгоритм жадный. Для реализации обдумайте надежный шаг.
*/

public class A_VideoRegistrator {

    public static void main(String[] args) {
        A_VideoRegistrator instance = new A_VideoRegistrator();
        double[] events = new double[]{1, 1.1, 1.6, 2.2, 2.4, 2.7, 3.9, 8.1, 9.1, 5.5, 3.7};
        List<Double> starts = instance.calcStartTimes(events, 1); //рассчитаем моменты старта, с длинной сеанса 1
        System.out.println(starts);                            //покажем моменты старта
    }

    //модификаторы доступа опущены для возможности тестирования
    List<Double> calcStartTimes(double[] events, double workDuration) {
        //events - события которые нужно зарегистрировать
        //timeWorkDuration время работы видеокамеры после старта
        List<Double> result;
        result = new ArrayList<>();
        Arrays.sort(events);
        int i = 0;                              //i - это индекс события events[i]
        while (i < events.length) {
            double start = events[i];     // старт регистратора на текущем событии
            result.add(start);            // добавляем старт в результат
            double endTime = start + workDuration; // время окончания работы регистратора

            // пропускаем все события, которые попадают в этот интервал
            while (i < events.length && events[i] <= endTime) {
                i++;
            }
        }

        return result;                        //вернем итог
    }
}
