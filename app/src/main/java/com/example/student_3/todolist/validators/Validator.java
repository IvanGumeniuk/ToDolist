package com.example.student_3.todolist.validators;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Student_3 on 09/11/2017.
 */

public class Validator<T> {

    private abstract static class Rule<T> {

        protected String message;

        public Rule(String message) {
            this.message = message;
        }

        public abstract boolean validate(T value);
    }

    private abstract static class CriteriaRule<T, C> extends Rule<T> {

        protected C criteria;

        public CriteriaRule(String message, C criteria) {
            super(message);
            this.criteria = criteria;
        }
    }

    private abstract static class DoubleCriteriaRule<T, R, C> extends Rule<T> {

        protected C criteria;
        protected R secondCriteria;

        public DoubleCriteriaRule(String message, C criteria, R secondCriteria) {
            super(message);
            this.criteria = criteria;
            this.secondCriteria = secondCriteria;
        }
    }

    private Set<Rule<T>> rules;
    private ArrayList<String> messages;

    private Validator() {
        rules = new HashSet<>();
        messages = new ArrayList<>();
    }

    public
    @Nullable
    String getLastMessage() {
        return messages.isEmpty() ? null : messages.get(messages.size() - 1);
    }

    public boolean validate(T value, String... fieldName) {
        boolean result = true;
        messages.clear();
        for (Rule<T> rule : rules) {
            if (!rule.validate(value)) {
                result = false;
                String message = rule.message;
                if (fieldName != null && fieldName.length > 0) {
                    message = String.format("%s %s", fieldName[0], rule.message);
                }
                messages.add(message);
            }
        }
        return result;
    }

    public static class StringValidatorBuilder {
        Validator<String> validator;
        private Rule<String> notEmptyRule;
        private Rule<String> notEmptyOrWhiteSpaceRule;
        private CriteriaRule<String, Integer> minLenghtRule;

        private Rule<String> getNotEmptyRule(String... messages) {
            String message = getMessageString(messages, "cannot be blank");
            if (notEmptyRule != null) {
                notEmptyRule.message = message;
            } else {
                notEmptyRule = new Rule<String>(message) {
                    @Override
                    public boolean validate(String value) {
                        return !TextUtils.isEmpty(value);
                    }
                };
            }
            return notEmptyRule;
        }

        private Rule<String> getNotEmptyOrWhiteSpaceRule(String... messages) {
            String message = getMessageString(messages, "cannot be blank");
            if (notEmptyOrWhiteSpaceRule != null) {
                notEmptyOrWhiteSpaceRule.message = message;
            } else {
                notEmptyOrWhiteSpaceRule = new Rule<String>(message) {
                    @Override
                    public boolean validate(String value) {
                        return !TextUtils.isEmpty(value) && !TextUtils.isEmpty(value.trim());
                    }
                };
            }
            return notEmptyOrWhiteSpaceRule;
        }

        private CriteriaRule<String, Integer> getMinLenghtRule(Integer value, String... messages) {
            String message = getMessageString(messages, String.format("must contain more than %d symbols", value));
            if(minLenghtRule != null){
                minLenghtRule.message = message;
            } else {
                minLenghtRule = new CriteriaRule<String, Integer>(message, value) {
                    @Override
                    public boolean validate(String value) {
                        return !TextUtils.isEmpty(value) && value.length() >= this.criteria;
                    }
                };
            }
            return minLenghtRule;
        }

        private String getMessageString(String[] messages, String defaultMessage) {
            String message = "";
            if(messages == null || messages.length == 0){
                message = defaultMessage;
            } else {
                message = messages[0];
            }
            return message;
        }

        public StringValidatorBuilder(){
            validator = new Validator<String>();
        }

        public StringValidatorBuilder setNotEmpty(String ... message){
            validator.rules.add(getNotEmptyRule(message));
            return this;
        }

        public StringValidatorBuilder setNotEmptyorWhiteSpace(String ... message){
            validator.rules.add(getNotEmptyOrWhiteSpaceRule(message));
            return this;
        }

