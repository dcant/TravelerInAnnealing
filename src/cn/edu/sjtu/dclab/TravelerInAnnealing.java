package cn.edu.sjtu.dclab;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;

public class TravelerInAnnealing {
	private int[] currentPath;
	private int[] minimalPath;
	private int[][] distances;
	private int cities;
	private String filePath;

	public TravelerInAnnealing(String filePath, int cities) {
		this.cities = cities;
		this.filePath = filePath;
	}

	private void init() throws IOException {
		currentPath = new int[cities];
		minimalPath = new int[cities];
		distances = new int[cities][cities];
		
		File file = new File(filePath);
		BufferedReader br = new BufferedReader(new FileReader(file));
		
		int row = 0;
		String bufLine = new String();
		while (row < cities && (bufLine = br.readLine()) != null) {
			String[] data = bufLine.split(" ");
			for (int i = 0; i < cities; i++) {
				distances[row][i] = Integer.valueOf(data[i]);
			}
			row++;
		}
		br.close();
		for (int i = 0; i < cities; i++) {
			currentPath[i] = i;
		}
	}

	private int totalDistance(int[] path) {
		int sum = 0;
		for (int i = 0; i < cities - 1; i++) {
			sum += distances[path[i]][path[i + 1]];
		}
		sum += distances[path[cities - 1]][0];
		return sum;
	}
	
	private int[] nextPath() {
		int[] next = Arrays.copyOf(currentPath, cities);
		Random r = new Random();
		int indx1 = r.nextInt(cities);
		int indx2 = r.nextInt(cities);
		while (indx1 == indx2) {
			indx1 = r.nextInt(cities);
			indx2 = r.nextInt(cities);
		}
		int tmp = next[indx1];
		next[indx1] = next[indx2];
		next[indx2] = tmp;
		return next;
	}
	
	public int annealing() throws IOException {
		init();
		double initialT = 100000;
		double finishT = 0.001;
		double delta;
		double rate = 0.999;
		int[] nextPath;
		Random r = new Random();
		int distance = totalDistance(currentPath);
		
		int count = 0;
		while (initialT > finishT) {
			nextPath = nextPath();
			delta = totalDistance(nextPath) - distance;
			
			if (delta < 0 || (delta > 0 && Math.exp(-delta/initialT) > r.nextDouble())) {
				currentPath = Arrays.copyOf(nextPath, nextPath.length);
				distance += delta;
			}
			initialT *= rate;
			System.out.println(count + ":" + Arrays.toString(currentPath) + distance);
			count++;
		}
		
		minimalPath = Arrays.copyOf(currentPath, currentPath.length);
		System.out.println(Arrays.toString(minimalPath));
		return distance;
	}
	
	public static void main(String[] args) throws IOException {
		TravelerInAnnealing ta = new TravelerInAnnealing("distance.dat", 10);
		int res = ta.annealing();
		System.out.println(res);
	}
}