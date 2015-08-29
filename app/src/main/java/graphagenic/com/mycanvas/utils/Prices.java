package graphagenic.com.mycanvas.utils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import graphagenic.com.mycanvas.customobjects.Item;

/**
 * Created by ibrahim on 6/25/15.
 */
public class Prices {
    public static ArrayList<Item> getGalleryPrices() {
        ArrayList<Item> allPrices = new ArrayList<Item>();


        LinkedHashMap<String, String> getGalleryStandardSizes = Prices.getGalleryStandardSizes();
        LinkedHashMap<String, String> getGalleryPanoramicSizes = Prices.getGalleryPanoramicSizes();
        LinkedHashMap<String, String> getGallerySquareSizes = Prices.getGallerySquareSizes();

        Item price = new Item();
        price.setSize("Standard");
        price.setPrice("Standard");
        price.setCode("Standard");
        allPrices.add(price);

        for (Map.Entry<String, String> entry : getGalleryStandardSizes.entrySet()) {
            price = new Item();
            price.setSize(entry.getKey());
            price.setPrice(entry.getValue());
            price.setCode("graphagenic.com.mycanvas.items." + entry.getKey().replaceAll("\"", "").replace("×", "_").replaceAll(" ", ""));
            if (price.getCode().equals("graphagenic.com.mycanvas.items.12_18")){
                price.setCode("graphagenic.com.mycanvas.items.12_18.");
            }
            allPrices.add(price);
        }
        price = new Item();
        price.setSize("Panoramic");
        price.setPrice("Panoramic");
        price.setCode("Panoramic");
        allPrices.add(price);
        for (Map.Entry<String, String> entry : getGalleryPanoramicSizes.entrySet()) {
            price = new Item();
            price.setSize(entry.getKey());
            price.setPrice(entry.getValue());
            price.setCode("graphagenic.com.mycanvas.items." + entry.getKey().replaceAll("\"", "").replace("×", "_").replaceAll(" ", ""));
            allPrices.add(price);
        }
        price = new Item();
        price.setSize("Square");
        price.setPrice("Square");
        price.setCode("Square");
        allPrices.add(price);
        for (Map.Entry<String, String> entry : getGallerySquareSizes.entrySet()) {
            price = new Item();
            price.setSize(entry.getKey());
            price.setPrice(entry.getValue());
            price.setCode("graphagenic.com.mycanvas.items." + entry.getKey().replaceAll("\"", "").replace("×", "_").replaceAll(" ", ""));
            allPrices.add(price);
        }

        return allPrices;
    }


    public static ArrayList<Item> getStandardPrices() {
        ArrayList<Item> allPrices = new ArrayList<Item>();


        LinkedHashMap<String, String> getStandardStandardSizes = Prices.getStandardStandardSizes();
        LinkedHashMap<String, String> getStandardPanoramicSizes = Prices.getStandardPanoramicSizes();
        LinkedHashMap<String, String> getStandardSquareSizes = Prices.getStandardSquareSizes();

        Item price = new Item();
        price.setSize("Standard");
        price.setPrice("Standard");
        price.setCode("Standard");
        allPrices.add(price);

        for (Map.Entry<String, String> entry : getStandardStandardSizes.entrySet()) {
            price = new Item();
            price.setSize(entry.getKey());
            price.setPrice(entry.getValue());
            price.setCode("graphagenic.com.items." + entry.getKey().replaceAll("\"", "").replace("×", "_").replaceAll(" ", "") + "_pan");
            allPrices.add(price);
        }
        price = new Item();
        price.setSize("Panoramic");
        price.setPrice("Panoramic");
        price.setCode("Panoramic");
        allPrices.add(price);
        for (Map.Entry<String, String> entry : getStandardPanoramicSizes.entrySet()) {
            price = new Item();
            price.setSize(entry.getKey());
            price.setPrice(entry.getValue());
            price.setCode("graphagenic.com.items." + entry.getKey().replaceAll("\"", "").replace("×", "_").replaceAll(" ", "") + "_pan");
            allPrices.add(price);
        }
        price = new Item();
        price.setSize("Square");
        price.setPrice("Square");
        price.setCode("Square");
        allPrices.add(price);
        for (Map.Entry<String, String> entry : getStandardSquareSizes.entrySet()) {
            price = new Item();
            price.setSize(entry.getKey());
            price.setPrice(entry.getValue());
            price.setCode("graphagenic.com.items." + entry.getKey().replaceAll("\"", "").replace("×", "_").replaceAll(" ", "") + "_pan");
            allPrices.add(price);
        }

        return allPrices;
    }


