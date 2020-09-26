package edu.ucsb.kevinzhang421.flourtreats;


import java.util.ArrayList;

public class Ingredient {
    private double grams = 0;
    private String ingredient = "";
    private ArrayList<String> phrases = new ArrayList<>();
    private String phrase;
    private double ratio;
    private double alternateGrams = 0;
    private boolean gramsNeedsToBeSet = false;

    public static String alphabet = "qwertyuiopasdfghjklzxcvbnm";
    public static String numbers = "1234567890";

    public Ingredient(String phrase) {
        this.phrase = phrase;
        breakPhraseIntoWords();
        //System.out.println("after break phrase into words "+phrases);
        concatenateWordPairs();
        // System.out.println("after concatenate" +phrases);
        removeUnknownWords();
        //System.out.println("after remove unknown words "+phrases);
        findIngredient();
        //System.out.println("before fract to deci "+phrases);
        setFractionsToDecimals();
        setGrams();
        //System.out.println(grams+"g of "+ingredient);
    }

    public double getGrams() {
        return this.grams;
    }

    public String getIngredient() {
        return ingredient;
    }

    private void breakPhraseIntoWords() {
        phrase = phrase.toLowerCase();
        removeTextSurroundedBy("(", ")");
        removeTextSurroundedBy("[", "]");
        String[] words = phrase.split(" ");
//        for (int i = 0; i < words.length; i++) {
//            System.out.print(words[i]+", ");
//        }
//        System.out.println();
        for (int i = 0; i < words.length; i++) {
            words[i].trim();
            boolean containsNumbers = containsNumbers(words[i]);
            boolean containsLetter = containsLetters(words[i]);
            int firstIndexOfNumber = getIndexOfFirstNumber(words[i]);
            int firstIndexOfLetter = getIndexOfFirstLetter(words[i]);
            if (containsNumbers && containsLetter) {
                int breakPoint = Math.max(firstIndexOfLetter, firstIndexOfNumber);
                ArrayList <String> w = separateHyphenFromWords(words[i].substring(0, breakPoint));
                ArrayList <String> w2 = separateHyphenFromWords(words[i].substring(breakPoint));
                for (int j = 0; j < w.size(); j++) {
                    if (w.get(j).equals("g")) {
                        phrases.add(" " + w.get(j) + " ");
                    } else {
                        phrases.add(w.get(j));
                    }
                }
                for (int j = 0; j < w2.size(); j++) {
                    if (w2.get(j).equals("g")) {
                        phrases.add(" " + w2.get(j) + " ");
                    } else {
                        phrases.add(w2.get(j));
                    }
                }

            } else {
                ArrayList <String> w = separateHyphenFromWords(words[i]);
                for (int j = 0; j < w.size(); j++) {
                    if (w.get(j).equals("g")) {
                        phrases.add(" " + w.get(j) + " ");
                    } else {
                        phrases.add(w.get(j));
                    }
                }
            }
        }
    }
    private ArrayList<String> separateHyphenFromWords(String word){
        ArrayList<String> words = new ArrayList<>();
        int i = word.indexOf("-");
        if (i!=-1 &&Conversions.getIngredientName(word).equals("null")) {
            String word1 = (word.substring(0, i));
            String word2 ="";
            if (i + 1 < word.length()) {
                word2 = word.substring(i + 1);
            }
            if (containsNumbers(word1) && containsNumbers(word2)) {
                words.add(word.substring(0, i));
                words.add(word.substring(i, i + 1));
                if (i + 1 < word.length()) words.add(word.substring(i + 1));
            }
            }else{
            words.add(word);
        }
        return words;
    }

    private void removeUnknownWords() {
        for (int i = 0; i < phrases.size(); i++) {
            if (!containsNumbers(phrases.get(i)) && Conversions.getIngredientName(phrases.get(i)).equals("null") && Conversions.getUnitOfMeasure(phrases.get(i)).equals("null") && !phrases.get(i).equals("to") && !phrases.get(i).equals("or") && !phrases.get(i).equals("-")) {
                phrases.remove(i);
                i--;
            }
        }
    }

    private void removeTextSurroundedBy(String char1, String char2) {
        if (phrase.contains(char1) && phrase.contains(char2)) {
            while (phrase.indexOf(char1) != -1) {
                if (phrase.indexOf(char1) < phrase.indexOf(char2)) {
                    phrase= phrase.substring(0, phrase.indexOf(char1)) + phrase.substring(phrase.indexOf(char2) + 1);
                }
            }
        }
    }

