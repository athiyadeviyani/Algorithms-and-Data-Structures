package matcher;

import java.util.*;
import java.io.*;

import javax.swing.JFrame;

@SuppressWarnings("unused")
public class Matcher {
	
	private static Queue matchNaively(String text, String pattern) {
		NaiveMatcher matcher = new NaiveMatcher(text, pattern);
		matcher.search();
		return matcher.getMatchIndices();
	}
	
	private static long getNaiveMatchRuntime(String text, String pattern) {
		NaiveMatcher matcher = new NaiveMatcher(text, pattern);
		long start = System.nanoTime();
		matcher.search();
		long stop = System.nanoTime();
		return (stop - start) / 1000;
	}
	
	private static Queue matchKMP(String text, String pattern) {
		StudentClass studentClass = new StudentClass(text, pattern);
		studentClass.buildPrefixFunction();
		studentClass.kmpMatcher.search();
		return studentClass.kmpMatcher.getMatchIndices();
	}
	
	public static long getKMPMatchRuntime(String text, String pattern) {
		StudentClass studentClass = new StudentClass(text, pattern);
		long start = System.nanoTime();
		studentClass.buildPrefixFunction();
		// long start = System.nanoTime();
		studentClass.kmpMatcher.search();
		long stop = System.nanoTime();
		return (stop - start) / 1000;
	}
	
	private static Queue matchStudent(String text, String pattern) {
		StudentClass studentClass = new StudentClass(text, pattern);
		studentClass.buildPrefixFunction();
		studentClass.kmpMatcher.search();
		return studentClass.kmpMatcher.getMatchIndices();
	}
	
	private static long getStudentMatchRuntime(String text, String pattern) {
		StudentClass studentClass = new StudentClass(text, pattern);
		long start = System.nanoTime();
		studentClass.buildPrefixFunction();
		studentClass.kmpMatcher.search();
		long stop = System.nanoTime();
		return (stop - start) / 1000;
	}
	
	public static String prefixFunctionToString(int[] prefixFunction) {
		String result = "[";
		int contentSize = prefixFunction.length;
		for (int index = 0; index < contentSize; index++) {
			if (index != 0)
				result += ", ";
			result += prefixFunction[index];
		}
		result += "]";
		return result;
	}
	
	public static int[] buildPrefixFunctionNaively(String pattern) {
		NaiveKMPMatcher matcher = new NaiveKMPMatcher(pattern);
		matcher.buildPrefixFunction();
		return matcher.getPrefixFunction();
	}
	
	public static String buildPrefixFunction(String pattern) {
		StudentClass studentClass = new StudentClass("", pattern);
		studentClass.buildPrefixFunction();
		int[] prefixFunction = studentClass.kmpMatcher.getPrefixFunction();
		return prefixFunctionToString(prefixFunction);
	}
	
	private static Boolean checkPrefixFunction(int[] solution, int[] answer) {
		if (solution.length != answer.length)
			return false;
		for (int i = 0; i < solution.length; i++) {
			if (solution[i] != answer[i])
				return false;
		}
		return true;
	}
	
	private static Boolean checkMatches(Queue solution, Queue answer) {
		return solution.equals(answer);
	}
	
	public static Boolean testPrefixFunction(String pattern) {
		NaiveKMPMatcher matcher = new NaiveKMPMatcher(pattern);
		matcher.buildPrefixFunction();
		int[] solution = matcher.getPrefixFunction();
		StudentClass studentClass = new StudentClass("", pattern);
		studentClass.buildPrefixFunction();
		int[] answer = studentClass.kmpMatcher.getPrefixFunction();
		return checkPrefixFunction(solution, answer);
	}
	
	public static void testKMPMatcher(int t, int l) {
		Random randomizer = new Random();
		boolean isSuccessful = true;
		for (int i = 0; i < t; i++) {
			int exId = randomizer.nextInt(ExampleData.examples.length);
			String text = ExampleData.examples[exId];
			int sentlen = text.length();
			int begpos = randomizer.nextInt(sentlen - l);
			String pattern = text.substring(begpos, begpos + l);
			System.out.println("Pattern " + (exId + 1) + ": " + pattern);
			Queue solution = matchNaively(text, pattern);
			//Queue solution = matchKMP(text, pattern);
			System.out.println("Solution: " + solution.toString());
			Queue answer = matchStudent(text, pattern);
			System.out.println("Given answer: " + answer.toString());
			if (checkMatches(solution, answer)) {
				System.out.println("Correct answer");
				System.out.println();
			} else {
				System.out.println("--- INCORRECT ANSWER ---");
				System.out.println();
				System.out.println("Matcher test unsuccessful");
				isSuccessful = false;
				break;
			}
		}
		if (isSuccessful) {
			System.out.println("Matcher test successful");
		}
	}
	
