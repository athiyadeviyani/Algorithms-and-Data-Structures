package matcher;

public class NaiveMatcher {
	
	private String text;
	private String pattern;
	private int textLen;
	private int patternLen;
	private Queue matchIndices;
	
	public NaiveMatcher(String text, String pattern) {
		this.text = text;
		this.pattern = pattern;
		this.textLen = text.length();
		this.patternLen = pattern.length();
		this.matchIndices = new Queue();
	}
	
	private boolean matchTextWithPattern(int startIndex) {
		for (int index = 0; index < patternLen; index++) {
			int offset = startIndex + index;
			if (offset >= textLen)
				return false;
			if (text.charAt(offset) != pattern.charAt(index))
				return false;
		}
		return true;
	}
	
	public void search() {
		if (patternLen > textLen)
			return;
		for (int startIndex = 0; startIndex < textLen; startIndex++) {
			if (matchTextWithPattern(startIndex))
				matchIndices.enqueue(startIndex);
		}
	}
	
	public Queue getMatchIndices() {
		return matchIndices;
	}
	
}