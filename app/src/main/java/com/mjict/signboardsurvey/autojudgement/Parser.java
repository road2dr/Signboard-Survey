package com.mjict.signboardsurvey.autojudgement;

import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Junseo on 2017-01-18.
 */
public class Parser {

    public static List<Item> getItemsFromXml(String filePath) {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(filePath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }

        List<Item> items = null;

        try {
            // get a new XmlPullParser object from Factory
            XmlPullParser parser = XmlPullParserFactory.newInstance().newPullParser();
            parser.setInput(fis, null);

            int eventType = parser.getEventType();

            Item item = null;
            while(eventType != XmlPullParser.END_DOCUMENT) {
                switch(eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        items = new ArrayList<>();
                        break;

                    case XmlPullParser.START_TAG:
                        // get tag name
                        String tagName = parser.getName();
                        Log.d("junseo", "tag name: "+tagName);

                        if(tagName.equalsIgnoreCase(Constants.VALUES)) {
                        }

                        else if(tagName.equalsIgnoreCase(Constants.ITEM)) {
                            item = new Item();
                            String name = parser.getAttributeValue(null, Constants.NAME);
                            int code = Integer.parseInt(parser.getAttributeValue(null, Constants.CODE));
                            String value = parser.nextText();

                            item.setName(name);
                            item.setCode(code);
                            item.setValue(value);
                        }

                    case XmlPullParser.END_TAG:
                        String endTagName = parser.getName();

                        if(endTagName.equalsIgnoreCase(Constants.ITEM)) {
                            items.add(item);
                        }
                        break;
                }
                // jump to next event
                eventType = parser.next();
            }
            // exception stuffs
        } catch (XmlPullParserException e) {
            e.printStackTrace();
            items = null;
        } catch (IOException e) {
            e.printStackTrace();
            items = null;
        } catch (Exception e) {
            e.printStackTrace();
            items = null;
        }

        return items;
    }

    public static List<Type> getTypesFromXml(String filePath) {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(filePath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }

        List<Type> types = null;

        try {
            // get a new XmlPullParser object from Factory
            XmlPullParser parser = XmlPullParserFactory.newInstance().newPullParser();
            parser.setInput(fis, null);

            int eventType = parser.getEventType();

            Type type = null;
            while(eventType != XmlPullParser.END_DOCUMENT) {
                switch(eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        types = new ArrayList<>();
                        break;

                    case XmlPullParser.START_TAG:
                        // get tag name
                        String tagName = parser.getName();

                        if(tagName.equalsIgnoreCase(Constants.TYPES)) {
                        }

                        else if(tagName.equalsIgnoreCase(Constants.TYPE)) {
                            type = new Type();
                            Category category = Category.toCategory(parser.getAttributeValue(null, Constants.CATEGORY));
                            String name = parser.getAttributeValue(null, Constants.NAME);
                            String value = parser.nextText();

                            type.setName(name);
                            type.setCategory(category);
                            type.setValue(value);
                        }

                    case XmlPullParser.END_TAG:
                        String endTagName = parser.getName();

                        if(endTagName.equalsIgnoreCase(Constants.TYPE)) {
                            types.add(type);
                        }
                        break;
                }
                // jump to next event
                eventType = parser.next();
            }
            // exception stuffs
        } catch (XmlPullParserException e) {
            e.printStackTrace();
            types = null;
        } catch (IOException e) {
            e.printStackTrace();
            types = null;
        } catch (Exception e) {
            e.printStackTrace();
            types = null;
        }

        return types;
    }

