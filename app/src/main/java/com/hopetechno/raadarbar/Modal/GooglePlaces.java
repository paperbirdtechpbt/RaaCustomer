package com.hopetechno.raadarbar.Modal;

import java.util.List;

public class GooglePlaces {

    /**
     * predictions : [{"description":"YMCA Cross Road, Narayanguda, Hyderabad, Telangana, India","id":"e9ab8c422183a9ed3784ba54fd5f782044fe8234","matched_substrings":[{"length":4,"offset":0}],"place_id":"EjlZTUNBIENyb3NzIFJvYWQsIE5hcmF5YW5ndWRhLCBIeWRlcmFiYWQsIFRlbGFuZ2FuYSwgSW5kaWEiLiosChQKEgmHnna7xZnLOxFyApJJ0yifmhIUChIJBzJfH8SZyzsRB0YRJdQKh4g","reference":"EjlZTUNBIENyb3NzIFJvYWQsIE5hcmF5YW5ndWRhLCBIeWRlcmFiYWQsIFRlbGFuZ2FuYSwgSW5kaWEiLiosChQKEgmHnna7xZnLOxFyApJJ0yifmhIUChIJBzJfH8SZyzsRB0YRJdQKh4g","structured_formatting":{"main_text":"YMCA Cross Road","main_text_matched_substrings":[{"length":4,"offset":0}],"secondary_text":"Narayanguda, Hyderabad, Telangana, India"},"terms":[{"offset":0,"value":"YMCA Cross Road"},{"offset":17,"value":"Narayanguda"},{"offset":30,"value":"Hyderabad"},{"offset":41,"value":"Telangana"},{"offset":52,"value":"India"}],"types":["route","geocode"]},{"description":"YMCA Circle, Hari Vihar Colony, Bhawani Nagar, Narayanguda, Hyderabad, Telangana, India","id":"5a9eddb1685e94302d0413d9ae44853d640f5991","matched_substrings":[{"length":4,"offset":0}],"place_id":"EldZTUNBIENpcmNsZSwgSGFyaSBWaWhhciBDb2xvbnksIEJoYXdhbmkgTmFnYXIsIE5hcmF5YW5ndWRhLCBIeWRlcmFiYWQsIFRlbGFuZ2FuYSwgSW5kaWEiLiosChQKEgmHnna7xZnLOxF0W0r6zwGYBRIUChIJIxTMRsSZyzsRJ3lAGsIO4gY","reference":"EldZTUNBIENpcmNsZSwgSGFyaSBWaWhhciBDb2xvbnksIEJoYXdhbmkgTmFnYXIsIE5hcmF5YW5ndWRhLCBIeWRlcmFiYWQsIFRlbGFuZ2FuYSwgSW5kaWEiLiosChQKEgmHnna7xZnLOxF0W0r6zwGYBRIUChIJIxTMRsSZyzsRJ3lAGsIO4gY","structured_formatting":{"main_text":"YMCA Circle","main_text_matched_substrings":[{"length":4,"offset":0}],"secondary_text":"Hari Vihar Colony, Bhawani Nagar, Narayanguda, Hyderabad, Telangana, India"},"terms":[{"offset":0,"value":"YMCA Circle"},{"offset":13,"value":"Hari Vihar Colony"},{"offset":32,"value":"Bhawani Nagar"},{"offset":47,"value":"Narayanguda"},{"offset":60,"value":"Hyderabad"},{"offset":71,"value":"Telangana"},{"offset":82,"value":"India"}],"types":["route","geocode"]},{"description":"YMCA University of Science and Technology, Mathura Road, Sector 6, Faridabad, Haryana, India","id":"438b825fcd0724b98b964bafd91038b0e36d8ef1","matched_substrings":[{"length":4,"offset":0}],"place_id":"ChIJV_Xp9nHcDDkRF4Xxn-weMOs","reference":"ChIJV_Xp9nHcDDkRF4Xxn-weMOs","structured_formatting":{"main_text":"YMCA University of Science and Technology","main_text_matched_substrings":[{"length":4,"offset":0}],"secondary_text":"Mathura Road, Sector 6, Faridabad, Haryana, India"},"terms":[{"offset":0,"value":"YMCA University of Science and Technology"},{"offset":43,"value":"Mathura Road"},{"offset":57,"value":"Sector 6"},{"offset":67,"value":"Faridabad"},{"offset":78,"value":"Haryana"},{"offset":87,"value":"India"}],"types":["university","point_of_interest","establishment"]},{"description":"YMCA Cross Road, Kozhikode, Kerala, India","id":"2477b73f2447bf82e4a462c1a870747382db1095","matched_substrings":[{"length":4,"offset":0}],"place_id":"EilZTUNBIENyb3NzIFJvYWQsIEtvemhpa29kZSwgS2VyYWxhLCBJbmRpYSIuKiwKFAoSCXcM9yY3WaY7EeLozDKg1bofEhQKEglHRz1WOFmmOxGr7DLKQQYVMg","reference":"EilZTUNBIENyb3NzIFJvYWQsIEtvemhpa29kZSwgS2VyYWxhLCBJbmRpYSIuKiwKFAoSCXcM9yY3WaY7EeLozDKg1bofEhQKEglHRz1WOFmmOxGr7DLKQQYVMg","structured_formatting":{"main_text":"YMCA Cross Road","main_text_matched_substrings":[{"length":4,"offset":0}],"secondary_text":"Kozhikode, Kerala, India"},"terms":[{"offset":0,"value":"YMCA Cross Road"},{"offset":17,"value":"Kozhikode"},{"offset":28,"value":"Kerala"},{"offset":36,"value":"India"}],"types":["route","geocode"]},{"description":"YMCA Road, Agripada, Mumbai, Maharashtra, India","id":"4a47c58188c5ec437bd0269d2bc6cc4df92833ad","matched_substrings":[{"length":4,"offset":0}],"place_id":"Ei9ZTUNBIFJvYWQsIEFncmlwYWRhLCBNdW1iYWksIE1haGFyYXNodHJhLCBJbmRpYSIuKiwKFAoSCS85Um5pzuc7ESj3bvUXhAN-EhQKEgnFXFYPaM7nOxHjWhQEd2eQUA","reference":"Ei9ZTUNBIFJvYWQsIEFncmlwYWRhLCBNdW1iYWksIE1haGFyYXNodHJhLCBJbmRpYSIuKiwKFAoSCS85Um5pzuc7ESj3bvUXhAN-EhQKEgnFXFYPaM7nOxHjWhQEd2eQUA","structured_formatting":{"main_text":"YMCA Road","main_text_matched_substrings":[{"length":4,"offset":0}],"secondary_text":"Agripada, Mumbai, Maharashtra, India"},"terms":[{"offset":0,"value":"YMCA Road"},{"offset":11,"value":"Agripada"},{"offset":21,"value":"Mumbai"},{"offset":29,"value":"Maharashtra"},{"offset":42,"value":"India"}],"types":["route","geocode"]}]
     * status : OK
     */

