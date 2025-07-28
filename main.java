import java.io.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.file.*;
import java.util.*;
import org.json.JSONObject;

public class ShamirSimplified {

    public static BigInteger convertToDecimal(String value, int base) {
        return new BigInteger(value, base);
    }

    public static BigInteger lagrangeInterpolationAtZero(List<long[]> points, int k) {
        BigDecimal result = BigDecimal.ZERO;
        for (int j = 0; j < k; j++) {
            BigDecimal term = new BigDecimal(points.get(j)[1]);
            for (int i = 0; i < k; i++) {
                if (i != j) {
                    long xi = points.get(i)[0];
                    long xj = points.get(j)[0];
                    BigDecimal numerator = new BigDecimal(-xi);
                    BigDecimal denominator = new BigDecimal(xj - xi);
                    term = term.multiply(numerator.divide(denominator, 20, BigDecimal.ROUND_HALF_UP));
                }
            }
            result = result.add(term);
        }
        return result.setScale(0, BigDecimal.ROUND_HALF_UP).toBigInteger();
    }

    public static BigInteger solveFromJson(String filename) throws Exception {
        String content = Files.readString(Paths.get(filename));
        JSONObject json = new JSONObject(content);
        int n = json.getJSONObject("keys").getInt("n");
        int k = json.getJSONObject("keys").getInt("k");
        List<long[]> decodedPoints = new ArrayList<>();
        for (String key : json.keySet()) {
            if (key.equals("keys")) continue;
            int x = Integer.parseInt(key);
            JSONObject obj = json.getJSONObject(key);
            int base = obj.getInt("base");
            String value = obj.getString("value");
            BigInteger y = convertToDecimal(value, base);
            decodedPoints.add(new long[]{x, y.longValue()});
        }
        List<long[]> selectedPoints = decodedPoints.subList(0, k);
        return lagrangeInterpolationAtZero(selectedPoints, k);
    }

    public static void main(String[] args) {
        try {
            BigInteger result1 = solveFromJson("input1.json");
            BigInteger result2 = solveFromJson("input2.json");
            System.out.println("Secret for Testcase 1: " + result1);
            System.out.println("Secret for Testcase 2: " + result2);
        } catch (Exception
 e) {
            e.printStackTrace();
        }
    }
}
