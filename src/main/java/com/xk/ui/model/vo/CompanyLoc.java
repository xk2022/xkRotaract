package com.xk.ui.model.vo;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * UpmsUser SaveResp
 * Created by yuan on 2024/08/05
 *
 * CompanyLoc 类用于表示各种类型的地点信息。
 */
@Data
public class CompanyLoc {

    private Map<String, List<Location>> locations;

    public Map<String, List<Location>> getLocations() {
        return locations;
    }

    public void setLocations(Map<String, List<Location>> locations) {
        this.locations = locations;
    }

    /**
     * Location 类表示地点的详细信息。
     */
    public static class Location {
        private String locId;
        private String name;
        private String description;
        private String lat;
        private String lng;

        // Getters and Setters
        public String getLocId() {
            return locId;
        }

        public void setLocId(String locId) {
            this.locId = locId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getLat() {
            return lat;
        }

        public void setLat(String lat) {
            this.lat = lat;
        }

        public String getLng() {
            return lng;
        }

        public void setLng(String lng) {
            this.lng = lng;
        }
    }
}
