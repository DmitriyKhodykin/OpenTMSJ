import java.io.FileInputStream;
import java.util.Properties;

public class GetToken {

    public static void main(String tokenName) {
        GetToken.getToken(tokenName);
    }

    public static String getToken(String tokenName) {
        // Read authorisation data from file
        FileInputStream fis;
        Properties property = new Properties();

        try {
            fis = new FileInputStream("src\\auth.properties");
            property.load(fis);
            return property.getProperty(tokenName);
        } catch (java.io.IOException e) {
            System.err.println("error: Token not found");
        }
        return null;
    }
}