    private String status;
    private List<PredictionsBean> predictions;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<PredictionsBean> getPredictions() {
        return predictions;
    }

    public void setPredictions(List<PredictionsBean> predictions) {
        this.predictions = predictions;
    }

    public static class PredictionsBean {
        /**
         * description : YMCA Cross Road, Narayanguda, Hyderabad, Telangana, India
         * id : e9ab8c422183a9ed3784ba54fd5f782044fe8234
         * matched_substrings : [{"length":4,"offset":0}]
         * place_id : EjlZTUNBIENyb3NzIFJvYWQsIE5hcmF5YW5ndWRhLCBIeWRlcmFiYWQsIFRlbGFuZ2FuYSwgSW5kaWEiLiosChQKEgmHnna7xZnLOxFyApJJ0yifmhIUChIJBzJfH8SZyzsRB0YRJdQKh4g
         * reference : EjlZTUNBIENyb3NzIFJvYWQsIE5hcmF5YW5ndWRhLCBIeWRlcmFiYWQsIFRlbGFuZ2FuYSwgSW5kaWEiLiosChQKEgmHnna7xZnLOxFyApJJ0yifmhIUChIJBzJfH8SZyzsRB0YRJdQKh4g
         * structured_formatting : {"main_text":"YMCA Cross Road","main_text_matched_substrings":[{"length":4,"offset":0}],"secondary_text":"Narayanguda, Hyderabad, Telangana, India"}
         * terms : [{"offset":0,"value":"YMCA Cross Road"},{"offset":17,"value":"Narayanguda"},{"offset":30,"value":"Hyderabad"},{"offset":41,"value":"Telangana"},{"offset":52,"value":"India"}]
         * types : ["route","geocode"]
         */

