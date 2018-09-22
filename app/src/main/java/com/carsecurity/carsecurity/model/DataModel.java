package com.carsecurity.carsecurity.model;

import java.util.List;

public class DataModel {


    /**
     * channel : {"id":510399,"name":"GPS_IR sensor based ESP8266","latitude":"0.0","longitude":"0.0","field1":"latitude","field2":"longitude","field3":"sensordata","created_at":"2018-06-04T07:06:27Z","updated_at":"2018-06-21T18:51:19Z","last_entry_id":660}
     * feeds : [{"created_at":"2018-06-21T12:39:34Z","entry_id":659,"field1":"29.99402","field2":"31.13314","field3":null},{"created_at":"2018-06-21T18:51:19Z","entry_id":660,"field1":null,"field2":null,"field3":"1"}]
     */

    private ChannelBean channel;
    private List<FeedsBean> feeds;

    public ChannelBean getChannel() {
        return channel;
    }

    public void setChannel(ChannelBean channel) {
        this.channel = channel;
    }

    public List<FeedsBean> getFeeds() {
        return feeds;
    }

    public void setFeeds(List<FeedsBean> feeds) {
        this.feeds = feeds;
    }

    public static class ChannelBean {
        /**
         * id : 510399
         * name : GPS_IR sensor based ESP8266
         * latitude : 0.0
         * longitude : 0.0
         * field1 : latitude
         * field2 : longitude
         * field3 : sensordata
         * created_at : 2018-06-04T07:06:27Z
         * updated_at : 2018-06-21T18:51:19Z
         * last_entry_id : 660
         */

        private int id;
        private String name;
        private String latitude;
        private String longitude;
        private String field1;
        private String field2;
        private String field3;
        private String created_at;
        private String updated_at;
        private int last_entry_id;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getLatitude() {
            return latitude;
        }

        public void setLatitude(String latitude) {
            this.latitude = latitude;
        }

        public String getLongitude() {
            return longitude;
        }

        public void setLongitude(String longitude) {
            this.longitude = longitude;
        }

        public String getField1() {
            return field1;
        }

        public void setField1(String field1) {
            this.field1 = field1;
        }

        public String getField2() {
            return field2;
        }

        public void setField2(String field2) {
            this.field2 = field2;
        }

        public String getField3() {
            return field3;
        }

        public void setField3(String field3) {
            this.field3 = field3;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public String getUpdated_at() {
            return updated_at;
        }

        public void setUpdated_at(String updated_at) {
            this.updated_at = updated_at;
        }

        public int getLast_entry_id() {
            return last_entry_id;
        }

        public void setLast_entry_id(int last_entry_id) {
            this.last_entry_id = last_entry_id;
        }
    }

    public static class FeedsBean {
        /**
         * created_at : 2018-06-21T12:39:34Z
         * entry_id : 659
         * field1 : 29.99402
         * field2 : 31.13314
         * field3 : null
         */

        private String created_at;
        private int entry_id;
        private String field1;
        private String field2;
        private Object field3;

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public int getEntry_id() {
            return entry_id;
        }

        public void setEntry_id(int entry_id) {
            this.entry_id = entry_id;
        }

        public String getField1() {
            return field1;
        }

        public void setField1(String field1) {
            this.field1 = field1;
        }

        public String getField2() {
            return field2;
        }

        public void setField2(String field2) {
            this.field2 = field2;
        }

        public Object getField3() {
            return field3;
        }

        public void setField3(Object field3) {
            this.field3 = field3;
        }
    }
}
