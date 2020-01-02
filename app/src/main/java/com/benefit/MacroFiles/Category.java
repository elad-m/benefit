package com.benefit.MacroFiles;
import java.util.List;

public class Category {

        private List<String> names;
        private List<String> images;

        public Category() {
        }

        public Category(List<String> names, List<String> images) {
            this.names = names;
            this.images = images;
        }

        public List<String> getNames() {
            return names;
        }

        public void setNames(List<String> names) {
            this.names = names;
        }

        public List<String> getImages() {
            return images;
        }

        public void setImages(List<String> images) {
            this.images = images;
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
