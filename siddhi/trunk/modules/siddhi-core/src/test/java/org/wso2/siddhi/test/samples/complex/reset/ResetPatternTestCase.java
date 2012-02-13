package org.wso2.siddhi.test.samples.complex.reset;

import junit.framework.Assert;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.wso2.siddhi.api.QueryFactory;
import org.wso2.siddhi.api.condition.Condition;
import org.wso2.siddhi.api.condition.pattern.FollowedByCondition;
import org.wso2.siddhi.api.condition.where.ConditionOperator;
import org.wso2.siddhi.api.eventstream.InputEventStream;
import org.wso2.siddhi.api.eventstream.query.Query;
import org.wso2.siddhi.core.SiddhiManager;
import org.wso2.siddhi.core.event.Event;
import org.wso2.siddhi.core.event.EventImpl;
import org.wso2.siddhi.core.exception.InvalidQueryException;
import org.wso2.siddhi.core.exception.ProcessorInitializationException;
import org.wso2.siddhi.core.exception.SiddhiException;
import org.wso2.siddhi.core.node.CallbackHandler;
import org.wso2.siddhi.core.node.InputHandler;

import java.util.ArrayList;
import java.util.List;

import static org.wso2.siddhi.api.condition.where.ConditionOperator.EQUAL;

/**
* Reset is applicable only to Pattern and Sequence Processors
*/
public class ResetPatternTestCase {

    private static final Logger log = Logger.getLogger(ResetPatternTestCase.class);
    private volatile int i = 0;
    private volatile boolean eventCaptured = false;

    public static void main(String[] args)
            throws InvalidQueryException, ProcessorInitializationException, InterruptedException,
                   SiddhiException {
        // Test code
        new ResetPatternTestCase().testCase();
    }

    @Before
    public void info() {
        log.debug("-----Query processed: Resets Pattern Processors-----");
    }

