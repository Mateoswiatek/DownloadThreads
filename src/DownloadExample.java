import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Locale;

public class DownloadExample {
    static int count = 0;

    // lista plików do pobrania
    static String [] toDownload = {
            "https://home.agh.edu.pl/~pszwed/wyklad-c/01-jezyk-c-intro.pdf",
            "https://home.agh.edu.pl/~pszwed/wyklad-c/02-jezyk-c-podstawy-skladni.pdf",
            "https://home.agh.edu.pl/~pszwed/wyklad-c/03-jezyk-c-instrukcje.pdf",
            "https://home.agh.edu.pl/~pszwed/wyklad-c/04-jezyk-c-funkcje.pdf",
            "https://home.agh.edu.pl/~pszwed/wyklad-c/05-jezyk-c-deklaracje-typy.pdf",
            "https://home.agh.edu.pl/~pszwed/wyklad-c/06-jezyk-c-wskazniki.pdf",
            "https://home.agh.edu.pl/~pszwed/wyklad-c/07-jezyk-c-operatory.pdf",
            "https://home.agh.edu.pl/~pszwed/wyklad-c/08-jezyk-c-lancuchy-znakow.pdf",
            "https://home.agh.edu.pl/~pszwed/wyklad-c/09-jezyk-c-struktura-programow.pdf",
            "https://home.agh.edu.pl/~pszwed/wyklad-c/10-jezyk-c-dynamiczna-alokacja-pamieci.pdf",
            "https://home.agh.edu.pl/~pszwed/wyklad-c/11-jezyk-c-biblioteka-we-wy.pdf",
            "https://home.agh.edu.pl/~pszwed/wyklad-c/preprocesor-make-funkcje-biblioteczne.pdf",
    };

    static class Downloader implements Runnable{
        private final String url;

        Downloader(String url){
            this.url = url;
        }

        public void run(){
            String fileName = url.substring(url.lastIndexOf('/') + 1); //nazwa pliku - wyodrębnij z z url - to c jest na końcu, po ostatnim /
            System.out.println("zaczynam pobieranie: " + fileName);
            try(InputStream in = new URL(url).openStream(); FileOutputStream out = new FileOutputStream(fileName) ){
                int data;
                while((data = in.read()) != -1){
                    out.write(data);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("Done:"+fileName);
            count+=1;
        }

        static void sequentialDownload(){
            double t1 = System.nanoTime()/1e6;
            for(String url:toDownload){
                new Downloader(url).run();  // uwaga tu jest run()
            }
            double t2 = System.nanoTime()/1e6;
            System.out.printf(Locale.US,"t2-t1=%f\n",t2-t1);
        }

        static void concurrentDownload(){
            double t1 = System.nanoTime()/1e6;
            for(String url:toDownload){
                new Thread(new Downloader(url)).start();
            }
            double t2 = System.nanoTime()/1e6;
            System.out.printf(Locale.US,"t2-t1=%f\n",t2-t1);
        }

        static void concurrentDownload2(){
            double t1 = System.nanoTime()/1e6;
            for(String url:toDownload){
                new Thread(new Downloader(url)).start();
            }
            double t2 = System.nanoTime()/1e6;

            while(count!=toDownload.length){
                Thread.yield();
            }
            System.out.println("ilosc pobranych: " + count);
            System.out.printf(Locale.US,"t2-t1=%f\n",t2-t1);

        }
    }
}