package com.pl.ios;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;

public class Main {

    public static void main(String[] args) {
        // write your code here
        //doTryWithResources();
        // doTryWithResourcesMulti();
        // doCloseThing();
        String[] data = {
                "Line 1",
                "Line 2 2",
                "Line 3 3 3"
        };
        try (FileSystem zipFs = openZip(Paths.get("myData.zip"))) {
        copyToZip(zipFs);
            writeToFileZip1(zipFs,data);
        } catch (IOException e) {
            System.out.println(e.getClass().getSimpleName() + " - " + e.getMessage());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
    private static FileSystem openZip(Path zipPath)throws IOException, URISyntaxException {
        Map<String, String>providerProps=new HashMap<>();
        providerProps.put("create","true");
        URI zipUri=new URI("jar:file",zipPath.toUri().getPath(),null);
        FileSystem zipFs= FileSystems.newFileSystem(zipUri,providerProps);
        return zipFs;
    }
private static void copyToZip(FileSystem zipFs)throws IOException{
   Path sourceFile=Paths.get("file2.txt");
   Path destFile=zipFs.getPath("/f2copy.txt");
    Files.copy(sourceFile,destFile, StandardCopyOption.REPLACE_EXISTING);
}
private static void writeToFileZip1(FileSystem zipFs,String[] data)throws IOException{
        try(BufferedWriter writer= Files.newBufferedWriter(zipFs.getPath("/newF1.txt"))){
            for(String d:data){
                writer.write(d);
                writer.newLine();
            }
        }
}
    public static void doTryWithResources() {
        char[] buff = new char[8];
        int length;
        try (Reader reader = Helper.openReader("file.txt.txt")) {
            while((length = reader.read(buff)) >= 0) {
                System.out.println("\nlength: " + length);
                for(int i=0; i < length; i++)
                    System.out.print(buff[i]);
            }
        } catch(IOException e) {
            System.out.println(e.getClass().getSimpleName() + " - " + e.getMessage());
        }
    }

    public static void doTryWithResourcesMulti() {
        char[] buff = new char[8];
        int length;
        try (Reader reader = Helper.openReader("file.txt.txt");
             Writer writer = Helper.openWriter("file2.txt"))  {
            while((length = reader.read(buff)) >= 0) {
                System.out.println("\nlength: " + length);
                writer.write(buff, 0, length);
            }
        } catch(IOException e) {
            System.out.println(e.getClass().getSimpleName() + " - " + e.getMessage());
        }
    }
    private static void doCloseThing() {
        try(MyAutoCloseable ac = new MyAutoCloseable()) {
            ac.saySomething();
        } catch (IOException e) {
            System.out.println(e.getClass().getSimpleName() + " - " + e.getMessage());

            for(Throwable t:e.getSuppressed())
                System.out.println("Suppressed: " + t.getMessage());
        }
    }
}

