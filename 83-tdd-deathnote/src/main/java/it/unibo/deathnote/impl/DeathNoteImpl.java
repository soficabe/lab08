package it.unibo.deathnote.impl;

import java.util.HashMap;
import java.util.Map;

import it.unibo.deathnote.api.DeathNote;

public class DeathNoteImpl implements DeathNote{
    private static final byte CAUSE_TIME = 40;
    private static final short DETAILS_TIME = 6000 + CAUSE_TIME;
    private Map<String, DeathInfos> deathNote;
    private String latestName;

    public DeathNoteImpl(){
        this.deathNote= new HashMap<>();
    }

    @Override
    public String getRule(int ruleNumber) {
        if(ruleNumber>=1 && ruleNumber<=RULES.size()){
            return RULES.get(ruleNumber-1);
        }else{
            throw new IllegalArgumentException("Rule number not valid: rule number doesn't exist");
        }
    }

    @Override
    public void writeName(String name) {
        this.latestName= name;
        this.deathNote.put(name, new DeathInfos());
    }

    @Override
    public boolean writeDeathCause(String cause) {
        if(!this.deathNote.containsKey(latestName)){
            throw new IllegalStateException("No name written before: can't write death cause");
        }else{
            if(System.currentTimeMillis()-this.deathNote.get(this.latestName).writingTime<=CAUSE_TIME){
                this.deathNote.get(this.latestName).cause= cause;
                return true;
            }else{
                return false;
            }
        }
    }

    @Override
    public boolean writeDetails(String details) {
        if(!this.deathNote.containsKey(latestName)){
            throw new IllegalStateException("No name written before: can't write death details");
        }else{
            if(System.currentTimeMillis()-this.deathNote.get(this.latestName).writingTime<=DETAILS_TIME){
                this.deathNote.get(this.latestName).details= details;
                return true;
            }else{
                return false;
            }
        }
    }

    @Override
    public String getDeathCause(String name) {
        return this.deathNote.get(this.latestName).cause;
    }

    @Override
    public String getDeathDetails(String name) {
        return this.deathNote.get(this.latestName).details;
    }

    @Override
    public boolean isNameWritten(String name) {
        return this.deathNote.containsKey(name);
    }

    private static class DeathInfos{
        private String cause;
        private String details;
        private final long writingTime;

        public DeathInfos(){
            this.cause="heart attack";
            this.details="";
            this.writingTime= System.currentTimeMillis();
        }
    }
}