    @Test
    public void testCase() throws SiddhiException, ProcessorInitializationException,
                                  InvalidQueryException, InterruptedException,
                                  InterruptedException {

        //Instantiate SiddhiManager
        SiddhiManager siddhiManager = new SiddhiManager();

        QueryFactory qf = siddhiManager.getQueryFactory();

        InputEventStream patientEventStream = new InputEventStream(
                "Patient",
                new String[]{"EncounterId", "Id", "Name", "Age"},
                new Class[]{Integer.class, Integer.class, String.class, Integer.class}
        );

        InputEventStream conceptEventStream = new InputEventStream(
                "Concept",
                new String[]{"EncounterId", "Id", "Name", "Value"},
                new Class[]{Integer.class, Integer.class, String.class, Integer.class}
        );
        InputEventStream acceptedPatientEventStream = new InputEventStream(
                "AcceptedPatient",
                new String[]{"CriticId", "EncounterId", "Id"},
                new Class[]{Integer.class, Integer.class, Integer.class}
        );
        InputEventStream acceptedConceptEventStream = new InputEventStream(
                "AcceptedConcept",
                new String[]{"CriticId", "EncounterId", "Id"},
                new Class[]{Integer.class, Integer.class, Integer.class}
        );

        InputHandler patientInputHandler = siddhiManager.addInputEventStream(patientEventStream);
        InputHandler conceptInputHandler = siddhiManager.addInputEventStream(conceptEventStream);
//        EventGenerator patientEventGenerator = EventGenerator.DefaultFactory.create(patientEventStream);
//        EventGenerator conceptEventGenerator = EventGenerator.DefaultFactory.create(conceptEventStream);

        Query query1 = qf.createQuery(
                "AcceptedPatient",
                qf.output("CriticId=(int)1", "EncounterId=Patient.EncounterId", "Id=Patient.Id"),
                qf.from(patientEventStream),
                qf.condition("Patient.Name", ConditionOperator.CONTAINS, "ab")
        );
        siddhiManager.addQuery(query1);

        Query query2 = qf.createQuery(
                "AcceptedConcept",
                qf.output("CriticId=(int)1", "EncounterId=Concept.EncounterId", "Id=Concept.Id"),
                qf.from(conceptEventStream),
                qf.condition("Concept.Value", ConditionOperator.GREATERTHAN, "20")
        );
        siddhiManager.addQuery(query2);


        List<Condition> conditionList = new ArrayList<Condition>();
        conditionList.add(qf.every(qf.condition("AcceptedPatient.CriticId", EQUAL, "1")));
        conditionList.add(qf.and(qf.condition("AcceptedConcept.CriticId", EQUAL, "1"), qf.condition("AcceptedConcept.EncounterId", EQUAL, "$0.EncounterId")));
        Query query3 = qf.createQuery(
                "AcceptedCritic",
                qf.output("CriticId=$0.CriticId", "EncounterId=$0.EncounterId", "IdPatient=$0.Id", "IdCritic=$1.Id"),
                qf.inputStreams(qf.from(acceptedPatientEventStream),qf.from(acceptedConceptEventStream)),
                new FollowedByCondition(conditionList)
        );
        siddhiManager.addQuery(query3);

//            Query query4 = qf.createQuery(
//                    "AcceptedPatient",
//                    qf.output("CriticId=(int)2", "EncounterId=Patient.EncounterId", "Id=Patient.Id"),
//                    qf.from(patientEventStream),
//                    qf.condition("Patient.Name", ConditionOperator.CONTAINS, "ja")
//            );
//            siddhiManager.addQuery(query4);
//
//            Query query5 = qf.createQuery(
//                    "AcceptedConcept",
//                    qf.output("CriticId=(int)2", "EncounterId=Concept.EncounterId", "Id=Concept.Id"),
//                    qf.from(conceptEventStream),
//                    qf.condition("Concept.Value", ConditionOperator.GREATERTHAN, "20")
//            );
//            siddhiManager.addQuery(query5);

//
//            List<Condition> conditionList1 = new ArrayList<Condition>();
//            conditionList1.add(qf.every(qf.condition("AcceptedPatient.CriticId", EQUAL, "2")));
//            conditionList1.add(qf.and(qf.condition("AcceptedConcept.CriticId", EQUAL, "2"), qf.condition("AcceptedConcept.EncounterId", EQUAL, "$0.EncounterId")));
//            Query query6 = qf.createQuery(
//                    "AcceptedCritic",
//                    qf.output("CriticId=$0.CriticId", "EncounterId=$0.EncounterId", "IdPatient=$0.Id", "IdCritic=$1.Id"),
//                    qf.inputStreams(acceptedPatientEventStream, acceptedConceptEventStream),
//                    new FollowedByCondition(conditionList1)
//            );
//            siddhiManager.addQuery(query6);


        siddhiManager.addCallback(new CallbackHandler("AcceptedPatient") {
            public void callBack(Event event) {
                log.debug("       Event captured  " + event + " ");
            }
        }
        );
        siddhiManager.addCallback(new CallbackHandler("AcceptedConcept") {
            public void callBack(Event event) {
                log.debug("       Event captured  " + event + " ");
            }
        }
        );

        siddhiManager.addCallback(new CallbackHandler("AcceptedCritic") {
            public void callBack(Event event) {
                log.debug("       Event captured  " + event + " ");
                if (77 == (Integer) event.getNthAttribute(2) && 46 == (Integer) event.getNthAttribute(3) && i == 0) {
                    eventCaptured = true;
                } else {
                    Assert.fail();
                }
                i++;
            }
        }
        );
        siddhiManager.update();
        patientInputHandler.sendEvent(new EventImpl("Patient", new Object[]{1, 75, "abcd", 20}));
        siddhiManager.reset(5);
        patientInputHandler.sendEvent(new EventImpl("Patient", new Object[]{5, 77, "abcde", 67}));


        Thread.sleep(2000);

        conceptInputHandler.sendEvent(new EventImpl("Concept", new Object[]{1, 45, "CD1", 25}));
        conceptInputHandler.sendEvent(new EventImpl("Concept", new Object[]{5, 46, "CD4", 42}));


        Thread.sleep(1000);
        Assert.assertTrue("Events have not been captured", eventCaptured);
        Assert.assertTrue(i == 1);
        siddhiManager.shutDownTask();
    }


}
