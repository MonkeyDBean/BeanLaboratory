package com.monkeybean.labo;

import com.monkeybean.labo.component.filter.CrossOriginFilter;
import com.monkeybean.labo.component.filter.TokenKeyFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

/**
 * 若打成war包，需继承SpringBootServletInitializer
 * <p>
 * Created by MonkeyBean on 2018/05/26.
 */
@SpringBootApplication
//排除默认json解析，使用如下代码或通过pom依赖排除
//@SpringBootApplication(exclude = {JacksonAutoConfiguration.class})
//若为war包，使用下行注释代码
//public class MainApplication extends SpringBootServletInitializer {
public class MainApplication {

    private static final Logger logger = LoggerFactory.getLogger(MainApplication.class);

    private final Environment env;

    @Autowired
    public MainApplication(Environment env) {
        this.env = env;
    }

    public static void main(String[] args) {

        SpringApplicationBuilder builder = new SpringApplicationBuilder(MainApplication.class);

//        //加载Secret配置项, 安全性要求高的私密配置如数据库配置可放置K8s Secret, 而不是本地或配置中心等隐私性较差的地方
//        //secretDirectory为环境变量key, value为secret所在目录
//        final String secretDirectory = "DIR_SECRET";
//        String secretFileDirStr = System.getenv(secretDirectory);
//        Properties p = new Properties();
//        try {
//            File secretFileDir = new File(secretFileDirStr);
//            for (File file : secretFileDir.listFiles()) {
//                if (!file.isDirectory()) {
//                    InputStream in = new BufferedInputStream(new FileInputStream(file));
//                    p.load(in);
//                    in.close();
//                }
//            }
//        } catch (IOException e) {
//            logger.error("get secretFile, IOException: [{}]", e);
//            return;
//        }
//        builder.properties(p).build();

        builder.bannerMode(Banner.Mode.LOG).run(args);

        //若打成war包，已重写的SpringApplicationBuilder，直接起应用
        // SpringApplication.run(MainApplication.class, args);
    }

//    /**
//     * 若打成war包，则重写SpringApplicationBuilder的构建
//     */
//    @Override
//    protected SpringApplicationBuilder configure(SpringApplicationBuilder applicationBuilder) {
//        applicationBuilder.bannerMode(Banner.Mode.LOG);
//        return applicationBuilder.sources(MainApplication.class);
//    }

    /**
     * 拦截器配置
     */
    @Bean
    public FilterRegistrationBean filterTokenKeyBean() {
        logger.info("filter token is:-> [{}], sniff token is:->[{}], dailyRequestMaxNum is:-> [{}]", env.getProperty("filter.token"), env.getProperty("sniff.token"), env.getProperty("other.dailyRequestMaxNum"));
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.addUrlPatterns("/*");
        registration.setFilter(new TokenKeyFilter(Boolean.parseBoolean(env.getProperty("filter.token")), env.getProperty("sniff.token"), Integer.parseInt(env.getProperty("other.dailyRequestMaxNum"))));
        return registration;
    }

    /**
     * 跨域配置
     */
    @Bean
    public FilterRegistrationBean filterCrossOriginBean() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.addUrlPatterns("/*");
        registration.setFilter(new CrossOriginFilter());
        return registration;
    }

//    /**
//     * 文件上传配置，在配置文件中配置即可，此处无需再次配置
//     */
//    @Bean
//    public MultipartConfigElement multipartConfigElement() {
//        MultipartConfigFactory factory = new MultipartConfigFactory();
//
//        //单个文件最大
//        factory.setMaxFileSize("10240KB"); //KB,MB
//
//        /// 设置总上传数据总大小
//        factory.setMaxRequestSize("102400KB");
//        return factory.createMultipartConfig();
//    }

}
