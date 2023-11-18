package it.unibo.deathnote;

//import java.util.List;
import it.unibo.deathnote.api.DeathNote;
import it.unibo.deathnote.impl.DeathNoteImpl;
import static it.unibo.deathnote.api.DeathNote.RULES;
import static java.lang.Thread.sleep;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


public class TestDeathNote { 

    private DeathNote deathNote;
    private static final String NAME= "Pippo";
    private static final String NAME2= "Pluto";

    @BeforeEach
    void init() {
        deathNote = new DeathNoteImpl();
    }

    @Test
    public void testRuleNotZeroOrNegative(){
        int ruleNumber;
        try {
            ruleNumber=0;
            deathNote.getRule(ruleNumber-1);
        } catch (IllegalArgumentException e) {
            Assertions.assertEquals("Rule number not valid: rule number doesn't exist", e.getMessage());
        }
        try {
            ruleNumber=-1;
            deathNote.getRule(ruleNumber-1);
        } catch (IllegalArgumentException e) {
            Assertions.assertEquals("Rule number not valid: rule number doesn't exist", e.getMessage());
        }
        try {
            ruleNumber=RULES.size()+1;
            deathNote.getRule(ruleNumber-1);
        } catch (IllegalArgumentException e) {
            Assertions.assertEquals("Rule number not valid: rule number doesn't exist", e.getMessage());
        }
    }

    @Test
    public void testRuleNotEmptyOrNull(){
        String rule;
        for(int i=1; i<=RULES.size(); i++){
            rule= deathNote.getRule(i);
            Assertions.assertNotNull(rule);
            Assertions.assertNotEquals("", rule);
        }
    }

    @Test
    public void testDeath(){
        Assertions.assertFalse(deathNote.isNameWritten(NAME));
        deathNote.writeName(NAME);
        Assertions.assertTrue(deathNote.isNameWritten(NAME));
        Assertions.assertFalse(deathNote.isNameWritten(NAME2));
        Assertions.assertFalse(deathNote.isNameWritten(""));
    }

    @Test
    public void testCauseOfDeath() throws InterruptedException{
        try {
            deathNote.writeDeathCause("Suffocation");
        } catch (IllegalStateException e) {
            Assertions.assertEquals("No name written before: can't write death cause", e.getMessage());
        }
        deathNote.writeName(NAME);
        Assertions.assertEquals("heart attack", deathNote.getDeathCause(NAME));
        deathNote.writeName(NAME2);
        Assertions.assertTrue(deathNote.writeDeathCause("karting accident"));
        Assertions.assertEquals("karting accident", deathNote.getDeathCause(NAME2));
        sleep(100);
        Assertions.assertFalse(deathNote.writeDeathCause("car accident"));
        Assertions.assertNotEquals("car accident", deathNote.getDeathCause(NAME2));
    }

    @Test
    public void testDeathDetails() throws InterruptedException{
        try {
            deathNote.writeDetails("ran for too long");
        } catch (IllegalStateException e) {
            Assertions.assertEquals("No name written before: can't write death details", e.getMessage());
        }
        deathNote.writeName(NAME);
        Assertions.assertEquals("", deathNote.getDeathDetails(NAME));
        Assertions.assertTrue(deathNote.writeDetails("ran for too long"));
        Assertions.assertEquals("ran for too long", deathNote.getDeathDetails(NAME));
        deathNote.writeName(NAME2);
        sleep(6100);
        Assertions.assertFalse(deathNote.writeDetails("cryed too much over OOP"));
        Assertions.assertEquals("", deathNote.getDeathDetails(NAME2));
    }
}