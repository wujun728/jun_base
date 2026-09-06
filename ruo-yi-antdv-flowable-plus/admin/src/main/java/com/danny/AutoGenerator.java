package com.danny;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import com.baomidou.mybatisplus.generator.fill.Column;

import java.util.Collections;

public class AutoGenerator {

    public static void main(String[] args) {
        FastAutoGenerator.create("jdbc:mysql://127.0.0.1:3306/table?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8&&allowMultiQueries=true"
                        , "root", "123456")
                .globalConfig(builder -> {
                    builder.author("danny") // 设置作者
//                            .enableSwagger() // 开启 swagger 模式
                            .disableOpenDir() // 执行完毕不打开文件夹
                            .fileOverride() // 覆盖已生成文件
                            .outputDir("C:\\e\\立信报表\\table\\system\\src\\main\\java"); // 指定输出目录
                })
                .packageConfig(builder -> {
                    builder.parent("com.danny") // 设置父包名
                            .moduleName("plus") // 设置父包模块名
                            .entity("domain")
                            .service("service")
                            .serviceImpl("service.impl")
                            .mapper("mapper")
                            .xml("mapper")
                            .controller("controller")
//                            .other("other")
                            .pathInfo(Collections.singletonMap(OutputFile.mapperXml, "C:\\e\\立信报表\\table\\system\\src\\main\\java\\com\\danny\\plus\\mapper")); // 设置mapperXml生成路径
                })
                .strategyConfig(builder -> {
                    builder.addInclude("z_bb_gongrrb") // 设置需要生成的表名
                            .addTablePrefix("sys_", "z_") // 设置过滤表前缀


                            .serviceBuilder() //开启service策略配置
//                            .formatServiceFileName("%sService") //取消Service前的I
                            .controllerBuilder() //开启controller策略配置
                            .enableRestStyle() //配置restful风格
                            .enableHyphenStyle() //url中驼峰转连字符

                            .entityBuilder().enableTableFieldAnnotation()
                            .columnNaming(NamingStrategy.underline_to_camel)
                            .idType(IdType.ASSIGN_UUID) //开启实体类配置
                            .addTableFills(
                                    new Column("add_time", FieldFill.INSERT)
                                    ,new Column("add_by", FieldFill.INSERT)
                                    ,new Column("last_edit_time", FieldFill.UPDATE)
                                    ,new Column("last_edit_by", FieldFill.UPDATE)
                                    ,new Column("create_by", FieldFill.INSERT)
                                    ,new Column("create_dept", FieldFill.INSERT)
                                    ,new Column("create_time", FieldFill.INSERT)
                                    ,new Column("create_time", FieldFill.INSERT)
                                    ,new Column("update_by", FieldFill.UPDATE)
                                    ,new Column("update_time", FieldFill.UPDATE)
                            ) //配置自动填充字段
                            .addIgnoreColumns()
                            .enableLombok() //开启lombok
                            .enableChainModel(); //开启lombok链式操作
                })
//                .templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .execute();
    }
}
