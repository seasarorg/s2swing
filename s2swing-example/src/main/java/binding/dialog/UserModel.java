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

package binding.dialog;

import org.seasar.swing.annotation.ReadWrite;
import org.seasar.swing.validator.annotation.Length;
import org.seasar.swing.validator.annotation.Pattern;

/**
 * @author kaiseh
 */

public class UserModel {
    @ReadWrite
    @Length(min = 4, max = 16)
    @Pattern("[A-Z]+")
    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