    public static String removeTextSurroundedBy(String char1, String char2, String word) {
        if (word.contains(char1) && word.contains(char2)) {
            while (word.indexOf(char1) != -1) {
                if (word.indexOf(char1) < word.indexOf(char2)) {
                    word = word.substring(0, word.indexOf(char1)) + word.substring(word.indexOf(char2) + 1);
                }
            }
        }
        return word;
    }

    private void concatenateWordPairs() {
        String[] ingredientNames = Conversions.getIngredientsDatabase();
        String[] volumeNames = Conversions.getVolumes();
        String[] quantityIngredientNames = Conversions.getQuantityIngredients();
        ArrayList<String> wordPairs = new ArrayList<>();
        addWordPairsTo(wordPairs, ingredientNames);
        addWordPairsTo(wordPairs, volumeNames);
        addWordPairsTo(wordPairs, quantityIngredientNames);
        //System.out.println(wordPairs);
        for (int i = 0; i + 1 < phrases.size(); i++) {
            String currentWord = phrases.get(i);
            String nextWord = phrases.get(i + 1);
            for (int j = 0; j < wordPairs.size(); j++) {
                String wordPair = wordPairs.get(j);
                int indexOfSpace = wordPair.indexOf(" ");
                if (wordPair.indexOf(" ", indexOfSpace + 1) != -1 && i + 2 < phrases.size()) {
                    String thirdWord = phrases.get(i + 2);
                    int indexOfSecondSpace = wordPair.indexOf(" ", indexOfSpace + 1);
                    String word1 = wordPair.substring(0, indexOfSpace);
                    String word2 = wordPair.substring(indexOfSpace + 1, indexOfSecondSpace);
                    String word3 = wordPair.substring(indexOfSecondSpace + 1);
                    if (currentWord.contains(word1) && nextWord.contains(word2) && thirdWord.contains(word3)) {
                        phrases.set(i, word1 + " " + word2 + " " + word3);
                        phrases.remove(i + 1);
                        phrases.remove((i + 1));
                        i -= 2;
                        break;
                    }
                } else {
                    String word1 = wordPair.substring(0, indexOfSpace);
                    String word2 = wordPair.substring(indexOfSpace + 1);
                    if (currentWord.contains(word1) && nextWord.contains(word2)) {
                        phrases.set(i, word1 + " " + word2);
                        phrases.remove(i + 1);
                        i--;
                        break;
                    }
                }
            }
        }
        //System.out.println(phrases);
    }

    private void addWordPairsTo(ArrayList<String> wordPairs, String[] phrases) {
        for (int i = 0; i < phrases.length; i++) {
            if (phrases[i].contains(" ") && phrases[i].indexOf(" ") != 0) {
                wordPairs.add(phrases[i]);
            }
        }
    }

    private void findIngredient() {
        for (int i = 0; i < phrases.size(); i++) {
            if (!Conversions.getIngredientName(phrases.get(i)).equals("null")) {
                ingredient = Conversions.getIngredientName(phrases.get(i));
            }
        }
    }

    private void setFractionsToDecimals() {
        for (int i = 0; i < phrases.size(); i++) {
            if (containsNumbers(phrases.get(i)) && phrases.get(i).contains("/")) {
                int locOfFractionSign = phrases.get(i).indexOf("/");
                if (locOfFractionSign!=0){
                    phrases.set(i, Double.toString(convertFractionToDecimal(phrases.get(i))));

                }else {
                    if (i>0){
                        if (containsNumbers(phrases.get(i-1))){
                            phrases.set(i-1,Double.toString(convertFractionToDecimal(phrases.get(i-1)+phrases.get(i))));
                            phrases.remove(i);
                            i--;
                        }
                    }else{
                        phrases.remove(i);
                        i--;
                    }
                }            }
        }
        //System.out.println("after setting frec to deci "+phrases);
        for (int i = 0; i + 1 < phrases.size(); i++) {
            if (containsNumbers(phrases.get(i)) && containsNumbers(phrases.get(i + 1))) {
                phrases.set(i, Double.toString(Double.parseDouble(removeNonNumbers(phrases.get(i))) + Double.parseDouble(removeNonNumbers(phrases.get(i + 1)))));
                phrases.remove(i+1);
            }
        }
    }

