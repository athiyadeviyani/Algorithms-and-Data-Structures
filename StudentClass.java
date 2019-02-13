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
		// CODE DONE
		int m = pattern.length();
		int pi[] = new int[m];
		for (int i : pi) {
			i = 0;
		}
		pi[0] = 0;
		int k = 0;

		for (int q = 1; q < m; q++) {
			while (k > 0 && (pattern.charAt(k) != pattern.charAt(q)) {
				k = pi[k - 1];
			} if (pattern.charAt(k) == pattern.charAt(q)) {
				k = k + 1;
			}
			pi[q] = k;
		}

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
			// CODE DONE
			int n = this.text.length();
			int m = this.pattern.length();
			int[] pi = computePrefixFunction(this.pattern);
			int q = 0;

			Queue Q = new Queue();

			for (int i = 0; i < n; i++) {

				while (q > 0 && (this.pattern.charAt(q) != this.text.charAt(i))) {
					q = pi[q - 1];
				}
				
				if (this.pattern.charAt(q) == this.text.charAt(i)) {
					q = q + 1;
				}

				if (q == m) {
					Q.enqueue(i-m);
					q = pi[q - 1];
				}
			}

			return Q;

		}
	}
}