package com.monkeybean.labo.component.reqres.res;

import java.util.List;

/**
 * Created by MonkeyBean on 2019/6/26.
 */
public class IpaParseRes {

    /**
     * 应用包名(唯一标识)
     */
    private String bundleId;

    /**
     * 文件版本号(可每次发版本递增)
     */
    private String bundleVersion;

    /**
     * 应用展示名称(显示在手机上)
     */
    private String displayName;

    /**
     * appStore显示的应用版本号(每次appStore发版递增)
     */
    private String versionNameOnAppStore;

    /**
     * 应用名称(应用安装时创建的文件夹名称)
     */
    private String bundleName;

    /**
     * 应用所需IOS最低版本
     */
    private String minIOSVersion;

    /**
     * 代签机构
     */
    private String teamName;

    /**
     * 图标名称数组(仅名称, 不包括.png格式后缀)
     */
    private List<String> iconNameList;

    public String getBundleId() {
        return bundleId;
    }

    public void setBundleId(String bundleId) {
        this.bundleId = bundleId;
    }

    public String getBundleVersion() {
        return bundleVersion;
    }

    public void setBundleVersion(String bundleVersion) {
        this.bundleVersion = bundleVersion;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getVersionNameOnAppStore() {
        return versionNameOnAppStore;
    }

    public void setVersionNameOnAppStore(String versionNameOnAppStore) {
        this.versionNameOnAppStore = versionNameOnAppStore;
    }

    public String getBundleName() {
        return bundleName;
    }

    public void setBundleName(String bundleName) {
        this.bundleName = bundleName;
    }

    public String getMinIOSVersion() {
        return minIOSVersion;
    }

    public void setMinIOSVersion(String minIOSVersion) {
        this.minIOSVersion = minIOSVersion;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public List<String> getIconNameList() {
        return iconNameList;
    }

    public void setIconNameList(List<String> iconNameList) {
        this.iconNameList = iconNameList;
    }
}