        private String description;
        private String id;
        private String place_id;
        private String reference;
        private StructuredFormattingBean structured_formatting;
        private List<MatchedSubstringsBean> matched_substrings;
        private List<TermsBean> terms;
        private List<String> types;

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getPlace_id() {
            return place_id;
        }

        public void setPlace_id(String place_id) {
            this.place_id = place_id;
        }

        public String getReference() {
            return reference;
        }

        public void setReference(String reference) {
            this.reference = reference;
        }

        public StructuredFormattingBean getStructured_formatting() {
            return structured_formatting;
        }

        public void setStructured_formatting(StructuredFormattingBean structured_formatting) {
            this.structured_formatting = structured_formatting;
        }

        public List<MatchedSubstringsBean> getMatched_substrings() {
            return matched_substrings;
        }

        public void setMatched_substrings(List<MatchedSubstringsBean> matched_substrings) {
            this.matched_substrings = matched_substrings;
        }

        public List<TermsBean> getTerms() {
            return terms;
        }

        public void setTerms(List<TermsBean> terms) {
            this.terms = terms;
        }

        public List<String> getTypes() {
            return types;
        }

        public void setTypes(List<String> types) {
            this.types = types;
        }

        public static class StructuredFormattingBean {
            /**
             * main_text : YMCA Cross Road
             * main_text_matched_substrings : [{"length":4,"offset":0}]
             * secondary_text : Narayanguda, Hyderabad, Telangana, India
             */

            private String main_text;
            private String secondary_text;
            private List<MainTextMatchedSubstringsBean> main_text_matched_substrings;

            public String getMain_text() {
                return main_text;
            }

            public void setMain_text(String main_text) {
                this.main_text = main_text;
            }

            public String getSecondary_text() {
                return secondary_text;
            }

            public void setSecondary_text(String secondary_text) {
                this.secondary_text = secondary_text;
            }

            public List<MainTextMatchedSubstringsBean> getMain_text_matched_substrings() {
                return main_text_matched_substrings;
            }

            public void setMain_text_matched_substrings(List<MainTextMatchedSubstringsBean> main_text_matched_substrings) {
                this.main_text_matched_substrings = main_text_matched_substrings;
            }

            public static class MainTextMatchedSubstringsBean {
                /**
                 * length : 4
                 * offset : 0
                 */

                private int length;
                private int offset;

                public int getLength() {
                    return length;
                }

                public void setLength(int length) {
                    this.length = length;
                }

                public int getOffset() {
                    return offset;
                }

                public void setOffset(int offset) {
                    this.offset = offset;
                }
            }
        }

        public static class MatchedSubstringsBean {
            /**
             * length : 4
             * offset : 0
             */

            private int length;
            private int offset;

            public int getLength() {
                return length;
            }

            public void setLength(int length) {
                this.length = length;
            }

            public int getOffset() {
                return offset;
            }

            public void setOffset(int offset) {
                this.offset = offset;
            }
        }

        public static class TermsBean {
            /**
             * offset : 0
             * value : YMCA Cross Road
             */

            private int offset;
            private String value;

            public int getOffset() {
                return offset;
            }

            public void setOffset(int offset) {
                this.offset = offset;
            }

            public String getValue() {
                return value;
            }

            public void setValue(String value) {
                this.value = value;
            }
        }
    }
}
