package com.eventx.wikiment.networkModels;

import java.util.ArrayList;

public class SearchResult {
    private Query query;

    public Query getQuery() {
        return query;
    }

    public void setQuery(Query query) {
        this.query = query;
    }

    public SearchResult(Query query) {

        this.query = query;
    }

    public class Query {
        private ArrayList<Pages> pages;

        public ArrayList<Pages> getPages() {
            return pages;
        }

        public void setPages(ArrayList<Pages> pages) {
            this.pages = pages;
        }

        public Query(ArrayList<Pages> pages) {

            this.pages = pages;
        }

        public class Pages {
            private long pageid;
            private String title;
            private Thumbnail thumbnail;
            private Terms terms;

            public long getPageid() {
                return pageid;
            }

            public void setPageid(long pageid) {
                this.pageid = pageid;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public Thumbnail getThumbnail() {
                return thumbnail;
            }

            public void setThumbnail(Thumbnail thumbnail) {
                this.thumbnail = thumbnail;
            }

            public Terms getTerms() {
                return terms;
            }

            public void setTerms(Terms terms) {
                this.terms = terms;
            }

            public Pages(long pageid, String title, Thumbnail thumbnail, Terms terms) {

                this.pageid = pageid;
                this.title = title;
                this.thumbnail = thumbnail;
                this.terms = terms;
            }

            public class Terms {
                private ArrayList<String> description;


                public ArrayList<String> getDescription() {
                    return description;
                }

                public void setDescription(ArrayList<String> description) {
                    this.description = description;
                }

                public Terms(ArrayList<String> description) {

                    this.description = description;
                }
            }

            public class Thumbnail {
                private String source;

                public String getSource() {
                    return source;
                }

                public void setSource(String source) {
                    this.source = source;
                }

                public Thumbnail(String source) {

                    this.source = source;
                }
            }
        }
    }
}
