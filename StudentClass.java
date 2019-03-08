package matcher;

public class StudentClass {

	public KMPMatcher kmpMatcher;

	public StudentClass(String text, String pattern) {
		kmpMatcher = new KMPMatcher(text, pattern);
	}

	public void buildPrefixFunction() {
		kmpMatcher.setPrefixFunction(computePrefixFunction(kmpMatcher.getPattern()));
	}
	
	public static int[] computePrefixFunction(String pattern) {
		// Initialise variables
		int m = pattern.length();
		int pi[] = new int[m];
		for (int i : pi) {
			i = 0;
		}

		// Length of prefix/suffix for the first character is always 0
		pi[0] = 0;
		int k = 0;

		// Iterate through every remaining characters in the pattern
		for (int q = 1; q < m; q++) {
			// Compute the prefix function
			while (k > 0 && (pattern.charAt(k) != pattern.charAt(q))) {
				k = pi[k - 1];
			} if (pattern.charAt(k) == pattern.charAt(q)) {
				k = k + 1;
			}

			// Store the result in the prefix array in the corresponding index
			pi[q] = k; 
		}

		// Returns the array pi, which is the completed prefix function for the pattern 
		// This will be used to perform the search() function
		return pi;
	}


	public static class KMPMatcher {

		private String text;
		private String pattern;
		private int textLen;
		private int patternLen;
		private int[] prefixFunction;
		private Queue matchIndices;

		public KMPMatcher(String text, String pattern) {
			this.text = text;
			this.pattern = pattern;
			this.textLen = text.length();
			this.patternLen = pattern.length();
			this.prefixFunction = new int[patternLen + 1];
			this.matchIndices = new Queue();
		}

		public void setPrefixFunction(int[] prefixFunction) {
			this.prefixFunction = prefixFunction;
		}

		public int[] getPrefixFunction() {
			return prefixFunction;
		}

		public String getPattern() {
			return pattern;
		}

		public Queue getMatchIndices() {
			return matchIndices;
		}

		public void search() {
			// Initialise the variables
			int n = textLen;
			int m = patternLen;
			int q = 0;

			// Get the prefix function of the pattern
			int[] pi = computePrefixFunction(pattern);
			

			// Iterate through every character of the text
			for (int i = 0; i < n; i++) {
				
				// Find out which character of the pattern do we start the comparison from, based on computePrefixFunction(pattern)
				while (q > 0 && (pattern.charAt(q) != text.charAt(i))) {
					q = pi[q - 1];
				}
				
				// Check if the current characters match
				if (pattern.charAt(q) == text.charAt(i)) {
					// If they match, increment the index q to move on to the next character to be compared
					q = q + 1;
				}
				
				// If q is equal to the length of the pattern, it means that the pattern is found
				if (q == m) {
					// Hence, we add the index at the text which the pattern was found to the queue
					matchIndices.enqueue(i-m+1);
					q = pi[q - 1];
				}
			}

		}
	}
}
