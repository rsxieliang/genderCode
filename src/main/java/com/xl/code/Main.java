package com.xl.code;

import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.rules.DbType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import org.apache.commons.lang.ArrayUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author xieliang
 * @Description
 * @Date Create in 上午 10:12 2018-03-02
 * Modified By:
 */
public class Main {

    public static void main(String[] args) throws Exception {
        getFile();
        generateByTables();
    }

    public static Properties properties = null;

    public static Properties getFile() {
        Properties properties = new Properties();
        InputStream is = null;
        is = Main.class.getResourceAsStream("/config.txt");
        if (is == null) {
            is = Main.class.getResourceAsStream("/config.properties");
        }
        try {
            properties.load(is);
        } catch (IOException ie) {
            ie.printStackTrace();
        }
        Main.properties = properties;
        return properties;
    }

    /**
     * 数据库配置
     *
     * @return
     */
    private static DataSourceConfig dataSourceConfig() {
        DataSourceConfig sourceConfig = new DataSourceConfig();
        sourceConfig.setDbType(DbType.valueOf(properties.getProperty("dataType", "MYSQL")));
        sourceConfig.setDriverName(properties.getProperty("dataDriver", "com.mysql.jdbc.Driver"));
        sourceConfig.setUrl(properties.getProperty("url"));
        sourceConfig.setUsername(properties.getProperty("username"));
        sourceConfig.setPassword(properties.getProperty("password"));
        return sourceConfig;
    }

    /**
     * 全局配置
     */
    public static GlobalConfig globalConfig() {
        GlobalConfig globalConfig = new GlobalConfig();
        // 是否开启动态语言
        globalConfig.setActiveRecord(Boolean.valueOf(properties.getProperty("activeRecord", "false")));
        // 是否生成 resultMap
        globalConfig.setBaseResultMap(Boolean.valueOf(properties.getProperty("baseResultMap", "false")));
        //作者名称
        globalConfig.setAuthor(properties.getProperty("author"));
        // 是否打开输出目录
        globalConfig.setOpen(Boolean.valueOf(properties.getProperty("open", "false")));
        //是否在xml中添加二级缓存配置
        globalConfig.setEnableCache(Boolean.valueOf(properties.getProperty("enableCache", "false")));
        //是否覆盖已有文件
        globalConfig.setFileOverride(Boolean.valueOf(properties.getProperty("fileOverride", "false")));
        //生成文件的输出目录【默认 D 盘根目录】
        globalConfig.setOutputDir(properties.getProperty("outputDir"));
        globalConfig.setIdType(IdType.valueOf(properties.getProperty("idType", "AUTO")));
        globalConfig.setServiceName("%sservice");
        return globalConfig;
    }


    public static StrategyConfig strategyConfig() {
        StrategyConfig sc = new StrategyConfig();
        //需要 lombok插件的支持 如果IDE工具继承了lombok和项目集成了lombok可以设置为true
        sc.setEntityLombokModel(Boolean.valueOf(properties.getProperty("entityLombokModel", "false")));

        sc.setDbColumnUnderline(Boolean.valueOf(properties.getProperty("db_column_underline", "false")));

        // 命令策略 把下划线转为驼峰规则
        String naming = properties.getProperty("naming", "false");
        if (naming.equals("true")) {
            sc.setNaming(NamingStrategy.underline_to_camel);
        }

        //字段下划线转驼峰
        String columnNaming = properties.getProperty("columnNaming", "false");
        if (columnNaming.equals("true")) {
            sc.setColumnNaming(NamingStrategy.underline_to_camel);
        }

        String tablePrefix = properties.getProperty("tablePrefix", "");
        String[] pres = tablePrefix.split(",");
        if (pres[pres.length - 1].equals("")) {
            pres = (String[]) ArrayUtils.remove(pres, pres.length - 1);
        }
        sc.setTablePrefix(pres);

        String filePrefix = properties.getProperty("fieldPrefix", "");
        String[] filePre = filePrefix.split(",");
        if (filePre[filePre.length - 1].equals("")) {
            filePre = (String[]) ArrayUtils.remove(filePre, filePre.length - 1);
        }
        sc.setFieldPrefix(filePre);

        String[] includes = properties.getProperty("include").split(",");
        if (includes[includes.length - 1].equals("")) {
            includes = (String[]) ArrayUtils.remove(includes, includes.length - 1);
        }
        if (includes.length != 0) {
            sc.setInclude(includes);
        }

        String[] exculdes = properties.getProperty("exclude").split(",");
        if (exculdes[exculdes.length - 1].equals("")) {
            exculdes = (String[]) ArrayUtils.remove(exculdes, exculdes.length - 1);
        }
        if (sc.getInclude() == null && exculdes.length != 0) {
            sc.setExclude(exculdes);
        }
        return sc;
    }

    private static void generateByTables() {
        AutoGenerator ag = new AutoGenerator();
        ag.setGlobalConfig(globalConfig());
        ag.setDataSource(dataSourceConfig());
        ag.setStrategy(strategyConfig());

        PackageConfig pc = new PackageConfig();
        pc.setParent("");
        pc.setController("controller");
        pc.setEntity("entity");
        ag.setPackageInfo(pc);
        ag.execute();
    }
}