	private static Vector<Vector<String>> genTestPatterns(String text, int noPatterns, int sizeExp) {
		String testStr = text;
		Vector<Vector<String>> result = new Vector<Vector<String>>();
		for (int i = 1; i < sizeExp; i++) {
			int size = (int) Math.pow(10, i);
			Vector<String> row = new Vector<String>();
			while (testStr.length() < size + noPatterns)
				testStr += text;
			for (int j = 0; j < noPatterns; j++)
				row.add(testStr.substring(j, j + size));
			result.add(row);
		}
		return result;
	}
	
	private static String genTestText(String text, int size) {
		String result = text;
		while (result.length() < size)
			result += text;
		return result.substring(0, size);
	}
	
	private static int findMaxSizeExp(int length) {
		int r = 0;
		int halfLen = (length + 1) / 2;
		while (Math.pow(10, r) <= halfLen)
			r++;
		return r;
	}
	
	private static float[] recordNaiveRuntimes(String searchText, Vector<Vector<String>> testPatterns) {
		float[] result = new float[testPatterns.size()];
		for (int i = 0; i < testPatterns.size(); i++) {
			Vector<String> row = testPatterns.get(i);
			long oldruntime = -1;
			int datasize = row.size();
			for (int j = 0; j < row.size(); j++) {
				long newruntime = getNaiveMatchRuntime(searchText, row.get(j));
				if (oldruntime < 0 || oldruntime < newruntime)
					oldruntime = newruntime;
			}
			result[i] = (float) oldruntime;
		}
		return result;
	}
	
	private static float[] recordKMPRuntimes(String searchText, Vector<Vector<String>> testPatterns) {
		float[] result = new float[testPatterns.size()];
		Vector<Float> test = new Vector<Float>();
		for (int i = 0; i < testPatterns.size(); i++) {
			Vector<String> row = testPatterns.get(i);
			long oldruntime = -1;
			int datasize = row.size();
			for (int j = 0; j < row.size(); j++) {
				// long newruntime = getStudentMatchRuntime(searchText, row.get(j));
				long newruntime = getKMPMatchRuntime(searchText, row.get(j));
				if (oldruntime < 0 || oldruntime < newruntime)
					oldruntime = newruntime;
			}
			result[i] = (float) oldruntime;
			test.add((float) oldruntime);
		}
		return result;
	}
	
	private static float recordNaiveRatio(String searchText, Vector<Vector<String>> testPatterns) {
		long oldruntime = -1;
		for (int i = 0; i < testPatterns.size(); i++) {
			Vector<String> row = testPatterns.get(i);
			int datasize = row.size();
			for (int j = 0; j < row.size(); j++) {
				long newruntime = getNaiveMatchRuntime(searchText, row.get(j));
				if (oldruntime < 0 || oldruntime < newruntime)
					oldruntime = newruntime;
			}
		}
		return (float) oldruntime / searchText.length();
	}
	
	private static float recordKMPRatio(String searchText, Vector<Vector<String>> testPatterns) {
		long oldruntime = -1;
		for (int i = 0; i < testPatterns.size(); i++) {
			Vector<String> row = testPatterns.get(i);
			int datasize = row.size();
			for (int j = 0; j < row.size(); j++) {
				long newruntime = getKMPMatchRuntime(searchText, row.get(j));
				if (oldruntime < 0 || oldruntime < newruntime)
					oldruntime = newruntime;
			}
		}
		return (float) oldruntime / searchText.length();
	}
	
	private static void writeRuntimeEntries(int textSize, Vector<Vector<String>> patterns, float[] naiveRuntimes, float[] kmpRuntimes, BufferedWriter output) throws IOException {
		for (int i = 0; i < patterns.size(); i++) {
			int patSize = patterns.get(i).get(0).length();
			output.write(
				new Formatter(new StringBuilder()).format(
					"%10d\t%10d\t%10.0f\t%10.0f\n", 
					patSize, textSize,
					naiveRuntimes[i], kmpRuntimes[i]
				).toString()
			);
			output.flush();
		}
	}
	
	private static void writeRatioEntry(int textSize, float kmpRatio, BufferedWriter output) throws IOException {
		output.write(
			new Formatter(new StringBuilder()).format(
				"%20d\t%10.6f\n", textSize, kmpRatio
			).toString()
		);
	}
	
	private static void writeRatioSummary(float[] ratios, int xOver, BufferedWriter output) throws IOException {
		int k = xOver/10000;
		Arrays.sort(ratios);
		output.write("Ignoring ratios before cross over point: ");
		output.write(
				new Formatter(new StringBuilder()).format(
					"%d\n", xOver
				).toString()
			);
		output.write("Sorted ratios are: ");
		float total = 0;
		for (int i = 0; i < ratios.length - k; i++) {
			if (i != 0) 
				output.write(", ");
			if (i % 5 == 0)
				output.newLine();
			output.write(new Formatter(new StringBuilder()).format(
					"%10.6f", 
					ratios[i]
				).toString());
			total = total + ratios[i];
		}
		output.newLine();
		output.write(new Formatter(new StringBuilder()).format(
				"Maximum ratio is: %10.6f\n",
				ratios[ratios.length - k - 1]
			).toString());
		output.write(new Formatter(new StringBuilder()).format(
				"Average ratio is: %10.6f\n", 
				(total / (ratios.length - k))
			).toString());
	}
	
