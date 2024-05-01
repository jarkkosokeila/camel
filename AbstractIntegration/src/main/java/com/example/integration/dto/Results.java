package com.example.integration.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.Date;

public class Results {

    @JsonProperty("results")
    public ArrayList<Person> getResults() {
        return this.people;
    }

    public void setResults(ArrayList<Person> people) {
        this.people = people;
    }

    ArrayList<Person> people;

    @JsonProperty("info")
    public Info getInfo() {
        return this.info;
    }

    public void setInfo(Info info) {
        this.info = info;
    }

    Info info;


    // import com.fasterxml.jackson.databind.ObjectMapper; // version 2.11.1
// import com.fasterxml.jackson.annotation.JsonProperty; // version 2.11.1
/* ObjectMapper om = new ObjectMapper();
Root root = om.readValue(myJsonString, Root.class); */
    public static class Coordinates {
        @JsonProperty("latitude")
        public String getLatitude() {
            return this.latitude;
        }

        public void setLatitude(String latitude) {
            this.latitude = latitude;
        }

        String latitude;

        @JsonProperty("longitude")
        public String getLongitude() {
            return this.longitude;
        }

        public void setLongitude(String longitude) {
            this.longitude = longitude;
        }

        String longitude;

        @Override
        public String toString() {
            return "Coordinates{" +
                    "latitude='" + latitude + '\'' +
                    ", longitude='" + longitude + '\'' +
                    '}';
        }
    }

    public static class Dob {
        @JsonProperty("date")
        public Date getDate() {
            return this.date;
        }

        public void setDate(Date date) {
            this.date = date;
        }

        Date date;

        @JsonProperty("age")
        public int getAge() {
            return this.age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        int age;

        @Override
        public String toString() {
            return "Dob{" +
                    "date=" + date +
                    ", age=" + age +
                    '}';
        }
    }

    public static class Id {
        @JsonProperty("name")
        public String getName() {
            return this.name;
        }

        public void setName(String name) {
            this.name = name;
        }

        String name;

        @JsonProperty("value")
        public String getValue() {
            return this.value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        String value;
    }

    public static class Info {
        @JsonProperty("seed")
        public String getSeed() {
            return this.seed;
        }

        public void setSeed(String seed) {
            this.seed = seed;
        }

        String seed;

        @JsonProperty("results")
        public int getResults() {
            return this.results;
        }

        public void setResults(int results) {
            this.results = results;
        }

        int results;

        @JsonProperty("page")
        public int getPage() {
            return this.page;
        }

        public void setPage(int page) {
            this.page = page;
        }

        int page;

        @JsonProperty("version")
        public String getVersion() {
            return this.version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        String version;

        @Override
        public String toString() {
            return "Info{" +
                    "seed='" + seed + '\'' +
                    ", results=" + results +
                    ", page=" + page +
                    ", version='" + version + '\'' +
                    '}';
        }
    }

    public static class Location {
        @JsonProperty("street")
        public Street getStreet() {
            return this.street;
        }

        public void setStreet(Street street) {
            this.street = street;
        }

        Street street;

