package com.monkeybean.labo.util;

import com.monkeybean.labo.component.reqres.res.IpaParseRes;
import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;
import java.util.Map;

/**
 * Created by MonkeyBean on 2019/6/26.
 */
public class IpaUtilTest {

    @Test
    public void parsePListToMap() throws FileNotFoundException {
        URL fileUrl = this.getClass().getClassLoader().getResource("back_test/test_info.plist");
        if (fileUrl == null) {
            return;
        }
        String filePath = fileUrl.getPath();
        File file = new File(filePath);
        if (file.exists()) {
            Map<String, Object> map = IpaUtil.parsePListToMap(new FileInputStream(file));
            String str1 = "CFBundleIdentifier";
            String str2 = "CFBundleVersion";
            String str3 = "CFBundleDisplayName";
            System.out.println(str1 + ": " + map.get(str1));
            System.out.println(str2 + ": " + map.get(str2));
            System.out.println(str3 + ": " + map.get(str3));
        }
    }

    @Test
    public void parseMobileProvisionToMap() throws FileNotFoundException {
        URL fileUrl = this.getClass().getClassLoader().getResource("back_test/test_embedded.mobileprovision");
        if (fileUrl == null) {
            return;
        }
        String filePath = fileUrl.getPath();
        File file = new File(filePath);
        if (file.exists()) {
            Map<String, Object> map = IpaUtil.parseMobileProvisionToMap(new FileInputStream(file));
            String str = "TeamName";
            System.out.println(str + ": " + map.get(str));
        }
    }

    @Test
    public void parseIpa() {
        URL fileUrl = this.getClass().getClassLoader().getResource("back_test/test.ipa");
        if (fileUrl == null) {
            return;
        }
        String filePath = fileUrl.getPath();
        File file = new File(filePath);
        IpaParseRes res = IpaUtil.parseIpa(file);
        if (res != null) {
            System.out.println("get valid param as follows");
            System.out.println(res.getBundleId());
            System.out.println(res.getBundleVersion());
            System.out.println(res.getDisplayName());
            System.out.println(res.getVersionNameOnAppStore());
            System.out.println(res.getBundleName());
            System.out.println(res.getMinIOSVersion());
            System.out.println(res.getTeamName());
            for (String eachIconName : res.getIconNameList()) {
                System.out.println(eachIconName);
            }

            //图片存储到目标路径
            //String storePath = "C:\\Users\\Administrator\\Desktop\\test_img_store";
            String storePath = "C:" + File.separator + "Users" + File.separator + "Administrator" + File.separator + "Desktop" + File.separator + "test_img_store";
            IpaUtil.parseAndStoreFile(file, res.getIconNameList(), storePath);
        }
    }

    @Test
    public void changePlistContent() throws Exception {
        String urlPrefix = "https://test.domain.com/package_ios";
        String name = "ReDx001";
        String version = "12345";
        String versionName = name + version;
        urlPrefix = urlPrefix + "/" + versionName + "/";
        String softwarePackageUrl = urlPrefix + "test.ipa";
        String displayImageUrl = urlPrefix + "Icon-57.png";
        String fullSizeImageUrl = urlPrefix + "Icon-512.png";
        String bundleIdentifierValue = "com.monkey.bean";
        String bundleVersionValue = "1.0.0.0";
        String titleValue = "测试标题";

        //方式1
//        URL fileUrl = this.getClass().getClassLoader().getResource("manifest.plist");
//        if (fileUrl == null) {
//            return;
//        }
//        File originFile = new File(fileUrl.getPath());

        //方式2
//        Resource resource = new ClassPathResource("manifest.plist");
//        File originFile = resource.getFile();

        //方式3
//        File originFile = ResourceUtils.getFile(ResourceUtils.CLASSPATH_URL_PREFIX + "manifest.plist");
//        if (!originFile.exists()) {
//            System.out.println("manifest.plist is not exist");
//            return;
//        }

        //方式4, 若读取的文件在tar包内, 读取文件报类似file:/data/xxx/xxx.jar!/BOOT-INF/classes!/xx.xx的错误, 此时以读取流的方式解决
        InputStream stream = IpaUtil.class.getClassLoader().getResourceAsStream("manifest.plist");
        File originFile = new File("/temp/manifest.plist");
        FileUtils.copyInputStreamToFile(stream, originFile);
        stream.close();
        String newFileParentPath = originFile.getParent() + File.separator + version;
        File parentDir = new File(newFileParentPath);
        if (!parentDir.exists()) {
            boolean createDir = parentDir.mkdir();
            if (!createDir) {
                System.err.println("parentDir create failed: " + parentDir);
                return;
            }
        }
        File aimFile = new File(newFileParentPath + File.separator + originFile.getName());
        IpaUtil.changePlistContent(originFile, aimFile, softwarePackageUrl, displayImageUrl, fullSizeImageUrl, bundleIdentifierValue, bundleVersionValue, titleValue);
    }

}