	public static void getRuntimes(int p, int t, String f) {
		try {
			System.out.println(">> Now measuring the runtimes.");
			BufferedWriter ofhdl = new BufferedWriter(new FileWriter(f));
			String text = ExampleData.longDnaSeq3;
			String testText = "";
			String searchText = "";
			ofhdl.write("// PatSize\t  TextSize\t  Naive-RT\t    KMP-RT\n");
			for (int i = 1; i <= t; i++) {
				int size = 10000 * i;
				System.out.print("   >> Running with text of " + size + " characters ... ");
				System.out.flush();
				while (testText.length() <= size)
					testText += text;
				searchText = testText.substring(0, size);
				int r = findMaxSizeExp(searchText.length());
				Vector<Vector<String>> patterns = genTestPatterns(searchText, p, r);
				float[] naiveRuntimes = recordNaiveRuntimes(searchText, patterns);
				float[] kmpRuntimes = recordKMPRuntimes(searchText, patterns);
				writeRuntimeEntries(searchText.length(), patterns, naiveRuntimes, kmpRuntimes, ofhdl);
				System.out.println("done");
				System.out.flush();
			}
			ofhdl.close();
			System.out.println(">> Runtime measurement done.\n");
			System.out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void getRatios(int p, int t, int xOver, String fRatios) {
		try {
			System.out.println(">> Now measuring the ratios.");
			BufferedWriter ofhdl = new BufferedWriter(new FileWriter(fRatios));
			String text = ExampleData.longDnaSeq3;
			String testText = "";
			String searchText = "";
			ofhdl.write("//            PatSize\t     Ratio\n");
			float[] ratios = new float[t];
			for (int i = 1; i <= t; i++) {
				int size = 10000 * i;
				System.out.print("   >> Running with text of " + size + " characters ... ");
				System.out.flush();
				while (testText.length() <= size)
					testText += text;
				searchText = testText.substring(0, size);
				int sizeExp = findMaxSizeExp(searchText.length());
				Vector<Vector<String>> patterns = genTestPatterns(searchText, p, sizeExp);
				float kmpRatio = recordKMPRatio(searchText, patterns);
				writeRatioEntry(searchText.length(), kmpRatio, ofhdl);
				ratios[i - 1] = kmpRatio;
				System.out.println("done");
				System.out.flush();
			}
			writeRatioSummary(ratios, xOver, ofhdl);
			ofhdl.close();
			System.out.println(">> Ratio measurement done.\n");
			System.out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void plotRuntimes(double c, double a, String f) {
		try {
			BufferedReader ifhdl = new BufferedReader(new FileReader(f));
			Hashtable<Integer, Integer> kmpTbl = new Hashtable<Integer, Integer>();
			Hashtable<Integer, Integer> naiveTbl = new Hashtable<Integer, Integer>();
			try {
				while (true) {
					String line = ifhdl.readLine();
					if (line == null) break;
					line = line.trim();
					if (line.startsWith("//")) continue;
					String[] tokens = line.split("[ \t]+");
					if (tokens.length == 0) continue;
					if (tokens.length != 4) throw new IllegalArgumentException();
					int patSize = Integer.parseInt(tokens[0]);
					int textSize = Integer.parseInt(tokens[1]);
					int naiveRT = Integer.parseInt(tokens[2]);
					int kmpRT = Integer.parseInt(tokens[3]);
					if (!kmpTbl.containsKey(textSize)){
						kmpTbl.put(textSize, Integer.MAX_VALUE);
						naiveTbl.put(textSize, Integer.MAX_VALUE);
					}
					if (kmpTbl.get(textSize) > kmpRT) {
						kmpTbl.remove(textSize);
						kmpTbl.put(textSize, kmpRT);
						naiveTbl.remove(textSize);
						naiveTbl.put(textSize, naiveRT);
					}
				}
			} catch (IOException e) {
			}
			double[][] data = new double[kmpTbl.size()][3];
			Integer[] keys = new Integer[kmpTbl.size()];
			keys = kmpTbl.keySet().toArray(keys);
			Arrays.sort(keys);
			int textSize = 0;
			int kmpRT = 0;
			int naiveRT = 0;
			for (int i = 0; i < keys.length; i++) {
				textSize = keys[i];
				kmpRT   = kmpTbl.get(textSize);
				naiveRT = naiveTbl.get(textSize);
				data[i][0] = textSize;
				data[i][1] = ((double) kmpRT) / 1000;
				data[i][2] = ((double) naiveRT) / 1000;
			}
			JFrame frame = new JFrame();
		    frame.setTitle("KMP Runtime Plot");
		    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		    GraphingData gd = new GraphingData(1.0, data, 1, 10, c, a, "plot.jpg");
		    frame.add(gd);
		    frame.setSize(800,800);
		    frame.setLocation(200,200);
		    frame.setVisible(true);
		    gd.save();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			System.err.println(">> Illegal file format: Graph plotter has terminated unexpectedly.");
		}
	}
	
}
