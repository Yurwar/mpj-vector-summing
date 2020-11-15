package com.yurwar;

import com.yurwar.utils.ArrayGenerator;
import mpi.*;

import java.util.Arrays;
import java.util.stream.IntStream;

public class VectorAdder {
    private static final int VECTOR_SIZE = 50;
    private static final int ROOT_RANK = 0;
    private static final String ARRAY_PRINTING_FORMAT = "%s: %s%n";
    private final ArrayGenerator generator = new ArrayGenerator();

    public void execute(String... args) {
        MPI.Init(args);
        int me = MPI.COMM_WORLD.Rank();
        int size = MPI.COMM_WORLD.Size();
        int unitSize = VECTOR_SIZE / size;


        int[] v1 = new int[VECTOR_SIZE];
        int[] v2 = new int[VECTOR_SIZE];
        int[] res = new int[VECTOR_SIZE];
        if (me == ROOT_RANK) {
            v1 = generator.getIntArray(VECTOR_SIZE);
            v2 = generator.getIntArray(VECTOR_SIZE);

            printArray(v1, "First vector");
            printArray(v2, "Second vector");
        }

        int[] subV1 = new int[unitSize];
        int[] subV2 = new int[unitSize];

        MPI.COMM_WORLD.Scatter(v1, 0, unitSize, MPI.INT, subV1, 0, unitSize, MPI.INT, ROOT_RANK);
        MPI.COMM_WORLD.Scatter(v2, 0, unitSize, MPI.INT, subV2, 0, unitSize, MPI.INT, ROOT_RANK);

        int[] subRes = sumVectors(subV1, subV2);

        MPI.COMM_WORLD.Gather(subRes, 0, unitSize, MPI.INT, res, 0, unitSize, MPI.INT, ROOT_RANK);

        if (me == ROOT_RANK) {
            printArray(res, "Result vector");
        }

        MPI.Finalize();
    }

    private int[] sumVectors(int[] v1, int[] v2) {
        return IntStream.range(0, v1.length)
                .map(index -> v1[index] + v2[index])
                .toArray();
    }

    private void printArray(int[] array, String name) {
        System.out.printf(ARRAY_PRINTING_FORMAT, name, Arrays.toString(array));
    }
}
