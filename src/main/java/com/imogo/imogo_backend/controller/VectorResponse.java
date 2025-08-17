package com.imogo.imogo_backend.controller;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class VectorResponse {

    private Data data;

    public Data getData() { return data; }
    public void setData(Data data) { this.data = data; }

    public static class Data {
        @JsonProperty("Get")
        private Get get;

        public Get getGet() { return get; }
        public void setGet(Get get) { this.get = get; }
    }

    public static class Get {
        @JsonProperty("Property")
        private List<Property> property;

        public List<Property> getProperty() { return property; }
        public void setProperty(List<Property> property) { this.property = property; }
    }

    public static class Property {
        @JsonProperty("_additional")
        private Additional additional;
        private int bedrooms;
        private String city;
        private String description;
        private String district;
        private int price;
        private String propertyType;
        private String title;

        // getters e setters
        public Additional getAdditional() { return additional; }
        public void setAdditional(Additional additional) { this.additional = additional; }

        public int getBedrooms() { return bedrooms; }
        public void setBedrooms(int bedrooms) { this.bedrooms = bedrooms; }

        public String getCity() { return city; }
        public void setCity(String city) { this.city = city; }

        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }

        public String getDistrict() { return district; }
        public void setDistrict(String district) { this.district = district; }

        public int getPrice() { return price; }
        public void setPrice(int price) { this.price = price; }

        public String getPropertyType() { return propertyType; }
        public void setPropertyType(String propertyType) { this.propertyType = propertyType; }

        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }
    }

    public static class Additional {
        private double certainty;
        private String id;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public double getCertainty() { return certainty; }
        public void setCertainty(double certainty) { this.certainty = certainty; }
    }
}
