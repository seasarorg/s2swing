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

package action1;

import org.seasar.swing.application.S2SingleFrameApplication;

/**
 * アクションの単純な使用例を示すアプリケーションです。
 * 
 * @author kaiseh
 */

public class ActionApplication1 extends S2SingleFrameApplication {
    public static void main(String[] args) {
        launch(ActionApplication1.class, args);
    }

    @Override
    protected void startup() {
        show(new MainFrame());
    }
}
