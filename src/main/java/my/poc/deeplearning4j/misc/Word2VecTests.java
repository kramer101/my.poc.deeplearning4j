package my.poc.deeplearning4j.misc;

import org.deeplearning4j.models.word2vec.VocabWord;
import org.deeplearning4j.models.word2vec.Word2Vec;
import org.deeplearning4j.text.sentenceiterator.FileSentenceIterator;
import org.deeplearning4j.text.sentenceiterator.SentenceIterator;
import org.deeplearning4j.text.tokenization.tokenizer.preprocessor.CommonPreprocessor;
import org.deeplearning4j.text.tokenization.tokenizerfactory.DefaultTokenizerFactory;
import org.deeplearning4j.text.tokenization.tokenizerfactory.TokenizerFactory;
import org.junit.Test;

import java.io.File;
import java.net.URL;
import java.util.List;

/**
 *
 */
public class Word2VecTests {


    public List<String> getNamesFromText() {

        try {

            URL filePath = ClassLoader.getSystemClassLoader().getResource("input-2.txt");
            if (filePath != null) {
                SentenceIterator iter = new FileSentenceIterator(new File(filePath.getPath()));

                TokenizerFactory tokenizerFactory = new DefaultTokenizerFactory();
                tokenizerFactory.setTokenPreProcessor(new CommonPreprocessor());


                Word2Vec vec = new Word2Vec.Builder()
                        .tokenizerFactory(tokenizerFactory)
                        .iterate(iter)
                        .minWordFrequency(1)
                        .iterations(1)
                        .layerSize(100)
                        .seed(42)
                        .windowSize(5)
                        .build();


                vec.fit();



               /* UiServer server = UiServer.getInstance();
                System.out.println("Started on port " + server.getPort());*/

                //vec.getLookupTable().getVocabCache().wordFrequency("monitor")
                System.out.println(vec.wordsNearest("monitor", 10));



                for (VocabWord word : vec.getVocab().tokens()) {

                   System.out.println(word.toJSON());
               }


/*
                WordVectorSerializer.writeWord2VecModel(vec, "c:/work/temp/trained1.txt");
                Word2Vec word2Vec = WordVectorSerializer.readWord2VecModel("c:/work/temp/trained1.txt");
                SentenceIterator iter2 = new LineSentenceIterator(new File(filePath.getPath()));

                TokenizerFactory tokenizerFactory2 = new DefaultTokenizerFactory();
                tokenizerFactory.setTokenPreProcessor(new CommonPreprocessor());

                word2Vec.setTokenizerFactory(tokenizerFactory2);
                word2Vec.setSentenceIterator(iter2);


                word2Vec.fit();
                System.out.println(vec.wordsNearest("monitor", 10));*/
            }


        } catch (Exception eParam) {
            eParam.printStackTrace();
        }


        return null;
    }



    @Test
    public void testGetNames() {

        Word2VecTests word2VecTests = new Word2VecTests();
        List<String> text = word2VecTests.getNamesFromText();


    }



}
