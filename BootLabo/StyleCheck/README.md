# 功能
代码风格检查

# 接入
待接入项目的根目录
```
git clone git@github.com:MonkeyDBean/StyleCheck.git
```
pom文件接入
```
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-antrun-plugin</>
    <version>1.8</version>
    <executions>
        <execution>
            <id>validate</id>
            <phase>validate</phase>
            <configuration>
                <target>
                    <exec executable="git">
                        <arg value="submodule" />
                        <arg value="update" />
                        <arg value="--init" />
                        <arg value="--recursive" />
                    </exec>
                    <exec executable="git">
                        <arg value="submodule"/>
                        <arg value="update"/>
                        <arg value="--remote"/>
                    </exec>
                </target>
            </configuration>
            <goals>
                <goal>run</goal>
            </goals>
        </execution>
    </executions>
</plugin>

<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-checkstyle-plugin</artifactId>
    <version>3.0.0</version>
    <dependencies>
        <dependency>
            <groupId>com.puppycrawl.tools</groupId>
            <artifactId>checkstyle</artifactId>
            <version>8.11</version>
        </dependency>
    </dependencies>
    <executions>
       <execution>
            <id>validate</id>
            <phase>validate</phase>
            <configuration>
                <!-- 自定义的规则文件 -->
                <configLocation>${basedir}/StyleCheck/google_code_style_check.xml</configLocation>
                <encoding>UTF-8</encoding>
                <!-- 将检查结果打印到控制台 -->
                <consoleOutput>true</consoleOutput>
                <failsOnError>true</failsOnError>
                <failOnViolation>true</failOnViolation>
                <!-- 这里的 error 改成 warning 则有警告就编译不通过 -->
                <violationSeverity>error</violationSeverity>
                <!-- 忽略检查的文件 -->
                <excludes>**/CrossOriginFilter.java,**/aspect/*</excludes>
            </configuration>
            <goals>
                <goal>check</goal>
            </goals>
        </execution>
    </executions>
</plugin>
```
导出检查结果
```
<reporting>
    <plugins>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-project-info-reports-plugin</artifactId>
            <version>2.9</version>
        </plugin>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-checkstyle-plugin</artifactId>
            <version>3.0.0</version>
            <configuration>
                <configLocation>${basedir}/StyleCheck/google_code_style_check.xml</configLocation>
            </configuration>
        </plugin>
    </plugins>
</reporting>
```

