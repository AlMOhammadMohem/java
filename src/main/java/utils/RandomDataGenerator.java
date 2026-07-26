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

    /**
     * Arabic-letters-only unique suffix. This MUST be used anywhere a value has to
         * pass an "Arabic letters only" validation rule (e.g. the Arabic payer name
         * field), because the plain alphanumeric uniqueSuffix() above contains Latin
         * letters/digits and will fail that validation. Uniqueness is achieved by
         * randomly sampling characters from the Arabic alphabet instead.
         */
    private static String arabicUniqueSuffix() {
                String arabicLetters =
                                    "\u0627\u0628\u062A\u062B\u062C\u062D\u062E\u062F\u0630\u0631\u0632\u0633\u0634\u0635\u0636\u0637\u0638\u0639\u063A\u0641\u0642\u0643\u0644\u0645\u0646\u0647\u0648\u064A";
                StringBuilder suffix = new StringBuilder();
                for (int i = 0; i < 6; i++) {
                                int index = (int) (Math.random() * arabicLetters.length());
                                suffix.append(arabicLetters.charAt(index));
                }
                return suffix.toString();
    }

    public static Payer generatePayer() {
                String suffix = uniqueSuffix();
                String arabicSuffix = arabicUniqueSuffix();

            Payer payer = new Payer();
                payer.setEnglishName("QA Payer " + faker.company().name() + " " + suffix);
                payer.setArabicName(generateArabicName() + " " + arabicSuffix);
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