    public static LinkedHashMap<String, String> getGalleryStandardSizes() {
        LinkedHashMap<String, String> prices = new LinkedHashMap<String, String>();
        prices.put("8\" × 10\"", "$26");
        prices.put("11\" × 14\"", "$28");
        prices.put("12\" × 16\"", "$30");
        prices.put("12\" × 18\"", "$32");
        prices.put("12\" × 24\"", "$41");
        prices.put("16\" × 20\"", "$39");
        prices.put("16\" × 24\"", "$43");
        prices.put("18\" × 24\"", "$43");
        prices.put("18\" × 36\"", "$56");
        prices.put("20\" × 24\"", "$45");
        prices.put("20\" × 30\"", "$59");
        prices.put("24\" × 30\"", "$60");
        prices.put("24\" × 32\"", "$60");
        prices.put("24\" × 36\"", "$62");
        prices.put("24\" × 48\"", "$67");
        prices.put("30\" × 40\"", "$80");
        prices.put("32\" × 48\"", "$83");
        prices.put("36\" × 48\"", "$91");

        for (Map.Entry<String, String> entry : prices.entrySet()) {
            String price = entry.getValue();
            entry.setValue(String.valueOf("$" + Math.round(Float.valueOf(price.substring(1, price.length())))).replace(".0", ""));
        }

        return prices;
    }

    public static LinkedHashMap<String, String> getGalleryPanoramicSizes() {
        LinkedHashMap<String, String> prices = new LinkedHashMap<String, String>();
        prices.put("8\" × 24\"", "$37");
        prices.put("12\" × 36\"", "$52");
        prices.put("16\" × 48\"", "$59");

        for (Map.Entry<String, String> entry : prices.entrySet()) {
            String price = entry.getValue();
            entry.setValue(String.valueOf("$" + Math.round(Float.valueOf(price.substring(1, price.length())))).replace(".0", ""));
        }
        return prices;
    }

    public static LinkedHashMap<String, String> getGallerySquareSizes() {
        LinkedHashMap<String, String> prices = new LinkedHashMap<String, String>();
        prices.put("8\" × 8\"", "$26");
        prices.put("12\" × 12\"", "$28");
        prices.put("16\" × 16\"", "$34");
        prices.put("20\" × 20\"", "$43");
        prices.put("24\" × 24\"", "$47");
        prices.put("30\" × 30\"", "$62");
        prices.put("36\" × 36\"", "$81");

        for (Map.Entry<String, String> entry : prices.entrySet()) {
            String price = entry.getValue();
            entry.setValue(String.valueOf("$" + Math.round(Float.valueOf(price.substring(1, price.length())))).replace(".0", ""));
        }
        return prices;
    }

    public static LinkedHashMap<String, String> getStandardStandardSizes() {
        LinkedHashMap<String, String> prices = new LinkedHashMap<String, String>();
        prices.put("12\" × 16\"", "$28");
        prices.put("12\" × 18\"", "$28");
        prices.put("12\" × 24\"", "$35");
        prices.put("16\" × 20\"", "$35");
        prices.put("16\" × 24\"", "$37");
        prices.put("18\" × 24\"", "$39");
        prices.put("18\" × 36\"", "$50");
        prices.put("20\" × 24\"", "$39");
        prices.put("20\" × 30\"", "$48");
        prices.put("24\" × 30\"", "$50");
        prices.put("24\" × 36\"", "$54");

        for (Map.Entry<String, String> entry : prices.entrySet()) {
            String price = entry.getValue();
            entry.setValue(String.valueOf("$" + Math.round(Float.valueOf(price.substring(1, price.length())))).replace(".0", ""));
        }
        return prices;
    }

    public static LinkedHashMap<String, String> getStandardPanoramicSizes() {
        LinkedHashMap<String, String> prices = new LinkedHashMap<String, String>();
        prices.put("12\" × 36\"", "$46");
        for (Map.Entry<String, String> entry : prices.entrySet()) {
            String price = entry.getValue();
            entry.setValue(String.valueOf("$" + Math.round(Float.valueOf(price.substring(1, price.length())))).replace(".0", ""));
        }
        return prices;
    }

    public static LinkedHashMap<String, String> getStandardSquareSizes() {
        LinkedHashMap<String, String> prices = new LinkedHashMap<String, String>();
        prices.put("12\" × 12\"", "$26");
        prices.put("16\" × 16\"", "$28");
        prices.put("20\" × 20\"", "$37");
        prices.put("24\" × 24\"", "$41");
        prices.put("30\" × 30\"", "$54");
        for (Map.Entry<String, String> entry : prices.entrySet()) {
            String price = entry.getValue();
            entry.setValue(String.valueOf("$" + Math.round(Float.valueOf(price.substring(1, price.length())))).replace(".0", ""));
        }
        return prices;
    }
}
