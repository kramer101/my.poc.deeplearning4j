package my.poc.deeplearning4j.misc;

import my.poc.deeplearning4j.misc.rss.CraigsListItem;
import my.poc.deeplearning4j.misc.rss.CraigsListRssSourceReader;
import org.apache.log4j.Logger;
import org.deeplearning4j.models.word2vec.VocabWord;
import org.deeplearning4j.models.word2vec.Word2Vec;
import org.deeplearning4j.text.sentenceiterator.BasicLineIterator;
import org.deeplearning4j.text.sentenceiterator.SentenceIterator;
import org.deeplearning4j.text.tokenization.tokenizer.preprocessor.CommonPreprocessor;
import org.deeplearning4j.text.tokenization.tokenizerfactory.DefaultTokenizerFactory;
import org.deeplearning4j.text.tokenization.tokenizerfactory.TokenizerFactory;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 *
 */
public class CLComputersAdsTrainer {

    private static Logger log =
            org.apache.log4j.LogManager.getLogger(CLComputersAdsTrainer.class);

    public static final String TRAINED_MODEL_FILE = "cl_trained.model";
    public static final String TRAINED_VOCAB_FILE = "cl_trained.vocab";

    public void train() throws Exception {


        log.debug("Begin training");
        CraigsListRssSourceReader reader = new CraigsListRssSourceReader();

        List<CraigsListItem> trainingData = reader.getArticles(
                //"https://sfbay.craigslist.org/search/sby/sys?format=rss&s=240");

                "https://sfbay.craigslist.org/search/sby/cto?min_auto_year=2000&format=rss");

        int count = 0;
        List<String> dump = new LinkedList<>();
        for (CraigsListItem item : trainingData) {
            log.debug(item);

            SentenceIterator iterator;
            TokenizerFactory tokenizerFactory;
            Word2Vec vec;

            try (InputStream dataInputStream =
                         new ByteArrayInputStream(item.getBody().getBytes("UTF-8"))) {

                iterator = new BasicLineIterator(dataInputStream);
                iterator.setPreProcessor(String::toLowerCase);

                tokenizerFactory = new DefaultTokenizerFactory();
                tokenizerFactory.setTokenPreProcessor(new CommonPreprocessor());

/*
                if (Files.exists(Paths.get(TRAINED_MODEL_FILE))) {

                    vec = WordVectorSerializer.readWord2VecModel(TRAINED_MODEL_FILE, true);
                    vec.setSentenceIterator(iterator);
                    vec.setTokenizerFactory(tokenizerFactory);
                   //vec.setVocab(WordVectorSerializer.readVocabCache(new File(TRAINED_VOCAB_FILE)));


                } else {
                    VocabCache<VocabWord> cache = new AbstractCache<>();
                    WeightLookupTable<VocabWord> table = new InMemoryLookupTable.Builder<VocabWord>()
                            .vectorLength(100)
                            .useAdaGrad(false)
                            .cache(cache).build();

                    vec = new Word2Vec.Builder()
                            .tokenizerFactory(tokenizerFactory)
                            .iterate(iterator)
                            .minWordFrequency(1)
                            .learningRate(10)
                            .iterations(20)
                            .layerSize(50)
                            .seed(42)
                            .windowSize(50)
                            .batchSize(5)
                            .epochs(1)
                            .lookupTable(table)
                            .vocabCache(cache)
                            .build();

                }*/


                vec = new Word2Vec.Builder()
                        .tokenizerFactory(tokenizerFactory)
                        .iterate(iterator)
                        .minWordFrequency(1)
                        .learningRate(10)
                        .iterations(20)
                        .layerSize(50)
                        .seed(42)
                        .windowSize(50)
                        .batchSize(5)
                        .epochs(1)
                        //.lookupTable(table)
                        //.vocabCache(cache)
                        .build();


                log.debug("Fitting on item #" + count + "...");

                vec.fit();

                /*logResult("apple", vec);
                logResult("air", vec);
                logResult("macbook", vec);
                logResult("laptop", vec);
                logResult("new", vec);
                logResult("new", vec);
                logResult("computer", vec);*/

                log.debug(item.getUrl());
                /*List<String> similarToNew = vec.similarWordsInVocabTo("new", .6);
                log.debug("similarToNew: " + similarToNew);

                List<String> similarToTires = vec.similarWordsInVocabTo("tires", .6);
                log.debug("similarToTires: " + similarToTires);*/

                VocabWord wordNew = vec.getVocab().wordFor("new");
                VocabWord wordTires = vec.getVocab().wordFor("tires");

                if (wordNew != null && wordTires != null) {

                    log.debug(wordNew.toJSON());
                    log.debug(wordTires.toJSON());

                    dump.add(item.getTitle());
                    dump.add(wordNew.toJSON());
                    dump.add(wordTires.toJSON());

                    dump.add(vec.getWordVectors(Arrays.asList("new", "tires")).data().toString());
                    dump.add("");

                }

                //WordVectorSerializer.writeWord2VecModel(vec, TRAINED_MODEL_FILE);
                //WordVectorSerializer.writeVocabCache(vec.getVocab(), new File(count + "_" + TRAINED_VOCAB_FILE));
                count++;

            }

            Files.write(Paths.get("result.txt"), dump, StandardOpenOption.WRITE,
                    StandardOpenOption.CREATE);

        }


        log.debug("DONE training");
    }

    private void logResult(final String word, final  Word2Vec vec) {
        log.debug("result " + word + ": " + vec.wordsNearest(word, 10)
                + ", word count: "
                + vec.getVocab().words().size());

        log.debug("wordFrequency " + word + ": " + vec.getVocab().wordFrequency(word));

        /*log.debug("similarity to " + word  + ": " + vec.similarWordsInVocabTo(word, 0.7)
                + ", word count: "
                + vec.getVocab().words().size());
        log.debug("wordFrequency " + word + ": " + vec.getVocab().wordFrequency(word));*/
    }

    @Test
    public void testTrain() {

        CLComputersAdsTrainer trainer = new CLComputersAdsTrainer();
        try {
            trainer.train();

        } catch (Exception e) {

            e.printStackTrace();
            Assert.fail(e.getMessage());
        }
    }

}
