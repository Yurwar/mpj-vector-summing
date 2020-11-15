package com.yurwar;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.RecursiveTask;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class VectorAddingTask extends RecursiveTask<List<Integer>> {
    private static final int THRESHOLD = 20;
    private final int[] v1;
    private final int[] v2;

    public VectorAddingTask(int[] v1, int[] v2) {
        this.v1 = v1;
        this.v2 = v2;
    }

    @Override
    protected List<Integer> compute() {
        if (v1.length > THRESHOLD) {
            int[] firstV1 = Arrays.copyOfRange(v1, 0, v1.length / 2);
            int[] secondV1 = Arrays.copyOfRange(v1, v1.length / 2, v1.length);

            int[] firstV2 = Arrays.copyOfRange(v2, 0, v2.length / 2);
            int[] secondV2 = Arrays.copyOfRange(v2, v2.length / 2, v2.length);

            VectorAddingTask firstTask = new VectorAddingTask(firstV1, firstV2);
            VectorAddingTask secondTask = new VectorAddingTask(secondV1, secondV2);

            List<Integer> result = firstTask.invoke();
            result.addAll(secondTask.invoke());
            return result;
        } else {
            return sumVectors(v1, v2);
        }
    }

    private List<Integer> sumVectors(int[] v1, int[] v2) {
        return IntStream.range(0, v1.length)
                .mapToObj(index -> v1[index] + v2[index])
                .collect(Collectors.toList());
    }


}
