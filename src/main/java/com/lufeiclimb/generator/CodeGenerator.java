package com.lufeiclimb.generator;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.io.File;

/**
 * CodeGenerator
 *
 * @author lufeixia
 * @version 1.0
 * @date 2022/5/27 12:24
 */
public class CodeGenerator {

    private static final String URL = "jdbc:mysql://localhost:3306/applets";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "123456";

    /** 自动生成代码输出目录，这里默认类路径下的src/test/java/com/universe包下，即当前工程的test目录下。 */
    private static final String OUTPUT_DIR =
            System.getProperty("user.dir") + File.separator + "src/main/java";

    public static void main(String[] args) {
        FastAutoGenerator.create(URL, USERNAME, PASSWORD)
                // 开启fileOverrride重新旧文件，disableOpenDir禁用代码生成后打开输出目录对话框
                .globalConfig(
                        builder ->
                                builder.author("lufeiclimb")
                                        .fileOverride()
                                        .disableOpenDir()
                                        .outputDir(OUTPUT_DIR))
                // parent指定生成的代码在哪个包下，entity可以指定实体(DO)所在的包名
                .packageConfig(builder -> builder.parent("com.lufeiclimb.generator.code").entity("pojo.domain"))
                // addInclude指定包含的表名，不调用该方法默认为所有表生成代码；addTablePrefix可以过滤表前缀，即t_user变成user
                .strategyConfig(
                        builder ->
                                builder.addInclude(
                                                "jijin_danjuan_chiyou",
                                                "jijin_danjuan_jinnianlai",
                                                "jijin_danjuan_lishi",
                                                "jijin_danjuan_zuijinqueren")
                                        // 禁用为实体类生成序列化ID；formatFileName格式化生成的实体类名称，即t_user -> UserDO
                                        .entityBuilder()
                                        .disableSerialVersionUID()
                                        .formatFileName("%sDO")
                                        // formatMapperFileName格式化Mapper接口名称，即t_user -> UserMapper
                                        // formatXmlFileName格式化Mapper.xml文件名称，即t_user ->
                                        // UserMapper.xml
                                        .mapperBuilder()
                                        .formatMapperFileName("%sMapper")
                                        .formatXmlFileName("%sMapper")
                                        .build())
                // MyBatis-Plus代码生成器是通过模板引擎来渲染文件的，默认模板引擎是Velocity，根据依赖我们使用Freemarker
                .templateEngine(new FreemarkerTemplateEngine())
                .execute();
    }
}
