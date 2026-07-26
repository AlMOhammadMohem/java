package utils;

import models.Payer;
import net.datafaker.Faker;
import org.apache.commons.lang3.RandomStringUtils;

public class RandomDataGenerator {

    private static final Faker faker = new Faker();

    private RandomDataGenerator() {
    }

    private static String uniqueSuffix() {
              return RandomStringUtils.randomAlphanumeric(6).toUpperCase();
    }

    public static Payer generatePayer() {
              String suffix = uniqueSuffix();

          Payer payer = new Payer();
              payer.setEnglishName("QA Payer " + faker.company().name() + " " + suffix);
              payer.setArabicName(generateArabicName() + " " + suffix);
              payer.setCode("PYR-" + suffix);
              payer.setDescription("Automated test payer generated on " + java.time.LocalDate.now() + " - " + faker.lorem().sentence());
              return payer;
    }

    /**
     * Arabic literals are Unicode-escaped in source rather than typed as raw UTF-8
       * characters, to avoid file-encoding issues across different editors/OSes when
       * this framework is cloned and compiled on machines with different default
       * source encodings.
       */
    private static String generateArabicName() {
              String[] arabicWords = {
                                "\u0634\u0631\u0643\u0629",
                                "\u0645\u062C\u0645\u0648\u0639\u0629",
                                "\u0645\u0624\u0633\u0633\u0629"
              };
              int index = (int) (Math.random() * arabicWords.length);
              return arabicWords[index];
    }
}
