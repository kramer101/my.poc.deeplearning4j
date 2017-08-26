package my.poc.deeplearning4j.misc;

import org.deeplearning4j.models.word2vec.Word2Vec;
import org.deeplearning4j.text.sentenceiterator.LineSentenceIterator;
import org.deeplearning4j.text.sentenceiterator.SentenceIterator;
import org.deeplearning4j.text.tokenization.tokenizer.preprocessor.CommonPreprocessor;
import org.deeplearning4j.text.tokenization.tokenizerfactory.DefaultTokenizerFactory;
import org.deeplearning4j.text.tokenization.tokenizerfactory.TokenizerFactory;
import org.deeplearning4j.ui.UiServer;
import org.junit.Test;

import java.io.File;
import java.net.URL;
import java.util.List;

/**
 *
 */
public class NameExtractor {


    public List<String> getNamesFromText() {

        try {

            URL filePath = ClassLoader.getSystemClassLoader().getResource("input-1.txt");
            if (filePath != null) {
                SentenceIterator iter = new LineSentenceIterator(new File(filePath.getPath()));

                TokenizerFactory tokenizerFactory = new DefaultTokenizerFactory();
                tokenizerFactory.setTokenPreProcessor(new CommonPreprocessor());

                Word2Vec vec = new Word2Vec.Builder()
                        .tokenizerFactory(tokenizerFactory)
                        .iterate(iter)
                        .minWordFrequency(5)
                        .iterations(1)
                        .layerSize(100)
                        .seed(42)
                        .windowSize(5)
                        .build();

                vec.fit();

                UiServer server = UiServer.getInstance();
                System.out.println("Started on port " + server.getPort());

                //ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

                //WordVectorSerializer.writeWord2VecModel(vec, byteArrayOutputStream);




                System.out.println(vec);
            }


        } catch (Exception eParam) {
            eParam.printStackTrace();
        }


        return null;
    }



    @Test
    public void testGetNames() {

        NameExtractor nameExtractor = new NameExtractor();
        List<String> namesFromText = nameExtractor.getNamesFromText();


    }



}
