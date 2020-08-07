package edu.ucsb.kevinzhang421.flourtreats;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;

public class DataLoader {
    private ArrayList<Recipe> recipes = new ArrayList<>();
    private int k;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public DataLoader(int k) {
        this.k = k;
    }

    public ArrayList<Recipe> getRecipes() {
        return recipes;
    }

//    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
//    private void readDataFrom(String filepath) {
//        try (Scanner sc = new Scanner(new File(filepath))) {
//            while (sc.hasNext()) {
//                String line = sc.nextLine();
//                String[] ingredients = line.split(",");
//                Recipe recipe = new Recipe();
//                recipe.setName(ingredients[0]);
//                for (int i = 1; i < ingredients.length; i++) {
//                    Ingredient ingredient = new Ingredient(ingredients[i]);
//                    String ingredientName = ingredient.getIngredient();
//                    if (ingredientName.equals("")) break;
//                    recipe.addIngredient(ingredient);
//                }
//                recipe.calculateRatios();
//                recipes.add(recipe);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    public static Recipe convertStringToRecipe(String listOfIngredients) {
        listOfIngredients = Ingredient.removeTextSurroundedBy("(", ")", listOfIngredients);
        listOfIngredients = Ingredient.removeTextSurroundedBy("[", "]", listOfIngredients);
        listOfIngredients = Ingredient.removeTextSurroundedBy("{", "}", listOfIngredients);
        listOfIngredients = Ingredient.removeTextSurroundedBy("<", ">", listOfIngredients);

        String[] ingredients = listOfIngredients.split(",");
        Recipe recipe = new Recipe();
        for (int i = 0; i < ingredients.length; i++) {
            Ingredient ingredient = new Ingredient(ingredients[i]);
            String ingredientName = ingredient.getIngredient();
            if (!ingredientName.equals("")) {
                recipe.addIngredient(ingredient);
            }
        }
        recipe.calculateRatios();
        return recipe;
    }

    public String getClosestRecipeName(Recipe unknown) {
        double smallestDist = getDistBtw(unknown, recipes.get(0));
        int indexOfClosestRecipe = 0;
        for (int i = 1; i < recipes.size(); i++) {
            double dist = getDistBtw(unknown, recipes.get(i));
            if (dist < smallestDist) {
                smallestDist = dist;
                indexOfClosestRecipe = i;
            }
        }
        return recipes.get(indexOfClosestRecipe).getName();
    }

    public ArrayList<Recipe> getKNearestNeighborRecipes(Recipe unknown) {
        ArrayList<Recipe> kNearestRecipes = new ArrayList<>();
        kNearestRecipes.add(recipes.get(0));

        for (int i = 1; i < recipes.size(); i++) {
            int indexOfUnknownRecipeInKNearestRecipes = -1;
            double distBtwUnknownAndRecipe = getDistBtw(unknown, recipes.get(i));
            for (int j = kNearestRecipes.size() - 1; j >= 0; j--) {
                double distBtwUnknownAndKNearestRecipes = getDistBtw(unknown, kNearestRecipes.get(j));
                if (distBtwUnknownAndRecipe < distBtwUnknownAndKNearestRecipes) {
                    indexOfUnknownRecipeInKNearestRecipes = j;
                }
            }
            if (indexOfUnknownRecipeInKNearestRecipes != -1) {
                kNearestRecipes.add(indexOfUnknownRecipeInKNearestRecipes, recipes.get(i));
            } else if (kNearestRecipes.size() < k) {
                kNearestRecipes.add(recipes.get(i));
            }
        }

        while (kNearestRecipes.size() > k) {
            kNearestRecipes.remove(k);
        }
        return sortByOccurrences(kNearestRecipes);
    }

    private ArrayList<Recipe> sortByOccurrences(ArrayList<Recipe> kNearestRecipes) {
        ArrayList<String> names = new ArrayList<>();
        ArrayList<Integer> counts = new ArrayList<>();
        setCountsOfNameOccurrences(names, counts, kNearestRecipes);

        ArrayList<Recipe> sortedByOccurrences = new ArrayList<>();
        while (counts.size() > 0) {
            int max = counts.get(0);
            int indexOfMax = 0;
            for (int j = 1; j < counts.size(); j++) {
                int count = counts.get(j);
                if (count > max) {
                    max = count;
                    indexOfMax = j;
                }
            }
            sortedByOccurrences.add(getRecipeByName(names.get(indexOfMax), kNearestRecipes));

            names.remove(indexOfMax);
            counts.remove(indexOfMax);
        }
        return sortedByOccurrences;
    }

    private Recipe getRecipeByName(String name, ArrayList<Recipe> kNearestRecipes) {
        for (int i = 0; i < kNearestRecipes.size(); i++) {
            if (kNearestRecipes.get(i).getName().equals(name)) {
                return kNearestRecipes.get(i);
            }
        }
        return new Recipe();
    }

    private void setCountsOfNameOccurrences(ArrayList<String> names, ArrayList<Integer> counts, ArrayList<Recipe> kNearestRecipes) {
        for (int i = 0; i < kNearestRecipes.size(); i++) {
            Recipe r = kNearestRecipes.get(i);
            if (!names.contains(r.getName())) {
                names.add(r.getName());
                counts.add(1);
            } else {
                int j = names.indexOf(r.getName());
                int newCount = counts.get(j) + 1;
                counts.set(j, (Integer) newCount);
            }
        }
    }

    private double getDistBtw(Recipe r1, Recipe r2) {
        double sumOfSquaredDifference = 0;
        Ingredient[] ingredients1 = r1.getIngredients();
        Ingredient[] ingredients2 = r2.getIngredients();
        for (int i = 0; i < ingredients1.length; i++) {
            if (ingredients1[i] != null && ingredients2[i] == null) {
                sumOfSquaredDifference += ingredients1[i].getRatio() * ingredients1[i].getRatio();
            } else if (ingredients1[i] == null && ingredients2[i] != null) {
                sumOfSquaredDifference += ingredients2[i].getRatio() * ingredients2[i].getRatio();
            } else if (ingredients1[i] != null && ingredients2[i] != null) {
                sumOfSquaredDifference += (ingredients2[i].getRatio() - ingredients1[i].getRatio()) * (ingredients2[i].getRatio() - ingredients1[i].getRatio());
            }
        }
        return sumOfSquaredDifference;
    }

    public void addRecipe(Recipe r) {
        recipes.add(r);
    }


}
