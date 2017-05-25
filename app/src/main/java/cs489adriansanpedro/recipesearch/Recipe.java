package cs489adriansanpedro.recipesearch;

/**
 * Created by Adrian on 5/13/2017.
 */

class Recipe {
    private String title;
    private String url;
    private String ingredients;
    private String image;

    Recipe(){
        this.title = "";
        this.url = "";
        this.ingredients = "";
        this.image = "";
    }

    public Recipe(String title, String url, String ingredients, String image){
        this.title = title;
        this.url = url;
        this.ingredients = ingredients;
        this.image = image;
    }

    void setTitle(String title){
        this.title = title;
    }

    void setURL(String url){
        this.url = url;
    }

    void setIngredients(String ingredients){
        this.ingredients = ingredients;
    }

    void setImage(String image){
        this.image = image;
    }

    public String getImage() {
        return image;
    }

    String getTitle() {
        return title;
    }

    String getUrl() {
        return url;
    }

    String getIngredients() {
        return ingredients;
    }
}
