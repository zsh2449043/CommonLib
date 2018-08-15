package com.sample.common.database;


import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Unique;

/**
 * version:2
 * @Id
 * private Long id;
 * @Property(nameInDb = "TABLE_THUMB_URI")
 * private String tableThumbUri;    //table 缩略图
 * @Property(nameInDb = "TABLE_NODE")
 * private String tableMode;               // table 类型    pageName
 * @Property(nameInDb = "URI_KEY")
 * private String uriKey;                  //dat缩略图
 * @Unique
 * @Property(nameInDb = "URI_VALUE")
 * private String uriValue;                //dat文件路径
 * @Property(nameInDb = "GP_SUK")
 * @Unique private String gpSuk;                //dat文件id
 */
@Entity
public class EffectInfo {
    @Id
    private Long id;
    @Property(nameInDb = "TABLE_THUMB_URI")
    private String tableThumbUri;    //table 缩略图
    @Property(nameInDb = "TABLE_NODE")
    private String tableMode;               // table 类型    pageName
    @Property(nameInDb = "URI_KEY")
    private String uriKey;                  //dat缩略图

    @Unique
    @Property(nameInDb = "URI_VALUE")
    private String uriValue;                //dat文件路径
    @Unique
    @Property(nameInDb = "GP_SUK")
    private String gpSuk;                //商品谷歌id
    @Property(nameInDb = "IS_HOT")
    private boolean isHot;                //最热
    @Property(nameInDb = "IS_NEW")
    private boolean isNew;                //最新
    @Property(nameInDb = "SUB_TYPE")
    private String subType;                //子类
    @Property(nameInDb = "PRICE")
    private String price;                //价格
    @Property(nameInDb = "NAME")
    private String name;                //名字
    @Generated(hash = 348160566)
    public EffectInfo(Long id, String tableThumbUri, String tableMode,
            String uriKey, String uriValue, String gpSuk, boolean isHot,
            boolean isNew, String subType, String price, String name) {
        this.id = id;
        this.tableThumbUri = tableThumbUri;
        this.tableMode = tableMode;
        this.uriKey = uriKey;
        this.uriValue = uriValue;
        this.gpSuk = gpSuk;
        this.isHot = isHot;
        this.isNew = isNew;
        this.subType = subType;
        this.price = price;
        this.name = name;
    }
    @Generated(hash = 1485609062)
    public EffectInfo() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getTableThumbUri() {
        return this.tableThumbUri;
    }
    public void setTableThumbUri(String tableThumbUri) {
        this.tableThumbUri = tableThumbUri;
    }
    public String getTableMode() {
        return this.tableMode;
    }
    public void setTableMode(String tableMode) {
        this.tableMode = tableMode;
    }
    public String getUriKey() {
        return this.uriKey;
    }
    public void setUriKey(String uriKey) {
        this.uriKey = uriKey;
    }
    public String getUriValue() {
        return this.uriValue;
    }
    public void setUriValue(String uriValue) {
        this.uriValue = uriValue;
    }
    public String getGpSuk() {
        return this.gpSuk;
    }
    public void setGpSuk(String gpSuk) {
        this.gpSuk = gpSuk;
    }
    public boolean getIsHot() {
        return this.isHot;
    }
    public void setIsHot(boolean isHot) {
        this.isHot = isHot;
    }
    public boolean getIsNew() {
        return this.isNew;
    }
    public void setIsNew(boolean isNew) {
        this.isNew = isNew;
    }
    public String getSubType() {
        return this.subType;
    }
    public void setSubType(String subType) {
        this.subType = subType;
    }
    public String getPrice() {
        return this.price;
    }
    public void setPrice(String price) {
        this.price = price;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "EffectInfo{" +
                "id=" + id +
                ", tableThumbUri='" + tableThumbUri + '\'' +
                ", tableMode='" + tableMode + '\'' +
                ", uriKey='" + uriKey + '\'' +
                ", uriValue='" + uriValue + '\'' +
                ", gpSuk='" + gpSuk + '\'' +
                ", isHot=" + isHot +
                ", isNew=" + isNew +
                ", subType='" + subType + '\'' +
                ", price='" + price + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
