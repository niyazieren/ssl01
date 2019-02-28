import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.sound.midi.Soundbank;
import java.math.BigInteger;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.X509Certificate;
import java.sql.SQLOutput;

public class Cert {
    public static void main(String[] args) throws NoSuchAlgorithmException, KeyManagementException {
        MyManager trm = new MyManager();

        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, new TrustManager[] { trm }, null);
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

        try {
            certInformation("https://expired.badssl.com/");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void certInformation(String aURL) throws Exception {
        URL destinationURL = new URL(aURL);
        HttpsURLConnection conn = (HttpsURLConnection) destinationURL.openConnection();
        conn.connect();
        Certificate[] certs = conn.getServerCertificates();
        System.out.println("Sertifika sayısı " + certs.length);
        for (Certificate cert : certs) {
            if (cert instanceof X509Certificate) {
                X509Certificate x = (X509Certificate) cert;
                System.out.println("sertifika bitiş tarihi " + x.getNotAfter());
                System.out.println(x.getSubjectDN());

                BigInteger serialNumber = x.getSerialNumber();
                System.out.println(serialNumber.toString(16));

                try {
                    x.checkValidity();
                    System.out.println("Sertifika  tarihi geçerli");
                } catch (CertificateExpiredException ex) {
                    System.out.println("Sertifika tarşhş geçmiş");
                } catch (CertificateNotYetValidException ex) {
                    System.out.println("Sertifika daha gecerlililk tarihi gelmemis");
                }
            }
        }
    }
}
