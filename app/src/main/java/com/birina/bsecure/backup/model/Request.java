package com.birina.bsecure.backup.model;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 3/21/2018.
 */

public class Request {


    /**
     * mobile : 9599385903
     * contact : [{"emails":[{"address":"ajarya55t@yahoo.in","type":"Other"}],"id":"1","name":"amar jarya","numbers":[{"number":"+917987697490","type":"Mobile"}]}]
     * sms : [{"msg":"","readState":"","id":"","address":"","folderName":"","time":""}]
     */

    private String mobile;
    private List<ContactBean> contact;
    private List<SmsBean> sms;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public List<ContactBean> getContact() {
        return contact;
    }

    public void setContact(List<ContactBean> contact) {
        this.contact = contact;
    }

    public List<SmsBean> getSms() {
        return sms;
    }

    public void setSms(List<SmsBean> sms) {
        this.sms = sms;
    }

    public static class ContactBean implements Comparable<ContactBean>  {
        /**
         * emails : [{"address":"ajarya55t@yahoo.in","type":"Other"}]
         * id : 1
         * name : amar jarya
         * numbers : [{"number":"+917987697490","type":"Mobile"}]
         */

        private String id;
        private String name;
        private List<EmailsBean> emails;
        private List<NumbersBean> numbers;


        public ContactBean(String id, String name) {
            this.id = id;
            this.name = name;
            this.emails = new ArrayList<EmailsBean>();
            this.numbers = new ArrayList<NumbersBean>();
        }


        public void addNumber(String number, String type) {
            numbers.add(new NumbersBean(number, type));
        }

        public void addEmail(String address, String type) {
            emails.add(new EmailsBean(address, type));
        }


        @Override
        public int compareTo(@NonNull ContactBean contact) {
            return this.name.compareTo(contact.name);

        }


        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<EmailsBean> getEmails() {
            return emails;
        }

        public void setEmails(List<EmailsBean> emails) {
            this.emails = emails;
        }

        public List<NumbersBean> getNumbers() {
            return numbers;
        }

        public void setNumbers(List<NumbersBean> numbers) {
            this.numbers = numbers;
        }

        public static class EmailsBean {
            /**
             * address : ajarya55t@yahoo.in
             * type : Other
             */

            private String address;
            private String type;

            public EmailsBean(String address, String type) {
                this.address = address;
                this.type = type;
            }

            public String getAddress() {
                return address;
            }

            public void setAddress(String address) {
                this.address = address;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }
        }

        public static class NumbersBean {
            /**
             * number : +917987697490
             * type : Mobile
             */

            private String number;
            private String type;


            public NumbersBean(String number, String type) {
                this.number = number;
                this.type = type;
            }

            public String getNumber() {
                return number;
            }

            public void setNumber(String number) {
                this.number = number;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }
        }
    }

    public static class SmsBean {
        /**
         * msg :
         * readState :
         * id :
         * address :
         * folderName :
         * time :
         */

        private String msg;
        private String readState;
        private String id;
        private String address;
        private String folderName;
        private String time;

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public String getReadState() {
            return readState;
        }

        public void setReadState(String readState) {
            this.readState = readState;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getFolderName() {
            return folderName;
        }

        public void setFolderName(String folderName) {
            this.folderName = folderName;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }
    }
}
