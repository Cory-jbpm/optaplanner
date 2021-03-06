/*
 * Copyright 2015 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.optaplanner.core.api.score.buildin.hardsoftdouble;

import org.junit.Test;
import org.kie.api.runtime.rule.RuleContext;
import org.optaplanner.core.api.score.holder.AbstractScoreHolderTest;

import static org.junit.Assert.*;

public class HardSoftDoubleScoreHolderTest extends AbstractScoreHolderTest {

    @Test
    public void addConstraintMatchWithConstraintMatch() {
        addConstraintMatch(true);
    }

    @Test
    public void addConstraintMatchWithoutConstraintMatch() {
        addConstraintMatch(false);
    }

    public void addConstraintMatch(boolean constraintMatchEnabled) {
        HardSoftDoubleScoreHolder scoreHolder = new HardSoftDoubleScoreHolder(constraintMatchEnabled);

        RuleContext hard1 = mockRuleContext("hard1");
        scoreHolder.addHardConstraintMatch(hard1, -0.01);
        assertEquals(HardSoftDoubleScore.valueOf(-0.01, -0.00), scoreHolder.extractScore(0));

        RuleContext hard2Undo = mockRuleContext("hard2Undo");
        scoreHolder.addHardConstraintMatch(hard2Undo, -0.08);
        // skip assertEquals due to floating point arithmetic rounding errors
        callOnDelete(hard2Undo);
        // skip assertEquals due to floating point arithmetic rounding errors

        RuleContext soft1 = mockRuleContext("soft1", DEFAULT_JUSTIFICATION, OTHER_JUSTIFICATION);
        scoreHolder.addSoftConstraintMatch(soft1, -0.10);
        callOnUpdate(soft1);
        scoreHolder.addSoftConstraintMatch(soft1, -0.20); // Overwrite existing

        RuleContext multi1 = mockRuleContext("multi1");
        scoreHolder.addMultiConstraintMatch(multi1, -1.00, -10.00);
        callOnUpdate(multi1);
        scoreHolder.addMultiConstraintMatch(multi1, -3.00, -40.00); // Overwrite existing

        RuleContext hard3 = mockRuleContext("hard3");
        scoreHolder.addHardConstraintMatch(hard3, -100.00);
        callOnUpdate(hard3);
        scoreHolder.addHardConstraintMatch(hard3, -500.00); // Overwrite existing

        RuleContext soft2Undo = mockRuleContext("soft2Undo", UNDO_JUSTIFICATION);
        scoreHolder.addSoftConstraintMatch(soft2Undo, -0.99);
        callOnDelete(soft2Undo);

        RuleContext multi2Undo = mockRuleContext("multi2Undo");
        scoreHolder.addMultiConstraintMatch(multi2Undo, -9.99, -9.99);
        callOnDelete(multi2Undo);

        assertEquals(HardSoftDoubleScore.valueOf(-503.01, -40.20), scoreHolder.extractScore(0));
        assertEquals(HardSoftDoubleScore.valueOfUninitialized(-7, -503.01, -40.20), scoreHolder.extractScore(-7));
        if (constraintMatchEnabled) {
            assertEquals(HardSoftDoubleScore.valueOf(-0.01, 0), findConstraintMatchTotal(scoreHolder, "hard1").getScore());
            assertEquals(HardSoftDoubleScore.valueOf(0.0, -0.20), scoreHolder.getIndictmentMap().get(OTHER_JUSTIFICATION).getScore());
            assertNull(scoreHolder.getIndictmentMap().get(UNDO_JUSTIFICATION));
        }
    }

}
