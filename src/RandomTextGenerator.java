import java.util.Random;

class RandomTextGenerator {
    public static String generateRandomText(int length) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append((char) ('a' + random.nextInt(26))); // Генеруємо випадкові літери
        }
        return sb.toString();
    }
}