package rh.dictionary;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public final class DictionaryMask {
	/**
	 * List of words to load.
	 */
	private static final String DICTIONARY_FILE = "dictionary.txt";

	private final static String[] dictionary;

	// the number of bits per dictionary word. this is mostly limited by the
	// number of words in our dictionary: 2^(wordBits+1)
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
		InputStream stream = DictionaryMask.class
				.getResourceAsStream(DICTIONARY_FILE);
				DataInputStream in = new DataInputStream(stream);
				InputStreamReader isr = new InputStreamReader(in);
				BufferedReader br = new BufferedReader(isr)) {

			String strLine;
			while (c < dictionary.length && (strLine = br.readLine()) != null) {
				dictionary[c++] = strLine;
			}

			br.close();
			stream.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

		return c;
	}

	/**
	 * pull an arbitrary amount of bits out of a byte array
	 * 
	 * @param srcBytes
	 * @param srcBitOffset
	 * @param srcBitLength
	 * @param dstByteLength
	 * @return
	 */
	private static byte[] getBits(byte[] srcBytes, int srcBitOffset,
			int srcBitLength, int dstByteLength) {
		byte[] returnBytes = new byte[dstByteLength--];
		int dstIndex = 0;
		for (int srcIndex = srcBitOffset; srcIndex < srcBitOffset
				+ srcBitLength; srcIndex++) {
			int bit = srcBytes[srcIndex / 8] >> (srcIndex % 8) & 1;
			returnBytes[dstByteLength] |= bit << dstIndex++;
			if (dstIndex % 8 == 0) {
				dstIndex = 0;
				dstByteLength--;
			}
		}
		return returnBytes;
	}

	/**
	 * @param digest
	 * @return
	 */
	public static String getDictionaryMask(byte[] digest) {
		int offset = 0;
		StringBuilder mask = new StringBuilder();

		// convert 2 sets of bits to dictionary words
		for (int c = 0; c < 2; c++) {
			// get the set of bits for the word
			ByteBuffer buffer = ByteBuffer.wrap(getBits(digest, offset,
					wordBits, 4));
			// get the unsigned version of these bits
			int num = buffer.getInt() & 0xffffffff;
			if (num >= dictionary.length) {
				num = dictionary.length - 1;
			}

			// get the dictionary word at that num index
			mask.append(dictionary[(int) num]);
			mask.append(' ');

			// increment the offset
			offset += wordBits;
		}

		// add a three-letter code at the end for an extra 15 bits of uniqueness
		for (int c = 0; c < 3; c++) {
			// get the bits for a digit of the three-letter code
			ByteBuffer buffer = ByteBuffer.wrap(getBits(digest, offset, 5, 1));
			// convert the bits to base36 (alphanumeric) and replace
			// easily-mistaken characters with more distinguishable characters
			mask.append(Integer.toString(buffer.get() & 0xff, 36).toUpperCase()
					.replaceAll("I", "Z").replaceAll("O", "Y")
					.replaceAll("8", "X").replaceAll("0", "V")
					.replaceAll("1", "W"));
			offset += 5;
		}

		return mask.toString();
	}

	public final static void main(String[] args) {
		try {
			// list of hash algorithms to test on
			String hashalgs[] = { "MD5", "SHA-256" };
			Random randomGenerator = new Random();

			for (String hashalg : hashalgs) {
				MessageDigest digest = MessageDigest.getInstance(hashalg);
				System.out.println(hashalg + " hashes:");
				byte[] hashdigest;

				// hash preset words to compare with previous results
				hashdigest = digest.digest("".getBytes("UTF-8"));
				System.out.println(new BigInteger(1, hashdigest).toString(16)
						+ " -> " + getDictionaryMask(hashdigest));
				hashdigest = digest.digest("Apple".getBytes("UTF-8"));
				System.out.println(new BigInteger(1, hashdigest).toString(16)
						+ " -> " + getDictionaryMask(hashdigest));
				hashdigest = digest.digest("Zoology".getBytes("UTF-8"));
				System.out.println(new BigInteger(1, hashdigest).toString(16)
						+ " -> " + getDictionaryMask(hashdigest));

				for (int c = 0; c < 7; c++) {
					// choose a random word from the dictionary to hash
					int index = randomGenerator.nextInt(dictionary.length);
					hashdigest = digest.digest(dictionary[index]
							.getBytes("UTF-8"));

					System.out.println(new BigInteger(1, hashdigest)
							.toString(16)
							+ " -> "
							+ getDictionaryMask(hashdigest));
				}
				System.out.println();
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}
}