        @JsonProperty("city")
        public String getCity() {
            return this.city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        String city;

        @JsonProperty("state")
        public String getState() {
            return this.state;
        }

        public void setState(String state) {
            this.state = state;
        }

        String state;

        @JsonProperty("country")
        public String getCountry() {
            return this.country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        String country;

        @JsonProperty("postcode")
        public String getPostcode() {
            return this.postcode;
        }

        public void setPostcode(String postcode) {
            this.postcode = postcode;
        }

        String postcode;

        @JsonProperty("coordinates")
        public Coordinates getCoordinates() {
            return this.coordinates;
        }

        public void setCoordinates(Coordinates coordinates) {
            this.coordinates = coordinates;
        }

        Coordinates coordinates;

        @JsonProperty("timezone")
        public Timezone getTimezone() {
            return this.timezone;
        }

        public void setTimezone(Timezone timezone) {
            this.timezone = timezone;
        }

        Timezone timezone;

        @Override
        public String toString() {
            return "Location{" +
                    "street=" + street +
                    ", city='" + city + '\'' +
                    ", state='" + state + '\'' +
                    ", country='" + country + '\'' +
                    ", postcode='" + postcode + '\'' +
                    ", coordinates=" + coordinates +
                    ", timezone=" + timezone +
                    '}';
        }
    }

    public static class Login {
        @JsonProperty("uuid")
        public String getUuid() {
            return this.uuid;
        }

        public void setUuid(String uuid) {
            this.uuid = uuid;
        }

        String uuid;

        @JsonProperty("username")
        public String getUsername() {
            return this.username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        String username;

        @JsonProperty("password")
        public String getPassword() {
            return this.password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        String password;

        @JsonProperty("salt")
        public String getSalt() {
            return this.salt;
        }

        public void setSalt(String salt) {
            this.salt = salt;
        }

        String salt;

        @JsonProperty("md5")
        public String getMd5() {
            return this.md5;
        }

        public void setMd5(String md5) {
            this.md5 = md5;
        }

        String md5;

        @JsonProperty("sha1")
        public String getSha1() {
            return this.sha1;
        }

        public void setSha1(String sha1) {
            this.sha1 = sha1;
        }

        String sha1;

        @JsonProperty("sha256")
        public String getSha256() {
            return this.sha256;
        }

        public void setSha256(String sha256) {
            this.sha256 = sha256;
        }

        String sha256;

        @Override
        public String toString() {
            return "Login{" +
                    "uuid='" + uuid + '\'' +
                    ", username='" + username + '\'' +
                    ", password='" + password + '\'' +
                    ", salt='" + salt + '\'' +
                    ", md5='" + md5 + '\'' +
                    ", sha1='" + sha1 + '\'' +
                    ", sha256='" + sha256 + '\'' +
                    '}';
        }
    }

    public static class Name {
        @JsonProperty("title")
        public String getTitle() {
            return this.title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        String title;

        @JsonProperty("first")
        public String getFirst() {
            return this.first;
        }

        public void setFirst(String first) {
            this.first = first;
        }

        String first;

        @JsonProperty("last")
        public String getLast() {
            return this.last;
        }

        public void setLast(String last) {
            this.last = last;
        }

        String last;

        @Override
        public String toString() {
            return "Name{" +
                    "title='" + title + '\'' +
                    ", first='" + first + '\'' +
                    ", last='" + last + '\'' +
                    '}';
        }
    }

    public static class Picture {
        @JsonProperty("large")
        public String getLarge() {
            return this.large;
        }

        public void setLarge(String large) {
            this.large = large;
        }

        String large;

        @JsonProperty("medium")
        public String getMedium() {
            return this.medium;
        }

        public void setMedium(String medium) {
            this.medium = medium;
        }

        String medium;

        @JsonProperty("thumbnail")
        public String getThumbnail() {
            return this.thumbnail;
        }

        public void setThumbnail(String thumbnail) {
            this.thumbnail = thumbnail;
        }

        String thumbnail;
    }

    public static class Registered {
        @JsonProperty("date")
        public Date getDate() {
            return this.date;
        }

        public void setDate(Date date) {
            this.date = date;
        }

        Date date;

        @JsonProperty("age")
        public int getAge() {
            return this.age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        int age;

        @Override
        public String toString() {
            return "Registered{" +
                    "date=" + date +
                    ", age=" + age +
                    '}';
        }
    }

    public static class Person {
        @JsonProperty("gender")
        public String getGender() {
            return this.gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        String gender;

        @JsonProperty("name")
        public Name getName() {
            return this.name;
        }

        public void setName(Name name) {
            this.name = name;
        }

        Name name;

        @JsonProperty("location")
        public Location getLocation() {
            return this.location;
        }

        public void setLocation(Location location) {
            this.location = location;
        }

        Location location;

        @JsonProperty("email")
        public String getEmail() {
            return this.email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        String email;

        @JsonProperty("login")
        public Login getLogin() {
            return this.login;
        }

        public void setLogin(Login login) {
            this.login = login;
        }

        Login login;

        @JsonProperty("dob")
        public Dob getDob() {
            return this.dob;
        }

        public void setDob(Dob dob) {
            this.dob = dob;
        }

        Dob dob;

        @JsonProperty("registered")
        public Registered getRegistered() {
            return this.registered;
        }

        public void setRegistered(Registered registered) {
            this.registered = registered;
        }

        Registered registered;

        @JsonProperty("phone")
        public String getPhone() {
            return this.phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        String phone;

        @JsonProperty("cell")
        public String getCell() {
            return this.cell;
        }

        public void setCell(String cell) {
            this.cell = cell;
        }

        String cell;

        @JsonProperty("id")
        public Id getId() {
            return this.id;
        }

        public void setId(Id id) {
            this.id = id;
        }

        Id id;

        @JsonProperty("picture")
        public Picture getPicture() {
            return this.picture;
        }

        public void setPicture(Picture picture) {
            this.picture = picture;
        }

        Picture picture;

        @JsonProperty("nat")
        public String getNat() {
            return this.nat;
        }

        public void setNat(String nat) {
            this.nat = nat;
        }

        String nat;

        @Override
        public String toString() {
            return "Person{" +
                    "gender='" + gender + '\'' +
                    ", name=" + name +
                    ", location=" + location +
                    ", email='" + email + '\'' +
                    ", login=" + login +
                    ", dob=" + dob +
                    ", registered=" + registered +
                    ", phone='" + phone + '\'' +
                    ", cell='" + cell + '\'' +
                    ", id=" + id +
                    ", picture=" + picture +
                    ", nat='" + nat + '\'' +
                    '}';
        }
    }

    public static class Street {
        @JsonProperty("number")
        public int getNumber() {
            return this.number;
        }

        public void setNumber(int number) {
            this.number = number;
        }

        int number;

        @JsonProperty("name")
        public String getName() {
            return this.name;
        }

        public void setName(String name) {
            this.name = name;
        }

        String name;

        @Override
        public String toString() {
            return "Street{" +
                    "number=" + number +
                    ", name='" + name + '\'' +
                    '}';
        }
    }

    public static class Timezone {
        @JsonProperty("offset")
        public String getOffset() {
            return this.offset;
        }

        public void setOffset(String offset) {
            this.offset = offset;
        }

        String offset;

        @JsonProperty("description")
        public String getDescription() {
            return this.description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        String description;

        @Override
        public String toString() {
            return "Timezone{" +
                    "offset='" + offset + '\'' +
                    ", description='" + description + '\'' +
                    '}';
        }
    }
}
