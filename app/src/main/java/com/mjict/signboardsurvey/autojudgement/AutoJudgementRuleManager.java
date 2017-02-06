package com.mjict.signboardsurvey.autojudgement;

import com.mjict.signboardsurvey.util.SyncConfiguration;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Junseo on 2017-01-18.
 */
public class AutoJudgementRuleManager {

    public static final String RULE_XML_FILE_NAME = "rule.xml";
    public static final String TYPE_XML_FILE_NAME = "types.xml";
    public static final String VALUE_XML_FILE_NAME = "values.xml";
    public static final String RESULT_XML_FILE_NAME = "result.xml";

    private static HashMap<String, AutoJudgementRule> rules = new HashMap<>();
    private static HashMap<String, Item> results = new HashMap<>();
    private static HashMap<String, Item> values = new HashMap<>();
    private static HashMap<String, Type> types = new HashMap<>();

//    static HashMap<String, Integer> valueTable;
//    static {
//        valueTable = new HashMap<String, Integer>();
//        InputType[] types = InputType.values();
//        for(int i=0; i<types.length; i++) {
//            valueTable.put(types[i].getName(), -1);
//        }
//    }

//    public static Integer getInputValue(String name) {
//        return valueTable.get(name);
//    }
//
//    public static void putInputValue(String name, Integer value) {
//        valueTable.put(name, value);
//    }
//
//    public static void initInputValue() {
//        Set<String>keySet = valueTable.keySet();
//        Iterator<String> iter = keySet.iterator();
//        while(iter.hasNext()) {
//            String key = iter.next();
//            valueTable.put(key, -1);
//        }
//    }

//    public static String[] getInputValueNames() {
//        String[] names = new String[valueTable.size()];
//        names = valueTable.keySet().toArray(names);
//
//        return names;
//    }

    public static Item findResult(String name) {
        return results.get(name);
    }

    public static Item findValue(String name) {
        return values.get(name);
    }

    public static Type findType(String name) {
        return types.get(name);
    }


    public static boolean loadRuleFromXml() {

        String typesXmlFilePath = SyncConfiguration.getDirectoryForInspectionRule()+TYPE_XML_FILE_NAME;
        String valuesXmlFilePath = SyncConfiguration.getDirectoryForInspectionRule()+VALUE_XML_FILE_NAME;
        String resultXmlFilePath = SyncConfiguration.getDirectoryForInspectionRule()+RESULT_XML_FILE_NAME;
        String ruleXmlFilePath = SyncConfiguration.getDirectoryForInspectionRule()+RULE_XML_FILE_NAME;

        // Rule 파일 보다 먼저 Type, Value 파일을 먼저 읽어야 한다.
        // Rule 파일을 읽는 도중 values, results, types 값을 참조 한다.

        List<Type> typeList = Parser.getTypesFromXml(typesXmlFilePath);
        if(typeList == null)
            return false;
        for(int i=0; i<typeList.size(); i++) {
            Type type = typeList.get(i);
            types.put(type.getName(), type);    // 해쉬맵, 같은 이름의 아이템은 들어가지 않는다.
        }

        List<Item> valueList = Parser.getItemsFromXml(valuesXmlFilePath);
        if(valueList == null)
            return false;
        for(int i=0; i<valueList.size(); i++) {
            Item item = valueList.get(i);
            values.put(item.getName(), item);
        }

        List<Item> resultList = Parser.getItemsFromXml(resultXmlFilePath);
        if(resultList == null)
            return false;
        for(int i=0; i<resultList.size(); i++) {
            Item item = resultList.get(i);
            results.put(item.getName(), item);
        }

        List<AutoJudgementRule> ruleList = Parser.getRulesFromXml(ruleXmlFilePath);
        if(ruleList == null)
            return false;

        for(int i=0; i<ruleList.size(); i++) {
            AutoJudgementRule rule = ruleList.get(i);
            String signTypeString = rule.getSignType();
            final String typePrefix = "@value/";
            if(signTypeString.startsWith(typePrefix) == false) {
                return false;
            }
            String typeName = signTypeString.substring(typePrefix.length());
            Item type = AutoJudgementRuleManager.findValue(typeName);
            if(type == null)
                return false;

            rules.put(type.getCode(), rule);
        }

        return true;
    }

    public static AutoJudgementRule findAutoJudgementRule(String code) {
        return rules.get(code);
    }
}
