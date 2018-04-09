package lda;

import java.io.IOException;
import java.util.Map;

public class LDATest {

	public static void main(String[] args) {
		
		if(args.length < 5) {
			System.out.println("Input args, such as : data_path topic_num alpha beta display_num");
			return;
		}
		try {
//			String data_path = "data/weixin_people";
			String data_path = args[0];
			
			// 1. Load corpus from disk
			Corpus corpus = Corpus.load(data_path);
			
			// 2. Create a LDA sampler
			LdaGibbsSampler ldaGibbsSampler = new LdaGibbsSampler(corpus.getDocument(), corpus.getVocabularySize());
			
			// 3. Train it
//			ldaGibbsSampler.gibbs(2);
			int topic_num = Integer.parseInt(args[1]);
			double alpha = Double.parseDouble(args[2]);
			double beta = Double.parseDouble(args[3]);
			ldaGibbsSampler.gibbs(topic_num, alpha, beta);
			
			// 4. The phi matrix is a LDA model, you can use LdaUtil to explain it.
			double[][] phi = ldaGibbsSampler.getPhi();
			Map<String, Double>[] topicMap = LdaUtil.translate(phi, corpus.getVocabulary(), Integer.parseInt(args[4]));
			LdaUtil.explain(topicMap);
			
			// 5. TODO:Predict. I'm not sure whether it works, it is not stable.
//			int[] document = Corpus.loadDocument(data_path+"/wx_data_singer", corpus.getVocabulary());
//			double[] tp = LdaGibbsSampler.inference(phi, document);
//			Map<String, Double> topic = LdaUtil.translate(tp, phi, corpus.getVocabulary(), 10);
//			LdaUtil.explain(topic);
		
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
