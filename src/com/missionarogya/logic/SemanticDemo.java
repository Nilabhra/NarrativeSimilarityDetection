package com.missionarogya.logic;

import edu.cmu.lti.lexical_db.ILexicalDatabase;
import edu.cmu.lti.lexical_db.NictWordNet;
import edu.cmu.lti.ws4j.impl.Lin;
import edu.cmu.lti.ws4j.util.PorterStemmer;
import edu.cmu.lti.ws4j.util.WS4JConfiguration;

public class SemanticDemo {

	private static ILexicalDatabase db = new NictWordNet();
	private static PorterStemmer ps = new PorterStemmer();

	/*
	 * //available options of metrics private static RelatednessCalculator[] rcs
	 * = { new HirstStOnge(db), new LeacockChodorow(db), new Lesk(db), new
	 * WuPalmer(db), new Resnik(db), new JiangConrath(db), new Lin(db), new
	 * Path(db) };
	 */
	private static String[] filter(String s) {
		// removing all punctuations and converting all uppercase to lowercase
		return ps
				.stemSentence(s.replaceAll("[^a-zA-Z0-9. ]", "").toLowerCase())
				.split("\\s+");
	}

	public static double computeSimilarity(String narrative1, String narrative2) {
		String[] words1 = filter(narrative1);
		String[] words2 = filter(narrative2);
		WS4JConfiguration.getInstance().setMFS(true);
		// return new Lin(db).calcRelatednessOfWords(word1, word2);
		double[][] matrix = new Lin(db).getSimilarityMatrix(words1, words2);
		Double sum = 0.0;
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[0].length; ++j) {
				if (matrix[i][j] > 10e9)
					sum += 1;
				else
					sum += matrix[i][j];
			}
		}
		// System.out.println(sum / (double) (matrix.length +
		// matrix[0].length));
		System.out.println(sum);
		return sum / (double) (matrix.length + matrix[0].length + 1);
	}

}