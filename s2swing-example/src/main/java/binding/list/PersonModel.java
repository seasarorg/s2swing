/*
 * Copyright 2004-2008 the Seasar Foundation and the Others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, 
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */

package binding.list;

import java.util.List;

import org.seasar.swing.annotation.Read;
import org.seasar.swing.annotation.ReadWriteSelection;

/**
 * @author kaiseh
 */

public class PersonModel {
    @ReadWriteSelection
    private String job;

    @Read(target = "job")
    private List<String> jobItems;

    @ReadWriteSelection
    private List<String> hobbies;

    @Read(target = "hobbies")
    private List<String> hobbyItems;

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public List<String> getJobItems() {
        return jobItems;
    }

    public void setJobItems(List<String> jobItems) {
        this.jobItems = jobItems;
    }

    public List<String> getHobbies() {
        return hobbies;
    }

    public void setHobbies(List<String> hobbies) {
        this.hobbies = hobbies;
    }

    public List<String> getHobbyItems() {
        return hobbyItems;
    }

    public void setHobbyItems(List<String> hobbyItems) {
        this.hobbyItems = hobbyItems;
    }
}
