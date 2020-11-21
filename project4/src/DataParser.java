import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class DataParser {
    public void backPropOutput(String dataset) throws FileNotFoundException {
        int[] layers = new int[]{0, 1, 2};
        int[] folds = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9};


        try {
            for (int layer : layers) {
                for (int fold : folds) {
                    ArrayList<String> parsedFile = new ArrayList<>();
                    String fileString = dataset + "_" + layer + "_" + fold + ".txt";
                    File file = new File(fileString);
                    Scanner scanner = new Scanner(file);
                    while (scanner.hasNext()) {
                        parsedFile.add(scanner.next());
                    }
                    for(int i = 0; i<parsedFile.size(); i++){
                        if(parsedFile.get(i).equals("layer:")){
                            if(layer == 2){
                                System.out.println(parsedFile.get(i+1) + parsedFile.get(i+2));
                            }
                            else {
                                System.out.println(parsedFile.get(i + 1));
                            }
                        }
                        if(parsedFile.get(i).equals("Overall")){
                            System.out.println(parsedFile.get(i+6));
                        }
                    }
                }
            }
        }
        catch(FileNotFoundException e){
            System.out.println("The file could not be read.");
            e.printStackTrace();
        }
    }
}
