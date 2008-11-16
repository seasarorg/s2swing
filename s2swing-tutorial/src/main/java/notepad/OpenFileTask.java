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

package notepad;

import java.io.File;

import org.jdesktop.application.Application;
import org.jdesktop.application.ResourceMap;
import org.jdesktop.application.Task;

/**
 * @author kaiseh
 */

public class OpenFileTask extends Task<String, Void> {
    private NotePadFrame owner;
    private File file;

    public OpenFileTask(NotePadFrame owner, File file) {
        super(Application.getInstance());
        this.owner = owner;
        this.file = file;
    }

    @Override
    protected String doInBackground() throws Exception {
        ResourceMap resourceMap = owner.getResourceMap();
        setMessage(resourceMap.getString("openFile.Task.message"));
        // 実際のアプリケーションでは、この位置に読み込み処理を記述する
        int dummyCount = 10;
        for (int i = 0; i < dummyCount; i++) {
            setProgress(i, 0, dummyCount);
            Thread.sleep(300);
        }
        return "[Dummy] " + file.getPath() + " was opened.";
    }

    @Override
    protected void succeeded(String result) {
        owner.getDocument().setText(result);
        owner.setCurrentFile(file);
        owner.setModified(false);
    }
}
