package edu.ucsb.kevinzhang421.flourtreats;

import java.util.ArrayList;

public class Conversions {
    private static String[] ingredientsDatabase = {
            "peanut butter", "almond butter","brown sugar",
//            "granulated sugar", "white sugar",
            "confectioners' sugar", "powdered sugar", "sugar",
//            "unsalted butter", "salted butter",
            "butter", "egg white", "egg yolk", "egg", "buttermilk",
            "water", "almond milk","oat milk", "olive oil", "strawberry puree",
            "coconut oil", "milk", "oil", "yeast", "baking powder",
            "baking soda", "vinegar", "salt", "cocoa", "chocolate chips",
            "dark chocolate", "white chocolate", "chocolate", //remove some chocolates?
            "cacao", "lemon zest","lemon juice", "banana", "pumpkin",
            "whole wheat flour", "honey", "cream cheese", "heavy cream", "yogurt",
            "ground ginger", "yam", "coffee powder", "espresso powder", "vanilla extract",
            "ground cinnamon", "rolled oat", "oat flour", "oat","almond flour",
            "cake flour", "bread flour","rye flour","flax meal", "flour",
            "sliced almond", "almond", "ginger","cloves", "sweet potato",
            "potato", "maple syrup", "molasses", "hazelnut", "shredded coconut",
            "ground cardamom", "light cream", "sour cream", "whipping cream", "evaporated milk", //remove some condensed milks?

            "condensed milk", "walnut", "cashew", "cinnamon stick",
            "cheese", "mayonnaise", "pomegranate", "peanut", "blueberries",
            "raspberries", "strawberries", "carrot", "matcha", "granulated garlic",
            "garlic", "cornmeal", "corn starch", "apple cider vinegar", "vinegar",
            "pistachio", "thyme", "rosemary", "basil", "sourdough starter",
            "applesauce", "apple", "orange juice", "orange", "vanilla",

            "sour salt", "citric acid", "avocado oil", "pizza dough flavor", "sesame seed",
            "anise seed", "caraway seed", "dark brown sugar", "olive", "tahini",
            "tangzhong", "allspice", "potato flour", "potato flakes", "dry milk",
            "milk powder", "pineapple juice", "pineapple", "xanathan gum", "wheat germ",
            "wild rice", "flax seed", "pumpkin pie spice", "self rising flour", "brandy",
            "corn syrup", "blackberries", "orange zest", "gelatin", "shortening",
            "fiori di sicilia", "pecans","date", "gingerbread spice","nutmeg",
            "coffee", "apricots", "cherries", "rum","apple juice",
            "cranberry juice", "cranberries", "raisin", "zucchini", "coconut milk powder",
            "coconut flavor", "oreo", "almond extract", "sherry", "matzo cake meal",
            "lemon oil", "cream of tartar", "meringue powder", "graham  cracker", "clearjel",
            "poppy seed", "orange oil", "maple flavor", "lemon curd", "pastry cream",
            "lemon extract", "tomato juice", "wholewheat flour", "wholemeal flour", "currant",

            "mint", "cinnamon chip", "m&m", "fruit", "nuts",
            "bran flakes", "cookie crumbs", "peach", "toffee", "ginger root",
            "crystallized ginger", "coffee creamer", "half-and-half", "vanilla bean", "ghee",
            "chana del", "fenugreek seeds"
    };
    private static Double[] gramsPerCup = {
            250., 250.,200.,
//            200., 215.,
            120., 120., 200.,
//            227., 227.,
            227., 256.78, 243., 250., 244.42,
            237., 253.61, 241., 130., 232.,
            216., 244.42, 198., 192., 192.,
            288., 237., 288., 100., 175.,
            175., 180., 175.,
            100., 96., 230.,300.,225.,
            130., 340.,225.,231.,245.,
            88., 200.,192., 96.,208.,
            132., 100., 100., 100., 112.,
            115., 127., 102., 149.,120.,
            110., 140., 88., 110.,200.,
            325., 322., 325., 150., 80.,
            98., 240., 240., 238., 252.,

             306., 110., 150., 132.,
            100., 200., 174., 140., 150.,
            123., 144., 50., 96., 101.44,
            136., 120., 160., 240., 230.,
            100., 46., 56., 20.1, 227.,
            250., 120., 249., 225., 208.,

            240., 240., 215., 120., 142.,
            113., 114., 200., 180., 260.,
            300., 102., 168., 84., 112.,
            125., 264.17, 225., 153.6, 83.,
            195.49, 149., 89.6, 113.,235.,
            300., 144., 96., 148., 190.,
            208., 128., 175., 100., 118.,
            237., 190., 140., 235., 248.,
            253., 100., 150., 124., 160.,
            208., 100.,208., 240., 126.8,
            224., 171.18, 152.16, 85., 64.,
            125., 224., 208., 304.33, 114.,
            208., 256.78, 113.6, 125., 150.,

            25., 240., 170., 190., 150.,
            35., 100., 225., 240., 96.,
            168.,144., 242.,100., 216.62,
            170.,188.
    };
    private static String[] volumes = {"tsp", "teaspoon", "tbsp", "tablespoon", "cup", "ml", "mL", "milliliter", "fl. oz.", "fl oz", "fluid ounce", "lb", "pound", "oz", "ounce", "gram", " g ","pinch"};
    private static String[] quantityIngredients = {"egg white", "egg yolk", "egg", "banana", "apple","cinnamon stick", "lemon", "orange", "carrot", "yam", "sweet potato", "potato", "yeast","date", "vanilla bean"};
    public static double[] gramsForAQuantity = {30., 18., 50., 118., 100, 2., 58., 80., 61., 114., 114., 213., 7.,24., 5.};

