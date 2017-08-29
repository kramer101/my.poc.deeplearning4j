package my.poc.deeplearning4j.misc;

import java.io.File;
import java.net.URL;
import org.deeplearning4j.bagofwords.vectorizer.BagOfWordsVectorizer;
import org.deeplearning4j.text.sentenceiterator.FileSentenceIterator;
import org.deeplearning4j.text.sentenceiterator.SentenceIterator;
import org.deeplearning4j.text.tokenization.tokenizer.preprocessor.CommonPreprocessor;
import org.deeplearning4j.text.tokenization.tokenizerfactory.DefaultTokenizerFactory;
import org.deeplearning4j.text.tokenization.tokenizerfactory.TokenizerFactory;
import org.junit.Test;

/**
 * https://deeplearning4j.org/bagofwords-tf-idf.html
 */
public class BagOfWordsTests {



    public void detectCategory() {

        try {

            URL filePath = ClassLoader.getSystemClassLoader().getResource("input-1.txt");
            if (filePath != null) {
                SentenceIterator iter = new FileSentenceIterator(new File(filePath.getPath()));

                TokenizerFactory tokenizerFactory = new DefaultTokenizerFactory();
                tokenizerFactory.setTokenPreProcessor(new CommonPreprocessor());

                BagOfWordsVectorizer bagOfWordsVectorizer =
                        new BagOfWordsVectorizer.Builder()
                                .setIterator(iter)
                                .setMinWordFrequency(1)
                                .setTokenizerFactory(tokenizerFactory)
                                .build();


                bagOfWordsVectorizer.fit();

                System.out.println(bagOfWordsVectorizer);

            }


        } catch (Exception eParam) {
            eParam.printStackTrace();
        }
    }



    @Test
    public void testDetectCategory() {
        BagOfWordsTests test = new BagOfWordsTests();
        test.detectCategory();

    }
}