    private void setGrams() {
        for (int i = 0; i < phrases.size(); i++) {
            if (i - 1 >= 0) {
                String previousPhrase = phrases.get(i - 1);
                String currentPhrase = phrases.get(i);
                if (currentPhrase.equals("to") || currentPhrase.equals("or") || currentPhrase.equals("-")){
                    if (!gramsNeedsToBeSet) {
                        alternateGrams = grams;
                    }
                    gramsNeedsToBeSet = true;
                    grams = 0;
                }
                if (containsNumbers(previousPhrase)) {
                    for (int j = 0; j < previousPhrase.length(); j++) {
                        if (!containsNumbers(previousPhrase.substring(j,j+1)) && !previousPhrase.substring(j,j+1).contains(".")){
                            previousPhrase = previousPhrase.substring(0,j)+previousPhrase.substring(j+1);
                        }
                    }
                    double number = Double.parseDouble(previousPhrase);

                    if (Conversions.getUnitOfMeasure(currentPhrase).equals("gram") || Conversions.getUnitOfMeasure(currentPhrase).equals(" g ")) {
                        grams += number;
                        gramsNeedsToBeSet = false;
                    } else if (Conversions.getUnitOfMeasure(currentPhrase).equals("lb") || Conversions.getUnitOfMeasure(currentPhrase).equals("pound")) {
                        grams += Conversions.convertPoundsToGrams(number);
                        gramsNeedsToBeSet = false;
                    } else if (Conversions.getUnitOfMeasure(currentPhrase).equals("oz") || Conversions.getUnitOfMeasure(currentPhrase).equals("ounce")) {
                        grams += Conversions.convertOuncesToGrams(number);
                        gramsNeedsToBeSet = false;
                    }  else if (!Conversions.getUnitOfMeasure(currentPhrase).equals("null")) {
                        if (Conversions.getGramsPerCupFor(ingredient) != -1) {
                            grams += number * Conversions.convertToCups(Conversions.getUnitOfMeasure(currentPhrase)) * Conversions.getGramsPerCupFor(ingredient);
                            gramsNeedsToBeSet = false;
                        }
                    } else if (Conversions.isQuantityIngredient(ingredient) && !Conversions.getIngredientName(currentPhrase).equals("null")) {
                        grams += Conversions.getGramsPer(ingredient) * number;
                        gramsNeedsToBeSet = false;
                    }
                }

            }
        }
        if (gramsNeedsToBeSet){
            grams=alternateGrams;
            gramsNeedsToBeSet=false;
        }
    }

    private int getIndexOfFirstLetter(String word) {
        for (int i = 0; i < word.length(); i++) {
            String character = word.substring(i, i + 1);
            if (alphabet.contains(character)) {
                return i;
            }
        }
        return -1;
    }

    private int getIndexOfFirstNumber(String word) {
        for (int i = 0; i < word.length(); i++) {
            String character = word.substring(i, i + 1);
            if (numbers.contains(character)) {
                return i;
            }
        }
        return -1;
    }

    private double convertFractionToDecimal(String fraction) {
        int locOfFractionSign = fraction.indexOf("/");
        //System.out.println(locOfFractionSign);
        String numerator = fraction.substring(0, locOfFractionSign);
        String denomenator = fraction.substring(locOfFractionSign + 1);
        numerator = removeNonNumbers(numerator);
        denomenator = removeNonNumbers(denomenator);
        // System.out.println(numerator+" : "+denomenator);
        //System.out.println(Double.parseDouble(numerator)/Double.parseDouble(denomenator));
        return Double.parseDouble(numerator) / Double.parseDouble(denomenator);
    }

    private boolean containsNumbers(String word) {
        for (int j = 0; j < numbers.length(); j++) {
            if (word.contains(numbers.substring(j, j + 1))) {
                return true;
            }
        }
        return false;
    }

    private boolean containsLetters(String word) {
        for (int j = 0; j < alphabet.length(); j++) {
            if (word.contains(alphabet.substring(j, j + 1))) {
                return true;
            }
        }
        return false;
    }

    private String removeNonNumbers(String phrase) {
        for (int j = 0; j < phrase.length(); j++) {
            String letter = phrase.substring(j,j+1);
            if (alphabet.contains(letter) || (!numbers.contains(letter) && !letter.equals("."))) {
                phrase = phrase.substring(0, j) + phrase.substring(j + 1);
                j--;
            }
        }
        return phrase;
    }

    public void setRatio(double r) {
        ratio = r;
    }

    public double getRatio() {
        return ratio;
    }
    public void addGrams(double g){
        this.grams +=g;
    }
}


