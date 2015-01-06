package cn.edu.sjtu.dclab;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class DistanceGenerator {
	public void generate(int row) throws IOException {
		File file = new File("distance.dat");
		BufferedWriter bw = new BufferedWriter(new FileWriter(file));
		Random r = new Random();
		int[][] matrix = new int[row][row];
		for (int i = 0; i < row; i++) {
			String str = "";
			for (int j = 0; j < i; j++) {
				matrix[i][j] = matrix[j][i];
				str += matrix[i][j] + " ";
			}
			matrix[i][i] = 0;
			str += matrix[i][i] + " ";
			for (int j = i + 1; j < row; j++) {
				matrix[i][j] = r.nextInt(1000);
				str += matrix[i][j] + " ";
			}
			bw.write(str);
			if (i < row - 1)
				bw.newLine();
		}
		bw.flush();
		bw.close();
	}
	
	public static void main(String[] args) throws IOException {
		DistanceGenerator dg = new DistanceGenerator();
		dg.generate(10);
	}
}
