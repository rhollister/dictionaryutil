package rh.dictionary;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.Normalizer;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.text.WordUtils;

public final class DictionaryGenerator {
	/**
	 * List of words to load.
	 */
	private static final String DICTIONARY_FILE = "dictionary.txt";

	private final static String[] dictionary;
	private final static int wordBits = 17;

	static {
		dictionary = new String[(int) Math.pow(2, wordBits + 1)];
		loadDictionary();
	}

	/**
	 * Load dictionary from dictionary.txt.
	 */
	private static int loadDictionary() {
		// Initialize dictionary for wordBits bits of possibilities
		int c = 0;
		try ( // load the dictionary
		BufferedReader br = new BufferedReader(new FileReader(DICTIONARY_FILE))) {

			String strLine;
			while (c < dictionary.length && (strLine = br.readLine()) != null) {
				dictionary[c++] = strLine;
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		return c;
	}

	/**
	 * Load words from every file from /resources/dictionary_words and filter it
	 * by /resources/allObscene.txt, and output suitable words to
	 * src/com/novetta/era/rest/util/dictionary.txt
	 */
	private static void generateWordList() {
		HashSet<String> obsceneSet = new HashSet<>();
		FileInputStream fstream;
		TreeSet<String> wordSet = new TreeSet<>(new Comparator<String>() {
			@Override
			public int compare(String s1, String s2) {
				if (s1.length() == s2.length()) {
					return s1.compareTo(s2);
				}
				return s1.length() - s2.length();
			}
		});
		try {
			try {
				fstream = new FileInputStream(new File(
						"resources/allObscene.txt"));

				DataInputStream in = new DataInputStream(fstream);
				BufferedReader br = new BufferedReader(
						new InputStreamReader(in));
				Pattern pattern = Pattern
						.compile("\\p{InCombiningDiacriticalMarks}+");
				String strLine;
				// read in the obscene words
				while ((strLine = br.readLine()) != null) {
					// only read words longer than two characters
					if (strLine.length() > 2) {
						// replace any accented characters with their English
						// counterparts
						String nfdNormalizedString = Normalizer.normalize(
								strLine, Normalizer.Form.NFD);
						strLine = pattern.matcher(nfdNormalizedString)
								.replaceAll("");
						// remove any whitespace and add it to the obscene list
						if (obsceneSet.add(strLine.trim().toLowerCase()
								.replaceAll(" ", ""))) {
						}
					}
				}
				br.close();
				fstream.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}

			// list of patterns to exclude
			String avoidPatternStr = "sex|sht|aa|ae|yn|hn|dn|dm|vd|fm|kth|bj|ii|fs|jd|tj|sg|rz|sf|kj|kl|kh|mh|fh|oe|^ct|^dp|^ll|^py|sss|^bs|^gh|^ill|^my|^mz|^oo|^ox|cz|zc|j.?$|kk|f$";
			try {
				fstream = new FileInputStream(new File(
						"resources/rareConsonantClusters.txt"));

				DataInputStream in = new DataInputStream(fstream);
				BufferedReader br = new BufferedReader(
						new InputStreamReader(in));
				String strLine;
				while ((strLine = br.readLine()) != null) {
					avoidPatternStr += "|" + strLine;
				}
				br.close();
				fstream.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}

			File directory = new File("resources/dictionary_words");
			File[] files = directory.listFiles();
			// counters for statistics
			int allWordsCount = 0, possibleCount = 0, obsceneCount = 0, duplicateCount = 0, bigWordsCount = 0, avoidCount = 0;
			Pattern vowels = Pattern.compile("a|e|i|o|u");
			Pattern vowelsy = Pattern.compile("a|e|i|o|u|y");

			// list of letter combinations to avoid, these are usually foreign
			// or very archaic words
			Pattern avoid = Pattern.compile(avoidPatternStr);
			Pattern consonants = Pattern.compile("[qwrtpsdfghjklzxcvbnm]");
			Pattern manyConsonants = Pattern
					.compile("[qwrtpsdfghjklzxcvbnm][qwrtpsdfghjklzxcvbnm][qwrtpsdfghjklzxcvbnm][qwrtpsdfghjklzxcvbnm]");

			Pattern nonAlphanumeric = Pattern.compile("[^a-z0-9]");
			Pattern pattern = Pattern
					.compile("\\p{InCombiningDiacriticalMarks}+");

			System.out.println("Processing dictionary files...");

			// loop through all of the word lists in this directory
			for (File file : files) {
				try {
					System.out.println(" " + file.getName());
					fstream = new FileInputStream(file);
					DataInputStream in = new DataInputStream(fstream);
					BufferedReader br = new BufferedReader(
							new InputStreamReader(in));
					String strLine;
					// read all lines in this file
					while ((strLine = br.readLine()) != null) {
						allWordsCount++;
						// remove any apostrophes
						strLine = strLine.replaceAll("'", "");
						// ensure the word is a decent length
						if (strLine.length() < 11 && strLine.length() > 2) {
							// replace any accented characters with their
							// English counterparts
							String nfdNormalizedString = Normalizer.normalize(
									strLine, Normalizer.Form.NFD);
							strLine = pattern.matcher(nfdNormalizedString)
									.replaceAll("");

							// check to see if this word contains any non-ascii
							boolean ascii = true;
							for (int c = 0; c < strLine.length(); c++) {
								if (strLine.charAt(c) > 127) {
									ascii = false;
								}
							}
							// if this word is only ascii
							if (ascii) {
								strLine = strLine.trim().toLowerCase();

								// skip this word if it contains letters we're
								// trying to avoid
								if (!avoid.matcher(strLine).find()) {

									// remove any non-alphanumeric characters
									strLine = nonAlphanumeric.matcher(strLine)
											.replaceAll("");
									// make sure this word has vowels
									boolean hasVowels = true;
									if (strLine.length() > 3) {
										hasVowels = vowels.matcher(strLine)
												.find();
									} else {
										hasVowels = vowelsy.matcher(strLine)
												.find();
									}
									// make sure this word has consonants too
									if (hasVowels
											&& consonants.matcher(strLine)
													.find()
											&& !manyConsonants.matcher(strLine)
													.find()) {
										possibleCount++;
										if (wordSet.contains(strLine)) {
											duplicateCount++;
										} else {
											// make sure this word doesn't
											// contain any obscene words
											boolean obscene = false;
											for (String ob : obsceneSet) {
												if (strLine.contains(ob)) {
													obscene = true;
													break;
												}
											}
											if (!obscene
													&& !obsceneSet
															.contains(strLine)) {
												// finally, add this word
												wordSet.add(strLine);
											} else {
												obsceneCount++;
											}
										}
									}
								} else {
									avoidCount++;
								}
							}
						} else {
							bigWordsCount++;
						}
					}
					// Close the input stream
					in.close();
				} catch (Exception e) {// Catch exception if any
					System.err.println("Error: " + e.getMessage());
				}
			}
			System.out.println("Obscene words loaded: " + obsceneSet.size());
			System.out.println("All words read:       " + allWordsCount);
			System.out.println("Big words ignored:    " + bigWordsCount);
			System.out.println("Weird words ignored:  " + avoidCount);
			System.out.println("Possible words read:  " + possibleCount);
			System.out.println("Duplicates:           " + duplicateCount);
			System.out.println("Obscene words ignored:" + obsceneCount);
			System.out.println("Dictionary capacity:  " + dictionary.length);
			System.out.println("Final clean list:     " + wordSet.size());
			System.out.println("-----------------------------");
			if (dictionary.length > wordSet.size()) {
				System.out.println("   *** WARNING ***    ");
				System.out.println("Words needed:         "
						+ (dictionary.length - wordSet.size()));
			} else {
				System.out.println("Extra unused words:   "
						+ (wordSet.size() - dictionary.length));
			}
			
			try ( // write the dictionary
			BufferedWriter bw = new BufferedWriter(new FileWriter(
					DICTIONARY_FILE))) {
				for (String s : wordSet) {
					bw.write(WordUtils.capitalize(s));
					bw.write("\n");
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

		} catch (Exception e) {
			System.err.println("Error: " + e.getMessage());
		}
	}

	static class ValueComparator implements Comparator<String> {

		Map<String, Integer> base;

		public ValueComparator(Map<String, Integer> base) {
			this.base = base;
		}

		// Note: this comparator imposes orderings that are inconsistent with
		// equals.
		@Override
		public int compare(String a, String b) {
			if (base.get(a) >= base.get(b)) {
				return -1;
			} else {
				return 1;
			} // returning 0 would merge keys
		}
	}

	private static void generateRareConsonantClusters() {
		Pattern consonants = Pattern
				.compile("([qwrtpsdfghjklzxcvbnm][qwrtpsdfghjklzxcvbnm]+)");
		Pattern valid = Pattern.compile("nsb|ffn|sps|ldl|ftw");
		HashMap<String, String> sampleWords = new HashMap<>();
		HashMap<String, Integer> clusters = new HashMap<>();
		ValueComparator bvc = new ValueComparator(clusters);
		TreeMap<String, Integer> sorted_clusters = new TreeMap<String, Integer>(
				bvc);

		for (String word : dictionary) {
			Matcher m = consonants.matcher(word);
			while (m.find()) {
				String cluster = m.group();
				if (!valid.matcher(cluster).find()) {
					Integer count = clusters.get(cluster);
					if (count == null) {
						count = 0;
						sampleWords.put(cluster, "");
					}
					if (count < 20) {
						word += ", " + sampleWords.get(cluster);

						sampleWords.put(cluster, word);
					}
					clusters.put(cluster, ++count);
				}
			}
		}
		sorted_clusters.putAll(clusters);
		int count = 0;
		String str = "";

		System.out.println(str);
		try {
			FileWriter fwstream = new FileWriter(
					"resources/rareConsonantClusters.txt");
			BufferedWriter out = new BufferedWriter(fwstream);
			for (Map.Entry<String, Integer> c : sorted_clusters.entrySet()) {

				if (c.getValue() < 70) {
					count += c.getValue();
					out.write(c.getKey());
					out.write("\n");
					if (c.getValue() > 40) {
						System.out.println(String.format("%5s", c.getValue())
								+ String.format("%4s", c.getKey()) + " - "
								+ sampleWords.get(c.getKey()));
					}
				}
			}
			System.out.println("Total clusters to ignore: " + count);
			out.close();
		} catch (Exception e) {
			System.err.println("Error: " + e.getMessage());
		}
	}

	public final static void main(String[] args) {

		generateWordList();
		int reloaded = loadDictionary();
		System.out.println("Reloaded " + reloaded + " words of "
				+ dictionary.length + " capacity");

		// TODO: if dictionary.txt is already created, uncommenting below will
		// find rare constant clusters in it
		// generateRareConsonantClusters();

		// display sample words from the dictionary
		Random randomGenerator = new Random();

		System.out.println("Sample words: ");

		int numWordsAcross = 10;
		String str = "";

		// display words representative of the entire dictionary
		for (int d = 0; d < dictionary.length - 1; d += dictionary.length
				/ (numWordsAcross - 1)) {
			str += String.format("%11s", dictionary[d]);
		}
		System.out.println(str
				+ String.format("%11s", dictionary[dictionary.length - 1]));

		// display random words
		for (int c = 0; c < 10; c++) {
			str = "";
			for (int d = 0; d < numWordsAcross; d++) {
				str += String.format("%11s",
						dictionary[randomGenerator.nextInt(dictionary.length)]);
			}
			System.out.println(str);
		}
	}
}
