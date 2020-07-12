package com.hu;

import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;

public class MybatisPlusGenerator {
    public static void main(String[] args) {
        //全局配置
        GlobalConfig globalConfig=new GlobalConfig();
        globalConfig.setAuthor("hu")//设置作者
                .setActiveRecord(true)//是否支持AR模式
                .setOutputDir("/Users/hu/Documents/IdeaProjects/myproject的副本/src/main/java")   //生成文件的路径
                .setFileOverride(true)   //文件覆盖
                .setServiceName("%sService");//默认service借口名首字母是I
        //.setIdType(IdType.AUTO); //主键策略

        //数据源配置
        DataSourceConfig dataSourceConfig=new DataSourceConfig();
        dataSourceConfig.setUrl("jdbc:mysql://localhost/myproject?serverTimezone=GMT%2B8&useSSL=false&characterEncoding=utf8")
                .setDriverName("com.mysql.jdbc.Driver")
                .setUsername("root")
                .setPassword("root");

        //策略配置
        StrategyConfig strategyConfig=new StrategyConfig();
        strategyConfig.setCapitalMode(true)  //全局大写
                //.setDbColumnUnderline(true) //数据库表名,字段名是否使用下划线
                .setNaming(NamingStrategy.underline_to_camel)  //数据库表映射到实体的命名策略
                .setInclude("promo");//表名,不指定表名生成全部
        // .setTablePrefix("tbl_");//表前缀

        //包名策略
        PackageConfig packageConfig=new PackageConfig();
        packageConfig.setParent("com.hu.springboot")
                .setEntity("entity")
                .setMapper("dao")
                .setService("service")
                .setController("controller")
                .setXml("dao");

        AutoGenerator autoGenerator=new AutoGenerator();
        autoGenerator.setDataSource(dataSourceConfig)
                .setPackageInfo(packageConfig)
                .setGlobalConfig(globalConfig)
                .setStrategy(strategyConfig);
        autoGenerator.execute();

    }
}