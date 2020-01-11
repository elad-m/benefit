package com.benefit.MacroFiles;
import java.util.List;

public class Category {

        private String name;
        private String image;

        public Category() {
        }

        public Category(String name, String images) {
            this.name = name;
            this.image = image;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getImages() {
            return image;
        }

        public void setImages(String images) {
            this.image = image;
        }
//
//        public List<Crust> getCrusts() {
//            return crusts;
//        }
//
//        public void setCrusts(List<Crust> crusts) {
//            this.crusts = crusts;
//        }
//    }

}