        public StringValidatorBuilder setMinLength(int minLength, String ... message){
            validator.rules.add(getMinLenghtRule(minLength, message));
            return this;
        }

        public Validator<String> build(){
            return validator;
        }
    }

    public static class NumberValidatorBuilder<T extends Number> {
        Validator<T> validator;
        private CriteriaRule<T, T> minNumberRule;
        private CriteriaRule<T, T> maxNumberRule;
        private DoubleCriteriaRule<T, T, T> rangeNumberRule;
        private T maxNumber;
        private T minNumber;

        public NumberValidatorBuilder(){
            validator = new Validator<T>();
        }

        private CriteriaRule<T, T> getMinNumberRule(T value, String ... messages){
            String message = getMessageString(messages, String.format("must be bigger than %s", value));
            if(minNumberRule != null){
                minNumberRule.message = message;
            } else {
                minNumberRule = new CriteriaRule<T, T>(message, value) {
                    @Override
                    public boolean validate(T value) {
                        return value.doubleValue() >= criteria.doubleValue();
                    }
                };
            }
            return minNumberRule;
        }

        private CriteriaRule<T, T> getMaxNumberRule(T value, String ... messages){
            String message = getMessageString(messages, String.format("must be smaller than %s", value));
            if(maxNumberRule != null){
                maxNumberRule.message = message;
            } else {
                maxNumberRule = new CriteriaRule<T, T>(message, value) {
                    @Override
                    public boolean validate(T value) {
                        return value.doubleValue() <= criteria.doubleValue();
                    }
                };
            }
            return maxNumberRule;
        }

        private DoubleCriteriaRule<T, T, T> getRangeNumberRule(T minValue, T maxValue,  String ... messages){
            String message = getMessageString(messages, String.format("must be in range [%s, %s]", minValue,
                    maxValue));
            if(rangeNumberRule != null){
                rangeNumberRule.message = message;
            } else {
                rangeNumberRule = new DoubleCriteriaRule<T, T, T>(message, minValue, maxValue) {
                    @Override
                    public boolean validate(T value) {
                        return value.doubleValue() >= criteria.doubleValue()
                                && value.doubleValue() <= secondCriteria.doubleValue();
                    }
                };
            }
            return rangeNumberRule;
        }

        private String getMessageString(String[] messages, String defaultMessage) {
            String message = "";
            if(messages == null || messages.length == 0){
                message = defaultMessage;
            } else {
                message = messages[0];
            }
            return message;
        }

        public NumberValidatorBuilder setMinNumber(T minNumber, String ... message){
            if(this.maxNumber != null && this.maxNumber.doubleValue() < minNumber.doubleValue()){
                this.minNumber = this.maxNumber;
                message = new String[]{String.format("must be exactly %s", this.maxNumber)};
                validator.rules.add(getMinNumberRule(this.minNumber, message));
            } else {
                this.minNumber = minNumber;
            }
            validator.rules.add(getMinNumberRule(this.minNumber, message));
            return this;
        }

        public NumberValidatorBuilder setMaxNumber(T maxNumber, String ... message){
            if(this.minNumber != null && this.minNumber.doubleValue() > maxNumber.doubleValue()){
                this.maxNumber = this.minNumber;
                message = new String[]{String.format("must be exactly %s", this.maxNumber)};
                validator.rules.add(getMinNumberRule(this.maxNumber, message));
            } else {
                this.maxNumber = maxNumber;
            }
            validator.rules.add(getMaxNumberRule(this.maxNumber, message));
            return this;
        }

        public NumberValidatorBuilder setRange(T minNumber, T maxNumber, String ... message){
            if(minNumber.doubleValue() > maxNumber.doubleValue()){
                this.maxNumber = minNumber;
                this.minNumber = maxNumber;
            } else {
                this.maxNumber = maxNumber;
                this.minNumber = minNumber;
            }
            validator.rules.add(getRangeNumberRule(this.minNumber, this.maxNumber, message));
            return this;
        }

        public Validator<? extends Number> build(){
            return validator;
        }
    }
}