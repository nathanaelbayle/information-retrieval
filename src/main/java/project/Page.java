package project;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This class is used to represent a Wikipedia page
 *
 * @author Nathanaël Bayle
 */
public class Page implements java.io.Serializable {
    private String title;
    private String text;

    private String category;

    private int date;

    private List<String> mainIngredients = new ArrayList<>();

    public Page(){
        this.title = "";
        this.text = "";
    }

    public Page(String title, String text) {
        this.title = title;
        this.text = text;
    }

    public void addIngredient(List<String> ingredients){
        this.mainIngredients.addAll(ingredients);
    }

    public String getTitle(){
        return this.title;
    }

    public String getText(){
        return this.text;
    }
    public void setTitle(String title){
        this.title = title.toLowerCase();
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setCategory(String category) {
        this.category = category.toLowerCase();
    }

    public String getCategory() {
        return this.category;
    }

    public void addIngredient(String ingredient) {
        this.mainIngredients.add(ingredient.toLowerCase());
    }

    public List<String> getMainIngredients() {
        return this.mainIngredients;
    }

    public String getMainIngredientsAsString() {
        return String.join(", ", this.mainIngredients);
    }

    public void setDate(int date) {
        this.date = date;
    }

    public int getDate() {
        return this.date;
    }

    public String toString() {
        return this.title + "\t\t" + this.category + "\t\t"+ this.mainIngredients + "\n";
    }
}