    public static List<AutoJudgementRule> getRulesFromXml(String filePath) {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(filePath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }

        List<AutoJudgementRule> rules = null;

        try {
            // get a new XmlPullParser object from Factory
            XmlPullParser parser = XmlPullParserFactory.newInstance().newPullParser();
            parser.setInput(fis, null);

            int eventType = parser.getEventType();

            AutoJudgementRule autoJudgementRule = null;
            Rule rule = null;
            Condition condition = null;

            while(eventType != XmlPullParser.END_DOCUMENT) {
                switch(eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        rules = new ArrayList<>();
                        break;

                    case XmlPullParser.START_TAG:
                        String tagName = parser.getName();
                        if(tagName.equalsIgnoreCase(Constants.RULES)) {
                            //
                        } else if(tagName.equalsIgnoreCase(Constants.INSPECTION_RULE)) {
                            autoJudgementRule = checkAndMakeAutoJudgementRule(parser);
                        } else if(tagName.equalsIgnoreCase(Constants.RULE)) {
                            rule = checkAndMakeRule(parser);
                        } else if(tagName.equalsIgnoreCase(Constants.EQUATION_CONDITION)) {
                            condition = checkAndMakeEquationCondition(parser);
                        } else if(tagName.equalsIgnoreCase(Constants.SCOPE_CONDITION)) {
                            condition = checkAndMakeScopeCondition(parser);
                        } else {
                            // nothing to do
                        }
                        break;

                    case XmlPullParser.END_TAG:
                        String endTagName = parser.getName();

                        if(endTagName.equalsIgnoreCase(Constants.RULES)) {
                            //
                        } else if(endTagName.equalsIgnoreCase(Constants.INSPECTION_RULE)) {
                            Log.d("junseo", "inspection_rule");
                            rules.add(autoJudgementRule);
                        } else if(endTagName.equalsIgnoreCase(Constants.RULE)) {
                            Log.d("junseo", "rule");
                            autoJudgementRule.addRule(rule);
                        } else if(endTagName.equalsIgnoreCase(Constants.EQUATION_CONDITION)) {
                            Log.d("junseo", "equation_condition");
                            rule.addCondition(condition);
                        } else if(endTagName.equalsIgnoreCase(Constants.SCOPE_CONDITION)) {
                            Log.d("junseo", "scope_condition");
                            rule.addCondition(condition);
                        } else {
                            // nothing to do
                        }
                        break;
                }
                // jump to next event
                eventType = parser.next();
            }
            // exception stuffs
        } catch (XmlPullParserException e) {
            e.printStackTrace();
            rules = null;
        } catch (IOException e) {
            e.printStackTrace();
            rules = null;
        } catch (Exception e) {
            e.printStackTrace();
            rules = null;
        }

        return rules;
    }




    private static AutoJudgementRule checkAndMakeAutoJudgementRule(XmlPullParser parser) throws Exception {
        String typeValue = parser.getAttributeValue(null, Constants.TYPE);
        String defaultResultValue = parser.getAttributeValue(null, Constants.DEFAULT_RESULT);

        if(typeValue == null || defaultResultValue == null) {
            String msg = String.format("필수 항목이 없습니다. (%s, %s)", Constants.TYPE, Constants.DEFAULT_RESULT);
            throw new Exception(msg);
        }

        typeValue = typeValue.toLowerCase();
        defaultResultValue = defaultResultValue.toLowerCase();

        final String typePrefix = "@value/";
        final String defaultResultPrefix = "@result/";

        AutoJudgementRule autoJudgementRule = null;

        if(typeValue.startsWith(typePrefix) == false) {
            String msg = String.format("잘못된 형식의 값: %s | @value/ 로 시작 되어야 합니다.", typeValue);
            throw new Exception(msg);
        }
        if(defaultResultValue.startsWith(defaultResultPrefix) == false) {
            String msg = String.format("잘못된 형식의 값: %s | @value/ 로 시작 되어야 합니다.", defaultResultValue);
            throw new Exception(msg);
        }

        String typeName = typeValue.substring(typePrefix.length());
        String defaultResultName = defaultResultValue.substring(defaultResultPrefix.length());

        Item type = AutoJudgementRuleManager.findValue(typeName);
        Item defaultResult = AutoJudgementRuleManager.findResult(defaultResultName);

        if(type == null) {
            String msg = String.format("%s 에 해당하는 %s 값을 찾을 수 없습니다.", typeName, Constants.TYPE);
            throw new Exception(msg);
        }
        if(defaultResult == null) {
            String msg = String.format("%s 에 해당하는 %s 값을 찾을 수 없습니다.", defaultResultName, Constants.DEFAULT_RESULT);
            throw new Exception(msg);
        }

        autoJudgementRule = new AutoJudgementRule();
        autoJudgementRule.setDefaultResult(defaultResult.getCode());
        autoJudgementRule.setType(type.getCode());

        return autoJudgementRule;
    }