    public static double convertPoundsToGrams(double lbs) {
        return lbs * 453.592;
    }

    public static double convertOuncesToGrams(double oz) {
        return oz * 453.592 / 16;
    }

    public static String[] getIngredientsDatabase() {
        return ingredientsDatabase;
    }

    public static String[] getVolumes() {
        return volumes;
    }

    public static String[] getQuantityIngredients() {
        return quantityIngredients;
    }

    public static String getIngredientName(String word) {
        ArrayList<String> matches = new ArrayList<>();
        String match = "";
        for (int i = 0; i < ingredientsDatabase.length; i++) {
            if (word.contains(ingredientsDatabase[i]))
                matches.add(ingredientsDatabase[i]);
        }
        if (matches.size()==0) {
            return "null";
        }else {
            match = matches.get(0);
            for (int i = 1; i < matches.size(); i++) {
                if (matches.get(i).length() > match.length()) {
                    match = matches.get(i);
                }
            }
            return match;
        }
    }

    public static double getGramsPerCupFor(String ingredient) {
        for (int i = 0; i < ingredientsDatabase.length; i++) {
            if (ingredient.contains(ingredientsDatabase[i])) {
                return gramsPerCup[i];
            }
        }
        return -1;
    }

    public static boolean isQuantityIngredient(String word) {
        for (int i = 0; i < quantityIngredients.length; i++) {
            if (word.contains(quantityIngredients[i])) {
                return true;
            }
        }
        return false;
    }

    public static double getGramsPer(String ingredient) {
        for (int i = 0; i < quantityIngredients.length; i++) {
            if (ingredient.contains(quantityIngredients[i])) {
                return gramsForAQuantity[i];
            }
        }
        return 0;
    }

    public static double convertToCups(String unitOfMeasurement) {
        if (unitOfMeasurement.equals("tsp") || unitOfMeasurement.equals("teaspoon"))
            return 1.0 / 48;
        if (unitOfMeasurement.equals("tbsp") || unitOfMeasurement.equals("tablespoon"))
            return 1.0 / 16;
        if (unitOfMeasurement.equals("cup")) return 1.0;
        if (unitOfMeasurement.equals("ml") || unitOfMeasurement.equals("milliliter"))
            return 1.0 / 237;
        if (unitOfMeasurement.equals("fl oz") || unitOfMeasurement.equals("fluid ounce") || unitOfMeasurement.equals("fl. oz."))
            return 1.0 / 8;
        if (unitOfMeasurement.equals("pinch"))
            return 1./768.;
        else return 0;
    }

    public static String getUnitOfMeasure(String word) {
        ArrayList<String> matches = new ArrayList<>();
        String match = "";
        for (int i = 0; i < volumes.length; i++) {
            if (word.contains(volumes[i]))
                matches.add(volumes[i]);
        }
        if (matches.size()==0) {
            return "null";
        }else{
            match = matches.get(0);
            for (int i = 1; i < matches.size(); i++) {
                if (matches.get(i).length()>match.length()){
                    match = matches.get(i);
                }
            }
            return match;
        }

    }
}


