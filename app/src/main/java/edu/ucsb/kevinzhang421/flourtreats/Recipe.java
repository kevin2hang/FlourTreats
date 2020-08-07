package edu.ucsb.kevinzhang421.flourtreats;

public class Recipe {
    private String name;
    private double totalWeightInGrams = 0;
    private Ingredient[] ingredients = new Ingredient[Conversions.getIngredientsDatabase().length];
    private String URL;


    public Recipe() {
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addIngredient(Ingredient ingredient) {
        if (!ingredient.getIngredient().equals("")) {
            String ingredientName = ingredient.getIngredient();
            for (int i = 0; i < ingredients.length; i++) {
                if (ingredientName.equals(Conversions.getIngredientsDatabase()[i])) {
                    if (ingredients[i] == null) ingredients[i] = ingredient;
                    else ingredients[i].addGrams(ingredient.getGrams());
                }
            }
            totalWeightInGrams += ingredient.getGrams();
        }
    }

    public Ingredient[] getIngredients() {
        return ingredients;
    }

    public void calculateRatios() {
        for (int i = 0; i < ingredients.length; i++) {
            if (ingredients[i] != null) {
                Ingredient ingredient = ingredients[i];
                double ratio = ingredient.getGrams() / totalWeightInGrams;
                ingredient.setRatio(ratio);
            }
        }
    }

}