    private static Rule checkAndMakeRule(XmlPullParser parser) throws Exception {
        String resultValue = parser.getAttributeValue(null, Constants.RESULT);
        String orderValue = parser.getAttributeValue(null, Constants.ORDER);

        if(resultValue == null || orderValue == null) {
            String msg = String.format("필수 항목이 없습니다. (%s, %s)", Constants.RESULT, Constants.ORDER);
            throw new Exception(msg);
        }

        resultValue = resultValue.toLowerCase();
        orderValue = orderValue.toLowerCase();

        final String resultPrefix = "@result/";
        final String intExpression = "\\d+";

        if(resultValue.startsWith(resultPrefix) == false) {
            String msg = String.format("잘못된 형식의 값: %s | %s 로 시작 되어야 합니다.", resultValue, resultPrefix);
            throw new Exception(msg);
        }
        if(orderValue.matches(intExpression) == false) {
            String msg = String.format("잘못된 형식의 값: %s | 0 이상의 정수값만 허용 됩니다.", orderValue);
            throw new Exception(msg);
        }

        String resultName = resultValue.substring(resultPrefix.length());

        Item result = AutoJudgementRuleManager.findResult(resultName);
        int order = Integer.parseInt(orderValue);

        if(result == null) {
            String msg = String.format("%s 에 해당하는 %s 값을 찾을 수 없습니다.", resultName, Constants.RESULT);
            throw new Exception(msg);
        }

        Rule rule = new Rule();
        rule.setOrder(order);
        rule.setResult(result.getCode());

        return rule;
    }

    private static EquationCondition checkAndMakeEquationCondition(XmlPullParser parser) throws Exception {
        String itemString = parser.getAttributeValue(null, Constants.ITEM);
        String equationString = parser.getAttributeValue(null, Constants.EQUATION);
        String valueString = parser.getAttributeValue(null, Constants.VALUE);
        String operationString = parser.getAttributeValue(null, Constants.OPERATION);

        if(itemString == null || equationString == null || valueString == null) {
            String msg = String.format("필수 항목이 없습니다. (%s, %s, %s)", Constants.ITEM, Constants.EQUATION, Constants.VALUE);
            throw new Exception(msg);
        }

        itemString = itemString.toLowerCase();
        equationString = equationString.toLowerCase();
        valueString = valueString.toLowerCase();
        if(operationString != null)
            operationString = operationString.toLowerCase();

        String itemPrefix = "@type/";
        String valuePrefix = "@value/";
        final String intExpression = "\\d+";

        if(itemString.startsWith(itemPrefix) == false) {
            String msg = String.format("잘못된 형식의 값: %s | %s 로 시작 되어야 합니다.", itemString, itemPrefix);
            throw new Exception(msg);
        }

        if(equationString.equals("true") == false && equationString.equals("false") == false) {
            String msg = String.format("잘못된 형식의 값: %s | 'true' 나 'false' 만 허용 됩니다.", equationString);
            throw new Exception(msg);
        }

        if(valueString.matches(intExpression) == false && valueString.startsWith(valuePrefix) == false) {
            String msg = String.format("잘못된 형식의 값: %s | 0 이상의 정수값 혹은 '%s'로 시작되는 값 만 허용 됩니다.", valueString, valuePrefix);
            throw new Exception(msg);
        }

        if(operationString != null) {
            if(operationString.equals("and") == false && operationString.equals("or") == false) {
                String msg = String.format("잘못된 형식의 값: %s | 'and' 나 'or' 만 허용 됩니다.", operationString);
                throw new Exception(msg);
            }
        }

        String itemName = itemString.substring(itemPrefix.length());
        Type itemType = AutoJudgementRuleManager.findType(itemName);
        if(itemType == null) {
            String msg = String.format("%s 에 해당하는 %s 값을 찾을 수 없습니다.", itemName, Constants.ITEM);
            throw new Exception(msg);
        }

        boolean equation = Boolean.parseBoolean(equationString);
        Operation operation = (operationString == null) ? Operation.END : Operation.toOperation(operationString);
        int value = -1;
        if(valueString.startsWith(valuePrefix)) {
            String valueName = valueString.substring(valuePrefix.length());
            Item valueItem = AutoJudgementRuleManager.findValue(valueName);
            if(valueItem == null) {
                String msg = String.format("%s 에 해당하는 %s 값을 찾을 수 없습니다.", valueName, Constants.VALUE);
                throw new Exception(msg);
            }
            value = valueItem.getCode();
        } else {
            value = Integer.parseInt(valueString);
        }

        EquationCondition condition = new EquationCondition(equation, value);
        condition.setValueType(itemType);
        condition.setOperation(operation);
        return condition;
    }

