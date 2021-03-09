package com.wfj.common.util;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import lombok.Data;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;


/**
 * @author wfj
 * @description TODO
 * @date 2020/9/23
 */
@Data
public class CodeGenerator {
    @Data
    static class Properties {
        @NonNull
        private String url;
        @NonNull
        private String driverName;
        @NonNull
        private String username;
        @NonNull
        private String password;
        @NonNull
        private String tableNameArrStr;
        @NonNull
        private String parentPc;
        @NonNull
        private String dir;
        @NonNull
        private String tablePrefix;
    }

    public static void shell(Properties properties) {
        // 代码生成器
        AutoGenerator mpg = new AutoGenerator();
        //配置数据库
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setUrl(properties.getUrl());
        /*dsc.setSchemaName("public");*/
        dsc.setDriverName(properties.getDriverName());
        dsc.setUsername(properties.getUsername());
        dsc.setPassword(properties.getPassword());
        mpg.setDataSource(dsc);
        //数据库表配置，通过该配置，可指定需要生成哪些表或者排除哪些表
        StrategyConfig strategy = new StrategyConfig();
        strategy.setNaming(NamingStrategy.underline_to_camel);
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
        strategy.setEntityLombokModel(true);
        strategy.setRestControllerStyle(true);
        strategy.setControllerMappingHyphenStyle(true);
        //添加需要生成表数组
        strategy.setInclude(properties.getTableNameArrStr().split(","));
        strategy.setTablePrefix(properties.getTablePrefix());
        mpg.setStrategy(strategy);
        // 包配置
        PackageConfig pc = new PackageConfig();
        pc.setParent(properties.parentPc);
        mpg.setPackageInfo(pc);
        //全局配置
        GlobalConfig gc = new GlobalConfig();
        gc.setOutputDir(properties.getDir() + "/src/main/java.");
        gc.setAuthor("wfj");
        gc.setOpen(false);
        //实体属性 Swagger2 注解
        gc.setSwagger2(false);
        gc.setDateType(DateType.ONLY_DATE);
        mpg.setGlobalConfig(gc);
        // 自定义配置
        mpg.setCfg(getInjectionConfig(properties));
        // 配置模板
        TemplateConfig templateConfig = new TemplateConfig();
        templateConfig.setXml(null);
        mpg.setTemplate(templateConfig);
        //配置
        mpg.setTemplateEngine(new FreemarkerTemplateEngine());
        mpg.execute();
    }

    public static InjectionConfig getInjectionConfig(Properties properties) {
        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
                //
            }
        };
        // 自定义输出配置
        List<FileOutConfig> focList = new ArrayList<FileOutConfig>();
        // 自定义配置会被优先输出
        String templatePath = "/templates/mapper.xml.ftl";
        // 自定义配置会被优先输出,配置mapper.xml
        focList.add(new FileOutConfig(templatePath) {
            @Override
            public String outputFile(TableInfo tableInfo) {
                // 自定义输出文件名 ， 如果你 Entity 设置了前后缀、此处注意 xml 的名称会跟着发生变化！！
               /* return projectPath + "/src/main/resources/mappers/" + pc.getModuleName()
                        + "/" + tableInfo.getEntityName() + "Mapper" + StringPool.DOT_XML;*/
                //根据自己的位置修改
                return properties.getDir() + "/src/main/resources/mapper/"
                        + "/" + tableInfo.getEntityName() + "Mapper" + StringPool.DOT_XML;
            }
        });
        cfg.setFileOutConfigList(focList);
        return cfg;
    }

    public static void main(String[] args) {
        String url = "jdbc:mysql://127.0.0.1/zlhk_psy?useUnicode=true&characterEncoding=utf8&autoReconnect=true&rewriteBatchedStatements=TRUE";
        String driver = "com.mysql.jdbc.Driver";
        String username = "root";
        String password = "123456";
        String tableArr = "user";
        String parent = "com.zlhk.psy";
        String dir = "D:\\test";
        String tablePrefix = "";
        Properties properties = new Properties(url, driver, username, password, tableArr, parent, dir, tablePrefix);
        shell(properties);
    }
}
