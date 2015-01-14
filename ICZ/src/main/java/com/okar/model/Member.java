package com.okar.model;
        import java.sql.Timestamp;
        import java.util.List;

/**
 * Created by snow on 14-7-30.
 */
public class Member {


    private int id;
    private String nickname;
    private String wxNickname;
    private Timestamp birthday;
    private String mobile;
    private Double lat;
    private Double lng;
    private String label;
    private String gender;
    private String intro;
    private String openid;
    private String uuid;
    private String realname;
    private Integer province;
    private Integer city;
    private String head;
    private String qq;
    private String carNumber;
    private String number;
    private List<MemberCar> cars;
    private String defaultCar;
    private Timestamp createTime;

    private int tid;

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public int getTid() {
        return tid;
    }

    public void setTid(int tid) {
        this.tid = tid;
    }

    public void setDefaultCar(String defaultCar) {
        this.defaultCar = defaultCar;
    }

    public List<Account> getJoins() {
        return joins;
    }

    public void setJoins(List<Account> joins) {
        this.joins = joins;
    }

    private List<Account> joins;

    public Member() {
    }

    public Member(int id) {
        this.id = id;
    }


    private int contribution;

    public int getContribution() {
        return contribution;
    }

    public void setContribution(int contribution) {
        this.contribution = contribution;
    }

    public Integer getProvince() {
        return province;
    }

    public void setProvince(Integer province) {
        this.province = province;
    }

    public Integer getCity() {
        return city;
    }

    public void setCity(Integer city) {
        this.city = city;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getCarNumber() {
        return carNumber;
    }

    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getWxNickname() {
        return wxNickname;
    }

    public void setWxNickname(String wxNickname) {
        this.wxNickname = wxNickname;
    }

    public Timestamp getBirthday() {
        return birthday;
    }

    public void setBirthday(Timestamp birthday) {
        this.birthday = birthday;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public String getNumber() {
        return number;
    }

    @Override
    public String toString() {
        return "Member{" +
                "id=" + id +
                ", nickname='" + nickname + '\'' +
                ", wxNickname='" + wxNickname + '\'' +
                ", birthday=" + birthday +
                ", mobile='" + mobile + '\'' +
                ", lat=" + lat +
                ", lng=" + lng +
                ", label='" + label + '\'' +
                ", gender='" + gender + '\'' +
                ", intro='" + intro + '\'' +
                ", openid='" + openid + '\'' +
                ", uuid='" + uuid + '\'' +
                ", realname='" + realname + '\'' +
                ", province=" + province +
                ", city=" + city +
                ", qq='" + qq + '\'' +
                ", carNumber='" + carNumber + '\'' +
                ", number='" + number + '\'' +
                ", cars=" + cars +
                ", contribution=" + contribution +
                ", tid=" + tid +
                '}';
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public List<MemberCar> getCars() {
        return cars;
    }

    public void setCars(List<MemberCar> cars) {
        this.cars = cars;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

}