    public static ScopeCondition checkAndMakeScopeCondition(XmlPullParser parser) throws Exception {
        String typeString = parser.getAttributeValue(null, Constants.ITEM);
        String leftString = parser.getAttributeValue(null, Constants.LEFT);
        String leftEquationString = parser.getAttributeValue(null, Constants.LEFT_EQUATION);
        String valueString = parser.getAttributeValue(null, Constants.VALUE);
        String rightEquationString = parser.getAttributeValue(null, Constants.RIGHT_EQUATION);
        String rightString = parser.getAttributeValue(null, Constants.RIGHT);
        String operationString = parser.getAttributeValue(null, Constants.OPERATION);

        if(typeString == null) {
            String msg = String.format("필수 항목이 없습니다. (%s)", Constants.ITEM);
            throw new Exception(msg);
        }
        if (leftString == null) {
            String msg = String.format("필수 항목이 없습니다. (%s)", Constants.LEFT);
            throw new Exception(msg);
        }
        if (valueString == null) {
            String msg = String.format("필수 항목이 없습니다. (%s)", Constants.VALUE);
            throw new Exception(msg);
        }
        if (rightString == null) {
            String msg = String.format("필수 항목이 없습니다. (%s)", Constants.RIGHT);
            throw new Exception(msg);
        }

        typeString = typeString.toLowerCase();
        leftString = leftString.toLowerCase();
        if (leftEquationString != null)
            leftEquationString = leftEquationString.toLowerCase();
        valueString = valueString.toLowerCase();
        if (rightEquationString != null)
            rightEquationString = rightEquationString.toLowerCase();
        rightString = rightString.toLowerCase();
        if (operationString != null)
            operationString = operationString.toLowerCase();

        String typePrefix = "@type/";
        String valuePrefix = "@value/";
        final String intExpression = "\\d+";

        if(typeString.startsWith(typePrefix) == false) {
            String msg = String.format("잘못된 형식의 값: %s | '%s'로 시작되는 값 만 허용 됩니다.", typeString, typePrefix);
            throw new Exception(msg);
        }

        if(leftString.matches(intExpression) == false) {
            String msg = String.format("잘못된 형식의 값: %s | 0 이상의 정수값 만 허용 됩니다.", leftString);
            throw new Exception(msg);
        }

        if(leftEquationString != null) {
            if (leftEquationString.equals("true") == false && leftEquationString.equals("false") == false) {
                String msg = String.format("잘못된 형식의 값: %s | 'true' 나 'false' 만 허용 됩니다.", leftEquationString);
                throw new Exception(msg);
            }
        }

        if(valueString.equals("-1") == false && valueString.startsWith(valuePrefix) == false) {
            String msg = String.format("잘못된 형식의 값: %s | -1(정의 되지 않은 값) 혹은 '%s'로 시작되는 값 만 허용 됩니다.", valueString, valuePrefix);
            throw new Exception(msg);
        }

        if(rightEquationString != null) {
            if (rightEquationString.equals("true") == false && rightEquationString.equals("false") == false) {
                String msg = String.format("잘못된 형식의 값: %s | 'true' 나 'false' 만 허용 됩니다.", rightEquationString);
                throw new Exception(msg);
            }
        }

        if(rightString.matches(intExpression) == false) {
            String msg = String.format("잘못된 형식의 값: %s | 0 이상의 정수값 만 허용 됩니다.", rightString);
            throw new Exception(msg);
        }

        if(operationString != null) {
            if (operationString.equals("and") == false && operationString.equals("or") == false) {
                String msg = String.format("잘못된 형식의 값: %s | 'and' 나 'or' 만 허용 됩니다.", operationString);
                throw new Exception(msg);
            }
        }

        String typeName = typeString.substring(typePrefix.length());
        Type type = AutoJudgementRuleManager.findType(typeName);
        if(type == null) {
            String msg = String.format("%s 에 해당하는 %s 값을 찾을 수 없습니다.", typeName, Constants.TYPE);
            throw new Exception(msg);
        }
        int left = Integer.parseInt(leftString);
        boolean leftEquation = (leftEquationString == null) ? true : Boolean.parseBoolean(leftEquationString);

        int value = -1;
        if(valueString.equals("-1"))
            value = -1;
        else {
            String valueName = valueString.substring(valuePrefix.length());
            Item valueItem = AutoJudgementRuleManager.findValue(valueName);
            if (valueItem == null) {
                String msg = String.format("%s 에 해당하는 %s 값을 찾을 수 없습니다.", valueName, Constants.VALUE);
                throw new Exception(msg);
            }
            value = valueItem.getCode();
        }
        boolean rightEquation = (rightEquationString == null) ? false : Boolean.parseBoolean(rightEquationString);
        int right = Integer.parseInt(rightString);
        Operation operation = (operationString == null) ? Operation.END : Operation.toOperation(operationString);

        ScopeCondition condition = new ScopeCondition(left, leftEquation, rightEquation, right);
        condition.setOperation(operation);
        condition.setValueType(type);
        return condition;
    }
}
