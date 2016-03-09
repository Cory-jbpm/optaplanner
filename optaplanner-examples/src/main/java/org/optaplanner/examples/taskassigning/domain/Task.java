/*
 * Copyright 2016 Red Hat, Inc. and/or its affiliates.
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

package org.optaplanner.examples.taskassigning.domain;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.variable.AnchorShadowVariable;
import org.optaplanner.core.api.domain.variable.CustomShadowVariable;
import org.optaplanner.core.api.domain.variable.PlanningVariable;
import org.optaplanner.core.api.domain.variable.PlanningVariableGraphType;
import org.optaplanner.examples.common.domain.AbstractPersistable;
import org.optaplanner.examples.taskassigning.domain.solver.StartTimeUpdatingVariableListener;

@PlanningEntity()
@XStreamAlias("MsTask")
public class Task extends AbstractPersistable implements TaskOrEmployee {

    private TaskType taskType;
    private int indexInTaskType;
    private Customer customer;

    // Planning variables: changes during planning, between score calculations.
    private TaskOrEmployee previousTaskOrEmployee;

    // Shadow variables
    private Task nextTask;
    private Employee employee;
    private Integer startTime;

    public TaskType getTaskType() {
        return taskType;
    }

    public void setTaskType(TaskType taskType) {
        this.taskType = taskType;
    }

    public int getIndexInTaskType() {
        return indexInTaskType;
    }

    public void setIndexInTaskType(int indexInTaskType) {
        this.indexInTaskType = indexInTaskType;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    @PlanningVariable(valueRangeProviderRefs = {"employeeRange", "taskRange"},
            graphType = PlanningVariableGraphType.CHAINED)
    public TaskOrEmployee getPreviousTaskOrEmployee() {
        return previousTaskOrEmployee;
    }

    public void setPreviousTaskOrEmployee(TaskOrEmployee previousTaskOrEmployee) {
        this.previousTaskOrEmployee = previousTaskOrEmployee;
    }

    @Override
    public Task getNextTask() {
        return nextTask;
    }

    @Override
    public void setNextTask(Task nextTask) {
        this.nextTask = nextTask;
    }

    @AnchorShadowVariable(sourceVariableName = "previousTaskOrEmployee")
    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    @CustomShadowVariable(variableListenerClass = StartTimeUpdatingVariableListener.class,
            // Arguable, to adhere to API specs (although this works), nextTask and employee should also be a source,
            // because this shadow must be triggered after nextTask and employee (but there is no need to be triggered by those)
            sources = {@CustomShadowVariable.Source(variableName = "previousTaskOrEmployee")})
    public Integer getStartTime() {
        return startTime;
    }

    public void setStartTime(Integer startTime) {
        this.startTime = startTime;
    }

    // ************************************************************************
    // Complex methods
    // ************************************************************************

    public int getMissingSkillCount() {
        if (employee == null) {
            return 0;
        }
        int count = 0;
        for (Skill skill : taskType.getRequiredSkillList()) {
            if (!employee.getSkillSet().contains(skill)) {
                count++;
            }
        }
        return count;
    }

    public int getDuration() {
        Affinity affinity = (employee == null) ? Affinity.NONE : employee.getAffinity(customer);
        return taskType.getBaseDuration() * affinity.getDurationMultiplier();
    }

    public Integer getEndTime() {
        if (startTime == null) {
            return null;
        }
        return startTime + getDuration();
    }

    public String getLabel() {
        return "<html><center>" + taskType + "-" + indexInTaskType + "<br/>"
                + taskType.getTitle() + "<br/>"
                + customer.getName() + "</center></html>";
    }

    @Override
    public String toString() {
        return taskType + "-" + indexInTaskType;
    }

}
